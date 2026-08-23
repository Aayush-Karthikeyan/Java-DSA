import java.util.*;

public class GraphsPart5 {

    // Use this value in Floyd-Warshall matrices to represent no direct path.
    static final long INF = Long.MAX_VALUE / 4;

    static class Edge {
        final int src;
        final int dest;
        final int weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return src + " - " + dest + " (weight " + weight + ")";
        }
    }

    @SuppressWarnings("unchecked")
    static ArrayList<Edge>[] createEmptyGraph(int vertices) {
        ArrayList<Edge>[] graph =
                (ArrayList<Edge>[]) new ArrayList<?>[vertices];

        for (int vertex = 0; vertex < vertices; vertex++) {
            graph[vertex] = new ArrayList<>();
        }
        return graph;
    }

    static void addUndirectedEdge(ArrayList<Edge>[] graph, int first,
                                  int second, int weight) {
        graph[first].add(new Edge(first, second, weight));
        graph[second].add(new Edge(second, first, weight));
    }

    /*
     * Problem:
     * Find the cheapest flight from src to dest using at most K stops.
     * K stops means the route may use at most K + 1 flight edges.
     *
     * Pattern:
     * Limited Bellman-Ford / Dynamic Programming
     *
     * Approach:
     * 1. Set the source cost to 0 and all other costs to infinity.
     * 2. Perform exactly K + 1 rounds of flight relaxation.
     * 3. Clone the previous costs before each round.
     * 4. Use only previous-round costs to make current-round updates.
     * 5. Return -1 if the destination remains unreachable.
     *
     * Time: O((K + 1) * E)
     * Space: O(V)
     */
    static long cheapestFlightWithinKStops(int cities, int[][] flights,
                                           int src, int dest, int k) {
        long[] cost = new long[cities];
        Arrays.fill(cost, INF);
        cost[src] = 0;

        // Round i permits routes using at most i + 1 flight edges.
        for (int round = 0; round <= k; round++) {
            long[] nextCost = cost.clone();

            for (int[] flight : flights) {
                int from = flight[0];
                int to = flight[1];
                int price = flight[2];

                if (cost[from] != INF
                        && cost[from] + price < nextCost[to]) {
                    nextCost[to] = cost[from] + price;
                }
            }
            cost = nextCost;
        }

        return cost[dest] == INF ? -1 : cost[dest];
    }

    static class CityCandidate implements Comparable<CityCandidate> {
        final int city;
        final int connectionCost;

        CityCandidate(int city, int connectionCost) {
            this.city = city;
            this.connectionCost = connectionCost;
        }

        @Override
        public int compareTo(CityCandidate other) {
            return Integer.compare(
                    this.connectionCost, other.connectionCost);
        }
    }

    /*
     * Problem:
     * Connect all cities with the minimum total cost.
     * cities[i][j] is the undirected connection cost; 0 means no edge.
     *
     * Pattern:
     * Minimum Spanning Tree - Prim's Algorithm
     *
     * Approach:
     * 1. Begin from city 0 with connection cost 0.
     * 2. Remove the cheapest city candidate from a min-priority queue.
     * 3. If it is new, include it and add its cost.
     * 4. Offer all connections from that city to unvisited cities.
     * 5. Reject the input if some city cannot be reached.
     *
     * Time: O(V^2 log V) for a V-by-V matrix
     * Space: O(V^2) worst case for the lazy priority queue
     */
    static long connectCities(int[][] cities) {
        if (cities.length == 0) {
            return 0;
        }

        boolean[] connected = new boolean[cities.length];
        PriorityQueue<CityCandidate> queue = new PriorityQueue<>();
        queue.add(new CityCandidate(0, 0));

        long totalCost = 0;
        int connectedCount = 0;

        while (!queue.isEmpty() && connectedCount < cities.length) {
            CityCandidate current = queue.remove();

            if (connected[current.city]) {
                continue;
            }

            connected[current.city] = true;
            connectedCount++;
            totalCost += current.connectionCost;

            for (int neighbor = 0;
                 neighbor < cities[current.city].length;
                 neighbor++) {
                int cost = cities[current.city][neighbor];

                if (cost != 0 && !connected[neighbor]) {
                    queue.add(new CityCandidate(neighbor, cost));
                }
            }
        }

        if (connectedCount != cities.length) {
            throw new IllegalArgumentException(
                    "All cities cannot be connected");
        }
        return totalCost;
    }

    /*
     * Disjoint Set Union keeps track of separate groups.
     *
     * find:
     * Returns the representative of a set and applies path compression.
     *
     * union:
     * Joins two sets by attaching the lower-rank tree below the higher one.
     *
     * Time: O(alpha(n)) amortized per operation (almost O(1))
     * Space: O(n)
     */
    static class DisjointSetUnion {
        private final int[] parent;
        private final int[] rank;
        private int components;

        DisjointSetUnion(int size) {
            parent = new int[size];
            rank = new int[size];
            components = size;

            for (int element = 0; element < size; element++) {
                parent[element] = element;
            }
        }

        int find(int element) {
            if (parent[element] != element) {
                // Path compression points nodes directly toward the root.
                parent[element] = find(parent[element]);
            }
            return parent[element];
        }

        boolean union(int first, int second) {
            int firstRoot = find(first);
            int secondRoot = find(second);

            if (firstRoot == secondRoot) {
                return false; // Already in one set; no merge happened.
            }

            if (rank[firstRoot] < rank[secondRoot]) {
                parent[firstRoot] = secondRoot;
            } else if (rank[firstRoot] > rank[secondRoot]) {
                parent[secondRoot] = firstRoot;
            } else {
                parent[secondRoot] = firstRoot;
                rank[firstRoot]++;
            }

            components--;
            return true;
        }

        boolean connected(int first, int second) {
            return find(first) == find(second);
        }

        int componentCount() {
            return components;
        }
    }

    static class MstResult {
        final long totalCost;
        final List<Edge> selectedEdges;

        MstResult(long totalCost, List<Edge> selectedEdges) {
            this.totalCost = totalCost;
            this.selectedEdges = selectedEdges;
        }

        @Override
        public String toString() {
            return "cost=" + totalCost + ", edges=" + selectedEdges;
        }
    }

    /*
     * Problem:
     * Find a minimum spanning tree from a list containing each logical
     * undirected edge once.
     *
     * Pattern:
     * Kruskal's Algorithm - Sorting + DSU
     *
     * Approach:
     * 1. Sort all edges by increasing weight.
     * 2. Use DSU to find each edge endpoint's set.
     * 3. Select an edge only when its endpoints are in different sets.
     * 4. Union those sets and stop after selecting V - 1 edges.
     * 5. Reject a disconnected graph that cannot supply V - 1 edges.
     *
     * Time: O(E log E)
     * Space: O(V + E), including the copied edge list
     */
    static MstResult kruskalMst(int vertices, List<Edge> inputEdges) {
        List<Edge> edges = new ArrayList<>(inputEdges);
        edges.sort(Comparator.comparingInt(edge -> edge.weight));

        DisjointSetUnion dsu = new DisjointSetUnion(vertices);
        List<Edge> selectedEdges = new ArrayList<>();
        long totalCost = 0;

        for (Edge edge : edges) {
            if (dsu.union(edge.src, edge.dest)) {
                selectedEdges.add(edge);
                totalCost += edge.weight;

                if (selectedEdges.size() == vertices - 1) {
                    break;
                }
            }
        }

        int requiredEdges = Math.max(0, vertices - 1);
        if (selectedEdges.size() != requiredEdges) {
            throw new IllegalArgumentException(
                    "MST does not exist: graph is disconnected");
        }

        return new MstResult(totalCost, selectedEdges);
    }

    /*
     * Problem:
     * Recolor the starting pixel and every 4-directionally connected
     * pixel that has the same original color.
     *
     * Pattern:
     * Grid DFS / Flood Fill
     *
     * Approach:
     * 1. Remember the starting pixel's original color.
     * 2. Return immediately if the new color is already the same.
     * 3. Recolor the current valid pixel to mark it visited.
     * 4. Recursively visit up, down, left, and right.
     *
     * Time: O(rows * columns)
     * Space: O(rows * columns) worst-case recursion stack
     */
    static int[][] floodFill(int[][] image, int startRow, int startColumn,
                             int newColor) {
        int originalColor = image[startRow][startColumn];

        if (originalColor == newColor) {
            return image;
        }

        floodFillDfs(
                image, startRow, startColumn, originalColor, newColor);
        return image;
    }

    private static void floodFillDfs(int[][] image, int row, int column,
                                     int originalColor, int newColor) {
        if (row < 0 || row >= image.length
                || column < 0 || column >= image[0].length
                || image[row][column] != originalColor) {
            return;
        }

        // Recolor before recursion so this pixel is not visited again.
        image[row][column] = newColor;

        floodFillDfs(image, row - 1, column, originalColor, newColor);
        floodFillDfs(image, row + 1, column, originalColor, newColor);
        floodFillDfs(image, row, column - 1, originalColor, newColor);
        floodFillDfs(image, row, column + 1, originalColor, newColor);
    }

    static class TarjanResult {
        final List<Edge> bridges;
        final List<Integer> articulationPoints;

        TarjanResult(List<Edge> bridges,
                     List<Integer> articulationPoints) {
            this.bridges = bridges;
            this.articulationPoints = articulationPoints;
        }

        @Override
        public String toString() {
            return "bridges=" + bridges
                    + ", articulationPoints=" + articulationPoints;
        }
    }

    /*
     * Reading Topic:
     * Find bridges and articulation points in a simple undirected graph.
     *
     * Pattern:
     * Tarjan DFS - Discovery Time + Lowest Reachable Time
     *
     * Approach:
     * 1. Store each vertex's DFS discovery time and low value.
     * 2. Update low values from child subtrees and back edges.
     * 3. Edge u-v is a bridge when low[v] > discovery[u].
     * 4. A non-root u is an articulation point when
     *    low[v] >= discovery[u] for a DFS child v.
     * 5. A DFS root is an articulation point when it has multiple children.
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space
     */
    static TarjanResult findBridgesAndArticulationPoints(
            ArrayList<Edge>[] graph) {
        int[] discovery = new int[graph.length];
        int[] low = new int[graph.length];
        Arrays.fill(discovery, -1);

        boolean[] articulation = new boolean[graph.length];
        List<Edge> bridges = new ArrayList<>();
        int[] timer = {0};

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (discovery[vertex] == -1) {
                tarjanDfs(graph, vertex, -1, discovery, low,
                        articulation, bridges, timer);
            }
        }

        List<Integer> articulationPoints = new ArrayList<>();
        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (articulation[vertex]) {
                articulationPoints.add(vertex);
            }
        }

        return new TarjanResult(bridges, articulationPoints);
    }

    private static void tarjanDfs(
            ArrayList<Edge>[] graph, int current, int parent,
            int[] discovery, int[] low, boolean[] articulation,
            List<Edge> bridges, int[] timer) {
        discovery[current] = low[current] = timer[0]++;
        int dfsChildren = 0;

        for (Edge edge : graph[current]) {
            int neighbor = edge.dest;

            if (neighbor == parent) {
                continue;
            }

            if (discovery[neighbor] == -1) {
                dfsChildren++;
                tarjanDfs(graph, neighbor, current, discovery, low,
                        articulation, bridges, timer);

                low[current] = Math.min(low[current], low[neighbor]);

                if (low[neighbor] > discovery[current]) {
                    bridges.add(new Edge(
                            current, neighbor, edge.weight));
                }

                if (parent != -1
                        && low[neighbor] >= discovery[current]) {
                    articulation[current] = true;
                }
            } else {
                // Back edge: use the neighbor's discovery time.
                low[current] =
                        Math.min(low[current], discovery[neighbor]);
            }
        }

        if (parent == -1 && dfsChildren > 1) {
            articulation[current] = true;
        }
    }

    static class FloydWarshallResult {
        final long[][] distance;
        final boolean hasNegativeCycle;

        FloydWarshallResult(long[][] distance,
                            boolean hasNegativeCycle) {
            this.distance = distance;
            this.hasNegativeCycle = hasNegativeCycle;
        }
    }

    /*
     * Reading Topic:
     * Find shortest distances between every pair of vertices.
     * Negative edges are allowed, but negative cycles invalidate distances.
     *
     * Pattern:
     * Floyd-Warshall - Dynamic Programming
     *
     * Approach:
     * 1. Copy the input distance matrix.
     * 2. Try every vertex as an allowed intermediate vertex.
     * 3. For each pair i, j, try distance[i][k] + distance[k][j].
     * 4. Never add paths that contain infinity.
     * 5. A negative diagonal value after processing proves a negative cycle.
     *
     * Time: O(V^3)
     * Space: O(V^2) for the returned matrix copy
     */
    static FloydWarshallResult floydWarshall(long[][] weights) {
        int vertices = weights.length;
        long[][] distance = new long[vertices][vertices];

        for (int row = 0; row < vertices; row++) {
            distance[row] = weights[row].clone();
        }

        for (int intermediate = 0;
             intermediate < vertices;
             intermediate++) {
            for (int from = 0; from < vertices; from++) {
                for (int to = 0; to < vertices; to++) {
                    if (distance[from][intermediate] != INF
                            && distance[intermediate][to] != INF) {
                        long throughIntermediate =
                                distance[from][intermediate]
                                + distance[intermediate][to];

                        if (throughIntermediate < distance[from][to]) {
                            distance[from][to] = throughIntermediate;
                        }
                    }
                }
            }
        }

        boolean hasNegativeCycle = false;
        for (int vertex = 0; vertex < vertices; vertex++) {
            if (distance[vertex][vertex] < 0) {
                hasNegativeCycle = true;
                break;
            }
        }

        return new FloydWarshallResult(distance, hasNegativeCycle);
    }

    private static String formatMatrix(long[][] matrix) {
        List<String> rows = new ArrayList<>();

        for (long[] row : matrix) {
            List<String> values = new ArrayList<>();
            for (long value : row) {
                values.add(value == INF ? "INF" : String.valueOf(value));
            }
            rows.add(values.toString());
        }
        return rows.toString();
    }

    public static void main(String[] args) {
        int[][] flights = {
                {0, 1, 100},
                {1, 2, 100},
                {2, 0, 100},
                {1, 3, 600},
                {2, 3, 200}
        };
        System.out.println("Cheapest flight with 1 stop: "
                + cheapestFlightWithinKStops(4, flights, 0, 3, 1));
        System.out.println("Cheapest flight with 2 stops: "
                + cheapestFlightWithinKStops(4, flights, 0, 3, 2));

        int[][] cities = {
                {0, 1, 2, 3, 4},
                {1, 0, 5, 0, 7},
                {2, 5, 0, 6, 0},
                {3, 0, 6, 0, 0},
                {4, 7, 0, 0, 0}
        };
        System.out.println("Connecting cities cost: "
                + connectCities(cities));

        DisjointSetUnion dsu = new DisjointSetUnion(7);
        dsu.union(1, 3);
        dsu.union(2, 4);
        dsu.union(3, 6);
        dsu.union(1, 4);
        dsu.union(1, 5);
        System.out.println("DSU: 3 and 5 connected: "
                + dsu.connected(3, 5));
        System.out.println("DSU component count: "
                + dsu.componentCount());

        List<Edge> edges = Arrays.asList(
                new Edge(0, 1, 10),
                new Edge(0, 2, 15),
                new Edge(0, 3, 30),
                new Edge(1, 3, 40),
                new Edge(2, 3, 50)
        );
        System.out.println("Kruskal MST: " + kruskalMst(4, edges));

        int[][] image = {
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
        };
        System.out.println("Flood fill: "
                + Arrays.deepToString(floodFill(image, 1, 1, 2)));

        ArrayList<Edge>[] criticalGraph = createEmptyGraph(5);
        addUndirectedEdge(criticalGraph, 0, 1, 1);
        addUndirectedEdge(criticalGraph, 1, 2, 1);
        addUndirectedEdge(criticalGraph, 2, 0, 1);
        addUndirectedEdge(criticalGraph, 1, 3, 1);
        addUndirectedEdge(criticalGraph, 3, 4, 1);
        System.out.println("Tarjan: "
                + findBridgesAndArticulationPoints(criticalGraph));

        long[][] weights = {
                {0, 5, INF, 10},
                {INF, 0, 3, INF},
                {INF, INF, 0, 1},
                {INF, INF, INF, 0}
        };
        FloydWarshallResult allPairs = floydWarshall(weights);
        System.out.println("Floyd-Warshall: "
                + formatMatrix(allPairs.distance));
        System.out.println("Floyd negative cycle: "
                + allPairs.hasNegativeCycle);
    }
}

import java.util.*;

public class GraphsPart1 {

    // One Edge object stores a connection from src to dest.
    // The weight is kept even when a question does not use it.
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
            return src + " -> " + dest + " (weight " + weight + ")";
        }
    }

    /*
     * Problem:
     * Create an empty adjacency-list graph with a list for every vertex.
     *
     * Pattern:
     * Graph Representation - Adjacency List
     *
     * Approach:
     * 1. Create an array with one position per vertex.
     * 2. Put an empty ArrayList at every position.
     * 3. Store outgoing Edge objects in the source vertex's list.
     *
     * Time: O(V) to create the empty graph
     * Space: O(V + E) after edges are added
     */
    @SuppressWarnings("unchecked")
    static ArrayList<Edge>[] createEmptyGraph(int vertices) {
        ArrayList<Edge>[] graph =
                (ArrayList<Edge>[]) new ArrayList<?>[vertices];

        for (int vertex = 0; vertex < vertices; vertex++) {
            graph[vertex] = new ArrayList<>();
        }
        return graph;
    }

    // A directed edge is stored only from src to dest.
    // Time: O(1), Space: O(1) for one new edge object.
    static void addDirectedEdge(ArrayList<Edge>[] graph,
                                int src, int dest, int weight) {
        graph[src].add(new Edge(src, dest, weight));
    }

    // An undirected edge is stored in both directions.
    // Time: O(1), Space: O(1) for two new edge objects.
    static void addUndirectedEdge(ArrayList<Edge>[] graph,
                                  int first, int second, int weight) {
        graph[first].add(new Edge(first, second, weight));
        graph[second].add(new Edge(second, first, weight));
    }

    /*
     * Problem:
     * Build the weighted, undirected sample graph used by the traversals.
     *
     * Pattern:
     * Adjacency List Construction
     *
     * Approach:
     * 1. Create one empty neighbor list per vertex.
     * 2. Add each undirected connection in both directions.
     * 3. Keep the weight inside each Edge object.
     *
     * Time: O(V + E)
     * Space: O(V + E)
     */
    static ArrayList<Edge>[] createSampleGraph() {
        ArrayList<Edge>[] graph = createEmptyGraph(7);

        addUndirectedEdge(graph, 0, 1, 1);
        addUndirectedEdge(graph, 0, 2, 1);
        addUndirectedEdge(graph, 1, 3, 1);
        addUndirectedEdge(graph, 2, 4, 1);
        addUndirectedEdge(graph, 3, 4, 1);
        addUndirectedEdge(graph, 3, 5, 1);
        addUndirectedEdge(graph, 4, 5, 1);
        addUndirectedEdge(graph, 5, 6, 1);

        return graph;
    }

    /*
     * Problem:
     * Visit every vertex level by level using Breadth-First Search.
     * This version also handles a graph with disconnected components.
     *
     * Pattern:
     * BFS + Queue
     *
     * Approach:
     * 1. Keep one visited array for the whole graph.
     * 2. Start BFS from every still-unvisited vertex.
     * 3. Mark a vertex when it enters the queue.
     * 4. Remove vertices in FIFO order and add their unvisited neighbors.
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space
     */
    static List<Integer> bfs(ArrayList<Edge>[] graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.length];

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]) {
                bfsFrom(graph, vertex, visited, order);
            }
        }
        return order;
    }

    private static void bfsFrom(ArrayList<Edge>[] graph, int start,
                                boolean[] visited, List<Integer> order) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.remove();
            order.add(current);

            for (Edge edge : graph[current]) {
                if (!visited[edge.dest]) {
                    // Mark now so another neighbor cannot enqueue it again.
                    visited[edge.dest] = true;
                    queue.add(edge.dest);
                }
            }
        }
    }

    /*
     * Problem:
     * Visit every vertex by going as deep as possible before backtracking.
     * This version also handles a graph with disconnected components.
     *
     * Pattern:
     * DFS + Recursion
     *
     * Approach:
     * 1. Keep one visited array for the whole graph.
     * 2. Start DFS from every still-unvisited vertex.
     * 3. Mark and record the current vertex.
     * 4. Recursively visit each unvisited neighbor.
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space
     */
    static List<Integer> dfs(ArrayList<Edge>[] graph) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.length];

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]) {
                dfsFrom(graph, vertex, visited, order);
            }
        }
        return order;
    }

    private static void dfsFrom(ArrayList<Edge>[] graph, int current,
                                boolean[] visited, List<Integer> order) {
        visited[current] = true;
        order.add(current);

        for (Edge edge : graph[current]) {
            if (!visited[edge.dest]) {
                dfsFrom(graph, edge.dest, visited, order);
            }
        }
    }

    /*
     * Problem:
     * Return whether any path exists from src to dest.
     *
     * Pattern:
     * DFS Reachability
     *
     * Approach:
     * 1. If the current vertex is the destination, return true.
     * 2. Mark the current vertex visited.
     * 3. Recursively search every unvisited neighbor.
     * 4. Return true when one branch succeeds; otherwise return false.
     *
     * Time: O(V + E) worst case
     * Space: O(V) for visited and the recursion stack
     */
    static boolean hasPath(ArrayList<Edge>[] graph, int src, int dest) {
        boolean[] visited = new boolean[graph.length];
        return hasPathDfs(graph, src, dest, visited);
    }

    private static boolean hasPathDfs(ArrayList<Edge>[] graph, int current,
                                      int dest, boolean[] visited) {
        if (current == dest) {
            return true;
        }

        visited[current] = true;

        for (Edge edge : graph[current]) {
            if (!visited[edge.dest]
                    && hasPathDfs(graph, edge.dest, dest, visited)) {
                return true;
            }
        }
        return false;
    }

    // Print every outgoing edge of one vertex.
    // Time: O(degree of vertex), Space: O(1).
    static void printNeighbors(ArrayList<Edge>[] graph, int vertex) {
        for (Edge edge : graph[vertex]) {
            System.out.println(edge);
        }
    }

    public static void main(String[] args) {
        /*
         * Sample undirected graph:
         *
         *       1 ----- 3
         *      /       / \
         *     0       4---5 ----- 6
         *      \     /
         *       2 ---
         */
        ArrayList<Edge>[] graph = createSampleGraph();

        System.out.println("Neighbors of vertex 3:");
        printNeighbors(graph, 3);

        System.out.println("BFS: " + bfs(graph));
        System.out.println("DFS: " + dfs(graph));
        System.out.println("Path 0 to 6: " + hasPath(graph, 0, 6));

        // A separate vertex proves that the traversal methods handle
        // disconnected components.
        ArrayList<Edge>[] disconnected = createEmptyGraph(4);
        addUndirectedEdge(disconnected, 0, 1, 1);
        addUndirectedEdge(disconnected, 2, 3, 1);
        System.out.println("Disconnected BFS: " + bfs(disconnected));
        System.out.println("Path 0 to 3: " + hasPath(disconnected, 0, 3));
    }
}

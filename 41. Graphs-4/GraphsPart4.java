import java.util.*;

public class GraphsPart4 {

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

    // Directed edge: used by the Bellman-Ford examples.
    static void addDirectedEdge(ArrayList<Edge>[] graph, int src,
                                int dest, int weight) {
        graph[src].add(new Edge(src, dest, weight));
    }

    // Undirected edge: used by Prim's MST and stored both ways.
    static void addUndirectedEdge(ArrayList<Edge>[] graph, int first,
                                  int second, int weight) {
        graph[first].add(new Edge(first, second, weight));
        graph[second].add(new Edge(second, first, weight));
    }

    static class BellmanFordResult {
        final long[] distance;
        final boolean hasReachableNegativeCycle;

        BellmanFordResult(long[] distance,
                          boolean hasReachableNegativeCycle) {
            this.distance = distance;
            this.hasReachableNegativeCycle = hasReachableNegativeCycle;
        }
    }

    /*
     * Problem:
     * Find shortest distances from one source in a weighted directed graph.
     * Negative edges are allowed, and a reachable negative cycle is reported.
     *
     * Pattern:
     * Bellman-Ford - Repeated Edge Relaxation
     *
     * Approach:
     * 1. Set source distance to 0 and all others to infinity.
     * 2. Relax every edge up to V - 1 times.
     * 3. Stop early if a complete round makes no update.
     * 4. Scan all edges once more.
     * 5. A further improvement proves a reachable negative cycle.
     *
     * Time: O(V * E) worst case
     * Space: O(V) auxiliary space
     */
    static BellmanFordResult bellmanFord(ArrayList<Edge>[] graph,
                                         int source) {
        long[] distance = new long[graph.length];
        Arrays.fill(distance, Long.MAX_VALUE);
        distance[source] = 0;

        for (int round = 1; round <= graph.length - 1; round++) {
            boolean updated = false;

            for (ArrayList<Edge> edges : graph) {
                for (Edge edge : edges) {
                    if (distance[edge.src] != Long.MAX_VALUE
                            && distance[edge.src] + edge.weight
                            < distance[edge.dest]) {
                        distance[edge.dest] =
                                distance[edge.src] + edge.weight;
                        updated = true;
                    }
                }
            }

            // No update means all shortest distances are already final.
            if (!updated) {
                break;
            }
        }

        boolean hasNegativeCycle = canStillRelaxAnyEdge(graph, distance);
        return new BellmanFordResult(distance, hasNegativeCycle);
    }

    private static boolean canStillRelaxAnyEdge(
            ArrayList<Edge>[] graph, long[] distance) {
        for (ArrayList<Edge> edges : graph) {
            for (Edge edge : edges) {
                if (distance[edge.src] != Long.MAX_VALUE
                        && distance[edge.src] + edge.weight
                        < distance[edge.dest]) {
                    return true;
                }
            }
        }
        return false;
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

    static class MstCandidate implements Comparable<MstCandidate> {
        final int vertex;
        final int parent;
        final int edgeCost;

        MstCandidate(int vertex, int parent, int edgeCost) {
            this.vertex = vertex;
            this.parent = parent;
            this.edgeCost = edgeCost;
        }

        @Override
        public int compareTo(MstCandidate other) {
            return Integer.compare(this.edgeCost, other.edgeCost);
        }
    }

    /*
     * Problem:
     * Find a minimum spanning tree of a connected, weighted,
     * undirected graph.
     *
     * Pattern:
     * Prim's Algorithm - Greedy + PriorityQueue
     *
     * Approach:
     * 1. Start with vertex 0 at cost 0.
     * 2. Remove the cheapest edge that can add a new vertex.
     * 3. Mark that vertex and add the chosen edge to the MST.
     * 4. Add its outgoing edges to the min-priority queue.
     * 5. Reject the input if not all vertices can be reached.
     *
     * Time: O(E log V)
     * Space: O(V + E) auxiliary space in this lazy queue version
     */
    static MstResult primMst(ArrayList<Edge>[] graph) {
        if (graph.length == 0) {
            return new MstResult(0, new ArrayList<>());
        }

        boolean[] inMst = new boolean[graph.length];
        PriorityQueue<MstCandidate> queue = new PriorityQueue<>();
        List<Edge> selectedEdges = new ArrayList<>();
        long totalCost = 0;
        int visitedVertices = 0;

        queue.add(new MstCandidate(0, -1, 0));

        while (!queue.isEmpty() && visitedVertices < graph.length) {
            MstCandidate current = queue.remove();

            if (inMst[current.vertex]) {
                continue;
            }

            inMst[current.vertex] = true;
            visitedVertices++;
            totalCost += current.edgeCost;

            if (current.parent != -1) {
                selectedEdges.add(new Edge(
                        current.parent, current.vertex, current.edgeCost));
            }

            for (Edge edge : graph[current.vertex]) {
                if (!inMst[edge.dest]) {
                    queue.add(new MstCandidate(
                            edge.dest, current.vertex, edge.weight));
                }
            }
        }

        if (visitedVertices != graph.length) {
            throw new IllegalArgumentException(
                    "MST does not exist: graph is disconnected");
        }

        return new MstResult(totalCost, selectedEdges);
    }

    private static String formatDistances(long[] distance) {
        List<String> values = new ArrayList<>();

        for (long value : distance) {
            values.add(value == Long.MAX_VALUE ? "INF"
                    : String.valueOf(value));
        }
        return values.toString();
    }

    public static void main(String[] args) {
        ArrayList<Edge>[] directedGraph = createEmptyGraph(6);
        addDirectedEdge(directedGraph, 0, 1, 2);
        addDirectedEdge(directedGraph, 0, 2, 4);
        addDirectedEdge(directedGraph, 1, 2, -4);
        addDirectedEdge(directedGraph, 2, 3, 2);
        addDirectedEdge(directedGraph, 3, 4, 4);
        addDirectedEdge(directedGraph, 4, 1, -1);
        // Vertex 5 is unreachable from source 0.

        BellmanFordResult shortestPaths =
                bellmanFord(directedGraph, 0);
        System.out.println("Bellman-Ford distances: "
                + formatDistances(shortestPaths.distance));
        System.out.println("Reachable negative cycle: "
                + shortestPaths.hasReachableNegativeCycle);

        ArrayList<Edge>[] negativeCycleGraph = createEmptyGraph(3);
        addDirectedEdge(negativeCycleGraph, 0, 1, 1);
        addDirectedEdge(negativeCycleGraph, 1, 2, -2);
        addDirectedEdge(negativeCycleGraph, 2, 1, -2);
        System.out.println("Negative-cycle example detected: "
                + bellmanFord(negativeCycleGraph, 0)
                .hasReachableNegativeCycle);

        ArrayList<Edge>[] undirectedGraph = createEmptyGraph(4);
        addUndirectedEdge(undirectedGraph, 0, 1, 10);
        addUndirectedEdge(undirectedGraph, 0, 2, 15);
        addUndirectedEdge(undirectedGraph, 0, 3, 30);
        addUndirectedEdge(undirectedGraph, 1, 3, 40);
        addUndirectedEdge(undirectedGraph, 2, 3, 50);

        System.out.println("Prim MST: " + primMst(undirectedGraph));

        ArrayList<Edge>[] disconnectedGraph = createEmptyGraph(3);
        addUndirectedEdge(disconnectedGraph, 0, 1, 5);
        try {
            primMst(disconnectedGraph);
        } catch (IllegalArgumentException exception) {
            System.out.println("Prim rejects disconnected graph: true");
        }
    }
}

import java.util.*;

public class GraphsPart3 {

    static class Edge {
        final int src;
        final int dest;
        final int weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
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

    // This chapter uses directed edges.
    static void addDirectedEdge(ArrayList<Edge>[] graph, int src,
                                int dest, int weight) {
        graph[src].add(new Edge(src, dest, weight));
    }

    /*
     * Problem:
     * Return a topological ordering of a directed acyclic graph using BFS.
     * For every edge u -> v, u must appear before v.
     *
     * Pattern:
     * Kahn's Algorithm - Indegree + Queue
     *
     * Approach:
     * 1. Count the incoming edges (indegree) of every vertex.
     * 2. Add all zero-indegree vertices to a queue.
     * 3. Remove one vertex and reduce the indegree of its destinations.
     * 4. Enqueue a destination when its indegree becomes zero.
     * 5. If fewer than V vertices are processed, a directed cycle exists.
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space
     */
    static List<Integer> topologicalSortKahn(ArrayList<Edge>[] graph) {
        int[] indegree = calculateIndegree(graph);
        Queue<Integer> queue = new ArrayDeque<>();

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (indegree[vertex] == 0) {
                queue.add(vertex);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.remove();
            order.add(current);

            for (Edge edge : graph[current]) {
                indegree[edge.dest]--;
                if (indegree[edge.dest] == 0) {
                    queue.add(edge.dest);
                }
            }
        }

        if (order.size() != graph.length) {
            throw new IllegalArgumentException(
                    "Topological order does not exist: graph has a cycle");
        }
        return order;
    }

    private static int[] calculateIndegree(ArrayList<Edge>[] graph) {
        int[] indegree = new int[graph.length];

        for (ArrayList<Edge> edges : graph) {
            for (Edge edge : edges) {
                indegree[edge.dest]++;
            }
        }
        return indegree;
    }

    /*
     * Problem:
     * Return all simple directed paths from src to dest.
     *
     * Pattern:
     * DFS + Backtracking
     *
     * Approach:
     * 1. Add the current vertex to the path.
     * 2. If it is the destination, save a copy of the path.
     * 3. Otherwise, recursively visit neighbors not already in this path.
     * 4. Remove the current vertex while backtracking.
     *
     * Time: Exponential; O(V * 2^V) worst case for a DAG
     * Space: O(V) auxiliary space, plus O(P * V) for P returned paths
     */
    static List<List<Integer>> allPaths(ArrayList<Edge>[] graph,
                                        int src, int dest) {
        List<List<Integer>> paths = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        boolean[] inCurrentPath = new boolean[graph.length];

        collectPaths(graph, src, dest, inCurrentPath, currentPath, paths);
        return paths;
    }

    private static void collectPaths(ArrayList<Edge>[] graph, int current,
                                     int dest, boolean[] inCurrentPath,
                                     List<Integer> currentPath,
                                     List<List<Integer>> paths) {
        currentPath.add(current);

        if (current == dest) {
            // Save a copy because currentPath will change during backtracking.
            paths.add(new ArrayList<>(currentPath));
        } else {
            inCurrentPath[current] = true;

            for (Edge edge : graph[current]) {
                if (!inCurrentPath[edge.dest]) {
                    collectPaths(graph, edge.dest, dest, inCurrentPath,
                            currentPath, paths);
                }
            }

            inCurrentPath[current] = false;
        }

        // Undo this choice so the same list can build the next path.
        currentPath.remove(currentPath.size() - 1);
    }

    static class VertexDistance implements Comparable<VertexDistance> {
        final int vertex;
        final long distance;

        VertexDistance(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance other) {
            return Long.compare(this.distance, other.distance);
        }
    }

    /*
     * Problem:
     * Find the shortest distance from one source to every vertex in a
     * weighted graph whose edge weights are non-negative.
     *
     * Pattern:
     * Dijkstra's Algorithm - Greedy + PriorityQueue
     *
     * Approach:
     * 1. Set the source distance to 0 and all others to infinity.
     * 2. Use a min-priority queue ordered by current distance.
     * 3. Remove the closest entry and skip it if it is outdated.
     * 4. Relax each outgoing edge: try dist[u] + weight < dist[v].
     * 5. Add every improved destination back to the priority queue.
     *
     * Time: O((V + E) log V)
     * Space: O(V + E) auxiliary space in the lazy priority-queue version
     */
    static long[] dijkstra(ArrayList<Edge>[] graph, int source) {
        rejectNegativeWeights(graph);

        long[] distance = new long[graph.length];
        Arrays.fill(distance, Long.MAX_VALUE);
        distance[source] = 0;

        PriorityQueue<VertexDistance> queue = new PriorityQueue<>();
        queue.add(new VertexDistance(source, 0));

        while (!queue.isEmpty()) {
            VertexDistance current = queue.remove();

            // A better distance was inserted after this older queue entry.
            if (current.distance != distance[current.vertex]) {
                continue;
            }

            for (Edge edge : graph[current.vertex]) {
                long candidate = distance[current.vertex] + edge.weight;

                if (candidate < distance[edge.dest]) {
                    distance[edge.dest] = candidate;
                    queue.add(new VertexDistance(edge.dest, candidate));
                }
            }
        }
        return distance;
    }

    private static void rejectNegativeWeights(ArrayList<Edge>[] graph) {
        for (ArrayList<Edge> edges : graph) {
            for (Edge edge : edges) {
                if (edge.weight < 0) {
                    throw new IllegalArgumentException(
                            "Dijkstra requires non-negative edge weights");
                }
            }
        }
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
        ArrayList<Edge>[] dag = createEmptyGraph(6);
        addDirectedEdge(dag, 2, 3, 1);
        addDirectedEdge(dag, 3, 1, 1);
        addDirectedEdge(dag, 4, 0, 1);
        addDirectedEdge(dag, 4, 1, 1);
        addDirectedEdge(dag, 5, 0, 1);
        addDirectedEdge(dag, 5, 2, 1);
        System.out.println("Kahn topological order: "
                + topologicalSortKahn(dag));

        ArrayList<Edge>[] cyclicGraph = createEmptyGraph(2);
        addDirectedEdge(cyclicGraph, 0, 1, 1);
        addDirectedEdge(cyclicGraph, 1, 0, 1);
        try {
            topologicalSortKahn(cyclicGraph);
        } catch (IllegalArgumentException exception) {
            System.out.println("Kahn rejects a directed cycle: true");
        }

        ArrayList<Edge>[] pathsGraph = createEmptyGraph(6);
        addDirectedEdge(pathsGraph, 5, 0, 1);
        addDirectedEdge(pathsGraph, 5, 2, 1);
        addDirectedEdge(pathsGraph, 0, 3, 1);
        addDirectedEdge(pathsGraph, 2, 3, 1);
        addDirectedEdge(pathsGraph, 3, 1, 1);
        addDirectedEdge(pathsGraph, 4, 0, 1);
        addDirectedEdge(pathsGraph, 4, 1, 1);
        System.out.println("All paths from 5 to 1: "
                + allPaths(pathsGraph, 5, 1));

        ArrayList<Edge>[] weightedGraph = createEmptyGraph(7);
        addDirectedEdge(weightedGraph, 0, 1, 2);
        addDirectedEdge(weightedGraph, 0, 2, 4);
        addDirectedEdge(weightedGraph, 1, 2, 1);
        addDirectedEdge(weightedGraph, 1, 3, 7);
        addDirectedEdge(weightedGraph, 2, 4, 3);
        addDirectedEdge(weightedGraph, 4, 3, 2);
        addDirectedEdge(weightedGraph, 3, 5, 1);
        addDirectedEdge(weightedGraph, 4, 5, 5);
        // Vertex 6 is unreachable from source 0.
        System.out.println("Dijkstra from 0: "
                + formatDistances(dijkstra(weightedGraph, 0)));
    }
}

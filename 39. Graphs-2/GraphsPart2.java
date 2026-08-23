import java.util.*;

public class GraphsPart2 {

    static class Edge {
        final int src;
        final int dest;

        Edge(int src, int dest) {
            this.src = src;
            this.dest = dest;
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

    // Directed: store only src -> dest.
    static void addDirectedEdge(ArrayList<Edge>[] graph, int src, int dest) {
        graph[src].add(new Edge(src, dest));
    }

    // Undirected: store both directions.
    static void addUndirectedEdge(ArrayList<Edge>[] graph,
                                  int first, int second) {
        graph[first].add(new Edge(first, second));
        graph[second].add(new Edge(second, first));
    }

    /*
     * Problem:
     * Return every connected component of an undirected graph.
     *
     * Pattern:
     * DFS + Connected Components
     *
     * Approach:
     * 1. Keep one visited array for the whole graph.
     * 2. Scan every vertex.
     * 3. If a vertex is unvisited, start a new DFS component.
     * 4. Collect all vertices reached by that DFS.
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space, excluding the returned components
     */
    static List<List<Integer>> connectedComponents(
            ArrayList<Edge>[] graph) {
        List<List<Integer>> components = new ArrayList<>();
        boolean[] visited = new boolean[graph.length];

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                collectComponent(graph, vertex, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private static void collectComponent(ArrayList<Edge>[] graph, int current,
                                         boolean[] visited,
                                         List<Integer> component) {
        visited[current] = true;
        component.add(current);

        for (Edge edge : graph[current]) {
            if (!visited[edge.dest]) {
                collectComponent(graph, edge.dest, visited, component);
            }
        }
    }

    /*
     * Problem:
     * Detect whether a simple undirected graph contains a cycle.
     *
     * Pattern:
     * DFS + Parent Tracking
     *
     * Approach:
     * 1. Start DFS from each unvisited component.
     * 2. Mark the current vertex and remember its parent.
     * 3. Recurse into every unvisited neighbor.
     * 4. A visited neighbor that is not the parent proves a cycle.
     *
     * Time: O(V + E)
     * Space: O(V) for visited and the recursion stack
     */
    static boolean hasUndirectedCycle(ArrayList<Edge>[] graph) {
        boolean[] visited = new boolean[graph.length];

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]
                    && hasUndirectedCycleDfs(graph, vertex, -1, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasUndirectedCycleDfs(
            ArrayList<Edge>[] graph, int current, int parent,
            boolean[] visited) {
        visited[current] = true;

        for (Edge edge : graph[current]) {
            int neighbor = edge.dest;

            if (!visited[neighbor]) {
                if (hasUndirectedCycleDfs(
                        graph, neighbor, current, visited)) {
                    return true;
                }
            } else if (neighbor != parent) {
                // The parent edge is expected in an undirected graph.
                // Any other visited neighbor gives an alternate route back.
                return true;
            }
        }
        return false;
    }

    /*
     * Problem:
     * Check whether an undirected graph can be colored with two colors
     * so that adjacent vertices always have different colors.
     *
     * Pattern:
     * BFS + Two Coloring
     *
     * Approach:
     * 1. Use -1 for uncolored, 0 for the first color, and 1 for the second.
     * 2. Start BFS from every uncolored component.
     * 3. Give each uncolored neighbor the opposite color.
     * 4. If adjacent vertices have the same color, return false.
     *
     * Time: O(V + E)
     * Space: O(V) for colors and the queue
     */
    static boolean isBipartite(ArrayList<Edge>[] graph) {
        int[] color = new int[graph.length];
        Arrays.fill(color, -1);

        for (int start = 0; start < graph.length; start++) {
            if (color[start] != -1) {
                continue;
            }

            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(start);
            color[start] = 0;

            while (!queue.isEmpty()) {
                int current = queue.remove();

                for (Edge edge : graph[current]) {
                    int neighbor = edge.dest;

                    if (color[neighbor] == -1) {
                        color[neighbor] = 1 - color[current];
                        queue.add(neighbor);
                    } else if (color[neighbor] == color[current]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     * Problem:
     * Detect whether a directed graph contains a cycle.
     *
     * Pattern:
     * DFS + Recursion Path
     *
     * Approach:
     * 1. Keep visited for all completed/discovered vertices.
     * 2. Keep inCurrentPath for the active DFS recursion path.
     * 3. Reaching a neighbor already in the current path proves a cycle.
     * 4. Remove the current vertex from the path while backtracking.
     *
     * Time: O(V + E)
     * Space: O(V) for both arrays and the recursion stack
     */
    static boolean hasDirectedCycle(ArrayList<Edge>[] graph) {
        boolean[] visited = new boolean[graph.length];
        boolean[] inCurrentPath = new boolean[graph.length];

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]
                    && hasDirectedCycleDfs(
                    graph, vertex, visited, inCurrentPath)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasDirectedCycleDfs(
            ArrayList<Edge>[] graph, int current, boolean[] visited,
            boolean[] inCurrentPath) {
        visited[current] = true;
        inCurrentPath[current] = true;

        for (Edge edge : graph[current]) {
            int neighbor = edge.dest;

            if (inCurrentPath[neighbor]) {
                // This edge points back to an ancestor in the same DFS path.
                return true;
            }

            if (!visited[neighbor]
                    && hasDirectedCycleDfs(
                    graph, neighbor, visited, inCurrentPath)) {
                return true;
            }
        }

        // This vertex is finished, so it is no longer in the active path.
        inCurrentPath[current] = false;
        return false;
    }

    /*
     * Problem:
     * Return a topological ordering of a directed acyclic graph (DAG).
     * For every edge u -> v, u must appear before v.
     *
     * Pattern:
     * DFS + Stack
     *
     * Approach:
     * 1. Start DFS from every unvisited vertex.
     * 2. Visit all outgoing neighbors first.
     * 3. Push the current vertex only after its neighbors are finished.
     * 4. Pop the stack to get the topological order.
     *
     * Time: O(V + E)
     * Space: O(V) for visited, recursion, stack, and returned order
     */
    static List<Integer> topologicalSort(ArrayList<Edge>[] graph) {
        boolean[] visited = new boolean[graph.length];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]) {
                topologicalSortDfs(graph, vertex, visited, stack);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) {
            order.add(stack.pop());
        }
        return order;
    }

    private static void topologicalSortDfs(
            ArrayList<Edge>[] graph, int current, boolean[] visited,
            Deque<Integer> stack) {
        visited[current] = true;

        for (Edge edge : graph[current]) {
            if (!visited[edge.dest]) {
                topologicalSortDfs(graph, edge.dest, visited, stack);
            }
        }

        // All destinations are placed first; stack reversal puts current
        // before them in the final answer.
        stack.push(current);
    }

    public static void main(String[] args) {
        ArrayList<Edge>[] componentsGraph = createEmptyGraph(7);
        addUndirectedEdge(componentsGraph, 0, 1);
        addUndirectedEdge(componentsGraph, 1, 2);
        addUndirectedEdge(componentsGraph, 3, 4);
        // Vertices 5 and 6 are isolated components.
        System.out.println("Connected components: "
                + connectedComponents(componentsGraph));

        ArrayList<Edge>[] undirectedCycle = createEmptyGraph(5);
        addUndirectedEdge(undirectedCycle, 0, 1);
        addUndirectedEdge(undirectedCycle, 1, 2);
        addUndirectedEdge(undirectedCycle, 2, 0);
        addUndirectedEdge(undirectedCycle, 0, 3);
        addUndirectedEdge(undirectedCycle, 3, 4);
        System.out.println("Undirected cycle: "
                + hasUndirectedCycle(undirectedCycle));

        ArrayList<Edge>[] tree = createEmptyGraph(4);
        addUndirectedEdge(tree, 0, 1);
        addUndirectedEdge(tree, 0, 2);
        addUndirectedEdge(tree, 2, 3);
        System.out.println("Tree has cycle: " + hasUndirectedCycle(tree));

        ArrayList<Edge>[] square = createEmptyGraph(4);
        addUndirectedEdge(square, 0, 1);
        addUndirectedEdge(square, 1, 2);
        addUndirectedEdge(square, 2, 3);
        addUndirectedEdge(square, 3, 0);
        System.out.println("Square is bipartite: " + isBipartite(square));
        System.out.println("Triangle is bipartite: "
                + isBipartite(undirectedCycle));

        ArrayList<Edge>[] directedCycle = createEmptyGraph(3);
        addDirectedEdge(directedCycle, 0, 1);
        addDirectedEdge(directedCycle, 1, 2);
        addDirectedEdge(directedCycle, 2, 0);
        System.out.println("Directed cycle: "
                + hasDirectedCycle(directedCycle));

        ArrayList<Edge>[] dag = createEmptyGraph(6);
        addDirectedEdge(dag, 2, 3);
        addDirectedEdge(dag, 3, 1);
        addDirectedEdge(dag, 4, 0);
        addDirectedEdge(dag, 4, 1);
        addDirectedEdge(dag, 5, 0);
        addDirectedEdge(dag, 5, 2);

        System.out.println("DAG has cycle: " + hasDirectedCycle(dag));
        System.out.println("Topological order: " + topologicalSort(dag));
    }
}

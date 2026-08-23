import java.util.*;

public class GraphsSupplemental {

    static class Edge {
        final int src;
        final int dest;

        Edge(int src, int dest) {
            this.src = src;
            this.dest = dest;
        }

        @Override
        public String toString() {
            return src + " - " + dest;
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

    static void addDirectedEdge(ArrayList<Edge>[] graph,
                                int src, int dest) {
        graph[src].add(new Edge(src, dest));
    }

    static void addUndirectedEdge(ArrayList<Edge>[] graph,
                                  int first, int second) {
        graph[first].add(new Edge(first, second));
        graph[second].add(new Edge(second, first));
    }

    /*
     * Problem:
     * Return all strongly connected components of a directed graph.
     * Inside one SCC, every vertex can reach every other vertex.
     *
     * Pattern:
     * Kosaraju's Algorithm - Two DFS Passes + Transpose
     *
     * Approach:
     * 1. Run DFS and push each vertex after its neighbors finish.
     * 2. Reverse every graph edge to build the transpose.
     * 3. Clear visited and pop vertices in finishing-time order.
     * 4. Each DFS on the transpose collects exactly one SCC.
     *
     * Time: O(V + E)
     * Space: O(V + E) including the transpose graph
     */
    static List<List<Integer>> stronglyConnectedComponents(
            ArrayList<Edge>[] graph) {
        boolean[] visited = new boolean[graph.length];
        Deque<Integer> finishOrder = new ArrayDeque<>();

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (!visited[vertex]) {
                fillFinishOrder(graph, vertex, visited, finishOrder);
            }
        }

        ArrayList<Edge>[] transpose = transpose(graph);
        Arrays.fill(visited, false);

        List<List<Integer>> components = new ArrayList<>();
        while (!finishOrder.isEmpty()) {
            int vertex = finishOrder.pop();

            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                collectComponent(
                        transpose, vertex, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private static void fillFinishOrder(
            ArrayList<Edge>[] graph, int current, boolean[] visited,
            Deque<Integer> finishOrder) {
        visited[current] = true;

        for (Edge edge : graph[current]) {
            if (!visited[edge.dest]) {
                fillFinishOrder(
                        graph, edge.dest, visited, finishOrder);
            }
        }

        // Push after all neighbors so later finishing vertices come first.
        finishOrder.push(current);
    }

    private static ArrayList<Edge>[] transpose(
            ArrayList<Edge>[] graph) {
        ArrayList<Edge>[] reversed = createEmptyGraph(graph.length);

        for (ArrayList<Edge> edges : graph) {
            for (Edge edge : edges) {
                addDirectedEdge(reversed, edge.dest, edge.src);
            }
        }
        return reversed;
    }

    private static void collectComponent(
            ArrayList<Edge>[] graph, int current, boolean[] visited,
            List<Integer> component) {
        visited[current] = true;
        component.add(current);

        for (Edge edge : graph[current]) {
            if (!visited[edge.dest]) {
                collectComponent(
                        graph, edge.dest, visited, component);
            }
        }
    }

    /*
     * Problem:
     * Return every bridge in a simple undirected graph.
     * Removing a bridge increases the number of connected components.
     *
     * Pattern:
     * Tarjan's Algorithm - Discovery Time + Low Value
     *
     * Approach:
     * 1. DFS every component and assign discovery times.
     * 2. low[u] stores the earliest ancestor reachable from u's subtree.
     * 3. Update low values after child DFS calls and through back edges.
     * 4. Tree edge u-v is a bridge when low[v] > discovery[u].
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space
     */
    static List<Edge> findBridges(ArrayList<Edge>[] graph) {
        int[] discovery = new int[graph.length];
        int[] low = new int[graph.length];
        Arrays.fill(discovery, -1);

        int[] timer = {0};
        List<Edge> bridges = new ArrayList<>();

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (discovery[vertex] == -1) {
                bridgeDfs(graph, vertex, -1, discovery, low,
                        timer, bridges);
            }
        }
        return bridges;
    }

    private static void bridgeDfs(
            ArrayList<Edge>[] graph, int current, int parent,
            int[] discovery, int[] low, int[] timer,
            List<Edge> bridges) {
        discovery[current] = low[current] = timer[0]++;

        for (Edge edge : graph[current]) {
            int neighbor = edge.dest;

            if (neighbor == parent) {
                continue;
            }

            if (discovery[neighbor] == -1) {
                bridgeDfs(graph, neighbor, current, discovery, low,
                        timer, bridges);

                low[current] = Math.min(low[current], low[neighbor]);

                if (low[neighbor] > discovery[current]) {
                    bridges.add(new Edge(current, neighbor));
                }
            } else {
                // Back edge: neighbor is already an ancestor/reached node.
                low[current] =
                        Math.min(low[current], discovery[neighbor]);
            }
        }
    }

    /*
     * Problem:
     * Return every articulation point in a simple undirected graph.
     * Removing one increases the number of connected components.
     *
     * Pattern:
     * Tarjan's Algorithm - Discovery Time + Low Value
     *
     * Approach:
     * 1. DFS every component and assign discovery and low values.
     * 2. A non-root u is critical when a child v cannot reach above u:
     *    low[v] >= discovery[u].
     * 3. A DFS root is critical when it has more than one DFS child.
     * 4. Use a boolean array so each answer is returned once.
     *
     * Time: O(V + E)
     * Space: O(V) auxiliary space
     */
    static List<Integer> findArticulationPoints(
            ArrayList<Edge>[] graph) {
        int[] discovery = new int[graph.length];
        int[] low = new int[graph.length];
        Arrays.fill(discovery, -1);

        boolean[] articulation = new boolean[graph.length];
        int[] timer = {0};

        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (discovery[vertex] == -1) {
                articulationDfs(graph, vertex, -1, discovery, low,
                        timer, articulation);
            }
        }

        List<Integer> answer = new ArrayList<>();
        for (int vertex = 0; vertex < graph.length; vertex++) {
            if (articulation[vertex]) {
                answer.add(vertex);
            }
        }
        return answer;
    }

    private static void articulationDfs(
            ArrayList<Edge>[] graph, int current, int parent,
            int[] discovery, int[] low, int[] timer,
            boolean[] articulation) {
        discovery[current] = low[current] = timer[0]++;
        int children = 0;

        for (Edge edge : graph[current]) {
            int neighbor = edge.dest;

            if (neighbor == parent) {
                continue;
            }

            if (discovery[neighbor] == -1) {
                children++;
                articulationDfs(graph, neighbor, current,
                        discovery, low, timer, articulation);

                low[current] = Math.min(low[current], low[neighbor]);

                if (parent != -1
                        && low[neighbor] >= discovery[current]) {
                    articulation[current] = true;
                }
            } else {
                low[current] =
                        Math.min(low[current], discovery[neighbor]);
            }
        }

        // Separate root rule: its independent DFS child subtrees cannot
        // reach one another without the root.
        if (parent == -1 && children > 1) {
            articulation[current] = true;
        }
    }

    public static void main(String[] args) {
        ArrayList<Edge>[] directedGraph = createEmptyGraph(5);
        addDirectedEdge(directedGraph, 0, 2);
        addDirectedEdge(directedGraph, 0, 3);
        addDirectedEdge(directedGraph, 1, 0);
        addDirectedEdge(directedGraph, 2, 1);
        addDirectedEdge(directedGraph, 3, 4);
        System.out.println("Strongly connected components: "
                + stronglyConnectedComponents(directedGraph));

        ArrayList<Edge>[] undirectedGraph = createEmptyGraph(6);
        addUndirectedEdge(undirectedGraph, 0, 1);
        addUndirectedEdge(undirectedGraph, 1, 2);
        addUndirectedEdge(undirectedGraph, 2, 0);
        addUndirectedEdge(undirectedGraph, 0, 3);
        addUndirectedEdge(undirectedGraph, 3, 4);
        addUndirectedEdge(undirectedGraph, 4, 5);
        addUndirectedEdge(undirectedGraph, 5, 3);

        System.out.println("Bridges: "
                + findBridges(undirectedGraph));
        System.out.println("Articulation points: "
                + findArticulationPoints(undirectedGraph));
    }
}

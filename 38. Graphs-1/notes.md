# 38. Graphs Part 1

## 1. Core Idea

A **graph** represents objects and the connections between them.

- A **vertex** (or node) is an object, such as a city or person.
- An **edge** is a connection between two vertices.
- `V` means the number of vertices.
- `E` means the number of edges.

Examples include road maps, social networks, computer networks, course prerequisites, and website links.

### Types of graphs based on edges

**Directed graph**

An edge has one direction. `A -> B` does not automatically mean `B -> A`.

```text
A ----> B
```

**Undirected graph**

The connection works in both directions.

```text
A ----- B
```

**Bidirectional edges**

A directed graph can contain both `A -> B` and `B -> A`. These are two directed edges.

**Weighted graph**

Each edge has a value such as distance, price, or time.

```text
A --5-- B
```

**Unweighted graph**

Edges have no meaningful cost. We only care whether the connection exists.

### Graph representations

| Representation | Space | Check edge | Visit neighbors | Good choice when |
|---|---:|---:|---:|---|
| Adjacency list | O(V + E) | O(degree) | O(degree) | Graph is sparse; most interview problems |
| Adjacency matrix | O(V²) | O(1) | O(V) | Graph is dense or fast edge checks matter |
| Edge list | O(E) | O(E) | O(E) | Algorithms process edges directly |
| 2D grid | O(rows × cols) | Depends | Usually O(1) neighbors | Grid itself is an implicit graph |

The chapter code uses an **adjacency list**:

```java
ArrayList<Edge>[] graph;
```

`graph[u]` contains all outgoing edges from vertex `u`.

```java
static class Edge {
    int src;
    int dest;
    int weight;
}
```

For a directed edge `0 -> 1`, store one edge in `graph[0]`.

For an undirected edge `0 - 1`, store both:

```java
graph[0].add(new Edge(0, 1, weight));
graph[1].add(new Edge(1, 0, weight));
```

The two stored objects still represent one logical undirected edge. The space remains O(V + E), because `2E` simplifies to O(E).

### Graph applications

- Maps: cities are vertices and roads are edges.
- Social networks: users are vertices and friendships/follows are edges.
- Networks: devices are vertices and links are edges.
- Dependencies: tasks or courses are vertices and prerequisites are directed edges.
- Web pages: pages are vertices and hyperlinks are directed edges.
- Grids: each cell is a vertex connected to nearby cells.

## 2. How to Recognize This Pattern

Think of a graph when the question contains:

- objects connected to other objects;
- roads, flights, friends, networks, links, or dependencies;
- “is there a path?” or “can I reach?”;
- connected groups or components;
- level-by-level exploration;
- shortest path in an **unweighted** graph;
- all possible neighboring states;
- a matrix where movement is allowed between nearby cells.

Choose the traversal:

- **BFS** when levels or the shortest number of unweighted edges matter.
- **DFS** when exploring a complete branch, checking reachability, or backtracking is natural.

## 3. Problems in This Folder

### Breadth-First Search (BFS)

**What the question asks**

Visit all vertices level by level, starting with the closest neighbors.

**Basic/less efficient approach**

- With an adjacency matrix, remove a vertex from the queue and scan all `V` possible neighbors.
- Time: O(V²). Auxiliary space: O(V).
- This is wasteful for a sparse graph because most matrix entries are empty.

**Optimized approach**

- Use an adjacency list and a FIFO queue.
- Mark the start visited when adding it to the queue.
- Remove one vertex, record it, then enqueue each unvisited neighbor.
- If the graph may be disconnected, start another BFS from every unvisited vertex.
- Time: O(V + E).
- Auxiliary space: O(V) for the queue, visited array, and returned traversal order.
- The graph itself uses O(V + E) space.

**Why it works**

A queue processes vertices in the same order they are discovered. Therefore, all vertices one edge away are processed before vertices two edges away.

**Interview explanation**

“I use a queue because BFS explores in first-in, first-out order. I mark a vertex when I enqueue it so it cannot be added more than once. Then I repeatedly remove a vertex and enqueue its unvisited neighbors. With an adjacency list, every vertex and edge is processed a constant number of times, so the complexity is O(V+E).”

**Common follow-up questions**

- Why mark visited when enqueuing? It prevents two neighbors from placing the same vertex in the queue.
- Why is the complexity not O(V × E)? Each vertex is queued once, and adjacency lists are scanned once overall.
- Can BFS find a shortest path? Yes, it finds the minimum number of edges in an unweighted graph.
- Why loop over all vertices outside BFS? A single start cannot reach other disconnected components.

**Dry run**

For edges `0-1`, `0-2`, `1-3`, `2-4`:

| Step | Removed | Queue after adding neighbors | Order |
|---:|---:|---|---|
| Start | — | `[0]` | `[]` |
| 1 | 0 | `[1, 2]` | `[0]` |
| 2 | 1 | `[2, 3]` | `[0, 1]` |
| 3 | 2 | `[3, 4]` | `[0, 1, 2]` |
| 4 | 3 | `[4]` | `[0, 1, 2, 3]` |
| 5 | 4 | `[]` | `[0, 1, 2, 3, 4]` |

**Common mistakes**

- Forgetting the visited array in a graph with cycles.
- Marking too late and adding the same vertex many times.
- Using a stack instead of a queue.
- Traversing only from vertex 0 when the graph may be disconnected.
- Claiming that BFS gives a minimum weighted cost; ordinary BFS only minimizes edge count.

### Depth-First Search (DFS)

**What the question asks**

Visit all vertices by following one branch as deeply as possible before returning.

**Basic/less efficient approach**

- DFS with an adjacency matrix scans all `V` possible neighbors for every vertex.
- Time: O(V²). Auxiliary space: O(V).
- It checks many nonexistent edges in a sparse graph.

**Optimized approach**

- Mark the current vertex visited.
- Record it, then recursively visit each unvisited adjacency-list neighbor.
- The recursion naturally backtracks after a vertex has no unvisited neighbor.
- Start from every unvisited vertex if the graph can be disconnected.
- Time: O(V + E).
- Auxiliary space: O(V) for visited, recursion stack, and returned order.

**Why it works**

DFS marks every reached vertex and completely explores its unvisited neighbors. The visited array prevents cycles from sending recursion back to an already explored vertex.

**Interview explanation**

“For DFS, I mark the current vertex visited and recursively visit every unvisited neighbor. When a branch has no new neighbor, recursion returns to the previous vertex and continues there. With an adjacency list, each vertex and edge is examined only a constant number of times, giving O(V+E) time.”

**Common follow-up questions**

- Can DFS be iterative? Yes, replace recursion with an explicit stack.
- Why is recursive space O(V)? A path can contain all vertices, producing V stack frames.
- Does DFS order have to be unique? No. It depends on the order of neighbors in the adjacency list.
- What can DFS solve? Reachability, connected components, cycle detection, and many backtracking problems.

**Dry run**

Using the same graph and neighbor order:

```text
0 -> 1 -> 3 -> backtrack -> 2 -> 4
```

Traversal: `[0, 1, 3, 2, 4]`.

**Common mistakes**

- Checking `visited[current]` instead of `visited[neighbor]` before recursion.
- Marking a vertex after recursion, which permits a cycle to revisit it.
- Forgetting that recursion can overflow for a very deep graph.
- Assuming one particular DFS order when adjacency order is unspecified.

### Has Path Using DFS

**What the question asks**

Return whether at least one route exists from source vertex `src` to destination vertex `dest`.

**Brute-force approach**

- Generate every possible route from the source and check whether it reaches the destination.
- Without a visited set, cyclic graphs can loop forever.
- Enumerating simple paths can be exponential because a graph may contain many different routes.

**Optimized approach**

- If `current == dest`, return true.
- Mark the current vertex visited.
- Recursively search each unvisited neighbor.
- Return true immediately when one recursive branch succeeds.
- Time: O(V + E) worst case.
- Space: O(V) for the visited array and recursion stack.

**Why it works**

DFS explores every vertex reachable from the source unless it finds the destination earlier. If all reachable branches fail, no path from the source to that destination exists.

**Interview explanation**

“I run DFS from the source. If the current vertex is the destination, I return true. Otherwise, I mark it visited and recursively search every unvisited neighbor. I stop as soon as one branch succeeds. In the worst case, DFS examines the entire reachable graph, so time is O(V+E) and space is O(V).”

**Common follow-up questions**

- What if `src == dest`? Return true immediately; the zero-edge path reaches itself.
- Does this return the actual path? No. To reconstruct it, store each vertex’s parent.
- Does direction matter? Yes. In a directed graph, only outgoing edges may be followed.
- Could BFS solve it? Yes. Both BFS and DFS can test reachability in O(V+E).

**Dry run**

For `0 -> 1`, `1 -> 3`, and `3 -> 5`, searching from 0 to 5:

```text
current 0: not destination
current 1: not destination
current 3: not destination
current 5: destination -> true
```

The `true` result returns through every recursive call.

**Common mistakes**

- Forgetting `return true` when a recursive call succeeds.
- Reusing an old visited array for a separate query without resetting it.
- Adding the reverse edge in a directed graph.
- Forgetting bounds or valid-vertex assumptions.

## 4. Topic-Level Interview Questions

**What is a graph?**  
A collection of vertices and edges that represent objects and their connections.

**Directed versus undirected graph?**  
A directed edge can be followed only in its stored direction. An undirected edge connects both directions.

**Weighted versus unweighted graph?**  
A weighted edge stores a cost such as distance or time. An unweighted edge represents only a connection.

**What is a vertex’s degree?**  
In an undirected graph, it is the number of incident edges. Directed graphs have separate in-degree and out-degree.

**Why use an adjacency list?**  
It uses O(V+E) space and lets us visit only real neighbors, making it efficient for sparse graphs.

**Adjacency list versus matrix?**  
A list saves space and traverses sparse graphs efficiently. A matrix uses O(V²) space but checks whether a particular edge exists in O(1).

**BFS versus DFS?**  
BFS explores level by level with a queue. DFS explores one branch deeply using recursion or a stack.

**Why is visited necessary?**  
Graphs may contain cycles and multiple routes to one vertex. Visited prevents repeated work and infinite traversal.

**What is a connected component?**  
In an undirected graph, it is a maximal group of vertices that can reach one another.

**Why is traversal O(V+E)?**  
Every vertex is visited once, and all adjacency-list entries are examined once. In an undirected graph, each edge appears twice, but O(2E) is O(E).

**Is traversal order fixed?**  
No. Valid BFS and DFS orders can differ based on the order in which neighbors were stored.

## 5. Quick Revision Sheet

### Important patterns

- Adjacency list: `graph[u]` stores edges leaving `u`.
- Directed edge: store `u -> v` once.
- Undirected edge: store both `u -> v` and `v -> u`.
- BFS: queue + visited.
- DFS: recursion/stack + visited.
- Disconnected graph: loop over every vertex and start at each unvisited one.
- Reachability: DFS or BFS from the source.

### Complexities

| Task | Time | Auxiliary space |
|---|---:|---:|
| Build adjacency list | O(V + E) | O(V + E) graph storage |
| BFS | O(V + E) | O(V) |
| DFS | O(V + E) | O(V) |
| Has path | O(V + E) worst case | O(V) |
| Check one matrix edge | O(1) | Matrix needs O(V²) total |

### One-line reminders

- Mark visited before repeated paths can add or recurse into a vertex.
- BFS uses FIFO; DFS uses LIFO behavior.
- Ordinary BFS gives shortest paths only in unweighted graphs.
- DFS order depends on adjacency-list order.
- For directed graphs, do not automatically add the reverse edge.
- `src == dest` is immediately true for Has Path.
- Graph space is separate from traversal’s auxiliary space.

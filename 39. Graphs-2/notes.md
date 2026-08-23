# 39. Graphs Part 2

## 1. Core Idea

Graph Part 2 builds on BFS, DFS, adjacency lists, and visited arrays.
The main questions are:

- How many separate connected groups exist?
- Does an undirected graph contain a cycle?
- Can an undirected graph be split into two groups?
- Does a directed graph contain a cycle?
- In what valid order can directed dependencies be completed?

### Connected and disconnected graphs

An undirected graph is **connected** if every vertex can reach every other
vertex. If not, it contains multiple **connected components**.

```text
0 -- 1 -- 2     3 -- 4     5

Component 1     Component 2 Component 3
```

A traversal starting from `0` cannot reach vertices `3`, `4`, or `5`.
Therefore, graph algorithms that must process the entire graph use this
outer loop:

```java
for (int vertex = 0; vertex < graph.length; vertex++) {
    if (!visited[vertex]) {
        dfs(vertex);
    }
}
```

### What is a cycle?

A cycle is a path that returns to its starting vertex.

- In an **undirected graph**, DFS must ignore the edge back to the parent.
- In a **directed graph**, direction matters. A cycle exists when DFS finds
  an edge back to a vertex in the current recursion path.

The undirected and directed methods are different. Do not use the
parent-based undirected method for a directed graph.

### What is a bipartite graph?

A graph is bipartite if its vertices can be colored using two colors so
that every edge connects different colors.

```text
Color 0:  0, 2
Color 1:  1, 3

0 -- 1
|    |
3 -- 2
```

An undirected graph is bipartite exactly when it has no odd-length cycle.
Even cycles are allowed.

### What is topological sorting?

A topological order is a linear ordering of a **directed acyclic graph
(DAG)**. For every edge `u -> v`, `u` appears before `v`.

Applications include:

- course prerequisites;
- task scheduling;
- build dependencies;
- package installation order.

A graph can have more than one valid topological order. A directed graph
with a cycle has no valid topological order.

## 2. How to Recognize This Pattern

Use these clues:

- “separate groups,” “provinces,” or “connected regions”:
  connected components;
- “does a loop exist?” in an undirected graph:
  DFS with parent tracking;
- “divide into two groups,” “two colors,” or “no adjacent equals”:
  bipartite checking;
- “cycle in prerequisites/dependencies”:
  directed DFS with a current-path array;
- “valid dependency order” or “finish before”:
  topological sorting;
- the graph may be disconnected:
  place an outer loop around BFS or DFS.

## 3. Problems in This Folder

### Connected Components

**What the question asks**

Return the separate groups of reachable vertices in an undirected graph.

**Brute-force approach**

- Run a new reachability search between many pairs of vertices.
- This repeats traversal work and can take O(V(V + E)).
- Space is O(V) per traversal.

**Optimized approach**

- Keep one visited array for the entire graph.
- Scan vertices from `0` to `V - 1`.
- An unvisited vertex starts a new component.
- Run DFS once to collect everything reachable from that start.
- Time: O(V + E).
- Auxiliary space: O(V), excluding the returned component lists.

**Why it works**

One DFS reaches exactly the vertices in the start vertex’s component.
Those vertices are marked, so no later DFS processes that component again.

**Interview explanation**

“I keep one visited array and scan every vertex. Whenever I find an
unvisited vertex, I know it belongs to a new component, so I start DFS and
collect every vertex reachable from it. Each vertex and edge is processed
once overall, giving O(V+E) time and O(V) auxiliary space.”

**Common follow-up questions**

- Does an isolated vertex count? Yes, it is a component containing one vertex.
- Why is the outer loop necessary? One DFS cannot reach a disconnected component.
- Can BFS be used? Yes, BFS and DFS both find components in O(V + E).
- What about directed graphs? “Connected” has strong and weak variants;
  this method is for undirected graphs.

**Dry run**

For edges `0-1`, `1-2`, and `3-4`, with isolated vertex `5`:

| Start scanned | Action | Components |
|---:|---|---|
| 0 | DFS reaches 0, 1, 2 | `[[0,1,2]]` |
| 1, 2 | Already visited | unchanged |
| 3 | DFS reaches 3, 4 | `[[0,1,2],[3,4]]` |
| 5 | Isolated new component | `[[0,1,2],[3,4],[5]]` |

**Common mistakes**

- Creating a new visited array for every start.
- Forgetting isolated vertices.
- Starting only from vertex 0.
- Adding reverse edges incorrectly when the input is already undirected.

### Cycle Detection in an Undirected Graph

**What the question asks**

Return whether a simple undirected graph contains any cycle.

**Brute-force approach**

- Generate possible paths and check whether one returns to an earlier
  vertex.
- The number of simple paths can be exponential.
- Without visited tracking, the search can also repeat forever.

**Optimized approach**

- Run DFS with both `current` and `parent`.
- Recurse normally into an unvisited neighbor.
- Ignore a visited neighbor when it is the parent because every
  undirected edge is stored in both directions.
- A visited neighbor different from the parent proves a cycle.
- Time: O(V + E).
- Space: O(V) for visited and recursion.

**Why it works**

The parent connection is the edge DFS just used to arrive, so it is not a
cycle. Any other visited neighbor creates a second route to a previously
reached vertex, closing a loop.

**Interview explanation**

“In an undirected graph, every edge appears in both directions, so seeing
the parent again is expected. During DFS I pass the parent vertex. If I
find an already visited neighbor that is not the parent, there is another
route back to a visited vertex, which proves a cycle. The traversal is
O(V+E).”

**Common follow-up questions**

- Why pass the parent? To avoid treating the reverse copy of the same edge
  as a cycle.
- Why check every component? A cycle may exist outside the component
  containing vertex 0.
- Does a tree contain a cycle? No; a connected acyclic undirected graph is
  a tree.
- What input is assumed? The standard parent method assumes a simple
  graph without parallel edges.

**Dry run**

For triangle `0-1-2-0`:

```text
DFS 0 -> 1 -> 2
At 2:
  neighbor 1 = parent, ignore
  neighbor 0 = visited and not parent, cycle found
```

**Common mistakes**

- Returning true for the parent edge.
- Forgetting to return the successful recursive result.
- Using this exact method for directed graphs.
- Checking only one connected component.

### Bipartite Graph

**What the question asks**

Determine whether an undirected graph can be divided into two groups with
no edge inside the same group.

**Brute-force approach**

- Try all two-color assignments for `V` vertices.
- There are `2^V` assignments, and checking all edges adds O(E) work.
- Time: O(2^V × E). Space: O(V).

**Optimized approach**

- Store `-1` for uncolored, `0` for the first color, and `1` for the second.
- Start BFS from every uncolored component.
- Give an uncolored neighbor the opposite color:
  `neighborColor = 1 - currentColor`.
- If a neighbor already has the current color, return false.
- Time: O(V + E).
- Space: O(V) for the color array and queue.

**Why it works**

Every edge requires opposite endpoint colors. BFS propagates that
requirement across the component. A same-color edge is a contradiction,
so no valid two-group division exists.

**Interview explanation**

“I use BFS and color values minus one, zero, and one. Each uncolored
neighbor receives the opposite color from the current vertex. If I ever
find an edge whose endpoints already have the same color, the graph is not
bipartite. I restart BFS for uncolored components, and the total complexity
is O(V+E).”

**Common follow-up questions**

- Are all trees bipartite? Yes, because trees contain no cycles.
- Is every cyclic graph non-bipartite? No. Even cycles are bipartite; odd
  cycles are not.
- Why process every component? One component may be bipartite while another
  contains an odd cycle.
- Could DFS color the graph? Yes, DFS works with the same opposite-color rule.

**Dry run**

For a square `0-1-2-3-0`:

| Removed | Its color | New coloring |
|---:|---:|---|
| 0 | 0 | color 1 and 3 with 1 |
| 1 | 1 | color 2 with 0 |
| 3 | 1 | vertex 2 already has opposite color |
| 2 | 0 | all neighbors have color 1 |

No conflict occurs, so the square is bipartite.

**Common mistakes**

- Using default `0` for “uncolored”; initialize with `-1`.
- Coloring a neighbor with the same color.
- Assuming an even cycle is invalid.
- Running BFS from only one component.

### Cycle Detection in a Directed Graph

**What the question asks**

Return whether a directed graph contains a cycle that follows edge
directions.

**Brute-force approach**

- Generate directed paths from every vertex and search for a return to the
  start.
- The number of paths can be exponential.
- A normal visited array alone cannot distinguish a cycle from a completed
  branch.

**Optimized approach**

- `visited[v]` means the vertex has been discovered at some time.
- `inCurrentPath[v]` means it is an active ancestor in the current DFS.
- Set both when entering a vertex.
- An edge to a vertex in the current path is a back edge and proves a cycle.
- Clear `inCurrentPath[current]` while backtracking.
- Time: O(V + E).
- Space: O(V) for arrays and recursion.

**Why it works**

A directed cycle must point back to an active ancestor in the same DFS
chain. An edge to a visited but completed vertex joins another finished
path and does not by itself form a cycle.

**Interview explanation**

“For a directed graph, I track both globally visited vertices and vertices
in the active recursion path. If an edge points to an active vertex, it
returns to an ancestor and forms a directed cycle. When DFS finishes a
vertex, I remove it from the active path. This takes O(V+E) time and O(V)
space.”

**Common follow-up questions**

- Why is visited alone insufficient? A visited neighbor may belong to a
  completed branch rather than the current path.
- Why clear the path flag? Once DFS backtracks, that vertex is no longer an
  ancestor of future calls.
- Can Kahn’s algorithm detect a cycle? Yes. If it processes fewer than V
  vertices, a directed cycle exists.
- Why not use the undirected parent rule? Direction changes what counts as
  returning along the same edge.

**Dry run**

For `0 -> 1 -> 2 -> 0`:

```text
Enter 0: path {0}
Enter 1: path {0,1}
Enter 2: path {0,1,2}
Edge 2 -> 0 reaches an active vertex: cycle
```

**Common mistakes**

- Never clearing `inCurrentPath`.
- Treating every edge to a visited vertex as a cycle.
- Adding reverse edges to a directed graph.
- Forgetting disconnected directed components.

### Topological Sorting Using DFS

**What the question asks**

Return a valid order of a DAG in which every source appears before its
destination.

**Brute-force approach**

- Try permutations of all vertices and check every edge.
- There are V! possible orders.
- Time: O(V! × E). Space: O(V).

**Optimized approach**

- Run DFS from every unvisited vertex.
- Explore all outgoing neighbors first.
- Push the current vertex after all its destinations are finished.
- Pop the stack to reverse finishing order.
- Time: O(V + E).
- Space: O(V) for visited, recursion, stack, and returned order.
- This method assumes the input is a DAG.

**Why it works**

For an edge `u -> v`, DFS finishes and pushes `v` before it pushes `u`.
Popping the stack reverses that finishing order, placing `u` before `v`.

**Interview explanation**

“I run DFS and push each vertex only after all of its outgoing neighbors
have been processed. That means dependencies finish into the stack before
the vertex that points to them. When I pop the stack, the order reverses,
so every source appears before its destination. This is O(V+E) for a DAG.”

**Common follow-up questions**

- Can there be multiple correct orders? Yes, vertices without ordering
  constraints may appear in different positions.
- What if the graph has a cycle? No topological order exists. Run directed
  cycle detection or use Kahn’s algorithm to detect it.
- Why push after recursion? Pushing before recursion would not guarantee
  that sources appear before destinations after reversal.
- Can topological sorting be done with BFS? Yes, Kahn’s algorithm uses
  in-degrees and a queue.

**Dry run**

For edges `5 -> 0`, `5 -> 2`, `2 -> 3`, and `3 -> 1`:

```text
DFS from 5 finishes: 0, then 1, 3, 2, then 5
Stack pop order: 5, 2, 3, 1, 0
```

Every source occurs before its destination.

**Common mistakes**

- Applying topological sorting to an undirected graph.
- Returning DFS visitation order instead of reversed finishing order.
- Forgetting vertices with no incoming or outgoing edges.
- Claiming a valid order for a cyclic graph.

## 4. Topic-Level Interview Questions

**What is a connected component?**  
A maximal group of vertices that can reach one another in an undirected
graph.

**How do you process a disconnected graph?**  
Scan all vertices and start BFS or DFS whenever a vertex is still
unvisited.

**How does undirected cycle detection work?**  
During DFS, a visited neighbor other than the parent proves a cycle.

**How does directed cycle detection work?**  
During DFS, an edge to a vertex in the active recursion path proves a cycle.

**Why are the two cycle algorithms different?**  
An undirected edge is stored both ways, so its parent edge must be ignored.
A directed graph instead needs to know whether an edge returns to an active
ancestor.

**What makes a graph bipartite?**  
It can be colored using two colors with different colors at the endpoints
of every edge.

**What is the odd-cycle rule?**  
An undirected graph is bipartite if and only if it contains no odd-length
cycle.

**What is a DAG?**  
A directed acyclic graph: it has directed edges and no directed cycle.

**When does a topological order exist?**  
Only for a DAG.

**Is topological order unique?**  
Not necessarily. It is unique only when the constraints force one choice
at every position.

**Why are all these algorithms O(V + E)?**  
Each vertex is processed once, and the adjacency lists inspect each edge a
constant number of times.

## 5. Quick Revision Sheet

### Important patterns

- Components: outer vertex loop + DFS/BFS.
- Undirected cycle: visited neighbor `!= parent`.
- Bipartite: `-1` uncolored; next color is `1 - currentColor`.
- Directed cycle: visited + active recursion path.
- Topological DFS: push **after** visiting all neighbors.
- Topological ordering is valid only for a DAG.

### Complexities

| Algorithm | Time | Auxiliary space |
|---|---:|---:|
| Connected components | O(V + E) | O(V) |
| Undirected cycle detection | O(V + E) | O(V) |
| Bipartite check | O(V + E) | O(V) |
| Directed cycle detection | O(V + E) | O(V) |
| DFS topological sort | O(V + E) | O(V) |

### One-line reminders

- An isolated vertex is its own component.
- Check every component, not only vertex 0.
- Ignore only the parent edge in undirected cycle DFS.
- Even cycle: possibly bipartite; odd cycle: never bipartite.
- Clear the directed recursion-path flag during backtracking.
- A visited directed neighbor is a cycle only when it is still active.
- Push a topological-sort vertex after its neighbors.
- Different valid traversal and topological orders are normal.

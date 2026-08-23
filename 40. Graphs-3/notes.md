# 40. Graphs Part 3

## 1. Core Idea

Graph Part 3 introduces three different goals:

- create a dependency order with **Kahn’s algorithm**;
- generate every route with **DFS and backtracking**;
- find minimum weighted distances with **Dijkstra’s algorithm**.

They all use an adjacency list, but their supporting data structures differ:

| Problem | Main data structure | Main idea |
|---|---|---|
| Kahn’s topological sort | Queue + indegree array | Process vertices with no remaining prerequisites |
| All paths | Recursion + current path | Choose, explore, and undo |
| Dijkstra | Min-priority queue + distances | Always process the closest known vertex |

### Indegree and outdegree

For a directed vertex:

- **indegree** is the number of incoming edges;
- **outdegree** is the number of outgoing edges.

```text
A ----> B ----> C
        ^
        |
        D
```

`B` has indegree 2 (`A -> B`, `D -> B`) and outdegree 1 (`B -> C`).

### Relaxation

Dijkstra repeatedly tries to improve a known distance across an edge
`u -> v` with weight `w`:

```text
if dist[u] + w < dist[v]
    dist[v] = dist[u] + w
```

This operation is called **relaxation**.

### Important assumptions

- Topological sorting works only when a directed graph has no cycle.
- The course’s All Paths problem uses a directed acyclic graph. The code
  also prevents revisiting a vertex in the current path, so it returns
  simple paths safely if a cycle is present.
- Dijkstra requires every edge weight to be non-negative.

## 2. How to Recognize This Pattern

Use these clues:

- “dependency order,” “prerequisites,” or “incoming edges”:
  Kahn’s topological sort;
- “return/print every route from source to target”:
  DFS and backtracking;
- “shortest distance from one source to all vertices” with non-negative
  weighted edges:
  Dijkstra;
- “minimum number of edges” in an unweighted graph:
  ordinary BFS instead of Dijkstra;
- negative edge weights:
  do not use Dijkstra; consider Bellman-Ford in a later chapter.

## 3. Problems in This Folder

### Topological Sort Using BFS (Kahn’s Algorithm)

**What the question asks**

Return a valid order of a directed graph where every source vertex appears
before its destination.

**Brute-force approach**

- Generate permutations of all `V` vertices.
- For each permutation, check whether every edge follows the ordering.
- Time: O(V! × (V + E)).
- Space: O(V) for one permutation.

**Optimized approach**

- Count each vertex’s indegree by scanning all edges.
- Add all vertices with indegree 0 to a queue.
- Remove a vertex, add it to the answer, and conceptually remove its
  outgoing edges by decrementing destination indegrees.
- Enqueue a destination when its indegree becomes 0.
- If the result contains fewer than `V` vertices, a cycle exists.
- Time: O(V + E).
- Auxiliary space: O(V) for indegrees, queue, and result.

**Why it works**

A zero-indegree vertex has no unfinished prerequisite, so it is safe to
place next. Removing its outgoing edges may make other vertices available.
A directed cycle never reaches indegree zero for all of its vertices.

**Interview explanation**

“I first calculate every vertex’s indegree and enqueue all vertices with
zero incoming edges. Each time I remove one, I add it to the order and
decrease the indegree of its destinations. A destination enters the queue
when all of its prerequisites are removed. This is O(V+E), and processing
fewer than V vertices tells me there is a cycle.”

**Common follow-up questions**

- Why can all zero-indegree vertices be used? None depends on another
  remaining vertex.
- Can there be multiple valid answers? Yes, choosing zero-indegree vertices
  in a different order can produce another valid ordering.
- How does Kahn’s algorithm detect a cycle? A cycle leaves some indegrees
  positive, so the processed count is less than V.
- Kahn versus DFS topological sort? Both are O(V + E); Kahn uses indegrees
  and a queue, while DFS uses reverse finishing order.

**Dry run**

For `0 -> 2`, `1 -> 2`, and `2 -> 3`:

| Step | Queue | Removed | Indegree changes | Order |
|---:|---|---:|---|---|
| Start | `[0,1]` | — | `[0,0,2,1]` | `[]` |
| 1 | `[1]` | 0 | `indegree[2]: 2 -> 1` | `[0]` |
| 2 | `[2]` | 1 | `indegree[2]: 1 -> 0` | `[0,1]` |
| 3 | `[3]` | 2 | `indegree[3]: 1 -> 0` | `[0,1,2]` |
| 4 | `[]` | 3 | — | `[0,1,2,3]` |

**Common mistakes**

- Calculating outdegree instead of indegree.
- Enqueuing only one initial zero-indegree vertex.
- Decrementing the current vertex’s indegree instead of its destination’s.
- Forgetting to detect a cycle when fewer than V vertices are processed.
- Assuming the topological order is unique.

### All Paths From Source to Target

**What the question asks**

Return every simple directed path beginning at `src` and ending at `dest`.

**Brute-force approach**

- Generate vertex sequences and test whether consecutive vertices have
  edges and the sequence reaches the target.
- There can be exponentially many sequences.
- A loose general bound can grow as high as factorial time for simple paths.

**Optimized approach**

- Start a DFS path with the source.
- Add the current vertex to one shared path list.
- At the destination, save a **copy** of that list.
- Recursively try each neighbor not already in the current path.
- Remove the current vertex while returning so another choice can reuse
  the list.
- Time: exponential because the output itself may contain exponentially
  many paths; O(V × 2^V) worst case for a DAG.
- Auxiliary space: O(V) for recursion, current path, and path flags.
- Returned output: O(P × V) for `P` paths of length at most `V`.

**Why it works**

DFS explores every possible next edge. Backtracking removes the previous
choice, returning the path to the correct state before exploring a sibling
branch.

**Interview explanation**

“I use DFS with backtracking. I add the current vertex to the path, and
when I reach the target I save a copy. Otherwise, I recursively explore
each valid neighbor. Before returning, I remove the current vertex so the
same list can build the next path. The running time is necessarily
exponential because the graph itself can contain exponentially many
source-to-target paths.”

**Common follow-up questions**

- Why copy the path at the destination? The shared path list changes during
  backtracking; storing the same reference would corrupt saved answers.
- Why remove the last vertex? It undoes the current choice before another
  branch is explored.
- Why track the current path? It prevents a cycle from causing infinite
  recursion and restricts results to simple paths.
- Can this be made polynomial? Not when every path must be returned,
  because the number of answers may already be exponential.

**Dry run**

For `5 -> 0`, `5 -> 2`, `0 -> 3`, `2 -> 3`, and `3 -> 1`:

```text
Choose 5 -> 0 -> 3 -> 1: save [5,0,3,1]
Backtrack to 5
Choose 5 -> 2 -> 3 -> 1: save [5,2,3,1]
```

**Common mistakes**

- Saving `currentPath` without creating a new copy.
- Forgetting to remove the final choice during backtracking.
- Printing only the first successful path.
- Using a permanent visited array, which can incorrectly block a vertex
  from a different valid path.
- Claiming O(V + E) when all paths are requested.

### Dijkstra’s Algorithm

**What the question asks**

Find the shortest distance from one source to every vertex in a weighted
graph with non-negative edge weights.

**Brute-force approach**

- Enumerate all simple paths from the source and keep each destination’s
  smallest cost.
- A graph can have exponentially many paths.
- Time is exponential and recursion uses O(V) auxiliary space.

**Optimized approach**

- Initialize the source distance to 0 and every other distance to infinity.
- Store `(vertex, distance)` pairs in a min-priority queue.
- Remove the pair with the smallest known distance.
- For each edge `u -> v`, try relaxation:
  `dist[u] + weight < dist[v]`.
- If improved, update `dist[v]` and add a new queue entry.
- Skip a removed entry when its distance no longer matches `dist[vertex]`;
  it is stale.
- Time: O((V + E) log V) with an adjacency list and binary heap.
- Auxiliary space: O(V + E) worst case for distances and lazy priority
  queue entries.

**Why it works**

With non-negative weights, taking the smallest current distance is safe:
any alternative route reaching that vertex later cannot become cheaper by
adding non-negative edges.

**Interview explanation**

“I initialize the source to zero and use a min-priority queue ordered by
distance. I repeatedly take the closest current entry and relax all
outgoing edges. Whenever a shorter route is found, I update the distance
and push the new pair. Old queue pairs are skipped as stale. This gives
O((V+E) log V), and it requires non-negative weights.”

**Common follow-up questions**

- Why does Dijkstra fail with negative edges? A vertex considered closest
  could later receive a cheaper path through a negative edge, breaking the
  greedy decision.
- What does infinity mean? The vertex has not yet been reached; if it stays
  infinity, it is unreachable from the source.
- Why can the priority queue contain duplicates? Java’s `PriorityQueue`
  has no simple decrease-key operation, so an improved pair is added and
  the old pair becomes stale.
- Does Dijkstra return actual paths? The code returns distances. Store a
  `parent[]` during relaxation to reconstruct paths.
- Directed or undirected? It works for either when the adjacency list
  stores the intended directions and all weights are non-negative.

**Dry run**

For `0 -> 1 (2)`, `0 -> 2 (4)`, and `1 -> 2 (1)`:

| Removed | Relaxation | Distances `[0,1,2]` |
|---:|---|---|
| 0 | set 1 to 2; set 2 to 4 | `[0,2,4]` |
| 1 | `2 + 1 < 4`, update 2 | `[0,2,3]` |
| 2 | no outgoing improvement | `[0,2,3]` |
| stale 2 at cost 4 | skip | `[0,2,3]` |

**Common mistakes**

- Using Dijkstra when a negative edge exists.
- Ordering the priority queue by vertex number instead of distance.
- Forgetting to add an improved destination back to the queue.
- Adding edge weight to the wrong distance.
- Using integer arithmetic that can overflow near infinity.
- Treating an unreachable vertex’s infinity value as a real distance.

## 4. Topic-Level Interview Questions

**What is indegree?**  
The number of directed edges entering a vertex.

**What is outdegree?**  
The number of directed edges leaving a vertex.

**What is Kahn’s algorithm?**  
A BFS-based topological sort that repeatedly processes vertices whose
indegree is zero.

**How can Kahn’s algorithm detect a cycle?**  
If fewer than V vertices are processed, the remaining vertices are blocked
by a directed cycle.

**Why is All Paths a backtracking problem?**  
The algorithm makes a neighbor choice, explores it, and then undoes that
choice to try another route.

**Why can All Paths be exponential?**  
Different choices can form exponentially many valid routes, all of which
must be produced.

**What does edge relaxation mean?**  
It checks whether reaching a destination through the current vertex gives
a smaller distance and updates it if so.

**Why does Dijkstra use a min-heap?**  
It efficiently retrieves the vertex with the smallest known distance.

**When should BFS be used instead of Dijkstra?**  
For an unweighted graph, or when every edge has the same cost, BFS finds
shortest edge counts more simply in O(V + E).

**Which algorithm handles negative weights?**  
Bellman-Ford handles negative edges and can detect reachable negative
cycles; it is covered later.

## 5. Quick Revision Sheet

### Important patterns

- Kahn: calculate indegrees, enqueue all zeroes, reduce destination indegrees.
- Kahn cycle check: `order.size() != V`.
- All Paths: add, explore, copy at target, remove.
- Never store the shared path reference directly.
- Dijkstra queue pair: `(vertex, distance)`.
- Relaxation: `dist[u] + weight < dist[v]`.
- Skip stale priority-queue entries.

### Complexities

| Algorithm | Time | Auxiliary space |
|---|---:|---:|
| Kahn’s topological sort | O(V + E) | O(V) |
| All Paths in a DAG | O(V × 2^V) worst case | O(V), plus output |
| Dijkstra with binary heap | O((V + E) log V) | O(V + E) worst case |

### One-line reminders

- A topological order exists only for a DAG.
- Kahn starts with every zero-indegree vertex.
- Multiple topological orders can be correct.
- All Paths is output-sensitive and cannot always be polynomial.
- Backtracking must undo the current choice.
- Dijkstra requires non-negative weights.
- Unreachable vertices remain at infinity.
- Priority must be based on distance, not vertex number.

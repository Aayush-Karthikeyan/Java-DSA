# 41. Graphs Part 4

## 1. Core Idea

Graph Part 4 covers two weighted-graph problems:

- **Bellman-Ford:** shortest distances from one source, even when edges
  can be negative.
- **Prim’s algorithm:** minimum total edge cost needed to connect every
  vertex in an undirected graph.

These solve different questions:

| Algorithm | Goal | Graph requirements |
|---|---|---|
| Bellman-Ford | Shortest paths from a source | Directed or undirected; negative edges allowed |
| Prim | Minimum spanning tree | Connected, weighted, undirected graph |

### Relaxation

For an edge `u -> v` with weight `w`, relaxation checks:

```text
if dist[u] is reachable AND dist[u] + w < dist[v]
    dist[v] = dist[u] + w
```

The reachability check is essential. We must not add a weight to infinity.

### Why Bellman-Ford uses V - 1 rounds

A shortest **simple** path can contain at most `V - 1` edges. If it used
more, some vertex would repeat and form a cycle.

Relaxing all edges once can extend correct information by another edge.
Therefore, `V - 1` complete rounds are enough when no reachable negative
cycle exists.

### Negative cycles

A negative cycle has a total weight below zero. Repeating it makes a path
cost smaller forever, so no finite shortest distance exists for affected
vertices.

After `V - 1` rounds, scan every edge once more:

- no improvement: no reachable negative cycle;
- another improvement: a reachable negative cycle exists.

Only cycles reachable from the chosen source affect its shortest paths.

### What is a Minimum Spanning Tree?

A **spanning tree** of a graph:

- includes every vertex;
- is connected;
- contains no cycle;
- contains exactly `V - 1` edges when `V > 0`.

A **minimum spanning tree (MST)** is the spanning tree with the smallest
possible total edge weight.

An MST is not a collection of shortest paths from one source. It minimizes
the total cost of connecting the whole graph.

### Prim’s greedy choice

Prim grows one connected tree. At each step, it chooses the cheapest edge
that connects the current tree to an unvisited vertex.

```text
visited tree ---- cheapest crossing edge ---- new vertex
```

The priority queue stores candidate edges by their individual edge cost.

## 2. How to Recognize This Pattern

Use these clues:

- “shortest paths from one source” with possible negative weights:
  Bellman-Ford;
- “detect a negative-weight cycle reachable from the source”:
  one extra Bellman-Ford relaxation round;
- “connect all cities/computers with minimum total cable or road cost”:
  MST;
- connected, weighted, undirected graph and “minimum spanning tree”:
  Prim or Kruskal;
- all weights are non-negative and faster single-source shortest paths are
  required:
  Dijkstra is usually preferred;
- minimize source-to-destination distance:
  shortest path, not MST.

## 3. Problems in This Folder

### Bellman-Ford Algorithm

**What the question asks**

Find the shortest distance from one source to every vertex when edge
weights may be negative, and report a reachable negative cycle.

**Brute-force approach**

- Enumerate all simple paths from the source and keep the smallest cost for
  each destination.
- A graph can contain exponentially many simple paths.
- Time is exponential and recursion can use O(V) space.

**Optimized approach**

- Initialize the source to `0` and every other distance to infinity.
- Repeat `V - 1` times:
  - scan every edge;
  - relax it when its source is reachable and the new cost is smaller.
- Stop early if a complete round makes no update.
- Scan all edges once more to detect a reachable negative cycle.
- Time: O(VE) worst case.
- Best case with early stopping: O(E) if one round produces no updates.
- Auxiliary space: O(V) for distances.

**Why it works**

After round `i`, shortest-path information can be correct for paths using
up to `i` edges. A simple shortest path uses at most `V - 1` edges, so
those rounds are sufficient unless a reachable negative cycle keeps
reducing a distance.

**Interview explanation**

“I initialize the source to zero and all other distances to infinity.
Then I relax every edge V minus one times, because a simple shortest path
can have at most V minus one edges. I only relax from reachable vertices.
Finally, I scan the edges once more; if any distance can still improve,
there is a reachable negative cycle. The worst-case time is O(VE).”

**Common follow-up questions**

- Why not use Dijkstra? Dijkstra’s greedy choice is not correct with
  negative edges.
- Why check `dist[u] != infinity`? An unreachable source endpoint cannot
  produce a valid route to `v`, and adding to an integer infinity can
  overflow.
- Why exactly `V - 1` rounds? A simple path visits at most V vertices and
  therefore contains at most V - 1 edges.
- Can Bellman-Ford handle a negative cycle? It can detect a reachable one,
  but finite shortest distances do not exist for vertices affected by it.
- Can it stop early? Yes, no update in a complete round means later rounds
  cannot change anything.

**Dry run**

For `0 -> 1 (2)`, `0 -> 2 (4)`, and `1 -> 2 (-4)`:

| Round | Important relaxations | Distances `[0,1,2]` |
|---:|---|---|
| Start | source is 0 | `[0, INF, INF]` |
| 1 | 0→1 gives 2; 0→2 gives 4; 1→2 gives -2 | `[0,2,-2]` |
| 2 | no improvement | `[0,2,-2]` |

The algorithm stops early after round 2.

**Common mistakes**

- Running only one edge-relaxation round.
- Relaxing from an unreachable vertex.
- Using Dijkstra with negative weights instead.
- Forgetting the extra scan for negative-cycle detection.
- Saying every negative edge is a negative cycle.
- Using `int` addition near infinity and causing overflow.

### Minimum Spanning Tree and Prim’s Algorithm

**What the question asks**

For a connected, weighted, undirected graph, choose edges that connect
every vertex with the minimum possible total cost.

**Brute-force approach**

- Try subsets of the `E` edges.
- For each subset, check whether it forms a connected acyclic graph with
  `V - 1` edges.
- Time: O(2^E × (V + E)).
- Space: O(V + E) for checking a subset.

**Optimized approach**

- Begin with vertex 0 and cost 0.
- Use a min-priority queue of candidates `(vertex, parent, edgeCost)`.
- Remove the cheapest candidate.
- Skip it if the vertex is already in the MST.
- Otherwise, add its edge and cost, then offer its edges to unvisited
  neighbors.
- If fewer than V vertices are reached, the graph is disconnected and one
  MST does not exist.
- Time: O(E log V).
- Auxiliary space: O(V + E) in this lazy priority-queue implementation.

**Why it works**

At every stage, Prim selects the cheapest edge crossing from the current
tree to a vertex outside it. The MST cut property guarantees that such a
minimum crossing edge is safe to include.

**Interview explanation**

“I grow one tree from vertex zero. A min-priority queue stores edges that
can connect the current tree to another vertex. I repeatedly choose the
lowest-cost candidate, skip it if that vertex is already included, and
otherwise add its cost and outgoing edges. This greedy choice produces an
MST in O(E log V) time for a connected undirected graph.”

**Common follow-up questions**

- Why is the graph undirected? A standard MST connects an undirected graph;
  directed spanning structures are a different problem.
- Why skip an already visited vertex? Different candidate edges may reach
  it, but including another would create a cycle.
- Does Prim allow negative edge weights? Yes. It compares edge costs and
  can safely choose negative edges.
- What if the graph is disconnected? No single spanning tree exists.
  Running Prim separately on components gives a minimum spanning forest.
- Is the MST always unique? No. Equal-weight choices can produce different
  MSTs with the same minimum total cost.
- Why should the result have V - 1 edges? Every tree with V vertices has
  exactly V - 1 edges.

**Dry run**

For edges `0-1 (10)`, `0-2 (15)`, `0-3 (30)`, `1-3 (40)`,
and `2-3 (50)`:

| Chosen candidate | New total | MST vertices |
|---|---:|---|
| start at 0, cost 0 | 0 | `{0}` |
| 0-1, cost 10 | 10 | `{0,1}` |
| 0-2, cost 15 | 25 | `{0,1,2}` |
| 0-3, cost 30 | 55 | `{0,1,2,3}` |

The MST cost is 55 and it contains 3 edges, which equals `V - 1`.

**Common mistakes**

- Adding directed edges instead of both undirected directions.
- Ordering the priority queue by vertex number instead of edge cost.
- Adding the cost before checking whether the vertex is already visited.
- Confusing an edge’s cost with cumulative source distance.
- Applying Prim to a disconnected graph and calling the partial result an
  MST.
- Confusing MST with shortest paths.

## 4. Topic-Level Interview Questions

**Bellman-Ford versus Dijkstra?**  
Bellman-Ford handles negative edges in O(VE). Dijkstra is faster with a
heap but requires non-negative edges.

**What is relaxation?**  
It checks whether reaching `v` through `u` produces a smaller distance and
updates `dist[v]` if so.

**What is a negative cycle?**  
A cycle whose edge weights sum to a negative value. Repeating it can lower
a path cost without limit.

**Does an unreachable negative cycle affect the source?**  
No. Bellman-Ford reports cycles reachable from the chosen source because
only those can affect its paths.

**What is an MST?**  
A cycle-free subset of edges connecting every vertex with minimum total
edge weight.

**How many edges are in an MST?**  
Exactly `V - 1` for a non-empty connected graph with V vertices.

**Prim versus Dijkstra?**  
Both use a min-priority queue, but Prim minimizes the next individual edge
joining the tree, while Dijkstra minimizes cumulative distance from a
source.

**Prim versus Kruskal?**  
Prim grows one tree from a vertex. Kruskal sorts all edges and joins
components using a disjoint-set structure.

**Can an MST contain negative edges?**  
Yes. Negative edges are allowed and may help reduce the total MST cost.

**Can there be multiple MSTs?**  
Yes, especially when several edges have equal weights.

**Shortest-path tree versus MST?**  
A shortest-path tree minimizes distance from one source. An MST minimizes
the sum of all selected edge weights.

## 5. Quick Revision Sheet

### Important patterns

- Bellman-Ford: relax every edge `V - 1` times.
- Relax only if `dist[u]` is not infinity.
- Extra relaxation round: reachable negative-cycle check.
- No update in a round: stop early.
- MST: connected + undirected + weighted + no cycles + minimum total cost.
- Prim: cheapest edge that adds an unvisited vertex.
- Prim queue candidate: `(vertex, parent, edgeCost)`.

### Complexities

| Algorithm | Time | Auxiliary space |
|---|---:|---:|
| Bellman-Ford | O(VE) worst case | O(V) |
| Negative-cycle check | O(E) extra | No additional asymptotic space |
| Prim with binary heap | O(E log V) | O(V + E) lazy queue |

### One-line reminders

- Negative edge does not automatically mean negative cycle.
- Bellman-Ford permits negative edges; Dijkstra does not.
- Only reachable negative cycles affect source-based shortest paths.
- Prim solves total connection cost, not source distance.
- Standard MST input is connected, weighted, and undirected.
- Add undirected edges in both directions.
- Check visited before adding a Prim candidate’s cost.
- An MST has V - 1 edges.

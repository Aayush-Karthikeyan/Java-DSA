# 42. Graphs Part 5

## 1. Core Idea

This chapter combines several graph patterns:

| Topic | Main pattern |
|---|---|
| Cheapest Flights Within K Stops | Limited Bellman-Ford |
| Connecting Cities | Prim’s MST on a matrix |
| Disjoint Set Union | Parent + rank + path compression |
| Kruskal’s Algorithm | Sort edges + DSU |
| Flood Fill | Grid DFS |
| Tarjan reading topic | Discovery time + low value |
| Floyd-Warshall reading topic | All-pairs dynamic programming |

### Important vocabulary

- A **stop** is an intermediate city. A route with at most `K` stops can
  use at most `K + 1` flight edges.
- An **MST** connects all vertices with minimum total edge cost and no
  cycle.
- **DSU** efficiently tracks which elements belong to the same component.
- In a grid, each cell can be treated as a graph vertex connected to nearby
  cells.
- A **bridge** is an edge whose removal increases the number of connected
  components.
- An **articulation point** is a vertex whose removal increases the number
  of connected components.
- An **all-pairs shortest-path** algorithm finds a distance for every
  source and destination pair.

## 2. How to Recognize This Pattern

- Cheapest route with a strict number of stops/edges:
  limited Bellman-Ford or state-based shortest path.
- Connect every city with minimum total cost:
  MST using Prim or Kruskal.
- Repeatedly join groups or ask whether two items are connected:
  DSU.
- Edges are already provided as one list:
  Kruskal is often convenient.
- Recolor a connected region in a matrix:
  grid DFS/BFS.
- Find critical roads or critical vertices:
  Tarjan’s bridge/articulation-point method.
- Find shortest distances between every pair of vertices:
  Floyd-Warshall.

## 3. Problems in This Folder

### Cheapest Flights Within K Stops

**What the question asks**

Find the minimum flight price from `src` to `dest` while using at most `K`
intermediate stops. Return `-1` when no allowed route exists.

**Brute-force approach**

- Recursively try every outgoing flight until the stop limit is exceeded.
- With branching factor `b`, time can approach O(b^(K+1)).
- Recursion uses O(K) space, excluding the graph.

**Optimized approach**

- Use a Bellman-Ford-style distance array.
- Run `K + 1` relaxation rounds because `K` stops permit `K + 1` flights.
- Clone the old array at the beginning of every round.
- Read only from the old array and write to the clone.
- Time: O((K + 1)E).
- Space: O(V).

**Why it works**

After round `i`, costs use at most `i + 1` flight edges. Reading from a
separate previous-round array prevents one round from accidentally using
several new flights.

**Interview explanation**

“K stops means I may take at most K plus one flights. I run K plus one
Bellman-Ford relaxation rounds. In each round I clone the previous costs,
read only from the old array, and write improvements to the clone. This
keeps every update within the allowed number of flights and takes
O((K+1)E) time.”

**Common follow-up questions**

- Why `K + 1` rounds? Zero stops still permits one direct flight.
- Why clone the distance array? In-place updates could chain several
  flights within one round and violate the limit.
- Why not ordinary Dijkstra? The state includes both city and flights used;
  one cheapest cost per city is not always enough under a stop limit.
- Are prices positive? The course problem uses positive prices, though the
  limited relaxation logic does not rely on a greedy choice.

**Dry run**

Flights: `0->1 (100)`, `1->2 (100)`, `0->2 (500)`, with `K = 1`.

| Round | Maximum flights | Costs `[0,1,2]` |
|---:|---:|---|
| Start | 0 | `[0, INF, INF]` |
| 1 | 1 | `[0,100,500]` |
| 2 | 2 | `[0,100,200]` |

Answer: 200.

**Common mistakes**

- Treating `K` stops as only `K` edges.
- Updating and reading the same array during a round.
- Returning infinity instead of `-1`.
- Ignoring the stop count in a normal shortest-path solution.

### Connecting Cities With Minimum Cost

**What the question asks**

Given a symmetric connection-cost matrix, connect every city with minimum
total cost. In this course input, `0` means no edge.

**Brute-force approach**

- Try edge subsets and check which ones connect all cities without cycles.
- Time can reach O(2^E × (V + E)).
- Checking a candidate needs O(V + E) space.

**Optimized approach**

- Use Prim’s algorithm.
- Start from city 0 with cost 0.
- A min-priority queue selects the cheapest connection to an unvisited city.
- When a city is selected, add its cost and offer all nonzero matrix edges.
- Reject the matrix if not all cities become reachable.
- Time: O(V² log V) with a matrix and lazy priority queue.
- Space: O(V²) worst case for queued candidates.

**Why it works**

Prim repeatedly chooses the cheapest edge crossing from the connected set
to an unconnected city. This safe greedy choice grows an MST.

**Interview explanation**

“I treat the matrix as a weighted undirected graph and run Prim’s
algorithm. The priority queue gives me the cheapest connection to a city
outside the current tree. I add that cost once, mark the city, and offer
its nonzero connections. With a V-by-V matrix, the time is O(V² log V).”

**Common follow-up questions**

- Why skip an already connected city? Several queued edges can lead to the
  same city; adding it twice creates a cycle and overcounts cost.
- What if zero is a valid edge cost? Then the input needs another marker,
  such as `-1` or infinity, for “no edge.”
- What if the matrix is disconnected? A single MST does not exist.
- Could this be O(V²)? Yes, matrix-based Prim can scan an array for the
  next minimum instead of using a heap.

**Dry run**

For costs `0-1=1`, `0-2=2`, and `1-2=5`, Prim selects costs 1 and 2.
All three cities are connected for total cost 3.

**Common mistakes**

- Adding cost before checking visited.
- Treating the matrix as directed when it represents undirected costs.
- Adding diagonal zeroes as edges.
- Confusing minimum total connection cost with shortest source distance.

### Disjoint Set Union

**What the question asks**

Maintain changing groups while supporting:

- `find(x)`: return `x`’s representative;
- `union(a, b)`: merge two groups;
- `connected(a, b)`: test whether they share a representative.

**Basic approach**

- Store parent pointers but attach trees without rank and never compress
  paths.
- A chain can make `find` and `union` O(n).
- Space: O(n).

**Optimized approach**

- Initialize `parent[i] = i`.
- `find` recursively finds the root and applies path compression.
- `union` attaches the lower-rank root under the higher-rank root.
- Increase rank only when two equal-rank roots merge.
- Time: O(alpha(n)) amortized per operation, effectively constant for
  practical input sizes.
- Space: O(n).

**Why it works**

Every set is represented by one root. Path compression shortens searches,
and union by rank prevents tall trees from forming.

**Interview explanation**

“DSU represents each component as a parent tree. Find returns the root and
compresses the path so future calls are faster. Union first finds both
roots, then attaches the lower-rank tree under the higher-rank tree.
Together these optimizations give almost constant amortized time.”

**Common follow-up questions**

- What does `find` return? The representative/root of the element’s set.
- When does union return false in this code? When both elements already
  have the same root, so no merge occurs.
- Why update only root parents? Joining non-root nodes can break the set
  representation.
- What is alpha(n)? The inverse Ackermann function, which grows extremely
  slowly.

**Dry run**

```text
Initially: {0} {1} {2} {3}
union(0,1): {0,1} {2} {3}
union(2,3): {0,1} {2,3}
union(1,3): {0,1,2,3}
find(3): same representative as find(0)
```

**Common mistakes**

- Forgetting `parent[i] = i`.
- Comparing original vertices instead of their roots.
- Increasing rank on every union.
- Omitting path compression from the recursive return assignment.

### Kruskal’s Algorithm

**What the question asks**

Find an MST from an undirected weighted edge list.

**Brute-force approach**

- Try all edge subsets and test whether each is a spanning tree.
- Time: O(2^E × (V + E)).
- Space: O(V + E).

**Optimized approach**

- Sort every logical undirected edge once by increasing weight.
- Process edges from cheapest to most expensive.
- DSU checks whether the endpoints are already connected.
- If roots differ, select the edge and union the sets.
- Stop after selecting `V - 1` edges.
- Time: O(E log E), dominated by sorting.
- Space: O(V + E) here because the method copies the edge list.

**Why it works**

Choosing the cheapest edge that joins different components cannot create a
cycle and is safe by the MST cut property.

**Interview explanation**

“I sort edges by weight and use DSU to track components. For each edge, if
its endpoints have different representatives, I add it to the MST and
union their sets. If they already share a root, the edge would form a
cycle, so I skip it. Sorting dominates the O(E log E) time.”

**Common follow-up questions**

- Why does DSU detect a cycle? Endpoints already in one set already have a
  path between them.
- How many edges are selected? Exactly `V - 1` for a connected non-empty
  graph.
- Prim versus Kruskal? Prim grows one tree; Kruskal joins components using
  globally sorted edges.
- Can weights be negative? Yes, MST algorithms can include negative edges.

**Dry run**

Sorted edges: `0-1 (10)`, `0-2 (15)`, `0-3 (30)`, `1-3 (40)`.
Kruskal selects the first three edges for cost 55 and stops at `V - 1`.

**Common mistakes**

- Forgetting to sort the edges.
- Adding both stored directions of every undirected edge to the input list.
- Selecting an edge whose endpoints already share a root.
- Forgetting to verify that `V - 1` edges were found.

### Flood Fill

**What the question asks**

Starting from one pixel, recolor every 4-directionally connected pixel
having the same original color.

**Brute-force approach**

- Repeatedly scan the whole grid and recolor matching pixels adjacent to
  an already changed pixel until no change occurs.
- This can take O((rows × columns)²) time.
- Space can be O(1), excluding the image.

**Optimized approach**

- Remember the starting color.
- If it already equals the new color, return immediately.
- DFS from the starting cell.
- Reject out-of-bounds cells and cells with another color.
- Recolor before making four recursive calls; the new color acts as visited.
- Time: O(rows × columns).
- Space: O(rows × columns) worst-case recursion depth.

**Why it works**

DFS reaches exactly the connected component of cells with the original
color. Recoloring immediately prevents a cell from being processed again.

**Interview explanation**

“I save the starting pixel’s original color and return early if it already
matches the new color. Otherwise, I run DFS in four directions. A call
stops when it leaves the grid or reaches a different color. I recolor
before recursing, which also marks the cell visited. Each cell is processed
at most once.”

**Common follow-up questions**

- Why handle equal original and new colors? Without that check or a separate
  visited array, recursion can revisit the same unchanged-looking cells.
- Does diagonal contact count? No; the problem specifies four directions.
- Does this mutate the input? Yes, the method recolors and returns the same
  matrix.
- Could BFS be used? Yes, a queue gives the same O(rows × columns) time.

**Dry run**

```text
Before:          Fill from (1,1) with 2:
1 1 1            2 2 2
1 1 0     ->     2 2 0
1 0 1            2 0 1
```

The bottom-right `1` remains unchanged because it is only diagonally
connected.

**Common mistakes**

- Forgetting to recolor before recursive calls.
- Checking the new color instead of the original color.
- Missing a boundary condition.
- Accidentally exploring diagonals.

### Tarjan’s Algorithm: Bridges and Articulation Points

**What the question asks**

Find critical edges and vertices in a simple undirected graph.

**Brute-force approach**

- Remove each edge or vertex one at a time and recount connected components.
- Time: O((V + E)(V + E)).
- Space: O(V) per traversal.

**Optimized approach**

- Run one DFS with:
  - `discovery[u]`: when `u` was first visited;
  - `low[u]`: earliest discovery time reachable from `u`’s subtree.
- Tree edge `u-v` is a bridge if `low[v] > discovery[u]`.
- Non-root `u` is an articulation point if a child has
  `low[v] >= discovery[u]`.
- A DFS root is an articulation point when it has more than one DFS child.
- Time: O(V + E).
- Auxiliary space: O(V).

**Interview explanation**

“Tarjan’s DFS stores each vertex’s discovery time and the earliest ancestor
reachable from its subtree. If a child cannot reach the current vertex or
an ancestor, its edge is a bridge. A similar low-value condition identifies
articulation points, with a separate multiple-child rule for a DFS root.
The whole algorithm is O(V+E).”

**Dry run**

A triangle `0-1-2-0` has no bridge. If edge `1-3` leads to a tail, the
subtree at 3 cannot reach the triangle without that edge, so `1-3` is a
bridge and vertex 1 is an articulation point.

**Common mistakes**

- Using `low[neighbor]` instead of `discovery[neighbor]` for a back edge.
- Forgetting the special root articulation rule.
- Treating the parent edge as a back edge.
- Applying this simple parent check to parallel edges without adaptation.

### Floyd-Warshall Algorithm

**What the question asks**

Find shortest distances between every ordered pair of vertices.

**Brute-force approach**

- Run a single-source shortest-path algorithm from every vertex.
- With Bellman-Ford, time is O(V²E).
- Space is O(V²) for all returned distances.

**Optimized approach**

- Start with the weighted adjacency matrix.
- Try each vertex `k` as an allowed intermediate.
- For every pair `(i, j)`, update:
  `dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])`.
- Never add a path containing infinity.
- A negative diagonal after processing indicates a negative cycle.
- Time: O(V³).
- Space: O(V²) because the implementation returns a copied matrix.

**Interview explanation**

“Floyd-Warshall is dynamic programming over allowed intermediate vertices.
For each intermediate k, I check whether traveling from i to k and then k
to j improves i to j. Three nested vertex loops give O(V³) time. Negative
edges are allowed, and a negative diagonal afterward indicates a negative
cycle.”

**Dry run**

If `0->1 = 5`, `1->2 = 3`, and direct `0->2` is infinity, allowing vertex
1 as intermediate updates `0->2` to `5 + 3 = 8`.

**Common mistakes**

- Putting the intermediate loop inside the other two loops.
- Adding infinity to a distance.
- Forgetting zeroes on the diagonal.
- Using Floyd-Warshall when only one source is needed on a large sparse graph.

## 4. Topic-Level Interview Questions

**Why is Cheapest Flights not ordinary shortest path?**  
The allowed number of edges is part of the state, so a route that is more
expensive at one city may still be useful if it used fewer flights.

**Prim versus Kruskal?**  
Prim grows one tree using a priority queue. Kruskal sorts edges and joins
components with DSU.

**What are DSU’s two optimizations?**  
Path compression in `find` and union by rank/size in `union`.

**What is a grid graph?**  
Cells are vertices and allowed moves define edges, often in four directions.

**Bridge versus articulation point?**  
A bridge is a critical edge; an articulation point is a critical vertex.

**Bellman-Ford versus Floyd-Warshall?**  
Bellman-Ford is single-source O(VE). Floyd-Warshall is all-pairs O(V³).

**Can Floyd-Warshall use negative edges?**  
Yes, but shortest distances are not well-defined through a negative cycle.

## 5. Quick Revision Sheet

### Important patterns

- K stops = at most K + 1 edges.
- Cheapest Flights: clone costs each relaxation round.
- Connecting Cities: matrix-based Prim.
- DSU: parent, rank, path compression.
- Kruskal: sort, different roots, union, stop at V - 1 edges.
- Flood Fill: recolor before four recursive calls.
- Tarjan: discovery and low values.
- Floyd: intermediate loop must be outermost.

### Complexities

| Algorithm | Time | Auxiliary space |
|---|---:|---:|
| Cheapest Flights | O((K + 1)E) | O(V) |
| Connecting Cities | O(V² log V) | O(V²) worst case |
| DSU operation | O(alpha(V)) amortized | O(V) total DSU storage |
| Kruskal | O(E log E) | O(V + E) here |
| Flood Fill | O(rows × columns) | O(rows × columns) |
| Tarjan bridges/points | O(V + E) | O(V) |
| Floyd-Warshall | O(V³) | O(V²) |

### One-line reminders

- Use previous-round costs to enforce a flight limit.
- `0` means no city edge only because this matrix format defines it so.
- Kruskal’s edge list contains each undirected edge once.
- DSU union operates on roots.
- Recoloring can serve as Flood Fill’s visited marker.
- Tarjan root and non-root articulation rules differ.
- Floyd-Warshall’s diagonal helps detect negative cycles.

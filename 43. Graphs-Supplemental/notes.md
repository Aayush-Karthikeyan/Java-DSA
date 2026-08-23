# 43. Graphs Supplemental

## 1. Core Idea

This supplemental chapter studies graph connectivity more deeply:

- **Strongly connected components:** mutually reachable groups in a
  directed graph.
- **Bridges:** critical edges in an undirected graph.
- **Articulation points:** critical vertices in an undirected graph.

### Strong connectivity

A directed graph is strongly connected when every vertex can reach every
other vertex. A **strongly connected component (SCC)** is a largest group
with this property.

```text
0 -> 1 -> 2
^         |
|_________|

{0,1,2} is one SCC.
```

### Transpose graph

The transpose reverses every directed edge:

```text
Original:  u -> v
Transpose: v -> u
```

### Discovery and low values

Tarjan’s bridge and articulation-point methods use:

- `discovery[u]`: the time DFS first reaches `u`;
- `low[u]`: the smallest discovery time reachable from `u`’s DFS subtree
  using tree edges and at most one back edge upward.

Updates:

```text
After child v: low[u] = min(low[u], low[v])
For back edge: low[u] = min(low[u], discovery[v])
```

Critical conditions:

```text
Bridge u-v:              low[v] > discovery[u]
Non-root articulation u: low[v] >= discovery[u]
Root articulation u:     DFS children > 1
```

The bridge condition uses `>`, while the articulation condition uses `>=`.

## 2. How to Recognize This Pattern

- Directed graph and “mutually reachable groups”:
  strongly connected components.
- Need SCCs in O(V + E):
  Kosaraju or Tarjan SCC algorithm.
- “Critical road/connection” whose removal disconnects a network:
  bridge.
- “Critical server/city/router” whose removal separates groups:
  articulation point.
- Need to avoid removing every edge/vertex separately:
  one DFS with discovery and low values.

## 3. Problems in This Folder

### Strongly Connected Components Using Kosaraju’s Algorithm

**What the question asks**

Return the maximal groups in a directed graph where every pair of vertices
can reach each other.

**Brute-force approach**

- Run a reachability traversal from every vertex.
- Compare which vertex pairs can reach each other and group them.
- Time: O(V(V + E)).
- Space: up to O(V²) if all reachability results are stored.

**Optimized approach**

1. Run DFS on the original graph and push each vertex after its neighbors
   finish.
2. Reverse every edge to build the transpose.
3. Reset visited.
4. Pop vertices in finishing-time order and run DFS on the transpose.
5. Each new DFS produces one SCC.

- Time: O(V + E).
- Space: O(V + E), including the transpose; auxiliary arrays and recursion
  use O(V).

**Why it works**

The first pass orders SCCs by finishing time. Reversing edges prevents the
second DFS from leaving the SCC selected next, so that traversal collects
exactly one component.

The first step resembles DFS topological sorting, but it is a
**finishing-time order**, not a topological ordering—the graph may contain
cycles.

**Interview explanation**

“Kosaraju uses two DFS passes. First, I push each vertex after its DFS
finishes. Then I reverse every edge. I pop vertices in finishing-time order
and run DFS on the transpose; each traversal gives exactly one strongly
connected component. Building the transpose and both DFS passes are all
linear, so the total is O(V+E).”

**Common follow-up questions**

- Why reverse the edges? It keeps the second DFS inside the selected SCC
  instead of following edges outward to a later component.
- Why push after visiting neighbors? SCC processing depends on decreasing
  DFS finishing time.
- Is one-way reachability enough? No. Every pair inside an SCC must be
  mutually reachable.
- Does an isolated vertex form an SCC? Yes, by itself.

**Dry run**

For `0->2`, `2->1`, `1->0`, `0->3`, and `3->4`:

```text
Mutually reachable cycle: {0,1,2}
One-way tail: 0 -> 3 -> 4
SCCs: {0,1,2}, {3}, {4}
```

**Common mistakes**

- Pushing a vertex before processing its neighbors.
- Forgetting to reset visited before the second pass.
- Running the second DFS on the original graph.
- Reversing only some edges.
- Calling the finish stack a valid topological order for a cyclic graph.

### Bridges Using Tarjan’s Algorithm

**What the question asks**

Return every edge in a simple undirected graph whose removal increases the
number of connected components.

**Brute-force approach**

- Remove one edge.
- Run DFS/BFS to count components.
- Restore it and repeat for every edge.
- Time: O(E(V + E)).
- Space: O(V) per traversal, excluding graph copying.

**Optimized approach**

- Run DFS and assign `discovery` and `low` values.
- Ignore the edge back to the parent.
- After DFS child `v`, update `low[u]` from `low[v]`.
- For a visited neighbor/back edge, update from `discovery[v]`.
- Edge `u-v` is a bridge when `low[v] > discovery[u]`.
- Time: O(V + E).
- Auxiliary space: O(V).

**Why it works**

If `low[v] > discovery[u]`, the subtree rooted at `v` cannot reach `u` or
any ancestor of `u` without edge `u-v`. Removing that edge separates the
subtree.

**Interview explanation**

“During DFS, discovery records when a vertex is first visited, and low
records the earliest ancestor its subtree can reach. After returning from
a child, if the child’s low value is greater than the current vertex’s
discovery time, there is no alternate route upward. That tree edge is
therefore a bridge. The algorithm processes the graph once in O(V+E).”

**Common follow-up questions**

- Why strictly `>`? Equality means the child subtree can reach the current
  vertex through another edge, so removing the tree edge does not separate it.
- Are all tree edges bridges? No. A back edge may provide another route.
- Can an edge inside a cycle be a bridge? No.
- Why scan every vertex? The undirected graph may be disconnected.

**Dry run**

```text
0 ----- 1
 \     /
   2
   |
   3
```

The triangle edges are not bridges. Edge `2-3` is a bridge because the
subtree at 3 has no alternate route to the triangle.

**Common mistakes**

- Treating the parent edge as a back edge.
- Updating a back edge with `low[neighbor]` instead of
  `discovery[neighbor]`.
- Reversing the bridge comparison.
- Using the simple parent check unchanged when parallel edges are allowed.

### Articulation Points Using Tarjan’s Algorithm

**What the question asks**

Return every vertex in a simple undirected graph whose removal increases
the number of connected components.

**Brute-force approach**

- Remove one vertex and all incident edges.
- Count components using DFS/BFS.
- Restore it and repeat for every vertex.
- Time: O(V(V + E)).
- Space: O(V), excluding graph copying.

**Optimized approach**

- Calculate discovery and low values with DFS.
- For a non-root `u`, mark it when a DFS child `v` satisfies
  `low[v] >= discovery[u]`.
- For a DFS root, mark it only when it has more than one DFS child.
- Store answers in a boolean array to avoid duplicates.
- Time: O(V + E).
- Auxiliary space: O(V).

**Why it works**

For a non-root, `low[v] >= discovery[u]` means the child subtree cannot
reach an ancestor above `u`, so removing `u` separates it. A root has no
ancestor, so it is critical only when it starts multiple independent DFS
subtrees.

**Interview explanation**

“I reuse discovery and low values. A non-root vertex is an articulation
point when one child subtree cannot reach an ancestor above it, which is
the condition low of child greater than or equal to discovery of current.
The DFS root needs a separate rule: it must have more than one DFS child.
The total time is O(V+E).”

**Common follow-up questions**

- Why `>=` instead of the bridge’s `>`? Even if a child can return to `u`
  exactly, removing vertex `u` destroys that connection.
- Why is the root different? It has no ancestor to compare against.
- Is a leaf an articulation point? Normally no; removing a leaf does not
  separate the remaining graph.
- Can one graph have several articulation points? Yes.

**Dry run**

```text
1
|
0 ----- 3 ----- 4
|
2
```

If this drawing represents the DFS tree without alternate edges, root `0`
has multiple children and is an articulation point. Vertex `3` is also an
articulation point because removing it separates vertex 4.

**Common mistakes**

- Applying the non-root rule to the DFS root.
- Counting all neighbors as root children instead of DFS-tree children.
- Printing the same articulation point once per qualifying child.
- Forgetting disconnected components.

## 4. Topic-Level Interview Questions

**Connected component versus SCC?**  
Connected components usually refer to undirected graphs. SCCs require
mutual reachability while respecting directions.

**What are Kosaraju’s three stages?**  
Finishing-time DFS, graph transposition, then DFS on the transpose in stack
order.

**What is a transpose graph?**  
A directed graph with every edge direction reversed.

**What does `low[u]` mean?**  
The earliest discovery time reachable from `u`’s DFS subtree through tree
edges and a back edge.

**Bridge versus articulation point?**  
A bridge is a critical edge; an articulation point is a critical vertex.

**Why use `discovery[neighbor]` for a back edge?**  
The back edge directly reaches that already discovered vertex. Using its
low value may incorrectly include paths that do not correspond to this edge.

**Can bridges appear in cycles?**  
No. A cycle provides another route between an edge’s endpoints.

**Are Tarjan SCC and Tarjan bridge algorithms the same?**  
No. Several graph algorithms carry Tarjan’s name. This chapter’s Tarjan
methods use low values for bridges and articulation points in undirected
graphs.

## 5. Quick Revision Sheet

### Important patterns

- Kosaraju: finish stack → transpose → DFS in stack order.
- SCC means mutual directed reachability.
- Tree-edge low update: `low[u] = min(low[u], low[v])`.
- Back-edge update: `low[u] = min(low[u], discovery[v])`.
- Bridge: `low[v] > discovery[u]`.
- Non-root articulation: `low[v] >= discovery[u]`.
- Root articulation: more than one DFS child.

### Complexities

| Algorithm | Time | Auxiliary/extra space |
|---|---:|---:|
| Kosaraju SCC | O(V + E) | O(V + E), including transpose |
| Tarjan bridges | O(V + E) | O(V) |
| Tarjan articulation points | O(V + E) | O(V) |

### One-line reminders

- Kosaraju operates on directed graphs.
- Bridges and articulation points here operate on simple undirected graphs.
- Reset visited between Kosaraju passes.
- Ignore the DFS parent edge in Tarjan’s undirected traversal.
- Bridge uses `>`; articulation point uses `>=`.
- The articulation-point root rule is separate.

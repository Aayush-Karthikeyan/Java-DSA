# Java & DSA — Learning Repository

Personal notes, code, and practice material covering Java fundamentals
through advanced data structures and algorithms. Built while working
through a structured DSA course, then re-organized for **interview
preparation and long-gap recall**.

**Goal:** first SWE internship, 12–16 months, starting May 2027.

---

## 🚀 Start here (returning after a long gap?)

You are not expected to remember any of this. That's what the repo is
for. The workflow below assumes **zero recall**.

### The daily loop (~30–45 min per topic)

| Step | Where | What you do |
|---|---|---|
| 1 | `notes.md` | Read **Core Idea** + **Quick Revision Sheet** only. ~5 min. |
| 2 | paper | Close the file. Write from memory: what it is, when to use it, the complexity. Bullets, not sentences. |
| 3 | `*.java` | Read **one** method. Understand it line by line. |
| 4 | scratch file | Close it. Rewrite that method from memory. **Run it.** Compare. |
| 5 | `notes.md` | Read **Common Mistakes** to patch what you got wrong. |

**Don't try to reproduce a whole file verbatim.** Rebuild the *shape* —
the recurrence, the loop structure, the base case. Details come back on
their own once the shape is right. Word-for-word memorization is the
wrong target.

### The review rhythm

New topics fade in days if you never revisit them. Don't go in a
straight line.

- **Weekdays:** 1 new topic per day, full loop above.
- **Saturday:** no new topic. Take 2–3 older topics (oldest first) and
  re-do **only** the Quick Revision Sheet recall — 5–10 min each.
- **Exam/midterm weeks:** no new topics at all. Review reps only.
  Protect what you have instead of pushing forward.

---

## 📊 What to prioritize

**48 topics is not 48 equal units.** Most interviews concentrate
heavily on a subset. Spend your time accordingly.

### 🔴 TOP — master these cold

Arrays (11–12) · Strings (15) · Hashing (36) · Recursion (18–19) ·
Divide & Conquer (20) · Time & Space Complexity (21) · Backtracking (22) ·
Linked Lists (24–25) · Stacks & Queues (26–28) · Binary Trees (30–32) ·
BSTs (33–34) · Graphs (38–42) · **DP (44–49)** · Sorting (13)

> Within Graphs, the must-know set is **BFS/DFS, cycle detection,
> topological sort, Dijkstra, Union-Find, Flood Fill**. Bellman-Ford,
> MST (Prim's/Kruskal's), Floyd-Warshall, Tarjan's and Kosaraju's are
> noticeably lower frequency — know what they solve, don't drill them.

### 🟡 MEDIUM — know solidly

2D Arrays (14) · Bit Manipulation (16) · OOPs (17) · ArrayLists (23) ·
Greedy (29) · Heaps (35)

### 🟢 LOW — situational

Tries (37) · Graphs-Supplemental (43) · Segment Trees (50)

### ⚪ SKIM — read once, never drill

Flowcharts (03) · Variables (04) · Operators (05) · Conditionals (06) ·
Loops (07) · Patterns (08) · Functions (09) · Advanced Patterns (10)

> These built your Java fluency. Nobody will ask you to explain a
> for-loop. Don't spend review cycles here.

---

## 📁 What's in this repo

```
Java/
├── 03. … 50.                  ← 48 topic folders (notes.md + .java)
├── Practice Questions/        ← course PDFs: Questions + Solutions per topic
├── Company Practice/
│   └── tracker.md             ← ~140 company-tagged problems, mapped + trackable
└── Java-Topics.code-workspace ← opens all topics as one VS Code workspace
```

### The four pieces, and what each is for

| Piece | Purpose |
|---|---|
| `notes.md` | **Learn / relearn the concept.** Self-contained — rebuilds understanding from zero. |
| `*.java` | **Worked examples to study and reproduce.** Every file compiles and runs. |
| `Practice Questions/` | **Blind practice.** Questions PDF and Solutions PDF are separate — attempt before peeking. |
| `Company Practice/tracker.md` | **Test yourself against real interview problems**, mapped back to the topic that teaches each one. |

### Notes format

Later topics (36–50) follow a consistent structure:

`Core Idea` → `How to Recognize This Pattern` → `Problems in This
Folder` (per problem: brute force → optimized → why it works →
interview explanation → dry run → common mistakes) →
`Topic-Level Interview Questions` → `Quick Revision Sheet`

Earlier topics use a lighter cheatsheet format — still complete, just
less formal.

---

## ⚡ Quick wins — problems you've already solved

`Company Practice/tracker.md` marks **17 problems as [DITTO]** — the
exact same problem is already worked in this repo. Solve these cold
first; they're the fastest confidence check that your recall works.

| Company problem | Already solved in |
|---|---|
| Climbing Stairs | `44. DP-1` |
| Longest Increasing Subsequence | `47. DP-4` |
| Wildcard Matching | `48. DP-5` |
| Largest Rectangle in Histogram | `27. Stacks Part 2` |
| Spiral Matrix | `14. 2D Arrays` |
| Search a 2D Matrix | `14. 2D Arrays` |
| Reverse a Linked List | `24. Linked Lists Part 1` |
| Palindromic Linked List | `24. Linked Lists Part 1` |
| Linked List Cycle | `25. Linked Lists Part 2` |
| Merge Two Sorted Lists | `25. Linked Lists Part 2` |
| Sort List | `25. Linked Lists Part 2` |
| Subsets | `22. Backtracking` |
| Permutations | `22. Backtracking` |
| Number of 1 Bits | `16. Bit Manipulation` |
| Find Min in Rotated Sorted Array | `20. Divide & Conquer` |
| Find City with Smallest Neighbors | `42. Graphs-5` (Floyd-Warshall) |
| Valid Palindrome | `15. Strings` |

---

## 🕳️ Known gaps

Two patterns appear in the company problem list but have **no coverage**
in this repo yet:

- **Sliding Window** — Longest Substring, Minimum Window Substring,
  Sliding Window Maximum, Shortest Subarray with Sum ≥ K.
  *(The Deque tool these need is covered in `28. Queues`.)*
- **Design problems** — LRU Cache, Design Hit Counter, Design File
  System, Logger Rate Limiter.
  *(Building blocks exist across `36. Hashing`, `24/25. Linked Lists`,
  `26/27. Stacks`, `37. Tries` — it's the design skill that's new.)*

**Partial gap:** DP on Grid. `22. Backtracking` covers "Grid Ways"
(= Unique Paths via recursion + formula), but not the DP-table version,
Minimum Path Sum, or Triangle.

No practice-question PDFs exist for `10. Advanced Patterns` or
`50. Segment Trees`.

---

## 🏃 Running the code

Each topic folder is self-contained:

```bash
cd "44. DP-1"
javac DPPart1.java
java DPPart1
```

Every file has a `main()` that prints worked examples, so running it
shows the algorithm working immediately.

Compiled `.class` files and `.idea/` are gitignored.

---

## 📌 Reminders to future me

- **Apply while studying, not after.** For a May 2027 start,
  applications run roughly **Aug 2026 → Jan 2027**. Waiting until DSA
  "feels finished" means missing the window. Confirm exact dates with
  the UCalgary co-op office.
- **Nobody masters 48 topics equally.** People walk into interviews
  solid on ~15–20 and passingly familiar with the rest. That's the
  realistic bar.
- **The skill is recognition, not memorization.** Seeing a problem and
  thinking *"this is a sliding window"* is worth more than
  reciting any implementation from memory.
- **State your approach out loud before coding.** It's the single
  biggest difference between a calm interview and a panicked one.

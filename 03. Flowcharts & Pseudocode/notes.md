# 03. Flowcharts & Pseudocode — Key Notes

> **Skim tier.** This is a planning/communication topic, not a coding
> one. Nobody asks you to draw a flowchart in an interview — but the
> *habit* of sketching logic before coding is what stops you freezing
> at the whiteboard. Read once, then move on.

## Why this matters at all

Before writing code, you describe *what* the program does without
worrying about syntax. Two tools do this:

- **Flowchart** — a visual diagram of the logic flow.
- **Pseudocode** — plain-English steps written in code-like structure.

In an interview, this is the "talk through your approach before you
type" step. Interviewers care far more about hearing a clear plan than
watching you start typing immediately.

## Flowchart Symbols

| Symbol | Shape | Means |
|---|---|---|
| Terminal | Oval / rounded | Start or End |
| Input / Output | Parallelogram | Read input, print output |
| Process | Rectangle | A calculation or assignment |
| Decision | Diamond | A condition — branches Yes / No |
| Arrow | Line with arrowhead | Direction of flow |
| Connector | Small circle | Joins parts of a split diagram |

**Rules:**

- Every flowchart starts with one **Start** and ends with **End**.
- A decision diamond always has exactly **two** exits (Yes / No).
- Arrows only ever point one direction — no ambiguity about what runs
  next.

## Example — largest of two numbers

```text
        ( Start )
            |
      [ Read a, b ]
            |
        < a > b ? >
        /         \
      Yes          No
       |            |
  [ print a ]  [ print b ]
        \         /
         ( End )
```

## Pseudocode

No fixed syntax — the goal is being unambiguous, not being valid Java.

```text
BEGIN
    READ a, b
    IF a > b THEN
        PRINT a
    ELSE
        PRINT b
    ENDIF
END
```

**Conventions worth keeping:**

- Capitalize keywords (`READ`, `PRINT`, `IF`, `WHILE`, `RETURN`).
- Indent inside blocks — it shows structure without braces.
- Close blocks explicitly (`ENDIF`, `ENDWHILE`) so nesting is clear.
- Skip declarations, semicolons, imports — that's syntax, not logic.

## Flowchart vs. Pseudocode

| | Flowchart | Pseudocode |
|---|---|---|
| Form | Visual diagram | Text |
| Best for | Seeing branching at a glance | Complex/nested logic |
| Scales to big programs? | Poorly — gets messy fast | Yes |
| Used in interviews? | Rarely drawn | **Yes** — verbally, constantly |

## What actually transfers to interviews

- **State your approach out loud before coding.** Pseudocode is that
  habit in written form.
- **Name the branches.** "If the list is empty, return 0; otherwise
  recurse on the rest" is pseudocode spoken aloud.
- **Confirm edge cases at the planning stage** — empty input, single
  element, duplicates — *before* you've written 20 lines built on a
  wrong assumption.

> 💡 The real skill from this topic: never start typing until you can
> say the whole algorithm in plain English. That's the single biggest
> difference between a calm interview and a panicked one.

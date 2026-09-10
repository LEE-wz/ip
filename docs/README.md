# Duke User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Profile pictures

Each user message and Remy response is shown beside an equally sized square profile picture with rounded corners.
Non-square profile pictures are cropped evenly from their longer sides, keeping the center visible without stretching
the image. The initial Remy greeting uses the same profile picture styling as later messages.

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Finding tasks

Use `find KEYWORD` to show all tasks whose descriptions contain `KEYWORD`. The search is case-insensitive.

Example: `find book`

```
____________________________________________________________

Here are the matching tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
____________________________________________________________
```

## Sorting tasks

Use `sort /by date [/order asc|desc]` to sort tasks chronologically. Ascending order is used when `/order` is omitted.

Deadlines are ordered by their due endpoints, while events are ordered by their start endpoints. Todos always appear
after dated tasks. Sorting updates the saved task order and the task numbers shown by `list`. Tasks added afterward
are appended normally until another sort command is used.

Example: `sort /by date /order asc`

```text
Here are the tasks in your list:
1. [D][ ] submit report (by: Aug 23 2026 18:00)
2. [E][ ] tutorial (from: Aug 24 2026 14:00 to: Aug 24 2026 15:00)
3. [T][ ] buy ingredients
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details

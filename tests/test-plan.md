# Sorting Feature Test Plan

## Scope

This plan verifies the `sort /by date [/order asc|desc]` command without changing the behavior of existing commands.
Automated JUnit tests cover parsing, ordering logic, persistence, and command integration. The manual checks focus on
the JavaFX interaction and user-visible output.

## Environment

- Java 25
- Application started with `./gradlew run`
- A backed-up or disposable `data/remy.txt`

## Automated coverage

| Area | Test class | Coverage |
| --- | --- | --- |
| Parsing | `ParserTest` | Default ascending order, explicit descending order, whitespace, invalid syntax, and keyword boundary |
| Ordering | `TaskListTest` | Mixed task types, both directions, date-only endpoints, stable ties, todo placement, completion independence, and later additions |
| Integration | `RemyTest` | Exact list output, canonical indices, persistence, find ordering, empty-list saving, invalid-command safety, and non-automatic re-sorting |

## Manual test cases

### SORT-01: Default ascending order

1. Add deadlines and events in nonchronological order, followed by at least one todo.
2. Enter `sort /by date`.
3. Verify dated tasks appear earliest first and todos appear last.
4. Enter `list` and verify it shows the same order and task numbers.

### SORT-02: Explicit descending order

1. Enter `sort /by date /order desc` with a mixed task list.
2. Verify dated tasks appear latest first.
3. Verify todos remain last rather than moving to the front.

### SORT-03: Date-only endpoints and stable ties

1. Add a date-only deadline and an event starting at `00:00` on the same date.
2. Enter `sort /by date`.
3. Verify the two tasks retain their previous relative order.
4. Add other timed tasks on that date and verify the midnight tasks appear before them in ascending order.

### SORT-04: Canonical task numbers

1. Sort a list that changes the positions of at least two tasks.
2. Enter `mark 1`, `unmark 1`, or `delete 1`.
3. Verify the command affects the task displayed as number 1 by the full sorted list.

### SORT-05: Persistence

1. Sort a mixed task list.
2. Close and restart Remy.
3. Enter `list` and verify the sorted order is preserved.

### SORT-06: Later additions

1. Sort dated tasks in ascending order.
2. Add a new task with an earlier date.
3. Enter `list` and verify the new task was appended rather than automatically inserted chronologically.

### SORT-07: Invalid syntax

1. Try each of these commands: `sort`, `sort /by name`, `sort /by date /order`, and
   `sort /by date /order ascending`.
2. Verify each produces exactly:

   ```text
   Invalid sort command.
   Use: sort /by date [/order asc|desc]
   Example: sort /by date /order asc
   ```

3. Enter `list` and verify the order was not changed.

### SORT-08: Unknown keyword prefix

1. Enter `sorter /by date`.
2. Verify Remy shows its existing unknown-command response rather than the sort guidance.

### SORT-09: Empty list

1. Start Remy with an empty task list.
2. Enter `sort /by date`.
3. Verify Remy displays `There are no tasks in your list yet.`
4. Verify an empty task file can be created by the valid sort operation.

### SORT-10: Find behavior

1. Sort a list containing multiple tasks that match the same search keyword.
2. Use `find KEYWORD`.
3. Verify matches follow their relative order in the canonical sorted list.
4. Verify find results retain their existing filtered-result numbering behavior.

### SORT-11: Save failure

1. Run Remy with a task file location that cannot be written.
2. Enter a valid sort command.
3. Verify the saving-error message is shown before the current in-memory sorted list.
4. Verify the in-memory order remains sorted for the session.

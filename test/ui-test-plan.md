# UI test plan

## Test environment

- Run the plan from the repository root with `py .codex/skills/test-ui/scripts/run_ui_tests.py`.
- The runner compiles every Java file in `src/main/java` before testing.
- ANSI colour codes are removed before comparison, so expected outputs contain plain text.
- Each test case starts a fresh Bob session. End every input block with `bye`.

## Test case: Create, update, and list all task types
Aim: Verify that Bob creates to-do, deadline, and event tasks, updates a task's completion status, and lists their formatted details.

Input:
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
unmark 1
list
bye
```

Expected output:
```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

Hello! I'm Bob.
What can I do for you?
____________________________________________________________

____________________________________________________________

Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________

Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________

Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________

____________________________________________________________

I marked this task as done:
[T][X] borrow book
____________________________________________________________

____________________________________________________________

I marked this task as not done:
[T][ ] borrow book
____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________

____________________________________________________________

Bye. Hope to see you again soon!
____________________________________________________________
```

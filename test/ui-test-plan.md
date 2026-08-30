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
deadline return book /by 2/12/2019 1800
event project meeting /from 03/12/2019 1400 /to 03/12/2019 1600
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
[D][ ] return book (by: Dec 02 2019 at 1800hrs)
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________

Got it. I've added this task:
[E][ ] project meeting (from: Dec 03 2019 at 1400hrs to: Dec 03 2019 at 1600hrs)
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
2.[D][ ] return book (by: Dec 02 2019 at 1800hrs)
3.[E][ ] project meeting (from: Dec 03 2019 at 1400hrs to: Dec 03 2019 at 1600hrs)
____________________________________________________________

____________________________________________________________

Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Show usage for commands without required arguments
Aim: Verify that bare commands requiring arguments show their usage instead of being reported as unknown commands.

Input:
```text
todo
deadline
event
mark
unmark
delete
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

Invalid command format. Use: todo <description>
____________________________________________________________

____________________________________________________________

Invalid command format. Use: deadline <description> /by <dd/MM/yyyy or dd/MM/yyyy HHmm>
____________________________________________________________

____________________________________________________________

Invalid command format. Use: event <description> /from <dd/MM/yyyy or dd/MM/yyyy HHmm> /to <dd/MM/yyyy or dd/MM/yyyy HHmm>
____________________________________________________________

____________________________________________________________

Invalid command format. Use: mark <task_number>
____________________________________________________________

____________________________________________________________

Invalid command format. Use: unmark <task_number>
____________________________________________________________

____________________________________________________________

Invalid command format. Use: delete <task_number>
____________________________________________________________

____________________________________________________________

Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Reject invalid commands without changing saved tasks
Aim: Verify that invalid deadline, event, mark, unmark, and delete commands do not corrupt saved tasks, while valid deletion removes and reindexes a task.

Input:
```text
todo first task
deadline missing date
deadline impossible date /by 31/02/2019
list
event meeting /from 2pm
event invalid time /from 03/12/2019 2400 /to 03/12/2019 2500
todo second task
delete 3
mark 2
delete 1
unmark nope
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
[T][ ] first task
Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________

Invalid command format. Use: deadline <description> /by <dd/MM/yyyy or dd/MM/yyyy HHmm>
____________________________________________________________

____________________________________________________________

Invalid date or time. Use dd/MM/yyyy or dd/MM/yyyy HHmm with a 24-hour time.
____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] first task
____________________________________________________________

____________________________________________________________

Invalid command format. Use: event <description> /from <dd/MM/yyyy or dd/MM/yyyy HHmm> /to <dd/MM/yyyy or dd/MM/yyyy HHmm>
____________________________________________________________

____________________________________________________________

Invalid date or time. Use dd/MM/yyyy or dd/MM/yyyy HHmm with a 24-hour time.
____________________________________________________________

____________________________________________________________

Got it. I've added this task:
[T][ ] second task
Now you have 2 tasks in the list.
____________________________________________________________

____________________________________________________________

Invalid task number.
____________________________________________________________

____________________________________________________________

I marked this task as done:
[T][X] second task
____________________________________________________________

____________________________________________________________

Noted. I've removed this task:
[T][ ] first task
Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________

Invalid task number format.
____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][X] second task
____________________________________________________________

____________________________________________________________

Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Reject tasks without a description
Aim: Verify that malformed deadline and event commands do not terminate Bob and that a subsequent valid task can still be added and listed.

Input:
```text
deadline /by Sunday
event /from 2pm /to 4pm
todo valid task
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

Invalid command format. Use: deadline <description> /by <dd/MM/yyyy or dd/MM/yyyy HHmm>
____________________________________________________________

____________________________________________________________

Invalid command format. Use: event <description> /from <dd/MM/yyyy or dd/MM/yyyy HHmm> /to <dd/MM/yyyy or dd/MM/yyyy HHmm>
____________________________________________________________

____________________________________________________________

Got it. I've added this task:
[T][ ] valid task
Now you have 1 tasks in the list.
____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] valid task
____________________________________________________________

____________________________________________________________

Bye. Hope to see you again soon!
____________________________________________________________
```

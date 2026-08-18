public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;

        System.out.println("____________________________________________________________\n");
        System.out.println(" Nice! I've marked this task as done:\n");
        System.out.println(this.toString());
        System.out.println("____________________________________________________________\n");
    }

    public void markAsUndone() {
        this.isDone = false;

        System.out.println("____________________________________________________________\n");
        System.out.println(" OK, I've marked this task as not done yet:\n");
        System.out.println(this.toString());
        System.out.println("____________________________________________________________\n");
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}

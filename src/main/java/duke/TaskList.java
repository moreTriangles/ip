package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;

/**
 * TaskList handles the loading, storing and interactions with tasks
 */
public class TaskList {
    /** ArrayList containing all tasks **/
    private ArrayList<Task> tasks;

    /** Storage instance for interaction with files **/
    private Storage storage;

    /**
     * Initializes a new TaskList with the given filepath
     * Filepath is used to initialize a Storage instance
     *
     * @param filePath Path to storage file
     */
    public TaskList(String filePath) {
        this.tasks = new ArrayList<>();
        this.storage = new Storage(filePath);
        this.getTasksFromStorage();
    }

    /**
     * Adds a new task to taskArrayList
     *
     * @param task Task object to be added
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Retrieves the task with the corresponding ID
     *
     * @param id ID of task
     * @return Task with corresponding ID
     */
    public Task get(int id) {
        return tasks.get(id - 1);
    }

    /**
     * Removes the task with corresponding ID
     *
     * @param id ID of task
     */
    public void remove(int id) {
        tasks.remove(id - 1);
    }

    /**
     * Lists the current Tasks in taskArrayList with numbering
     */
    public void list() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i+1 + "." + tasks.get(i).toString());
        }
        System.out.println();
    }

    /**
     * Retrieves the number of tasks currently
     *
     * @return Number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves a list of tasks from previous sessions, stored in data/duke.txt
     * If file does not exist, it will be created
     * Parses and adds tasks line by line to taskArrayList
     */
    public void getTasksFromStorage() {
        File dataFile = storage.getFile();
        Scanner sc;
        try {
            sc = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (sc.hasNext()) {
            String[] commandArr = sc.nextLine().split("\\|");
            boolean isDone = commandArr[1].equals("1");
            switch (commandArr[0]) {
            case "T":
                add(new ToDo(commandArr[2], isDone));
                break;
            case "D":
                LocalDate deadlineDate = LocalDate.parse(commandArr[3]);
                add(new Deadline(commandArr[2], deadlineDate, isDone));
                break;
            case "E":
                LocalDate eventDate = LocalDate.parse(commandArr[3]);
                add(new Event(commandArr[2], eventDate, isDone));
                break;
            }
        }
        sc.close();
    }

    /**
     * Saves the current list of tasks to data/duke.txt to be used in future sessions
     */
    public void saveTasksToStorage() {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String taskString;
            String isDone = task.isDone() ? "1|" : "0|";
            if (task instanceof ToDo) {
                taskString = "T|" + isDone + task.getName();
            } else if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                taskString = "D|" + isDone + task.getName() + "|" + deadline.getEndDate();
            } else {
                Event event = (Event) task;
                taskString = "E|" + isDone + task.getName() + "|" + event.getEventDate();
            }
            if (i == 0) {
                storage.writeToDataFile(taskString);
            } else {
                storage.appendToDataFile("\n" + taskString);
            }
        }
    }
}

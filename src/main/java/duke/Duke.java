package duke;

import java.time.format.DateTimeParseException;

import duke.exceptions.DukeException;
import duke.exceptions.InvalidCommandException;
import duke.exceptions.InvalidTaskNumberException;
import duke.exceptions.MissingDeadlineException;
import duke.exceptions.MissingEventTimeException;
import duke.exceptions.MissingTaskNameException;
import duke.exceptions.MissingTaskNumberException;

/**
 * Duke is a Personal Assistant Chatbot that keeps track of various tasks.
 *
 * @author Adam Oh Zhi Hong
 */
public class Duke {
    /** A class to keep track of all tasks of the Duke instance. **/
    private final TaskList taskList;

    /** Parses the user's strings **/
    private final Parser parser;

    /** Keeps track of whether the current reply is an exception. **/
    private boolean isException = false;

    /**
     * Initializes a new Duke chatbot with respective file path
     *
     * @param filePath Path to data file
     */
    public Duke(String filePath) {
        this.taskList = new TaskList(filePath);
        this.parser = new Parser(taskList);
    }

    /**
     * Gets a response from Duke.
     *
     * @return String response from Duke.
     */
    public String getResponse(String input) {
        String dukeResponse;
        isException = true;
        try {
            dukeResponse = parser.handle(input);
            isException = false;
        } catch (InvalidCommandException e) {
            dukeResponse = "I'm afraid I don't recognise that, please try again!";
        } catch (MissingTaskNameException e) {
            dukeResponse = "Task name cannot be empty!";
        } catch (MissingTaskNumberException e) {
            dukeResponse = "Did you forget to enter your task number?";
        } catch (InvalidTaskNumberException e) {
            dukeResponse = "Sorry, that task does not exist!";
        } catch (MissingDeadlineException e) {
            dukeResponse = "When is that due? Let me know after '/by'!";
        } catch (MissingEventTimeException e) {
            dukeResponse = "When is the event happening? Let me know after '/at'!";
        } catch (DateTimeParseException e) {
            dukeResponse = "Oops, did you enter your date in yyyy-mm-dd format?";
        } catch (DukeException e) {
            dukeResponse = Ui.dukeException();
        }
        return dukeResponse;
    }

    public boolean isException() {
        return isException;
    }
}


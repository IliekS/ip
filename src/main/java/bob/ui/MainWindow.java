package bob.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import bob.Bob;
import bob.storage.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controls Bob's main chat window. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;

    private final ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
    private final Bob bob = new Bob(new Ui(new Scanner(""), new PrintStream(responseBuffer)),
            Storage.createDefaultStorage());
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image bobImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        responseBuffer.reset();
        new Ui(new Scanner(""), new PrintStream(responseBuffer)).showWelcome();
        dialogContainer.getChildren().add(DialogBox.getBobDialog(cleanOutput(), bobImage));
    }

    /** Processes the entered command and displays Bob's response. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        responseBuffer.reset();
        boolean shouldExit = bob.respond(input);
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                DialogBox.getBobDialog(cleanOutput(), bobImage));
        userInput.clear();
        if (shouldExit) {
            userInput.setDisable(true);
        }
    }

    /** Removes ANSI terminal colour codes before console output is shown in the GUI. */
    private String cleanOutput() {
        return responseBuffer.toString(StandardCharsets.UTF_8).replaceAll("\\u001B\\[[;\\d]*m", "");
    }
}

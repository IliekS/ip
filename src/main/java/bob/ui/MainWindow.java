package bob.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import bob.Bob;
import bob.storage.Storage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Bob's main chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;

    private final ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
    private final Ui ui = new Ui(new Scanner(""), new PrintStream(responseBuffer));
    private final Bob bob = new Bob(ui, Storage.createDefaultStorage());
    private final Image userImage = loadImage("/images/User.png");
    private final Image bobImage = loadImage("/images/Bob.png");

    /**
     * Loads an optional avatar, leaving it blank when the resource is missing or unreadable.
     */
    static Image loadImage(String path) {
        URL resource = MainWindow.class.getResource(path);
        if (resource == null) {
            return null;
        }
        Image image = new Image(resource.toExternalForm());
        return image.isError() ? null : image;
    }

    /**
     * Displays startup warnings separately from Bob's normal welcome message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String warning = cleanOutput();
        if (!warning.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getBobDialog(warning, bobImage, true));
        }
        responseBuffer.reset();
        ui.resetError();
        ui.showWelcome();
        dialogContainer.getChildren().add(DialogBox.getBobDialog(cleanOutput(), bobImage));
    }

    /**
     * Processes the entered command and displays Bob's response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        responseBuffer.reset();
        ui.resetError();
        boolean shouldExit = bob.respond(input);
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                DialogBox.getBobDialog(cleanOutput(), bobImage, ui.hasError()));
        userInput.clear();
        if (shouldExit) {
            Platform.exit();
        }
    }

    /**
     * Removes ANSI terminal color codes before console output is shown in the GUI.
     */
    private String cleanOutput() {
        return cleanOutput(responseBuffer.toString(StandardCharsets.UTF_8));
    }

    /**
     * Removes terminal colors and trailing whitespace while preserving lines within a GUI reply.
     */
    static String cleanOutput(String output) {
        return output.replaceAll("\\u001B\\[[;\\d]*m", "").stripTrailing();
    }
}

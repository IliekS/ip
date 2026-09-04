package bob.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Displays one chat message and its speaker image. */
public class DialogBox extends HBox {
    @FXML private Label dialog;
    @FXML private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load a dialog box.", exception);
        }
        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /** Returns a dialog box for a user message. */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /** Returns a dialog box for a Bob message. */
    public static DialogBox getBobDialog(String text, Image image) {
        DialogBox box = new DialogBox(text, image);
        Node firstChild = box.getChildren().get(0);
        Node secondChild = box.getChildren().get(1);
        box.getChildren().setAll(secondChild, firstChild);
        box.setAlignment(Pos.TOP_LEFT);
        box.dialog.getStyleClass().add("reply-label");
        return box;
    }
}

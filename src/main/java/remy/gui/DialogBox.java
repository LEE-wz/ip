package remy.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the dialog layout.", e);
        }

        dialog.setText(text);
        dialog.maxWidthProperty()
                .bind(widthProperty()
                        .multiply(0.72));
        displayPicture.setImage(image);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a right-aligned dialog spoken by the user.
     *
     * @param text message to display
     * @param image profile image to display
     * @return user dialog box
     */
    public static DialogBox getUserDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);

        dialogBox.dialog
                .getStyleClass()
                .add("user-bubble");

        dialogBox.displayPicture
                .setAccessibleText("User");

        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog spoken by Remy.
     *
     * @param text message to display
     * @param image profile image to display
     * @return Remy dialog box
     */
    public static DialogBox getRemyDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);

        dialogBox.dialog
                .getStyleClass()
                .add("remy-bubble");

        dialogBox.displayPicture
                .setAccessibleText("Remy");

        dialogBox.flip();
        return dialogBox;
    }
}

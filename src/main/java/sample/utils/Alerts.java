package sample.utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import java.util.Optional;

public class Alerts {

    private Alert alert;

    private Alerts(Alert.AlertType alertType) {
        this.alert = new Alert(alertType);
    }

    public static Alerts create(Alert.AlertType alertType) {
        return new Alerts(alertType);
    }


    public Alerts setHeaderText(String headerText) {
        alert.setHeaderText(headerText);
        return this;
    }

    public Alerts setIcon(Image icon) {
        //TODO how set icon
        return this;
    }

    public Alerts setButtons(ButtonType... buttonTypes) {
        alert.getButtonTypes().setAll(buttonTypes);
        return this;
    }

    public Alerts setHeaderContent(Node node) {
        alert.getDialogPane().setHeader(node);
        return this;
    }

    public Alerts setExpandableContent(Node node) {
        alert.getDialogPane().setExpanded(true);
        alert.getDialogPane().setExpandableContent(node);
        return this;
    }

    public Optional<ButtonType> showAndWait() {
        return alert.showAndWait();
    }

    public void show() {
        alert.show();
    }


    public Alerts setTitle(String title) {
        alert.setTitle(title);
        return this;
    }


    public Alerts setContentText(String contentText) {
        alert.setContentText(contentText);
        return this;
    }
}
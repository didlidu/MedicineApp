package com.bunjlabs.medicineapp.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class FXUtils {

    public static void warningAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        alert.showAndWait();
    }
}

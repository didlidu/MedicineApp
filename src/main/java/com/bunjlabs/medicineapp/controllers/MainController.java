package com.bunjlabs.medicineapp.controllers;

import com.bunjlabs.medicineapp.db.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainController implements Initializable {

    private static final Logger log = LogManager.getLogger(MainController.class);

    private static final class LeftPage {

        public String name;
        public Parent parent;

        public LeftPage(String name, Parent parent) {
            this.name = name;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    private final List<LeftPage> leftPages = new ArrayList<>();

    private User user;

    @FXML
    private ListView leftList;

    @FXML
    private AnchorPane content;

    public MainController(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            if (user.getRole().equalsIgnoreCase("medic") || user.getRole().equalsIgnoreCase("admin")) {
                leftPages.add(new LeftPage("Истории болезней", FXMLLoader.load(MainController.class.getResource("/fxml/panels/disease_history.fxml"))));
                leftPages.add(new LeftPage("Стандарты", FXMLLoader.load(MainController.class.getResource("/fxml/panels/standarts.fxml"))));
            }
            if (user.getRole().equalsIgnoreCase("researcher") || user.getRole().equalsIgnoreCase("admin")) {
                leftPages.add(new LeftPage("Отчеты", FXMLLoader.load(MainController.class.getResource("/fxml/panels/results.fxml"))));
            }
        } catch (IOException ex) {
            log.catching(ex);
        }

        leftList.setItems(FXCollections.observableList(leftPages));
        leftList.getSelectionModel().selectedItemProperty().addListener(new PageChangeListener());
        leftList.getSelectionModel().select(0);
    }

    @FXML
    public void handleExit(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    private class PageChangeListener implements ChangeListener<LeftPage> {

        public PageChangeListener() {
        }

        @Override
        public void changed(ObservableValue<? extends LeftPage> observable, LeftPage oldValue, LeftPage newValue) {
            content.getChildren().setAll(newValue.parent);
        }
    }

}

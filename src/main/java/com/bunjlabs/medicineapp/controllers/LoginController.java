package com.bunjlabs.medicineapp.controllers;

import com.bunjlabs.medicineapp.db.User;
import com.bunjlabs.medicineapp.utils.FXUtils;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Root;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController implements Initializable {

    private static final Logger log = LogManager.getLogger(LoginController.class);

    private static final class RoleForBox {

        public String roleId;
        public String roleName;

        public RoleForBox(String roleId, String roleName) {
            this.roleId = roleId;
            this.roleName = roleName;
        }

        @Override
        public String toString() {
            return roleName;
        }
    }

    private final List<RoleForBox> roles = new ArrayList<>();

    @FXML
    private ChoiceBox<RoleForBox> roleBox;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String role = roleBox.getValue().roleId;
        String login = loginField.getText();
        String password = passwordField.getText();

        log.info("Autorizating: role {}, login {}", role, login);

        User u = User.getByLoginPass(login, password);

        if (u == null || !u.getRole().equalsIgnoreCase(role)) {
            FXUtils.warningAlert("Ошибка", "Неверный логин или пароль");
            return;
        }

        log.info("Autorized!");

        try {

            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));

            Parent mainRoot = (Parent) fxmlLoader.load();

            MainController controller = fxmlLoader.<MainController>getController();
            controller.setUser(u);

            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/main.fxml")));
            stage.setTitle("Медицина Апп");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            log.catching(ex);
        }

        Stage loginStage = (Stage) roleBox.getScene().getWindow();
        loginStage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roles.add(new RoleForBox("medic", "Врач"));
        roles.add(new RoleForBox("researcher", "Исследователь"));
        roles.add(new RoleForBox("admin", "Администратор"));

        roleBox.setItems(FXCollections.observableArrayList(roles));
    }

}

package com.bunjlabs.medicineapp.controllers.situations;

import com.bunjlabs.medicineapp.db.Situation;
import com.bunjlabs.medicineapp.db.Situation.SituationHuman;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SituationEditController implements Initializable {

    @FXML
    private TextField deseaseField;
    @FXML
    private TextField fioField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField growthField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField planField;
    @FXML
    private TextField factorsField;
    @FXML
    private TextField coDeseasesField;
    @FXML
    private TextField specialsField;

    protected SituationController situationController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(SituationHuman sh) {
        deseaseField.setText(sh.desease);
        fioField.setText(sh.fio);
        ageField.setText(sh.age + "");
        growthField.setText(sh.growth + "");
        weightField.setText(sh.weight + "");
        planField.setText(String.join(", ", sh.plan));
        factorsField.setText(String.join(", ", sh.factors));
        coDeseasesField.setText(String.join(", ", sh.coDeseases));
        specialsField.setText(String.join(", ", sh.specials));
    }

    public void handleSave(ActionEvent event) {
        SituationHuman sh = new SituationHuman();

        sh.desease = deseaseField.getText();
        sh.fio = fioField.getText();
        sh.age = Long.parseLong(ageField.getText());
        sh.growth = Long.parseLong(growthField.getText());
        sh.weight = Long.parseLong(weightField.getText());

        sh.plan = Arrays.asList(Stream.of(planField.getText().split(",")).map(s -> s.trim()).toArray(size -> new String[size]));
        sh.factors = Arrays.asList(Stream.of(factorsField.getText().split(",")).map(s -> s.trim()).toArray(size -> new String[size]));
        sh.coDeseases = Arrays.asList(Stream.of(coDeseasesField.getText().split(",")).map(s -> s.trim()).toArray(size -> new String[size]));
        sh.specials = Arrays.<String>asList(Stream.of(specialsField.getText().split(",")).map(s -> s.trim()).toArray(size -> new String[size]));;

        situationController.situationAddReturnValue = sh;


        ((Stage) deseaseField.getScene().getWindow()).close();
    }

}
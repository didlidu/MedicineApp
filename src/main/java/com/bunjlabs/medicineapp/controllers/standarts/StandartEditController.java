package com.bunjlabs.medicineapp.controllers.standarts;

import com.bunjlabs.medicineapp.db.Rule;
import com.bunjlabs.medicineapp.db.Rule.RuleHuman;
import com.bunjlabs.medicineapp.db.Situation;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StandartEditController {

    @FXML
    TextField deseaseField;
    @FXML
    TextField medicinesField;

    StandartsController standartController;

    public void setData(Rule.RuleHuman rh) {
        deseaseField.setText(rh.desease);
        medicinesField.setText(String.join(", ", rh.recomendedMedicines));
    }

    @FXML
    public void handleSave(ActionEvent event) {
        RuleHuman rh = new RuleHuman();

        rh.desease = deseaseField.getText();
        rh.recomendedMedicines = Arrays.asList(Stream.of(medicinesField.getText().split(",")).map(s -> s.trim()).toArray(size -> new String[size]));

        standartController.standartAddReturnValue = rh;

        ((Stage) deseaseField.getScene().getWindow()).close();
    }

}

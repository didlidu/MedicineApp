package com.bunjlabs.medicineapp.controllers.standarts;

import com.bunjlabs.medicineapp.db.Rule.RuleHuman;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class StandartEditController {
    
    @FXML TextField deseaseField;
    @FXML TextField medicinesField;

    StandartsController standartController;
    
    @FXML
    public void handleSave(ActionEvent event) {
        RuleHuman rh = new RuleHuman();
        
        rh.desease = deseaseField.getText();
        rh.recomendedMedicines = Arrays.asList(Stream.of(medicinesField.getText().split(",")).map(s -> s.trim()).toArray(size -> new String[size]));

        standartController.standartAddReturnValue = rh;

        ((Stage) deseaseField.getScene().getWindow()).close();
    }
    
}

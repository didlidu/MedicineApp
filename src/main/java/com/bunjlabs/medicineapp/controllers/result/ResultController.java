package com.bunjlabs.medicineapp.controllers.result;

import com.bunjlabs.medicineapp.db.Primitive;
import com.bunjlabs.medicineapp.db.Rule;
import com.bunjlabs.medicineapp.db.Rule.RuleHuman;
import com.bunjlabs.medicineapp.db.Situation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class ResultController implements Initializable {

    @FXML
    Accordion accordion;
    @FXML
    public TextField diagnoseField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void addPaneToAccordion(String name, String text) {
        TitledPane gridTitlePane = new TitledPane();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));
        TextArea control = null;
        control = new TextArea(text);
        grid.add(control, 0, 0);
        gridTitlePane.setText(name);
        gridTitlePane.setContent(grid);
        accordion.getPanes().add(gridTitlePane);
        gridTitlePane.setExpanded(true);
    }

    @FXML
    public void handleResult(ActionEvent event) {
        accordion.getPanes().clear();

        String diagnose = diagnoseField.getText();

        List<Situation.SituationHuman> situations = Situation.selectAll();
        List<Rule.RuleHuman> rules = new Rule().selectAll();

        List<String> medicinesForDiagnose = null;

        for (RuleHuman r : rules) {
            if (r.desease.equals(diagnose)) {
                if (medicinesForDiagnose != null) {
                    // песда, у на база плохая
                    return;
                }
                medicinesForDiagnose = r.recomendedMedicines;
            }
        }

        if (medicinesForDiagnose == null) {
            // песда, нехрен больше делать
            return;
        }

        List<Primitive> medicines = Primitive.selectAll("medicines");

        List<String> antiMedicinesForDiagnose = new ArrayList<>();

        for (Primitive m : medicines) {
            if (!medicinesForDiagnose.contains(m.getName())) {
                antiMedicinesForDiagnose.add(m.getName());
            }
        }

        Map<String, List<Situation.SituationHuman>> crosses = new HashMap<>();

        for (String medicine : antiMedicinesForDiagnose) {
            List<Situation.SituationHuman> curcross = new ArrayList<>();
            for (Situation.SituationHuman situation : situations) {
                if (situation.desease.equals(diagnose) && situation.plan.contains(medicine)) {
                    curcross.add(situation);
                }
            }
            if (!curcross.isEmpty()) {
                crosses.put(medicine, curcross);
            }
        }

        if (crosses.isEmpty()) {
            // говори, что нечего показать
        }

        crosses.forEach((key, value) -> {
            StringBuilder sb = new StringBuilder();
            value.forEach((sit) -> {
                sb.append(String.format(
                        "Ид. %d, Диаг. %s, ФИО %s, Рост %d, Вес %d, Возр. %d, План %s, Факторы %s, Соп. %s, Спец. %s\n",
                        sit.id, sit.desease, sit.fio, sit.growth, sit.weight, sit.age,
                        sit.plan, sit.factors, sit.coDeseases, sit.specials
                ));
            });
            
            addPaneToAccordion(key, sb.toString());
        });

    }
}

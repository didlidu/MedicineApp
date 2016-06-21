package com.bunjlabs.medicineapp.controllers.result;

import com.bunjlabs.medicineapp.db.Primitive;
import com.bunjlabs.medicineapp.db.Rule;
import com.bunjlabs.medicineapp.db.Rule.RuleHuman;
import com.bunjlabs.medicineapp.db.Situation;
import com.bunjlabs.medicineapp.utils.FXUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

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
        //GridPane grid = new GridPane();
        //grid.setVgap(4);
        //grid.setPadding(new Insets(5, 5, 5, 5));
        TextArea control = new TextArea(text);
        control.setEditable(false);
        //grid.add(control, 0, 0);
        VBox box = new VBox();
        box.getChildren().add(control);
        gridTitlePane.setText(name);
        gridTitlePane.setContent(box);
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
                    FXUtils.warningAlert("Ошибка", "Для указанного диагноза найдено несколько одинаковых и рекомендуемых медикамента.");
                    return;
                }
                medicinesForDiagnose = r.recomendedMedicines;
            }
        }
        
        if (medicinesForDiagnose == null) {
            FXUtils.warningAlert("Ошибка", "Нет подходящих медикоментов для указанного диагноза: " + diagnose + ".");
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
            FXUtils.warningAlert("Ошибка", "Нет данных для отображения.");
            return;
        }
        
        crosses.forEach((key, value) -> {
            Locale.setDefault(Locale.US);
            StringBuilder sb = new StringBuilder();
            value.forEach((sit) -> {
                sb.append(String.format(
                        "Ид. %d, Диаг. %s, ФИО %s, Рост %f, Вес %f, Пол %s, Возр. %d, План %s, Факторы %s, Соп. %s, Спец. %s\n",
                        sit.id, sit.desease, sit.fio, sit.growth, sit.weight, sit.sex, sit.age,
                        sit.plan, sit.factors, sit.coDeseases, sit.specials
                ));
            });
            
            List<String> similarPlans = new ArrayList<>();
            
            value.forEach((sit) -> {
                sit.plan.forEach((plan) -> {
                    if (value.stream().allMatch((sit2) -> sit2.plan.contains(plan))) {
                        if (!similarPlans.contains(plan)) {
                            similarPlans.add(plan);
                        }
                    }
                });
            });
            
            List<String> similarFactors = new ArrayList<>();
            
            value.forEach((sit) -> {
                sit.factors.forEach((factor) -> {
                    if (value.stream().allMatch((sit2) -> sit2.factors.contains(factor))) {
                        if (!similarFactors.contains(factor)) {
                            similarFactors.add(factor);
                        }
                    }
                });
            });
            
            List<String> similarCoDeas = new ArrayList<>();
            
            value.forEach((sit) -> {
                sit.coDeseases.forEach((coDesease) -> {
                    if (value.stream().allMatch((sit2) -> sit2.coDeseases.contains(coDesease))) {
                        if (!similarCoDeas.contains(coDesease)) {
                            similarCoDeas.add(coDesease);
                        }
                    }
                });
            });
            
            List<String> similarSpecials = new ArrayList<>();
            
            value.forEach((sit) -> {
                sit.specials.forEach((special) -> {
                    if (value.stream().allMatch((sit2) -> sit2.specials.contains(special))) {
                        if (!similarSpecials.contains(special)) {
                            similarSpecials.add(special);
                        }
                    }
                });
            });
            boolean similarGender = value.stream().allMatch((sit) -> sit.sex.equals(value.get(0).sex));
            
            boolean similarAge = Math.abs(
                    value.stream().min((a, b) -> (int) (a.age - b.age)).get().age
                    - value.stream().max((a, b) -> (int) (a.age - b.age)).get().age
            ) <= 5;
            
            boolean similarGrowth = Math.abs(
                    value.stream().min((a, b) -> (int) (a.growth - b.growth)).get().growth
                    - value.stream().max((a, b) -> (int) (a.growth - b.growth)).get().growth
            ) <= 10;
            
            boolean similarWeight
                    = value.stream().allMatch((sit)
                            -> getWeightCategory(sit.weight, sit.growth) == getWeightCategory(value.get(0).weight, value.get(0).growth));
            
            sb.append("\n");
            if (value.size() > 1) {
                if (!similarPlans.isEmpty()) {
                    sb.append("В планах лечения пациентов имеются схожие препараты: ").append(similarPlans.toString()).append("\n");
                }
                if (!similarFactors.isEmpty()) {
                    sb.append("Все пациенты имеют схожие сопутствующие факторы: ").append(similarFactors.toString()).append("\n");
                }
                if (!similarCoDeas.isEmpty()) {
                    sb.append("Все пациенты имеют схожие сопутствующие диагнозы: ").append(similarCoDeas.toString()).append("\n");
                }
                if (!similarSpecials.isEmpty()) {
                    sb.append("Все пациенты имеют схожие особенности: ").append(similarSpecials.toString()).append("\n");
                }
                
                if (similarGender) {
                    sb.append("Все пациенты одного пола.\n");
                }
                if (similarAge) {
                    sb.append("Разница в возрасте между самым молодым и самым пожилым пациентом не превышает 5 лет.\n");
                }
                if (similarGrowth) {
                    sb.append("Разница в росте между самым высоким и самым низким пациентом не превышает 10см.\n");
                }
                if (similarWeight) {
                    sb.append("Все пациенты одной весовой категории.\n");
                }
                
                if (!(!similarPlans.isEmpty() || !similarFactors.isEmpty() || !similarCoDeas.isEmpty() || !similarSpecials.isEmpty()
                        || similarGender || similarAge || similarGrowth || similarWeight)) {
                    sb.append("Среди пациентов не наблюдается сходств.");
                }
            } else {
                sb.append("В группе всего один пациент.");
            }
            addPaneToAccordion(key, sb.toString());
        });
        
    }
    
    private int getWeightCategory(double weight, double growth) {
        double index = (weight / Math.pow(growth, 2));
        
        if (index <= 20) {
            return 0;
        } else if (index <= 25) {
            return 1;
        } else if (index <= 30) {
            return 2;
        } else if (index <= 40) {
            return 3;
        } else {
            return 4;
        }
        
    }
}

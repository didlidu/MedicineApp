package com.bunjlabs.medicineapp.controllers.situations;

import com.bunjlabs.medicineapp.db.Situation;
import com.bunjlabs.medicineapp.db.Situation.SituationHuman;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SituationController implements Initializable {

    @FXML
    private TableView table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn idCol = new TableColumn("Ид.");
        idCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, Long>("id"));

        TableColumn deseaseCol = new TableColumn("Диагноз");
        deseaseCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, String>("name"));

        TableColumn fioCol = new TableColumn("ФИО");
        fioCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, String>("fio"));

        TableColumn ageCol = new TableColumn("Возраст");
        ageCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, Long>("age"));

        TableColumn growthCol = new TableColumn("Рост");
        growthCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, Long>("growth"));

        TableColumn weightCol = new TableColumn("Вес");
        weightCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, Long>("weight"));

        TableColumn planCol = new TableColumn("План лечения");
        planCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, String>("plan"));

        TableColumn factorsCol = new TableColumn("Сопутствующие факторы");
        factorsCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, String>("factors"));

        TableColumn coDeseasesCol = new TableColumn("Сопутствующие заболевания");
        coDeseasesCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, String>("coDeseases"));

        TableColumn specialsCol = new TableColumn("Особенности");
        specialsCol.setCellValueFactory(
                new PropertyValueFactory<SituationHumanFx, String>("specials"));

        table.getColumns().addAll(
                idCol,
                deseaseCol,
                fioCol,
                ageCol,
                growthCol,
                weightCol,
                planCol,
                factorsCol,
                coDeseasesCol,
                specialsCol
        );

        List<SituationHuman> shs = Situation.selectAll();

        List<SituationHumanFx> shfx = new ArrayList<>();

        shs.forEach((sh) -> shfx.add(new SituationHumanFx(sh)));

        table.setItems(FXCollections.observableList(shfx));
    }

    @FXML
    public void handleAdd(ActionEvent event) {

    }

    private static final class SituationHumanFx {

        private final SimpleLongProperty id;
        private final SimpleStringProperty desease;
        private final SimpleStringProperty fio;
        private final SimpleLongProperty age;
        private final SimpleLongProperty growth;
        private final SimpleLongProperty weight;
        private final SimpleStringProperty plan;
        private final SimpleStringProperty factors;
        private final SimpleStringProperty coDeseases;
        private final SimpleStringProperty specials;

        public SituationHumanFx(SituationHuman sh) {
            this.id = new SimpleLongProperty(sh.id);
            this.desease = new SimpleStringProperty(sh.desease);
            this.fio = new SimpleStringProperty(sh.fio);
            this.age = new SimpleLongProperty(sh.age);
            this.growth = new SimpleLongProperty(sh.growth);
            this.weight = new SimpleLongProperty(sh.weight);
            this.plan = new SimpleStringProperty(String.join(", ", sh.plan));
            this.factors = new SimpleStringProperty(String.join(", ", sh.factors));
            this.coDeseases = new SimpleStringProperty(String.join(", ", sh.coDeseases));
            this.specials = new SimpleStringProperty(String.join(", ", sh.specials));

        }
    }

}

package com.bunjlabs.medicineapp.controllers.situations;

import com.bunjlabs.medicineapp.db.Situation;
import com.bunjlabs.medicineapp.db.Situation.SituationHuman;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
                new PropertyValueFactory<SituationHumanFx, String>("desease"));

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

        refreshTable();
    }

    private void refreshTable() {

        List<SituationHuman> shs = Situation.selectAll();

        List<SituationHumanFx> shfx = new ArrayList<>();

        shs.forEach((sh) -> shfx.add(new SituationHumanFx(sh)));

        table.setItems(FXCollections.observableList(shfx));
    }
    protected SituationHuman situationAddReturnValue;

    @FXML
    public void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/fxml/situationedit.fxml"));
        Parent root = fXMLLoader.load();
        SituationEditController controller = fXMLLoader.getController();
        controller.situationController = this;
        Stage stage = new Stage();
        stage.setTitle("Добавление ситуации");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();

        if (situationAddReturnValue != null) {
            situationAddReturnValue.insertSituation();
            situationAddReturnValue = null;

            refreshTable();
        }
    }

    public static final class SituationHumanFx {

        public SimpleLongProperty id;
        public SimpleStringProperty desease;
        public SimpleStringProperty fio;
        public SimpleLongProperty age;
        public SimpleLongProperty growth;
        public SimpleLongProperty weight;
        public SimpleStringProperty plan;
        public SimpleStringProperty factors;
        public SimpleStringProperty coDeseases;
        public SimpleStringProperty specials;

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

        public Long getId() {
            return id.get();
        }

        public String getDesease() {
            return desease.get();
        }

        public String getFio() {
            return fio.get();
        }

        public Long getAge() {
            return age.get();
        }

        public Long getGrowth() {
            return growth.get();
        }

        public Long getWeight() {
            return weight.get();
        }

        public String getPlan() {
            return plan.get();
        }

        public String getFactors() {
            return factors.get();
        }

        public String getCoDeseases() {
            return coDeseases.get();
        }

        public String getSpecials() {
            return specials.get();
        }

        public void setId(SimpleLongProperty id) {
            this.id = id;
        }

        public void setDesease(SimpleStringProperty desease) {
            this.desease = desease;
        }

        public void setFio(SimpleStringProperty fio) {
            this.fio = fio;
        }

        public void setAge(SimpleLongProperty age) {
            this.age = age;
        }

        public void setGrowth(SimpleLongProperty growth) {
            this.growth = growth;
        }

        public void setWeight(SimpleLongProperty weight) {
            this.weight = weight;
        }

        public void setPlan(SimpleStringProperty plan) {
            this.plan = plan;
        }

        public void setFactors(SimpleStringProperty factors) {
            this.factors = factors;
        }

        public void setCoDeseases(SimpleStringProperty coDeseases) {
            this.coDeseases = coDeseases;
        }

        public void setSpecials(SimpleStringProperty specials) {
            this.specials = specials;
        }
    }

}

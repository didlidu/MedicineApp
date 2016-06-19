/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.medicineapp.controllers.standarts;

import com.bunjlabs.medicineapp.controllers.situations.SituationController;
import com.bunjlabs.medicineapp.controllers.situations.SituationEditController;
import com.bunjlabs.medicineapp.db.Binding;
import com.bunjlabs.medicineapp.db.Rule;
import com.bunjlabs.medicineapp.db.Rule.RuleHuman;
import com.bunjlabs.medicineapp.db.Situation;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;


public class StandartsController implements Initializable {

    @FXML
    private TableView table;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn idCol = new TableColumn("Ид.");
        idCol.setCellValueFactory(
                new PropertyValueFactory<StandartHumanFx, Long>("id"));
        
        TableColumn deseaseCol = new TableColumn("Диагноз");
        deseaseCol.setCellValueFactory(
                new PropertyValueFactory<StandartHumanFx, Long>("desease"));
        
        TableColumn medicinesCol = new TableColumn("Рекомендованные препараты");
        medicinesCol.setCellValueFactory(
                new PropertyValueFactory<StandartHumanFx, Long>("medicines"));
        
        table.getColumns().addAll(
                idCol,
                deseaseCol,
                medicinesCol
        );
        
        table.setRowFactory(new Callback<TableView<StandartHumanFx>, TableRow<StandartHumanFx>>() {
            @Override
            public TableRow<StandartHumanFx> call(TableView<StandartHumanFx> tableView) {
                final TableRow<StandartHumanFx> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Удалить");
                removeMenuItem.setOnAction((ActionEvent event) -> {

                    Rule.delete(row.getItem().id.get());
                    Binding.delete2("recomended_medicine_bindings", row.getItem().id.get());
                    
                    refreshTable();
                });
                contextMenu.getItems().add(removeMenuItem);

                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }
        });
        
        refreshTable();
    }
    
    private void refreshTable() {
        List<RuleHuman> shs = new Rule().selectAll();
        List<StandartHumanFx> shfx = new ArrayList<>();
        shs.forEach((sh) -> shfx.add(new StandartHumanFx(sh)));
        table.setItems(FXCollections.observableList(shfx));
    }
    
    public RuleHuman standartAddReturnValue;

    @FXML
    private void handleAdd(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/fxml/standartedit.fxml"));
        Parent root = fXMLLoader.load();
        StandartEditController controller = fXMLLoader.getController();
        controller.standartController = this;
        Stage stage = new Stage();
        stage.setTitle("Добавление правила");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();

        if (standartAddReturnValue != null) {
            standartAddReturnValue.insertRule();
            standartAddReturnValue = null;
            refreshTable();
        }
    }
    
    public class StandartHumanFx {
        public SimpleLongProperty id;
        public SimpleStringProperty desease;
        public SimpleStringProperty medicines;
        
        public StandartHumanFx(RuleHuman rh) {
            this.id = new SimpleLongProperty(rh.id);
            this.desease = new SimpleStringProperty(rh.desease);
            this.medicines = new SimpleStringProperty(String.join(", ", rh.recomendedMedicines));
        }
        
        public Long getId() {
            return id.get();
        }
        
        public String getDesease() {
            return desease.get();
        }
        
        public String getMedicines() {
            return medicines.get();
        }
    }
    
}

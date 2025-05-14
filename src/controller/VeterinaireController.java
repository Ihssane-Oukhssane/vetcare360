package controller;

import dao.VeterinaireDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Veterinaire;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.function.Predicate;


public class VeterinaireController {
    @FXML private TextField tfRecherche;
    @FXML private TableView<Veterinaire> tableView;
    @FXML private TableColumn<Veterinaire, String> colNom, colPrenom, colSpecialite, colTelephone, colEmail;
    @FXML private TextField tfNom, tfPrenom, tfSpecialite, tfTelephone, tfEmail;

    private ObservableList<Veterinaire> veterinaireList;

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrenom()));
        colSpecialite.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSpecialite()));
        colTelephone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelephone()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        veterinaireList = FXCollections.observableArrayList(VeterinaireDAO.getAll());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FilteredList<Veterinaire> filteredData = new FilteredList<>(veterinaireList, p -> true);

        tfRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                filteredData.setPredicate(v -> true);
            } else {
                String lower = newVal.toLowerCase();

                ObservableList<Veterinaire> sorted = FXCollections.observableArrayList(veterinaireList.sorted((v1, v2) -> {
                    boolean v1Match = v1.getNom().toLowerCase().startsWith(lower) || v1.getPrenom().toLowerCase().startsWith(lower);
                    boolean v2Match = v2.getNom().toLowerCase().startsWith(lower) || v2.getPrenom().toLowerCase().startsWith(lower);

                    if (v1Match && !v2Match) return -1;
                    if (!v1Match && v2Match) return 1;
                    return 0;
                }));

                filteredData.setPredicate(v -> v.getNom().toLowerCase().contains(lower) ||
                        v.getPrenom().toLowerCase().contains(lower) ||
                        v.getSpecialite().toLowerCase().contains(lower) ||
                        v.getEmail().toLowerCase().contains(lower));

                tableView.setItems(sorted.filtered((Predicate<Veterinaire>) filteredData.getPredicate()));
            }
        });


        SortedList<Veterinaire> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> fillForm(newVal));
    }


    private void loadData() {
        veterinaireList = FXCollections.observableArrayList(VeterinaireDAO.getAll());
        tableView.setItems(veterinaireList);
    }

    private void fillForm(Veterinaire v) {
        if (v != null) {
            tfNom.setText(v.getNom());
            tfPrenom.setText(v.getPrenom());
            tfSpecialite.setText(v.getSpecialite());
            tfTelephone.setText(v.getTelephone());
            tfEmail.setText(v.getEmail());
        }
    }

    @FXML
    private void ajouter() {
        Veterinaire v = new Veterinaire(tfNom.getText(), tfPrenom.getText(), tfSpecialite.getText(), tfTelephone.getText(), tfEmail.getText());
        VeterinaireDAO.insert(v);
        loadData();
        clearForm();
    }

    @FXML
    private void modifier() {
        Veterinaire selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setNom(tfNom.getText());
            selected.setPrenom(tfPrenom.getText());
            selected.setSpecialite(tfSpecialite.getText());
            selected.setTelephone(tfTelephone.getText());
            selected.setEmail(tfEmail.getText());
            VeterinaireDAO.update(selected);
            loadData();
            clearForm();
        }
    }

    @FXML
    private void supprimer() {
        Veterinaire selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            VeterinaireDAO.delete(selected.getId());
            loadData();
            clearForm();
        }
    }

    private void clearForm() {
        tfNom.clear();
        tfPrenom.clear();
        tfSpecialite.clear();
        tfTelephone.clear();
        tfEmail.clear();
    }


}

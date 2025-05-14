package controller;

import dao.AnimalDAO;
import dao.VeterinaireDAO;
import dao.VisiteDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.Animal;
import model.Veterinaire;
import model.Visite;

import java.time.LocalDate;

public class VisiteController {

    @FXML private TableView<Visite> tableVisite;
    @FXML private TableColumn<Visite, LocalDate> colDate;
    @FXML private TableColumn<Visite, String> colDescription, colAnimal, colVeterinaire;

    @FXML private DatePicker dpDate;
    @FXML private TextArea taDescription;
    @FXML private ComboBox<Animal> cbAnimal;
    @FXML private ComboBox<Veterinaire> cbVeterinaire;
    @FXML private TextField tfRecherche;

    private final VisiteDAO visiteDAO = new VisiteDAO();
    private final AnimalDAO animalDAO = new AnimalDAO();
    private final VeterinaireDAO veterinaireDAO = new VeterinaireDAO();
    private final ObservableList<Visite> visites = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDateVisite()));
        colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        colAnimal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomAnimal()));
        colVeterinaire.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNomVeterinaire()));

        cbAnimal.setItems(FXCollections.observableArrayList(animalDAO.getAll()));
        cbVeterinaire.setItems(FXCollections.observableArrayList(veterinaireDAO.getAll()));

        cbAnimal.setConverter(new StringConverter<>() {
            public String toString(Animal a) { return a == null ? "" : a.getNom(); }
            public Animal fromString(String s) { return null; }
        });

        cbVeterinaire.setConverter(new StringConverter<>() {
            public String toString(Veterinaire v) { return v == null ? "" : v.getNom() + " " + v.getPrenom(); }
            public Veterinaire fromString(String s) { return null; }
        });

        loadVisites();

        tableVisite.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                dpDate.setValue(newVal.getDateVisite());
                taDescription.setText(newVal.getDescription());

                cbAnimal.getItems().stream()
                        .filter(a -> a.getId() == newVal.getIdAnimal())
                        .findFirst().ifPresent(cbAnimal::setValue);

                cbVeterinaire.getItems().stream()
                        .filter(v -> v.getId() == newVal.getIdVeterinaire())
                        .findFirst().ifPresent(cbVeterinaire::setValue);
            }
        });

        FilteredList<Visite> filteredData = new FilteredList<>(visites, p -> true);
        tfRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal == null ? "" : newVal.toLowerCase();
            filteredData.setPredicate(v ->
                    v.getDescription().toLowerCase().contains(lower) ||
                            v.getNomAnimal().toLowerCase().contains(lower) ||
                            (v.getNomVeterinaire() != null && v.getNomVeterinaire().toLowerCase().contains(lower))
            );

            tableVisite.setItems(filteredData);
            if (!filteredData.isEmpty()) {
                tableVisite.getSelectionModel().select(0);
            }
        });
    }

    private void loadVisites() {
        visites.setAll(visiteDAO.getAll());
        tableVisite.setItems(visites);
    }

    @FXML
    private void ajouter() {
        Animal animal = cbAnimal.getValue();
        Veterinaire vet = cbVeterinaire.getValue();
        if (dpDate.getValue() == null || animal == null || vet == null) {
            showAlert("Champs requis", "Veuillez remplir tous les champs.");
            return;
        }

        Visite v = new Visite(0, dpDate.getValue(), taDescription.getText(),
                animal.getId(), animal.getNom(), vet.getId(), vet.getNom() + " " + vet.getPrenom());

        visiteDAO.insert(v);
        clearFields();
        loadVisites();
    }

    @FXML
    private void modifier() {
        Visite selected = tableVisite.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Veuillez sélectionner une visite.");
            return;
        }

        Animal animal = cbAnimal.getValue();
        Veterinaire vet = cbVeterinaire.getValue();

        selected.setDateVisite(dpDate.getValue());
        selected.setDescription(taDescription.getText());
        selected.setIdAnimal(animal.getId());
        selected.setNomAnimal(animal.getNom());
        selected.setIdVeterinaire(vet.getId());
        selected.setNomVeterinaire(vet.getNom() + " " + vet.getPrenom());

        visiteDAO.update(selected);
        clearFields();
        loadVisites();
    }

    @FXML
    private void supprimer() {
        Visite selected = tableVisite.getSelectionModel().getSelectedItem();
        if (selected != null) {
            visiteDAO.delete(selected.getId());
            clearFields();
            loadVisites();
        }
    }

    private void clearFields() {
        dpDate.setValue(null);
        taDescription.clear();
        cbAnimal.getSelectionModel().clearSelection();
        cbVeterinaire.getSelectionModel().clearSelection();
        tableVisite.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

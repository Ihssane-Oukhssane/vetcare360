package controller;

import dao.AnimalDAO;
import dao.ProprietaireDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import model.Animal;
import model.Proprietaire;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.time.LocalDate;
import java.util.function.Predicate;

public class AnimalController {

    @FXML private TableView<Animal> animalTable;
    @FXML private TableColumn<Animal, String> colNom, colEspece, colRace, colSexe, colProprietaire;
    @FXML private TableColumn<Animal, LocalDate> colDateNaissance;
    @FXML private TextField tfRecherche;

    @FXML private TextField tfNom, tfEspece, tfRace;
    @FXML private DatePicker dpNaissance;
    @FXML private ComboBox<String> cbSexe;
    @FXML private ComboBox<Proprietaire> cbProprietaire;

    private final AnimalDAO animalDAO = new AnimalDAO();
    private final ProprietaireDAO proprietaireDAO = new ProprietaireDAO();
    private final ObservableList<Animal> animalList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Colonnes
        colNom.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNom()));
        colEspece.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEspece()));
        colRace.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRace()));
        colSexe.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSexe()));
        colDateNaissance.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getDateNaissance()));
        colProprietaire.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNomProprietaire()));

        // Remplir ComboBox
        cbSexe.setItems(FXCollections.observableArrayList("Male", "Femelle"));
        cbProprietaire.setItems(FXCollections.observableArrayList(proprietaireDAO.getAll()));
        cbProprietaire.setConverter(new StringConverter<>() {
            @Override public String toString(Proprietaire p) { return p == null ? "" : p.getNom() + " " + p.getPrenom(); }
            @Override public Proprietaire fromString(String s) { return null; }
        });

        // Charger les animaux
        loadAnimaux();
        FilteredList<Animal> filteredData = new FilteredList<>(animalList, p -> true);

        tfRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                filteredData.setPredicate(p -> true);
                animalTable.setItems(animalList);
            } else {
                String lower = newVal.toLowerCase();

                ObservableList<Animal> sorted = FXCollections.observableArrayList(animalList.sorted((a1, a2) -> {
                    boolean a1Match = a1.getNom().toLowerCase().startsWith(lower);
                    boolean a2Match = a2.getNom().toLowerCase().startsWith(lower);
                    if (a1Match && !a2Match) return -1;
                    if (!a1Match && a2Match) return 1;
                    return 0;
                }));

                filteredData.setPredicate(a ->
                        a.getNom().toLowerCase().contains(lower) ||
                                a.getEspece().toLowerCase().contains(lower) ||
                                a.getRace().toLowerCase().contains(lower) ||
                                a.getNomProprietaire().toLowerCase().contains(lower)
                );

                ObservableList<Animal> filteredAndSorted = sorted.filtered((Predicate<Animal>) filteredData.getPredicate());
                animalTable.setItems(filteredAndSorted);

                if (!filteredAndSorted.isEmpty()) {
                    animalTable.getSelectionModel().select(0);
                }
            }
        });


        animalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tfNom.setText(newVal.getNom());
                tfEspece.setText(newVal.getEspece());
                tfRace.setText(newVal.getRace());
                cbSexe.setValue(newVal.getSexe());
                dpNaissance.setValue(newVal.getDateNaissance());

                for (Proprietaire p : cbProprietaire.getItems()) {
                    if (p.getId() == newVal.getIdProprietaire()) {
                        cbProprietaire.setValue(p);
                        break;
                    }
                }
            }
        });
    }

    private void loadAnimaux() {
        animalList.setAll(animalDAO.getAll());
        animalTable.setItems(animalList);
    }

    @FXML
    private void ajouterAnimal() {
        if (tfNom.getText().isEmpty() || cbProprietaire.getValue() == null) {
            showAlert("Champs requis", "Nom et propriétaire obligatoires.");
            return;
        }

        Animal a = new Animal(0, tfNom.getText(), tfEspece.getText(), tfRace.getText(), cbSexe.getValue(),
                dpNaissance.getValue(), cbProprietaire.getValue().getId(), cbProprietaire.getValue().getNom() + " " + cbProprietaire.getValue().getPrenom());
        animalDAO.insert(a);
        clearFields();
        loadAnimaux();
    }

    @FXML
    private void modifierAnimal() {
        Animal selected = animalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Sélectionnez un animal à modifier.");
            return;
        }

        selected.setNom(tfNom.getText());
        selected.setEspece(tfEspece.getText());
        selected.setRace(tfRace.getText());
        selected.setSexe(cbSexe.getValue());
        selected.setDateNaissance(dpNaissance.getValue());
        selected.setIdProprietaire(cbProprietaire.getValue().getId());
        selected.setNomProprietaire(cbProprietaire.getValue().getNom() + " " + cbProprietaire.getValue().getPrenom());

        animalDAO.update(selected);
        clearFields();
        loadAnimaux();
    }

    @FXML
    private void supprimerAnimal() {
        Animal selected = animalTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            animalDAO.delete(selected.getId());
            clearFields();
            loadAnimaux();
        }
    }

    private void clearFields() {
        tfNom.clear();
        tfEspece.clear();
        tfRace.clear();
        dpNaissance.setValue(null);
        cbSexe.getSelectionModel().clearSelection();
        cbProprietaire.getSelectionModel().clearSelection();
        animalTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

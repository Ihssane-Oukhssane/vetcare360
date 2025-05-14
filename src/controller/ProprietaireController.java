package controller;

import dao.ProprietaireDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Proprietaire;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.function.Predicate;

public class ProprietaireController {
    @FXML private TextField tfRecherche;
    @FXML private TableView<Proprietaire> proprietaireTable;
    @FXML private TableColumn<Proprietaire, Integer> colId;
    @FXML private TableColumn<Proprietaire, String> colNom;
    @FXML private TableColumn<Proprietaire, String> colPrenom;
    @FXML private TableColumn<Proprietaire, String> colAdresse;
    @FXML private TableColumn<Proprietaire, String> colTelephone;
    @FXML private TableColumn<Proprietaire, String> colEmail;

    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfAdresse;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfEmail;

    private final ProprietaireDAO proprietaireDAO = new ProprietaireDAO();
    private final ObservableList<Proprietaire> proprietaireList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialiser les colonnes du tableau
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colNom.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNom()));
        colPrenom.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPrenom()));
        colAdresse.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getAdresse()));
        colTelephone.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTelephone()));
        colEmail.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));

        loadProprietaires();

        // Recherche dynamique avec tri personnalisé
        FilteredList<Proprietaire> filteredData = new FilteredList<>(proprietaireList, p -> true);

        tfRecherche.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                filteredData.setPredicate(p -> true);
                proprietaireTable.setItems(proprietaireList);
            } else {
                String lower = newVal.toLowerCase();

                // Tri personnalisé : les correspondances exactes d'abord
                ObservableList<Proprietaire> sorted = FXCollections.observableArrayList(proprietaireList.sorted((p1, p2) -> {
                    boolean p1Match = p1.getNom().toLowerCase().startsWith(lower) || p1.getPrenom().toLowerCase().startsWith(lower);
                    boolean p2Match = p2.getNom().toLowerCase().startsWith(lower) || p2.getPrenom().toLowerCase().startsWith(lower);

                    if (p1Match && !p2Match) return -1;
                    if (!p1Match && p2Match) return 1;
                    return 0;
                }));

                filteredData.setPredicate(p ->
                        p.getNom().toLowerCase().contains(lower) ||
                                p.getPrenom().toLowerCase().contains(lower) ||
                                p.getEmail().toLowerCase().contains(lower) ||
                                p.getTelephone().toLowerCase().contains(lower));

                ObservableList<Proprietaire> filteredAndSorted = sorted.filtered((Predicate<Proprietaire>) filteredData.getPredicate());
                proprietaireTable.setItems(filteredAndSorted);

                // Sélection automatique du premier résultat
                if (!filteredAndSorted.isEmpty()) {
                    proprietaireTable.getSelectionModel().select(0);
                }
            }
        });

        // Remplissage automatique des champs
        proprietaireTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tfNom.setText(newSelection.getNom());
                tfPrenom.setText(newSelection.getPrenom());
                tfAdresse.setText(newSelection.getAdresse());
                tfTelephone.setText(newSelection.getTelephone());
                tfEmail.setText(newSelection.getEmail());
            }
        });
    }



    private void loadProprietaires() {
        proprietaireList.setAll(proprietaireDAO.getAll());
        proprietaireTable.setItems(proprietaireList);
    }

    @FXML
    private void ajouterProprietaire() {
        if (tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty()) {
            showAlert("Champs requis", "Veuillez remplir au moins le nom et le prénom.");
            return;
        }

        Proprietaire p = new Proprietaire(0,
                tfNom.getText(),
                tfPrenom.getText(),
                tfAdresse.getText(),
                tfTelephone.getText(),
                tfEmail.getText()
        );
        proprietaireDAO.insert(p);
        clearFields();
        loadProprietaires();
    }

    @FXML
    private void modifierProprietaire() {
        Proprietaire selected = proprietaireTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un propriétaire à modifier.");
            return;
        }

        selected.setNom(tfNom.getText());
        selected.setPrenom(tfPrenom.getText());
        selected.setAdresse(tfAdresse.getText());
        selected.setTelephone(tfTelephone.getText());
        selected.setEmail(tfEmail.getText());

        proprietaireDAO.update(selected);
        clearFields();
        loadProprietaires();
    }

    @FXML
    private void supprimerProprietaire() {
        Proprietaire selected = proprietaireTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un propriétaire à supprimer.");
            return;
        }

        proprietaireDAO.delete(selected.getId());
        clearFields();
        loadProprietaires();
    }

    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfAdresse.clear();
        tfTelephone.clear();
        tfEmail.clear();
        proprietaireTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

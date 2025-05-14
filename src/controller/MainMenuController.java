package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private AnchorPane contentPane;

    public void initialize() throws IOException {
        loadAccueil(); // Affiche la page d'accueil au lancement
    }

    public void loadAccueil() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
        contentPane.getChildren().setAll(pane);
    }

    public void loadVeterinaires() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/fxml/ListeVeterinaires.fxml"));
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        contentPane.getChildren().setAll(node);

    }
    public void loadProprietaires() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/fxml/proprietaire_list.fxml"));
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        contentPane.getChildren().setAll(node);

    }
    public void loadAnimaux() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/fxml/animal.fxml"));
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        contentPane.getChildren().setAll(node);
    }

    public void loadVisites() throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/fxml/visite.fxml"));
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        contentPane.getChildren().setAll(node);
    }
}

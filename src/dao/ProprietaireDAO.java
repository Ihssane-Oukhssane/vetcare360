package dao;

import model.Proprietaire;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProprietaireDAO {

    public List<Proprietaire> getAll() {
        List<Proprietaire> proprietaires = new ArrayList<>();
        String query = "SELECT * FROM proprietaire";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Proprietaire p = new Proprietaire(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email")
                );
                proprietaires.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proprietaires;
    }

    public void insert(Proprietaire p) {
        String query = "INSERT INTO proprietaire (nom, prenom, adresse, telephone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setString(3, p.getAdresse());
            stmt.setString(4, p.getTelephone());
            stmt.setString(5, p.getEmail());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Proprietaire p) {
        String query = "UPDATE proprietaire SET nom = ?, prenom = ?, adresse = ?, telephone = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setString(3, p.getAdresse());
            stmt.setString(4, p.getTelephone());
            stmt.setString(5, p.getEmail());
            stmt.setInt(6, p.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM proprietaire WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Proprietaire getById(int id) {
        String query = "SELECT * FROM proprietaire WHERE id = ?";
        Proprietaire p = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Proprietaire(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p;
    }
}

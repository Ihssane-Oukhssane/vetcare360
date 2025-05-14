package dao;

import model.Veterinaire;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinaireDAO {

    public static List<Veterinaire> getAll() {
        List<Veterinaire> list = new ArrayList<>();
        String sql = "SELECT * FROM veterinaire";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Veterinaire(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("specialite"),
                        rs.getString("telephone"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insert(Veterinaire v) {
        String sql = "INSERT INTO veterinaire (nom, prenom, specialite, telephone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getNom());
            stmt.setString(2, v.getPrenom());
            stmt.setString(3, v.getSpecialite());
            stmt.setString(4, v.getTelephone());
            stmt.setString(5, v.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Veterinaire v) {
        String sql = "UPDATE veterinaire SET nom=?, prenom=?, specialite=?, telephone=?, email=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, v.getNom());
            stmt.setString(2, v.getPrenom());
            stmt.setString(3, v.getSpecialite());
            stmt.setString(4, v.getTelephone());
            stmt.setString(5, v.getEmail());
            stmt.setInt(6, v.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM veterinaire WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


package dao;

import model.Animal;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {

    public List<Animal> getAll() {
        List<Animal> list = new ArrayList<>();
        String query = "SELECT animal.*, CONCAT(p.nom, ' ', p.prenom) AS nom_proprietaire " +
                "FROM animal JOIN proprietaire p ON animal.id_proprietaire = p.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Animal a = new Animal(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("espece"),
                        rs.getString("race"),
                        rs.getString("sexe"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getInt("id_proprietaire"),
                        rs.getString("nom_proprietaire")
                );
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert(Animal a) {
        String query = "INSERT INTO animal (nom, espece, race, sexe, date_naissance, id_proprietaire) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, a.getNom());
            stmt.setString(2, a.getEspece());
            stmt.setString(3, a.getRace());
            stmt.setString(4, a.getSexe());
            stmt.setDate(5, Date.valueOf(a.getDateNaissance()));
            stmt.setInt(6, a.getIdProprietaire());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Animal a) {
        String query = "UPDATE animal SET nom=?, espece=?, race=?, sexe=?, date_naissance=?, id_proprietaire=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, a.getNom());
            stmt.setString(2, a.getEspece());
            stmt.setString(3, a.getRace());
            stmt.setString(4, a.getSexe());
            stmt.setDate(5, Date.valueOf(a.getDateNaissance()));
            stmt.setInt(6, a.getIdProprietaire());
            stmt.setInt(7, a.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM animal WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package dao;

import model.Visite;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VisiteDAO {
    public List<Visite> getAll() {
        List<Visite> visites = new ArrayList<>();
        String query = """
            SELECT v.*, a.nom AS nom_animal,
                   CONCAT(ve.nom, ' ', ve.prenom) AS nom_veterinaire
            FROM visite v
            JOIN animal a ON v.id_animal = a.id
            LEFT JOIN veterinaire ve ON v.id_veterinaire = ve.id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Visite v = new Visite(
                        rs.getInt("id"),
                        rs.getDate("date_visite").toLocalDate(),
                        rs.getString("description"),
                        rs.getInt("id_animal"),
                        rs.getString("nom_animal"),
                        rs.getInt("id_veterinaire"),
                        rs.getString("nom_veterinaire")
                );
                visites.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visites;
    }

    public void insert(Visite v) {
        String query = "INSERT INTO visite (date_visite, description, id_animal, id_veterinaire) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(v.getDateVisite()));
            stmt.setString(2, v.getDescription());
            stmt.setInt(3, v.getIdAnimal());

            if (v.getIdVeterinaire() > 0) {
                stmt.setInt(4, v.getIdVeterinaire());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Visite v) {
        String query = "UPDATE visite SET date_visite=?, description=?, id_animal=?, id_veterinaire=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(v.getDateVisite()));
            stmt.setString(2, v.getDescription());
            stmt.setInt(3, v.getIdAnimal());

            if (v.getIdVeterinaire() > 0) {
                stmt.setInt(4, v.getIdVeterinaire());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setInt(5, v.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM visite WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

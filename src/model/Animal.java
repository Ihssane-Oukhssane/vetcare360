package model;

import java.time.LocalDate;

public class Animal {
    private int id;
    private String nom, espece, race, sexe;
    private LocalDate dateNaissance;
    private int idProprietaire;
    private String nomProprietaire; // pour affichage

    public Animal(int id, String nom, String espece, String race, String sexe, LocalDate dateNaissance, int idProprietaire, String nomProprietaire) {
        this.id = id;
        this.nom = nom;
        this.espece = espece;
        this.race = race;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.idProprietaire = idProprietaire;
        this.nomProprietaire = nomProprietaire;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getEspece() { return espece; }
    public String getRace() { return race; }
    public String getSexe() { return sexe; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public int getIdProprietaire() { return idProprietaire; }
    public String getNomProprietaire() { return nomProprietaire; }

    public void setNom(String nom) { this.nom = nom; }
    public void setEspece(String espece) { this.espece = espece; }
    public void setRace(String race) { this.race = race; }
    public void setSexe(String sexe) { this.sexe = sexe; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setIdProprietaire(int idProprietaire) { this.idProprietaire = idProprietaire; }
    public void setNomProprietaire(String nomProprietaire) { this.nomProprietaire = nomProprietaire; }
}

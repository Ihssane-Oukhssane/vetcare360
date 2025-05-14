package model;

import java.time.LocalDate;

public class Visite {
    private int id;
    private LocalDate dateVisite;
    private String description;
    private int idAnimal;
    private String nomAnimal;
    private int idVeterinaire;
    private String nomVeterinaire;

    public Visite(int id, LocalDate dateVisite, String description, int idAnimal, String nomAnimal, int idVeterinaire, String nomVeterinaire) {
        this.id = id;
        this.dateVisite = dateVisite;
        this.description = description;
        this.idAnimal = idAnimal;
        this.nomAnimal = nomAnimal;
        this.idVeterinaire = idVeterinaire;
        this.nomVeterinaire = nomVeterinaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(LocalDate dateVisite) {
        this.dateVisite = dateVisite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNomAnimal() {
        return nomAnimal;
    }

    public void setNomAnimal(String nomAnimal) {
        this.nomAnimal = nomAnimal;
    }

    public int getIdVeterinaire() {
        return idVeterinaire;
    }

    public void setIdVeterinaire(int idVeterinaire) {
        this.idVeterinaire = idVeterinaire;
    }

    public String getNomVeterinaire() {
        return nomVeterinaire;
    }

    public void setNomVeterinaire(String nomVeterinaire) {
        this.nomVeterinaire = nomVeterinaire;
    }
}

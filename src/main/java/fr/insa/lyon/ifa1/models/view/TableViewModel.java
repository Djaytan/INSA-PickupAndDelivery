package fr.insa.lyon.ifa1.models.view;

public class TableViewModel {
    private String idColis;
    private String adresse;
    private String type;
    private String heureArrivee;
    private String tempsVoyage;
    private String tempsLivraison;

    public TableViewModel(String idColis, String adresse, String type, String heureArrivee, String tempsVoyage, String tempsLivraison) {
        this.idColis = idColis;
        this.adresse = adresse;
        this.type = type;
        this.heureArrivee = heureArrivee;
        this.tempsVoyage = tempsVoyage;
        this.tempsLivraison = tempsLivraison;
    }

    public String getIdColis() {
        return this.idColis;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public String getType() {
        return type;
    }

  public String getTempsVoyage() {
    return tempsVoyage;
  }

    public String getHeureArrivee() {
        return heureArrivee;
    }

    public String getTempsLivraison() {
        return tempsLivraison;
    }
}

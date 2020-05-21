package com.suntelecoms.dynamicform.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PosterAnnonce {

    @SerializedName("prix")
    @Expose
    private String prix;

    @SerializedName("titre")
    @Expose
    private String titre;

    @SerializedName("poids")
    @Expose
    private float poids;

    @SerializedName("disponibilite")
    @Expose
    private String disponibilite;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id_sous_categoires")
    @Expose
    private String idSousCategori;

    @SerializedName("id_type_annonces")
    @Expose
    private int idTypeAnnonces;

    @SerializedName("contenu")
    @Expose
    private String contenu;

    @SerializedName("url_image")
    @Expose
    private String urlImage;

    @SerializedName("name_path")
    private String namePath;

    @SerializedName("date_fin_urgent")
    private String dateFinUrgent;

    @SerializedName("capacite")
    @Expose
    private String capacite;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("id")
    private int id;

    @SerializedName("motif")
    private String motif;

    @SerializedName("date_fin_evenement")
    @Expose
    private String dateFinEvenement;

    @SerializedName("urgent")
    @Expose
    private int urgent;

    @SerializedName("validation")
    private boolean validation;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("quantite")
    @Expose
    private int quantite;

    @SerializedName("Programmes")
    @Expose
    private String programmes;

    @SerializedName("localisation")
    @Expose
    private String localisation;

    @SerializedName("active")
    private boolean active;

    @SerializedName("id_user")
    private int idUser;

    @SerializedName("criteres")
    @Expose
    private String criteres;

    @SerializedName("deleted_at")
    private String deletedAt;

    @SerializedName("nombre_update")
    private int nombreUpdate;

    @SerializedName("rejet")
    private boolean rejet;

    @SerializedName("is_pro")
    private boolean isPro;

    @SerializedName("date_debut_evenement")
    @Expose
    private String dateDebutEvenement;

    @SerializedName("date_fin_annonce")
    @Expose
    private String dateFinAnnonce;

    @SerializedName("type_service")
    @Expose
    private String typeService;

    public PosterAnnonce() {
    }

    public PosterAnnonce(String prix, String titre, String contenu, String urlImage) {
        this.prix = prix;
        this.titre = titre;
        this.contenu = contenu;
        this.urlImage = urlImage;
    }

    public void setPrix(String prix){
        this.prix = prix;
    }

    public String getPrix(){
        return prix;
    }

    public void setTitre(String titre){
        this.titre = titre;
    }

    public String getTitre(){
        return titre;
    }

    public void setPoids(float poids){
        this.poids = poids;
    }

    public float getPoids(){
        return poids;
    }

    public void setDisponibilite(String disponibilite){
        this.disponibilite = disponibilite;
    }

    public String getDisponibilite(){
        return disponibilite;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setIdSousCategori(String idSousCategori){
        this.idSousCategori = idSousCategori;
    }

    public String getIdSousCategori(){
        return idSousCategori;
    }

    public void setIdTypeAnnonces(int idTypeAnnonces){
        this.idTypeAnnonces = idTypeAnnonces;
    }

    public int getIdTypeAnnonces(){
        return idTypeAnnonces;
    }

    public void setContenu(String contenu){
        this.contenu = contenu;
    }

    public String getContenu(){
        return contenu;
    }

    public void setUrlImage(String urlImage){
        this.urlImage = urlImage;
    }

    public String getUrlImage(){
        return urlImage;
    }

    public void setNamePath(String namePath){
        this.namePath = namePath;
    }

    public String getNamePath(){
        return namePath;
    }

    public void setDateFinUrgent(String dateFinUrgent){
        this.dateFinUrgent = dateFinUrgent;
    }

    public String getDateFinUrgent(){
        return dateFinUrgent;
    }

    public void setCapacite(String capacite){
        this.capacite = capacite;
    }

    public String getCapacite(){
        return capacite;
    }

    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt(){
        return updatedAt;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setMotif(String motif){
        this.motif = motif;
    }

    public String getMotif(){
        return motif;
    }

    public void setDateFinEvenement(String dateFinEvenement){
        this.dateFinEvenement = dateFinEvenement;
    }

    public String getDateFinEvenement(){
        return dateFinEvenement;
    }

    public void setUrgent(int urgent){
        this.urgent = urgent;
    }

    public int getUrgent(){
        return urgent;
    }

    public void setValidation(boolean validation){
        this.validation = validation;
    }

    public boolean getValidation(){
        return validation;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setQuantite(int quantite){
        this.quantite = quantite;
    }

    public int getQuantite(){
        return quantite;
    }

    public void setProgrammes(String programmes){
        this.programmes = programmes;
    }

    public String getProgrammes(){
        return programmes;
    }

    public void setLocalisation(String localisation){
        this.localisation = localisation;
    }

    public String getLocalisation(){
        return localisation;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean getActive(){
        return active;
    }

    public void setIdUser(int idUser){
        this.idUser = idUser;
    }

    public int getIdUser(){
        return idUser;
    }

    public void setCriteres(String criteres){
        this.criteres = criteres;
    }

    public String getCriteres(){
        return criteres;
    }

    public void setDeletedAt(String deletedAt){
        this.deletedAt = deletedAt;
    }

    public String getDeletedAt(){
        return deletedAt;
    }

    public void setNombreUpdate(int nombreUpdate){
        this.nombreUpdate = nombreUpdate;
    }

    public int getNombreUpdate(){
        return nombreUpdate;
    }

    public void setRejet(boolean rejet){
        this.rejet = rejet;
    }

    public boolean getRejet(){
        return rejet;
    }

    public void setIsPro(boolean isPro){
        this.isPro = isPro;
    }

    public boolean getIs_pro(){
        return isPro;
    }

    public void setDateDebutEvenement(String dateDebutEvenement){
        this.dateDebutEvenement = dateDebutEvenement;
    }

    public String getDateDebutEvenement(){
        return dateDebutEvenement;
    }

    public void setDateFinAnnonce(String dateFinAnnonce){
        this.dateFinAnnonce = dateFinAnnonce;
    }

    public String getDateFinAnnonce(){
        return dateFinAnnonce;
    }

    public void setTypeService(String typeService){
        this.typeService = typeService;
    }

    public String getTypeService(){
        return typeService;
    }

    @Override
    public String toString(){
        return
                "TransactionModel{" +
                        "prix = '" + prix + '\'' +
                        ",titre = '" + titre + '\'' +
                        ",poids = '" + poids + '\'' +
                        ",disponibilite = '" + disponibilite + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",id_sous_categori = '" + idSousCategori + '\'' +
                        ",id_type_annonces = '" + idTypeAnnonces + '\'' +
                        ",contenu = '" + contenu + '\'' +
                        ",url_image = '" + urlImage + '\'' +
                        ",name_path = '" + namePath + '\'' +
                        ",date_fin_urgent = '" + dateFinUrgent + '\'' +
                        ",capacite = '" + capacite + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",id = '" + id + '\'' +
                        ",motif = '" + motif + '\'' +
                        ",date_fin_evenement = '" + dateFinEvenement + '\'' +
                        ",urgent = '" + urgent + '\'' +
                        ",validation = '" + validation + '\'' +
                        ",email = '" + email + '\'' +
                        ",quantite = '" + quantite + '\'' +
                        ",programmes = '" + programmes + '\'' +
                        ",localisation = '" + localisation + '\'' +
                        ",active = '" + active + '\'' +
                        ",id_user = '" + idUser + '\'' +
                        ",criteres = '" + criteres + '\'' +
                        ",deleted_at = '" + deletedAt + '\'' +
                        ",nombre_update = '" + nombreUpdate + '\'' +
                        ",rejet = '" + rejet + '\'' +
                        ",is_pro = '" + isPro + '\'' +
                        ",date_debut_evenement = '" + dateDebutEvenement + '\'' +
                        ",date_fin_annonce = '" + dateFinAnnonce + '\'' +
                        ",type_service = '" + typeService + '\'' +
                        "}";
    }
}
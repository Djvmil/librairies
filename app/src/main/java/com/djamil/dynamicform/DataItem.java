package com.djamil.dynamicform;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DataItem  implements Comparable<DataItem>{

	@SerializedName("date")
	private String date;

	@SerializedName("volume_horaire_restant")
	private Double volumeHoraireRestant;

	@SerializedName("libelle_salle")
	private String libelleSalle;

	@SerializedName("volume_horaire_planifie")
	private Double volumeHorairePlanifie;

	@SerializedName("couleur")
	private String couleur;

	@SerializedName("professeur")
	private String professeur;

	@SerializedName("objet")
	private String objet;

	@SerializedName("type")
	private String type;

	@SerializedName("libelle_cours")
	private String libelleCours;

	@SerializedName("heure_fin")
	private String heureFin;

	@SerializedName("volume_horaire_globale")
	private Double volumeHoraireGlobale;

	@SerializedName("libelle_classe")
	private String libelleClasse;

	@SerializedName("heure_debut")
	private String heureDebut;

	public DataItem() {
	}

	public DataItem(String type, String professeur, String objet, String heureFin, String libelleClasse, String heureDebut) {
		this.type = type;
		this.professeur = professeur;
		this.objet = objet;
		this.heureFin = heureFin;
		this.libelleClasse = libelleClasse;
		this.heureDebut = heureDebut;
	}
	public DataItem(String professeur, String libelle_cours, String libelle_classe, String heure_debut, String heureFin) {
		this.professeur = professeur;
		this.libelleCours = libelle_cours;
		this.libelleClasse = libelle_classe;
		this.heureDebut = heure_debut;
		this.heureFin = heureFin;

	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setVolumeHoraireRestant(Double volumeHoraireRestant){
		this.volumeHoraireRestant = volumeHoraireRestant;
	}

	public Double getVolumeHoraireRestant(){
		return volumeHoraireRestant;
	}

	public void setLibelleSalle(String libelleSalle){
		this.libelleSalle = libelleSalle;
	}

	public String getLibelleSalle(){
		return libelleSalle;
	}

	public void setVolumeHorairePlanifie(Double volumeHorairePlanifie){
		this.volumeHorairePlanifie = volumeHorairePlanifie;
	}

	public Double getVolumeHorairePlanifie(){
		return volumeHorairePlanifie;
	}

	public void setCouleur(String couleur){
		this.couleur = couleur;
	}

	public String getCouleur(){
		return couleur;
	}

	public void setProfesseur(String professeur){
		this.professeur = professeur;
	}

	public String getProfesseur(){
		return professeur;
	}

	public void setObjet(String objet){
		this.objet = objet;
	}

	public String getObjet(){
		return objet;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setLibelleCours(String libelleCours){
		this.libelleCours = libelleCours;
	}

	public String getLibelleCours(){
		return libelleCours;
	}

	public void setHeureFin(String heureFin){
		this.heureFin = heureFin;
	}

	public String getHeureFin(){
		return heureFin;
	}

	public void setVolumeHoraireGlobale(Double volumeHoraireGlobale){
		this.volumeHoraireGlobale = volumeHoraireGlobale;
	}

	public Double getVolumeHoraireGlobale(){
		return volumeHoraireGlobale;
	}

	public void setLibelleClasse(String libelleClasse){
		this.libelleClasse = libelleClasse;
	}

	public String getLibelleClasse(){
		return libelleClasse;
	}

	public void setHeureDebut(String heureDebut){
		this.heureDebut = heureDebut;
	}

	public String getHeureDebut(){
		return heureDebut;
	}

	@Override
	public String toString() {
		return "DataItem{" +
				"date='" + date + '\'' +
				", volumeHoraireRestant=" + volumeHoraireRestant +
				", libelleSalle='" + libelleSalle + '\'' +
				", volumeHorairePlanifie=" + volumeHorairePlanifie +
				", couleur='" + couleur + '\'' +
				", professeur='" + professeur + '\'' +
				", objet='" + objet + '\'' +
				", type='" + type + '\'' +
				", libelleCours='" + libelleCours + '\'' +
				", heureFin='" + heureFin + '\'' +
				", volumeHoraireGlobale=" + volumeHoraireGlobale +
				", libelleClasse='" + libelleClasse + '\'' +
				", heureDebut='" + heureDebut + '\'' +
				'}';
	}

	@Override
	public int compareTo(@NonNull DataItem dataItem) {
		try {
			//return heureDebut.compareTo(dataItem.heureDebut);
			return new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(date.concat(" "+heureDebut)).compareTo(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(dataItem.date.concat(" "+heureDebut)));
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
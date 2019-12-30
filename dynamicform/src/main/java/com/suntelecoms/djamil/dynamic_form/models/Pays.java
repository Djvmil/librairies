package com.suntelecoms.djamil.dynamic_form.models;

import com.suntelecoms.djamil.dynamic_form.models.annotations.Generated;
import com.suntelecoms.djamil.dynamic_form.models.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Pays{

	@SerializedName("libelle")
	private String libelle;

	public void setLibelle(String libelle){
		this.libelle = libelle;
	}

	public String getLibelle(){
		return libelle;
	}

	@Override
 	public String toString(){
		return 
			"Pays{" + 
			"libelle = '" + libelle + '\'' + 
			"}";
		}
}
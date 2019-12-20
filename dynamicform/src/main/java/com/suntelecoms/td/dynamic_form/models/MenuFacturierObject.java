package com.suntelecoms.td.dynamic_form.models;

import com.google.gson.annotations.SerializedName;
import com.suntelecoms.td.dynamic_form.models.RichFieldItem;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class MenuFacturierObject {

	@SerializedName("code")
	private String code;

	@SerializedName("name")
	private String name;

	@SerializedName("url_image")
	private String urlImage;

	@SerializedName("pays")
	private String pays;

	@SerializedName("richField")
	private List<RichFieldItem> richField;

	public MenuFacturierObject(String urlImage, String name) {
		this.urlImage = urlImage;
		this.name = name;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUrlImage(String urlImage){
		this.urlImage = urlImage;
	}

	public String getUrlImage(){
		return urlImage;
	}

	public void setPays(String pays){
		this.pays = pays;
	}

	public String getPays(){
		return pays;
	}

	public void setRichField(List<RichFieldItem> richField){
		this.richField = richField;
	}

	public List<RichFieldItem> getRichField(){
		return richField;
	}

	@Override
 	public String toString(){
		return 
			"MenuFacturierObject{" +
			"code = '" + code + '\'' + 
			",name = '" + name + '\'' + 
			",url_image = '" + urlImage + '\'' + 
			",pays = '" + pays + '\'' + 
			",richField = '" + richField + '\'' + 
			"}";
		}
}
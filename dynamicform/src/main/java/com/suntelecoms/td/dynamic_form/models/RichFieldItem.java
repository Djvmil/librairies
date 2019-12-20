package com.suntelecoms.td.dynamic_form.models;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RichFieldItem{

	private int id;

	@SerializedName("isRequired")
	private boolean isRequired;

	@SerializedName("method")
	private String method;

	@SerializedName("field")
	private String field;

	@SerializedName("label")
	private String label;

	@SerializedName("type")
	private String type;

	@SerializedName("url")
	private String url;

	public RichFieldItem(String label, String field, String type) {
		this.label = label;
		this.field = field;
		this.type  = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean required) {
		isRequired = required;
	}

	public void setIsRequired(boolean isRequired){
		this.isRequired = isRequired;
	}

	public boolean isIsRequired(){
		return isRequired;
	}

	public void setMethod(String method){
		this.method = method;
	}

	public String getMethod(){
		return method;
	}

	public void setField(String field){
		this.field = field;
	}

	public String getField(){
		return field;
	}

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"RichFieldItem{" + 
			"isRequired = '" + isRequired + '\'' + 
			",method = '" + method + '\'' + 
			",field = '" + field + '\'' + 
			",label = '" + label + '\'' + 
			",type = '" + type + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}
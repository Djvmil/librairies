package com.suntelecoms.djamil.dynamic_form.models;

import androidx.annotation.LayoutRes;

import com.suntelecoms.djamil.dynamic_form.models.annotations.Expose;
import com.suntelecoms.djamil.dynamic_form.models.annotations.SerializedName;


/**
 *
 *   Djvmil 19/12/2020
 *
 **/
public class RichFieldItem{
	@SerializedName("id")
	@Expose

	private int id;
	@SerializedName("isRequired")
	@Expose

	private boolean isRequired;
	@SerializedName("method")
	@Expose

	private String method;
	@SerializedName("field")
	@Expose

	private String field;
	@SerializedName("label")
	@Expose

	private String label;
	@SerializedName("type")
	@Expose

	private String type;
	@SerializedName("url")
	@Expose
	private String url;

	@SerializedName("value")
	@Expose
	private String value;

	@SerializedName("listSelect")
	@Expose
	private String[] listSelect;

	@SerializedName("listRadio")
	@Expose
	private String[] listRadio;

	@SerializedName("listCheckBox")
	@Expose
	private String[] listCheckBox;

	@SerializedName("listValueSelect")
	@Expose
	private String[] valueSelect;

	@SerializedName("color")
	@Expose
	private int color;

	@SerializedName("template")
	@Expose
	@LayoutRes
	private int template;


	public RichFieldItem() {
		this.template = 0;
		this.color = 0;
	}

	public RichFieldItem(String label, String field, String type) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.template = 0;
		this.color = 0;

	}

	public RichFieldItem(String label, String field, String type, @LayoutRes int template) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.template = template;
		this.color = 0;

	}

	public RichFieldItem(String label, String field, String type, String[] listRadio) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.color = 0;
		this.template = 0;
		this.listRadio = listRadio;

	}

	public RichFieldItem(String label, String field, String type, String value) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.template = 0;
		this.value = value;
		this.color = 0;

	}

	public RichFieldItem(int id, boolean isRequired, String method, String field, String label, String type, String url, String value, String[] listSelect, String[] listRadio, String[] listCheckBox, String[] valueSelect, int color, int template) {
		this.id = id;
		this.isRequired = isRequired;
		this.method = method;
		this.field = field;
		this.label = label;
		this.type = type;
		this.url = url;
		this.value = value;
		this.listSelect = listSelect;
		this.listRadio = listRadio;
		this.listCheckBox = listCheckBox;
		this.valueSelect = valueSelect;
		this.color = color;
		this.template = template;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String[] getListSelect() {
		return listSelect;
	}

	public void setListSelect(String[] listSelect) {
		this.listSelect = listSelect;
	}

	public String[] getListRadio() {
		return listRadio;
	}

	public void setListRadio(String[] listRadio) {
		this.listRadio = listRadio;
	}

	public String[] getListCheckBox() {
		return listCheckBox;
	}

	public void setListCheckBox(String[] listCheckBox) {
		this.listCheckBox = listCheckBox;
	}

	public String[] getValueSelect() {
		return valueSelect;
	}

	public void setValueSelect(String[] valueSelect) {
		this.valueSelect = valueSelect;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getTemplate() {
		return template;
	}

	public void setTemplate(int template) {
		this.template = template;
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
package com.suntelecoms.djamil.dynamic_form.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.LayoutRes;

import com.suntelecoms.djamil.dynamic_form.models.annotations.Expose;
import com.suntelecoms.djamil.dynamic_form.models.annotations.Generated;
import com.suntelecoms.djamil.dynamic_form.models.annotations.SerializedName;

import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class IOFieldsItem implements Parcelable {

	@SerializedName("isRequired")
	@Expose
	private boolean isRequired;

	@SerializedName("field")
	@Expose
	private String field;

	@SerializedName("method")
	@Expose
	private String method;

	@SerializedName("label")
	@Expose
	private String label;

	@SerializedName("type")
	@Expose
	private String type;

	@SerializedName("errors")
	@Expose
	private Errors errors;

	@SerializedName("url")
	@Expose
	private String url;

//------------------------------------------------------------------------------------------------------------------------
	@SerializedName("id")
	@Expose
	private int id;

	@SerializedName("value")
	@Expose
	private String value;

	@SerializedName("listSelect")
	@Expose
	private List<String> listSelect;

	@SerializedName("listRadio")
	@Expose
	private List<String> listRadio;

	@SerializedName("listCheckBox")
	@Expose
	private List<String> listCheckBox;

	@SerializedName("listValueSelect")
	@Expose
	private List<String> valueSelect;

	@SerializedName("color")
	@Expose
	private int color;

	@SerializedName("template")
	@Expose
	@LayoutRes
	private int template;


	public IOFieldsItem() {
		this.template = 0;
		this.color = 0;
	}

	public IOFieldsItem(String label, String field, String type) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.template = 0;
		this.color = 0;

	}

	public IOFieldsItem(String label, String field, String type, @LayoutRes int template) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.template = template;
		this.color = 0;

	}

	public IOFieldsItem(String label, String field, String type, List<String> listRadio) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.color = 0;
		this.template = 0;
		this.listRadio = listRadio;

	}

	public IOFieldsItem(String label, String field, String type, String value) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.template = 0;
		this.value = value;
		this.color = 0;

	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getListSelect() {
		return listSelect;
	}

	public void setListSelect(List<String> listSelect) {
		this.listSelect = listSelect;
	}

	public List<String> getListRadio() {
		return listRadio;
	}

	public void setListRadio(List<String> listRadio) {
		this.listRadio = listRadio;
	}

	public List<String> getListCheckBox() {
		return listCheckBox;
	}

	public void setListCheckBox(List<String> listCheckBox) {
		this.listCheckBox = listCheckBox;
	}

	public List<String> getValueSelect() {
		return valueSelect;
	}

	public void setValueSelect(List<String> valueSelect) {
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

//------------------------------------------------------------------------------------------------------------------------

	public void setIsRequired(boolean isRequired){
		this.isRequired = isRequired;
	}

	public boolean isIsRequired(){
		return isRequired;
	}

	public void setField(String field){
		this.field = field;
	}

	public String getField(){
		return field;
	}

	public void setMethod(String method){
		this.method = method;
	}

	public String getMethod(){
		return method;
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

	public void setErrors(Errors errors){
		this.errors = errors;
	}

	public Errors getErrors(){
		return errors;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}


	@Override
	public String toString() {
		return "IOFieldsItem{" +
				"isRequired=" + isRequired +
				", field='" + field + '\'' +
				", method='" + method + '\'' +
				", label='" + label + '\'' +
				", type='" + type + '\'' +
				", errors=" + errors +
				", url='" + url + '\'' +
				", id=" + id +
				", value='" + value + '\'' +
				", listSelect=" + listSelect +
				", listRadio=" + listRadio +
				", listCheckBox=" + listCheckBox +
				", valueSelect=" + valueSelect +
				", color=" + color +
				", template=" + template +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}
	protected IOFieldsItem(Parcel in) {
		id = in.readInt();
		isRequired = in.readInt() == 1;
		field = in.readString();
		method = in.readString();
		label = in.readString();
		type = in.readString();
		//errors = in.readString();
		url = in.readString();
		value = in.readString();
		listSelect = in.createStringArrayList();
		listRadio = in.createStringArrayList();
		listCheckBox = in.createStringArrayList();
		valueSelect = in.createStringArrayList();

		color = in.readInt();
		template = in.readInt();
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(label);
		dest.writeInt(isRequired ? 1 : 0);
		dest.writeString(field);
		dest.writeString(method);
		dest.writeString(type);
		dest.writeString(url);
		dest.writeString(value);
		dest.writeInt(template);
		dest.writeInt(color);
		dest.writeString(value);

		//errors;

		dest.writeStringList(listCheckBox);
		dest.writeStringList(listRadio);
		dest.writeStringList(listSelect);
		dest.writeStringList(valueSelect);
	}


	public static final Creator<IOFieldsItem> CREATOR = new Creator<IOFieldsItem>() {
		@Override
		public IOFieldsItem createFromParcel(Parcel in) {
			return new IOFieldsItem(in);
		}

		@Override
		public IOFieldsItem[] newArray(int size) {
			return new IOFieldsItem[size];
		}
	};
}
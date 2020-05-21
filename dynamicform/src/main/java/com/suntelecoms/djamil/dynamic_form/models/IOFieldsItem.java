package com.suntelecoms.djamil.dynamic_form.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.LayoutRes;

import com.suntelecoms.djamil.dynamic_form.annotations.Expose;
import com.suntelecoms.djamil.dynamic_form.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class IOFieldsItem implements Parcelable, Comparable<IOFieldsItem> {

	@SerializedName("isRequired")
	@Expose
	private boolean isRequired;

	@SerializedName("children")
	@Expose
	private Object children;

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

	@SerializedName("url")
	@Expose
	private String url;

	@SerializedName("order")
	@Expose
	private int order = 0;

	@SerializedName("isReadOnly")
	@Expose
	private boolean isReadOnly;

	@SerializedName("shouldBeShown")
	@Expose
	private Boolean shouldBeShown;

	//------------------------------------------------------------------------------------------------------------------------
	@SerializedName("id")
	@Expose
	private int id;

	private int idIndicatif;

	private int idBillerFields;

	@SerializedName("value")
	@Expose
	private String value;

	@SerializedName("color")
	@Expose
	private int color = 0;

	private ArrayList<IOFieldsItem> listSelect;

	@LayoutRes
	private int template = 0;


	public IOFieldsItem() {
	}

	public IOFieldsItem(String label, String field, String type) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.value  = type;

	}

	public IOFieldsItem(String label, String field, String type, @LayoutRes int template) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.value  = type;
		this.template = template;
		this.color = 0;

	}

	public IOFieldsItem(String label, String field, String type, List<String> listRadio) {
		this.label = label;
		this.field = field;
		this.type  = type;

	}

	public IOFieldsItem(String label, String field, String type, String value) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.value = value;

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


	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean readOnly) {
		isReadOnly = readOnly;
	}

	public void setIsRequired(boolean isRequired){
		this.isRequired = isRequired;
	}

	public boolean isRequired(){
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

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public int getIdBillerFields() {
		return idBillerFields;
	}

	public void setIdBillerFields(int idBillerFields) {
		this.idBillerFields = idBillerFields;
	}


	public Object getChildren() {
		return children;
	}

	public void setChildren(Object children) {
		this.children = children;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Boolean getShouldBeShown() {
		return shouldBeShown;
	}

	public void setShouldBeShown(Boolean shouldBeShown) {
		this.shouldBeShown = shouldBeShown;
	}

	public int getIdIndicatif() {
		return idIndicatif;
	}

	public void setIdIndicatif(int idIndicatif) {
		this.idIndicatif = idIndicatif;
	}

	@Override
	public String toString() {
		return "IOFieldsItem{" +
				"isRequired=" + isRequired +
				", children=" + children +
				", field='" + field + '\'' +
				", method='" + method + '\'' +
				", label='" + label + '\'' +
				", type='" + type + '\'' +
				", url='" + url + '\'' +
				", order=" + order +
				", isReadOnly=" + isReadOnly +
				", shouldBeShown=" + shouldBeShown +
				", id=" + id +
				", idBillerFields=" + idBillerFields +
				", value='" + value + '\'' +
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
		shouldBeShown = in.readInt() == 1;
		order = in.readInt();
		field = in.readString();
		method = in.readString();
		label = in.readString();
		type = in.readString();
		//errors = in.readString();
		url = in.readString();
		value = in.readString();

		color = in.readInt();
		template = in.readInt();
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(label);
		dest.writeInt(order);
		dest.writeInt(isRequired ? 1 : 0);
		dest.writeInt(shouldBeShown ? 1 : 0);
		dest.writeString(field);
		dest.writeString(method);
		dest.writeString(type);
		dest.writeString(url);
		dest.writeString(value);
		dest.writeInt(template);
		dest.writeInt(color);
		dest.writeString(value);

		//errors;
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

	@Override
	public int compareTo(IOFieldsItem item) {
		return (this.order - item.order);
	}
}
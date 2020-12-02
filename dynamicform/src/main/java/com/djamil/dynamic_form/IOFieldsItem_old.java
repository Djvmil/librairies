package com.djamil.dynamic_form;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.LayoutRes;

import com.djamil.dynamic_form.annotations.Expose;
import com.djamil.dynamic_form.annotations.InputTypeDF;
import com.djamil.dynamic_form.annotations.SerializedName;
import com.djamil.dynamic_form.models.ItemDF;

import java.util.ArrayList;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */

public class IOFieldsItem_old implements Parcelable, Comparable<IOFieldsItem_old> {

	@SerializedName("id")
	@Expose
	private int id;

	@SerializedName("isRequired")
	@Expose
	private boolean isRequired = false;

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
	private boolean isReadOnly = false;

	@SerializedName("shouldBeShown")
	@Expose
	private boolean shouldBeShown = true;

	@SerializedName("value")
	@Expose
	private String value;

	@SerializedName("color")
	@Expose
	private int color = 0;

	@SerializedName("devise")
	@Expose
	private String devise = "XOF";

	@SerializedName("pays_alpha2")
	@Expose
	private String paysAlpha2;

	@SerializedName("msg_hint")
	@Expose
	private String msgHint = "";

	private int idOperationBiller = -1;

	private ArrayList<ItemDF> listItemDF;
	private ArrayList<ItemDF> listItemDFSelected;
	private ItemDF itemDFSelected;
	private int idView = 0;
	private int indicatif = 0;
	private int numPage = 0;
	private int idBillerFields;
	private boolean formatter = false;
	private boolean isMoney = false;
	@LayoutRes private int template = 0;

	public IOFieldsItem_old() {
	}

	public IOFieldsItem_old(String label, String field, @InputTypeDF String type) {
		this.label = label;
		this.field = field;
		this.type  = type;

	}

	public IOFieldsItem_old(String label, String field, @InputTypeDF String type, Boolean isMoney) {
		this.label   = label;
		this.field   = field;
		this.type    = type;
		this.isMoney = isMoney;
	}

	public IOFieldsItem_old(String label, String field, @InputTypeDF String type, @LayoutRes int template) {
		this.label    = label;
		this.field    = field;
		this.type     = type;
		this.value    = type;
		this.template = template;

	}

	public IOFieldsItem_old(String label, String field, @InputTypeDF String type, String value) {
		this.label = label;
		this.field = field;
		this.type  = type;
		this.value = value;

	}

	public IOFieldsItem_old(boolean isRequired, String field, String label, @InputTypeDF String type, int order, boolean isReadOnly, Boolean shouldBeShown, String value, int color, String devise, String paysAlpha2, String msgHint, int indicatif, boolean isMoney, int template) {
		this.isRequired = isRequired;
		this.field = field;
		this.label = label;
		this.type = type;
		this.order = order;
		this.isReadOnly = isReadOnly;
		this.shouldBeShown = shouldBeShown;
		this.value = value;
		this.color = color;
		this.devise = devise;
		this.paysAlpha2 = paysAlpha2;
		this.msgHint = msgHint;
		this.indicatif = indicatif;
		this.isMoney = isMoney;
		this.template = template;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdView() {
		return idView;
	}

	public void setIdView(int idView) {
		this.idView = idView;
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

	public void setType(@InputTypeDF String type){
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

	public boolean getShouldBeShown() {
		return shouldBeShown;
	}

	public void setShouldBeShown(Boolean shouldBeShown) {
		this.shouldBeShown = shouldBeShown;
	}

	public int getIndicatif() {
		return indicatif;
	}

	public void setIndicatif(int indicatif) {
		this.indicatif = indicatif;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public boolean isFormatter() {
		return formatter;
	}

	public void setFormatter(boolean formatter) {
		this.formatter = formatter;
	}

	public boolean isMoney() {
		return isMoney;
	}

	public void setMoney(boolean money) {
		isMoney = money;
	}


	public String getPaysAlpha2() {
		return paysAlpha2;
	}

	public void setPaysAlpha2(String paysAlpha2) {
		this.paysAlpha2 = paysAlpha2;
	}

	public ArrayList<ItemDF> getListItemDF() {
		return listItemDF;
	}

	public void setListItemDF(ArrayList<ItemDF> listItemDF) {
		this.listItemDF = listItemDF;
	}

	public void getItemDFByIdView(int idView) {
		for (ItemDF item : this.listItemDF)
			if(item.getIdView() == idView)
				itemDFSelected = item;

	}

	public void getListItemDFByView(View view) {
		this.listItemDFSelected = new ArrayList<>();
		for (ItemDF item : this.listItemDF){
			if (((CheckBox)view.findViewById(item.getIdView())).isChecked());
			this.listItemDFSelected.add(item);
		}

	}

	public ItemDF getItemDFSelected() {
		return itemDFSelected;
	}

	public void setItemDFSelected(ItemDF itemDFSelected) {
		this.itemDFSelected = itemDFSelected;
	}

	public ArrayList<ItemDF> getListItemDFSelected() {
		return listItemDFSelected;
	}

	public void setListItemDFSelected(ArrayList<ItemDF> listItemDFSelected) {
		this.listItemDFSelected = listItemDFSelected;
	}

	public String getMsgHint() {
		return msgHint;
	}

	public void setMsgHint(String msgHint) {
		this.msgHint = msgHint;
	}

	public int getNumPage() {
		return numPage;
	}

	public void setNumPage(int numPage) {
		this.numPage = numPage;
	}

	public int getIdOperationBiller() {
		return idOperationBiller;
	}

	public void setIdOperationBiller(int idOperationBiller) {
		this.idOperationBiller = idOperationBiller;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	protected IOFieldsItem_old(Parcel in) {
		id = in.readInt();
		isRequired = in.readInt() == 1;
		shouldBeShown = in.readInt() == 1;
		isMoney    = in.readInt() == 1;
		isReadOnly = in.readInt() == 1;
		formatter = in.readInt() == 1;
		order = in.readInt();
		field = in.readString();
		method = in.readString();
		label = in.readString();
		type = in.readString();
		url = in.readString();
		value = in.readString();
		color = in.readInt();
		order = in.readInt();
		idOperationBiller = in.readInt();
		idView = in.readInt();
		numPage = in.readInt();
		devise = in.readString();
		msgHint = in.readString();
		paysAlpha2 = in.readString();
		template = in.readInt();
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(label);
		dest.writeInt(order);
		dest.writeInt(isRequired ? 1 : 0);
		dest.writeInt(isMoney ? 1 : 0);
		dest.writeInt(isReadOnly ? 1 : 0);
		dest.writeInt(formatter ? 1 : 0);
		dest.writeInt(shouldBeShown ? 1 : 0);

		dest.writeInt(idOperationBiller);
		dest.writeString(field);
		dest.writeString(method);
		dest.writeString(type);
		dest.writeString(url);
		dest.writeString(value);
		dest.writeInt(template);
		dest.writeInt(order);
		dest.writeInt(color);
		dest.writeInt(idView);
		dest.writeInt(numPage);
		dest.writeString(devise);
		dest.writeString(paysAlpha2);
		dest.writeString(msgHint);
		dest.writeString(value);
	}


	public static final Creator<IOFieldsItem_old> CREATOR = new Creator<IOFieldsItem_old>() {
		@Override
		public IOFieldsItem_old createFromParcel(Parcel in) {
			return new IOFieldsItem_old(in);
		}

		@Override
		public IOFieldsItem_old[] newArray(int size) {
			return new IOFieldsItem_old[size];
		}
	};

	@Override
	public int compareTo(IOFieldsItem_old item) {
		return (this.order - item.order);
	}

	@Override
	public String toString() {
		return "IOFieldsItem{" +
				"id=" + id +
				", isRequired=" + isRequired +
				", children=" + children +
				", field='" + field + '\'' +
				", method='" + method + '\'' +
				", label='" + label + '\'' +
				", type='" + type + '\'' +
				", url='" + url + '\'' +
				", order=" + order +
				", isReadOnly=" + isReadOnly +
				", shouldBeShown=" + shouldBeShown +
				", value='" + value + '\'' +
				", color=" + color +
				", devise='" + devise + '\'' +
				", paysAlpha2='" + paysAlpha2 + '\'' +
				", msgHint='" + msgHint + '\'' +
				", idOperationBiller=" + idOperationBiller +
				", listItemDF=" + listItemDF +
				", listItemDFSelected=" + listItemDFSelected +
				", itemDFSelected=" + itemDFSelected +
				", idView=" + idView +
				", indicatif=" + indicatif +
				", numPage=" + numPage +
				", idBillerFields=" + idBillerFields +
				", formatter=" + formatter +
				", isMoney=" + isMoney +
				", template=" + template +
				'}';
	}
}
package com.djamil.dynamicform;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItemList{

	@SerializedName("size")
	private int size;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("photo")
	private String photo;

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setPhoto(String photo){
		this.photo = photo;
	}

	public String getPhoto(){
		return photo;
	}

	@Override
 	public String toString(){
		return 
			"DataItemList{" + 
			"size = '" + size + '\'' + 
			",data = '" + data + '\'' + 
			",photo = '" + photo + '\'' + 
			"}";
		}
}
package com.suntelecoms.dynamicform.api;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ResponseAddAnnonce {

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private String data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private String status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(String data){
		this.data = data;
	}

	public String isData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setError(String error){
		this.error = error;
	}

	public String isError(){
		return error;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseAddAnnonce{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}
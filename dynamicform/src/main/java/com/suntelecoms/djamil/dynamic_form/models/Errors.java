package com.suntelecoms.djamil.dynamic_form.models;

import com.suntelecoms.djamil.dynamic_form.models.annotations.Generated;
import com.suntelecoms.djamil.dynamic_form.models.annotations.SerializedName;

import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class Errors{

	@SerializedName("errors")
	private List<Object> errors;

	public void setErrors(List<Object> errors){
		this.errors = errors;
	}

	public List<Object> getErrors(){
		return errors;
	}

	@Override
 	public String toString(){
		return 
			"Errors{" + 
			"errors = '" + errors + '\'' + 
			"}";
		}
}
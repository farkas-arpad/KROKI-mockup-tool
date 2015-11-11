package com.krogen.model.django.modelpy;

import java.util.ArrayList;
import java.util.List;

public class DjangoModel {

	private String name;
	private String label;	
	private List<DjangoModelField> fieldsList = new ArrayList<DjangoModelField>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<DjangoModelField> getFieldsList() {
		return fieldsList;
	}

	public void setEntriesList(List<DjangoModelField> fieldsList) {
		this.fieldsList = fieldsList;
	}


}

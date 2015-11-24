package com.krogen.model.django.modelpy;

public class DjangoModelField {

//	EntryTypesEnum entryType
	private String fieldName;
	private String className;
	private Boolean hidden;
	private Boolean key;
	private String label;
	private String name;	
	private Integer length;
	private String enumerationName;
	private EntryTypesEnum entryTypesEnum;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntryTypesEnum getEntryTypesEnum() {
		return entryTypesEnum;
	}

	public void setEntryTypesEnum(EntryTypesEnum entryTypesEnum) {
		this.entryTypesEnum = entryTypesEnum;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public Boolean getKey() {
		return key;
	}

	public void setKey(Boolean key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getEnumerationName() {
		return enumerationName;
	}

	public void setEnumerationName(String enumerationName) {
		this.enumerationName = enumerationName;
	}

	
}

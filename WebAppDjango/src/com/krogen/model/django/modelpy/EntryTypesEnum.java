package com.krogen.model.django.modelpy;

public enum EntryTypesEnum {
	BOOLEANFIELD("BooleanField"),
	CHARFIELD("CharField"),
	DATEFIELD("DateField"),
	INTEGERFIELD("IntegerField"),
	TEXTFIELD("TextField");

	String type;

	EntryTypesEnum() {
	}

	EntryTypesEnum(String type) {
		this.type = type;
	}

	@Override
	public String toString(){
		return type;
	}

}

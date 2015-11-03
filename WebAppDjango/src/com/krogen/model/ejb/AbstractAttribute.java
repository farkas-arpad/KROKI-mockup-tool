package com.krogen.model.ejb;

import com.krogen.model.AbstractElement;

public abstract class AbstractAttribute extends AbstractElement {
	
	protected String fieldName;
	protected Boolean hidden = false;
	protected Boolean disabled = false;

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

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

}

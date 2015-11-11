package com.krogen.model.ejb;

import java.util.ArrayList;
import java.util.List;

import com.krogen.model.AbstractElement;

public class EjbClass extends AbstractElement {
	
	protected List<AbstractAttribute> attributes = new ArrayList<AbstractAttribute>();
	protected String entityClass;

	public EjbClass() {
	}

	public EjbClass(String name, String label) {
		super(name, label);
	}

	public void add(AbstractAttribute attribute) {
		attributes.add(attribute);
	}

	public void remove(AbstractAttribute attribute) {
		attributes.remove(attribute);
	}

	public List<AbstractAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AbstractAttribute> attributes) {
		this.attributes = attributes;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String ejbClass) {
		this.entityClass = ejbClass;
	}

	@Override
	public String toString() {
		return "EntityBean [entityClass=" + entityClass + "]";
	}
}

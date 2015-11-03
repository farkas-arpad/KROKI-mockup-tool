package com.krogen.model.panel.configuration.operation;

import java.util.ArrayList;
import java.util.List;

import com.krogen.enumerations.OperationType;
import com.krogen.model.AbstractElement;

public class Operation extends AbstractElement {

	protected List<Parameter> parameters = new ArrayList<Parameter>();
	protected OperationType type;
	protected Boolean allowed = true;
	protected String target;
	protected String parentGroup;
	
	public void add(Parameter p) {
		parameters.add(p);
	}
	
	public int getParameterCount() {
		return parameters.size();
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public Boolean getAllowed() {
		return allowed;
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(String parentGroup) {
		this.parentGroup = parentGroup;
	}
}
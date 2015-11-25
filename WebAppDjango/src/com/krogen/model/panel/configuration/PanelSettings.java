package com.krogen.model.panel.configuration;

import com.krogen.enumerations.OpenedAs;
import com.krogen.enumerations.StateMode;
import com.krogen.enumerations.ViewMode;

public class PanelSettings {

	protected String add = "true";
	protected String update = "true";
	protected String copy = "true";
	protected String delete = "true";
	protected String changeMode = "true";
	protected String dataNavigation =  "true";
	protected ViewMode viewMode = ViewMode.TABLEVIEW;
	protected String hideToolbar = "false";
	protected StateMode stateMode = StateMode.UPDATE;
	protected OpenedAs openedAs = OpenedAs.DEFAULT;
	
	public PanelSettings() {
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}	

	public String getChangeMode() {
		return changeMode;
	}

	public void setChangeMode(String changeMode) {
		this.changeMode = changeMode;
	}

	public String getDataNavigation() {
		return dataNavigation;
	}

	public void setDataNavigation(String dataNavigation) {
		this.dataNavigation = dataNavigation;
	}

	public String getHideToolbar() {
		return hideToolbar;
	}

	public void setHideToolbar(String hideToolbar) {
		this.hideToolbar = hideToolbar;
	}

	public ViewMode getViewMode() {
		return viewMode;
	}

	public void setViewMode(ViewMode viewMode) {
		this.viewMode = viewMode;
	}

	public String getCopy() {
		return copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}


	public StateMode getStateMode() {
		return stateMode;
	}

	public void setStateMode(StateMode stateMode) {
		this.stateMode = stateMode;
	}

	public OpenedAs getOpenedAs() {
		return openedAs;
	}

	public void setOpenedAs(OpenedAs openedAs) {
		this.openedAs = openedAs;
	}
	
	
}

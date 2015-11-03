package com.krogen.model.panel.configuration;

import com.krogen.enumerations.PanelType;
import com.krogen.model.AbstractElement;

public class Zoom extends AbstractElement {

	protected String panelId;
	protected PanelType panelType;
	
	public String getPanelId() {
		return panelId;
	}
	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}
	public PanelType getPanelType() {
		return panelType;
	}
	public void setPanelType(PanelType panelType) {
		this.panelType = panelType;
	}
}

package com.krogen.model.panel;

import java.util.ArrayList;
import java.util.List;

import com.krogen.model.ejb.EjbClass;
import com.krogen.model.panel.configuration.DataSettings;
import com.krogen.model.panel.configuration.Next;
import com.krogen.model.panel.configuration.PanelSettings;
import com.krogen.model.panel.configuration.Zoom;
import com.krogen.model.panel.configuration.operation.SpecificOperations;

public class AdaptStandardPanel extends DjangoPanel {

	protected EjbClass entityBean;
	protected Integer level;
	protected PanelSettings panelSettings;
	protected SpecificOperations standardOperations;
	protected DataSettings dataSettings;
	protected String associationEnd;
	protected List<Next> nextPanels = new ArrayList<Next>();
	protected List<Zoom> zoomPanels = new ArrayList<Zoom>();
	
	public EjbClass getEntityBean() {
		return entityBean;
	}
	public void setEntityBean(EjbClass entityBean) {
		this.entityBean = entityBean;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public PanelSettings getPanelSettings() {
		return panelSettings;
	}
	public void setPanelSettings(PanelSettings panelSettings) {
		this.panelSettings = panelSettings;
	}
	public SpecificOperations getStandardOperations() {
		return standardOperations;
	}
	public void setStandardOperations(SpecificOperations standardOperations) {
		this.standardOperations = standardOperations;
	}
	public DataSettings getDataSettings() {
		return dataSettings;
	}
	public void setDataSettings(DataSettings dataSettings) {
		this.dataSettings = dataSettings;
	}
	public List<Next> getNextPanels() {
		return nextPanels;
	}
	public void setNextPanels(List<Next> nextPanels) {
		this.nextPanels = nextPanels;
	}
	public List<Zoom> getZoomPanels() {
		return zoomPanels;
	}
	public void setZoomPanels(List<Zoom> zoomPanels) {
		this.zoomPanels = zoomPanels;
	}
	public String getAssociationEnd() {
		return associationEnd;
	}
	public void setAssociationEnd(String associationEnd) {
		this.associationEnd = associationEnd;
	}
	
}

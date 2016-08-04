package com.krogen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.krogen.model.ejb.EjbClass;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.menu.DjangoMenu;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.model.panel.AdaptParentChildPanel;
import com.krogen.model.panel.DjangoPanel;

/**
 * Application model that holds object representations of
 * xml entities obtained from application repository.
 * @author Milorad Filipovic
 */
public class DataModel {

	//--------------------------------------------------------------------| OBJECTS LISTS
	protected LinkedHashMap<String,EjbClass> entityBeans = new LinkedHashMap<String,EjbClass>();
	protected List<DjangoMenu> menus = new ArrayList<DjangoMenu>();
	protected List<DjangoPanel> panels = new ArrayList<DjangoPanel>();
	protected List<AdaptParentChildPanel> parentChildPanel = new ArrayList<AdaptParentChildPanel>();
	protected Map<String, Enumeration> enumerations = new HashMap<String, Enumeration>();
	protected DjangoSubMenu defaultMenu = new DjangoSubMenu();

	//--------------------------------------------------------------------| MAPPING DATA
	protected Map<String, String> ejbMappings = new HashMap<String, String>();
	protected Map<String, String> panelClassMap = new HashMap<String, String>();
	protected Map<String, String> componentTypeMap = new HashMap<String, String>();

	//--------------------------------------------------------------------| ADD METHODS
	/**
	 * Adds XML-to-EJB mapping to model
	 * @param className EJB class name
	 * @param xmlFile xml specification of EJB class
	 */
	public void addEjbMapping(String className, String xmlFile) {
		ejbMappings.put(className, xmlFile);
	}

	public void addEjbClass(EjbClass ejb) {
		if (!entityBeans.containsKey(ejb.getEntityClass()))
			entityBeans.put(ejb.getEntityClass(),ejb);
	}

	public void addMenu(DjangoMenu menu) {
		menus.add(menu);
	}

	public void addMenus(ArrayList<DjangoSubMenu> allMenus) {
		menus.addAll(allMenus);
	}

	public void add(DjangoPanel panel) {
		panels.add(panel);
	}
	
	public void add(AdaptParentChildPanel newParentChildPanel){
		parentChildPanel.add(newParentChildPanel);
	}

	public void add(String name, Enumeration enumeration) {
		enumerations.put(name, enumeration);
	}

	public void addPanelClassMapping(String className, String panelId) {
		panelClassMap.put(className, panelId);
	}

	public void addComponentTypeMapping(String languageType, String component) {
		componentTypeMap.put(languageType, component);
	}

	//--------------------------------------------------------------------| GETTERS AND SETTERS
	public Map<String, String> getXmlMappings() {
		return ejbMappings;
	}

	public LinkedHashMap<String,EjbClass> getEntityBeans() {
		return entityBeans;
	}

	public List<DjangoMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<DjangoMenu> menus) {
		this.menus = menus;
	}

	public List<DjangoPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<DjangoPanel> panels) {
		this.panels = panels;
	}

	public Map<String, Enumeration> getEnumerations() {
		return enumerations;
	}

	public void setEnumerations(Map<String, Enumeration> enumerations) {
		this.enumerations = enumerations;
	}

	public Map<String, String> getPanelClassMap() {
		return panelClassMap;
	}

	public void setPanelClassMap(Map<String, String> panelCLassMap) {
		this.panelClassMap = panelCLassMap;
	}

	public Map<String, String> getComponentTypeMap() {
		return componentTypeMap;
	}

	public void setComponentTypeMap(Map<String, String> componentTypeMap) {
		this.componentTypeMap = componentTypeMap;
	}

	public void setEjbMappings(Map<String, String> ejbMappings) {
		this.ejbMappings = ejbMappings;
	}

	public DjangoSubMenu getDefaultMenu() {
		return defaultMenu;
	}

	public void setDefaultMenu(DjangoSubMenu defaultMenu) {
		this.defaultMenu = defaultMenu;
	}

	public List<AdaptParentChildPanel> getParentChildPanel() {
		return parentChildPanel;
	}

	public void setParentChildPanel(List<AdaptParentChildPanel> parentChildPanel) {
		this.parentChildPanel = parentChildPanel;
	}

}

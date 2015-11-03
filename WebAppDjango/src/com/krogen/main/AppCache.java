package com.krogen.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.krogen.model.Model;
import com.krogen.model.ejb.EntityBean;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.menu.AdaptMenu;
import com.krogen.model.menu.AdaptSubMenu;
import com.krogen.model.panel.AdaptPanel;
import com.krogen.model.panel.AdaptStandardPanel;

/**
 * Singleton class used to store objects from xml specification to application model
 * and as a communication bridge between some classes.
 * @author Milorad Filipovic
 */
public class AppCache {

	private static Application application;
	private static AppCache instance = null;
	protected Model model = new Model();
		
	/**
	 * Singleton constructor.
	 * @return existing instance or creates new if none exist.
	 */
	public static AppCache getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new AppCache();
			return instance;
		}
	}
	
	//---------------------------------------------------------------------| ADD METHODS
	/**
	 * Adds XML-to-EJB mapping to model
	 * @param className
	 * @param xmlFile
	 */
	public void addToCache(String className, String xmlFile) {
		model.addEJBMapping(className, xmlFile);
	}
	
	/**
	 * Adds entity bean object to application model
	 * @param ejb
	 */
	public void addToCache(EntityBean ejb) {
		if(findEJBByClassName(ejb.getName()) == null) {
			model.add(ejb);
		}
	}
	
	public void addToCache(AdaptMenu menu) {
		model.add(menu);
	}
	
	public void addToCache(ArrayList<AdaptSubMenu> menus) {
		model.add(menus);
	}
	
	public void addToCache(String name, Enumeration enumeration) {
		model.add(name, enumeration);
	}
	
	public void addToCachePanelClassMapping(String className, String panelId) {
		model.addPanelClassMapping(className, panelId);
	}
	
	public void addTypeComponentMapping(String languageType, String componentTemplate) {
		model.addComponentTypeMapping(languageType, componentTemplate);
	}
	
	public void addToCacheDefaultMenu(AdaptSubMenu rootMenu) {
		model.setDefaultMenu(rootMenu);
	}
	//---------------------------------------------------------------------| UTIL METHODS
	public EntityBean findEJBByClassName(String className) {
		Iterator<EntityBean> it = model.getEntityBeans().iterator();
		EntityBean ejb = null;
		while (it.hasNext()) {
			ejb = it.next();
			if (ejb.getEntityClass().getName().equals(className)) {
				return ejb;
			}
		}
		return null;
	}

	public Enumeration findEnumerationByName(String name) {
		Enumeration enumeration = null;
		for (String  enumName : model.getEnumerations().keySet()) {
			if(enumName.equals(name)) {
				return model.getEnumerations().get(enumName);
			}
		}
		return null;
	}
	
	public AdaptStandardPanel getPanelByName(String name) {
		AdaptPanel panel = model.getPanels().get(name);
		if(panel instanceof AdaptStandardPanel) {
			return (AdaptStandardPanel) panel;
		}else {
			return null;
		}
	}
	
	public String getPanelId(String className) {
		return model.getPanelClassMap().get(className);
	}
	
	public String getComponentForType(String typeName) {
		return model.getComponentTypeMap().get(typeName);
	}
	
	public Enumeration getEnumByName(String enumName) {
		return model.getEnumerations().get(enumName);
	}
	
	/**
	 * Proxy method for {@code displayText} method in {@code AdaptMainFrame} class
	 * @param type 0 - info, 1 - error, 2 - warning
	 */
	public static void displayTextOnMainFrame(String text, int type) {
		Application.getMainFrame().displayText(text, type);
	}
	
	public static void displayStackTraceOnMainFrame(Exception e) {
		Application.getMainFrame().displayStackTrace(e);
	}
	
	//---------------------------------------------------------------------| GETTERS
	public String getXMLFileName(String className) {
		return model.getXmlMappings().get(className);
	}
	
	public static Application getApplication() {
		return application;
	}

	public static void setApplication(Application application) {
		AppCache.application = application;
	}
	
	public ArrayList<AdaptMenu> getMenuList() {
		return (ArrayList<AdaptMenu>) model.getMenus();
	}
	
	public AdaptSubMenu getDefaultMenu() {
		return model.getDefaultMenu();
	}
	
	public Map<String, AdaptPanel> getPanels(){
		return model.getPanels();
	}

	public Map<String, String> getPanelClassMap() {
		return model.getPanelClassMap();
	}
}

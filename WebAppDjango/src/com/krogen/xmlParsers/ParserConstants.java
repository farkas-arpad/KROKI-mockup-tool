package com.krogen.xmlParsers;

import java.io.File;

import com.krogen.repository_utils.RepositoryPathsUtil;

public class ParserConstants {

	// Default paths
	protected static String generatedRepoPath = RepositoryPathsUtil.getGeneratedModelPath();
	protected static String generatedModelPath = RepositoryPathsUtil.getGeneratedModelPath();
	protected static String staticModelPath		= RepositoryPathsUtil.getStaticModelPath();
	protected static String staticRepoPath		= RepositoryPathsUtil.getStaticRepositoryPath();
	
	protected static String panelsDirectoryPath	= generatedModelPath + File.separator + "panel" + File.separator;
	

	// MENU data xml files
	public static String xmlFileName = "menu-generated.xml";
	public static String xmlFileNameDefault = "menu-generated-default.xml";

	// MENU parser xml tags
	public static final String SUBMENU = "submenu";
	public static final String MENU_ITEM_MAIN = "menu_item_main";
	public static final String ROLES = "roles";
	public static final String ROLE = "role";
	public static final String MENU_NAME = "menu_name";
	public static final String FORM_NAME = "form_name";
	public static final String ACTIVATE = "activate";
	public static final String PANEL_TYPE = "panel_type";
	public static final String SUBMENU_PARENT = "submenu_parent";
	public static final String SUBMENU_NAME = "submenu_name";
	public static final String MENU_ITEM = "menu_item";

	// PANEL data xml files
	protected static String panelMappingFileName = "panel-map.xml";
	protected static String panelsFileName = "panel.xml";
	
	// EJB data xml files
	protected static String EjbMappingFileName 	= "xml-mapping.xml";

	// ENUMERATION data xml files
	protected static String enumStaticFileName = "enumerations.xml";
	protected static String enumGeneratedFileName = "enumerations-generated.xml";

	// component data xml files
	protected static String componentsFile = "components-web.xml";
	protected static String componentsMappingFile = "type-component-mappings.xml";


}

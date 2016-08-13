package com.krogen.xmlParsers;

import java.io.File;

import com.krogen.main.DataContainer;
import com.krogen.xmlParsers.enums.MenuType;



/**
 * Main parser class it is responsible for all of the parsings.
 * 
 * @author Tihomir Turzai
 */
public class MainParser {


	/**
	 * Parse all of the xmls and put the data into the model classes
	 */
	public MainParser(){
		// Add configuration if needed
	}

	/**
	 * Consume all the necessary files that contain data regarding to the Django project
	 * @throws Exception 
	 * 
	 */
	public void parseData() throws Exception{
		DataContainer.getInstance().clearInstance();
		
		// parse the Enumerations
		EnumerationParser enumeParser = new EnumerationParser();
		enumeParser.parseEnumerations();
		
		// parse menu data which exist by default	
		MenuParser menuParser = new MenuParser();
		menuParser.parse(MenuType.DEFAULT_MENU);
		
		//if there are any menu which is different from the default ones (custom made) it should be in the specific file
		if (new File(ParserConstants.generatedModelPath + File.separator + ParserConstants.xmlFileName).exists())
			menuParser.parse(MenuType.GENERATED_MENUS);
		
		// parse model data based upon the forms
		EntityParser entityParser = new EntityParser();
		entityParser.parseEjbToXMLMappings();
		entityParser.parseEjbBeansUsingMappings(DataContainer.getInstance().getXMLMappings());
		
		// parse form data based upon the menu items
		PanelParser panelParser = new PanelParser();
		panelParser.parsePanelMappings();
		panelParser.parsePanels();		
		panelParser.parseParentChildPanel();		
	}
}

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

	public void parseData(){
		// parseEnumerations
		EnumerationParser enumeParser = new EnumerationParser();
		enumeParser.parseEnumerations();
		
		// parse menudata, if 		
		MenuParser menuParser = new MenuParser();
		menuParser.parse(MenuType.DEFAULT_MENU);
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

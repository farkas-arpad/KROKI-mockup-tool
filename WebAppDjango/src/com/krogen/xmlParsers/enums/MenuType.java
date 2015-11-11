package com.krogen.xmlParsers.enums;

import com.krogen.xmlParsers.ParserConstants;

/**
 * Menu type enumerations
 * @author Tihomir Turzai
 *
 */
public enum MenuType {
	DEFAULT_MENU(ParserConstants.xmlFileNameDefault),
	GENERATED_MENUS(ParserConstants.xmlFileName);
	
	String filename;	
	
	MenuType(String filename) {
		this.filename = filename;
	}
		
	public String getFilename(){
		return filename;
	}
}

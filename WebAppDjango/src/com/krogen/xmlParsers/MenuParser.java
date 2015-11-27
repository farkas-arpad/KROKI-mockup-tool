package com.krogen.xmlParsers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.krogen.enumerations.PanelType;
import com.krogen.exceptions.GeneratedMenuMissingException;
import com.krogen.exceptions.PanelTypeParsingException;
import com.krogen.main.DataContainer;
import com.krogen.model.menu.DjangoMenuItem;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.util.resolvers.PanelTypeResolver;
import com.krogen.xmlParsers.enums.MenuType;
import com.krogen.xml_utils.XMLParserUtils;


/**
 * Util class that reads menu structure specification from application
 * repository
 *  TODO - use constants from tags
 * @author Tihomir Turzai
 */
public class MenuParser {

	protected static String logPrefix = "MENU READER: ";

	protected ArrayList<DjangoSubMenu> rootMenus;
	protected ArrayList<HashMap<String, DjangoSubMenu>> menuMaps;

	/**
	 * 
	 * @param menuType
	 */
	public MenuParser(){

	}

	public void parse(MenuType menuType){
		// clean temporary variables
		rootMenus = new ArrayList<DjangoSubMenu>();
		menuMaps  = new ArrayList<HashMap<String, DjangoSubMenu>>(); 
		try {
			// parse menu
			createMenus(menuType);
			
			//save menu into model
			switch (menuType) {
			case DEFAULT_MENU:
				DjangoSubMenu temp = rootMenus.get(0);
				DataContainer.getInstance().setDefaultMenu(temp);
				break;
			case GENERATED_MENUS:
				DataContainer.getInstance().addMenus(rootMenus);
				break;
			}


		} catch (GeneratedMenuMissingException e) {
			// TODO log error
			e.printStackTrace();
		}


	}

	public void createMenus(MenuType menuType) throws GeneratedMenuMissingException {
		Document resDoc = XMLParserUtils.parseXml(ParserConstants.generatedModelPath
				+ File.separator + menuType.getFilename());

		if (resDoc == null && menuType.getFilename().equals(ParserConstants.xmlFileName)) {
			throw new GeneratedMenuMissingException(menuType.getFilename() + ", Generated menu missing, proceeding with default");
		}

		// Parsing menu file
		NodeList resNodes = resDoc.getElementsByTagName("menu");

		for (int i = 0; i < resNodes.getLength(); i++) {
			DjangoSubMenu rootMenu = new DjangoSubMenu();
			rootMenu.setName("Menu");
			rootMenus.add(rootMenu);

			HashMap<String, DjangoSubMenu> menuMap = new HashMap<String, DjangoSubMenu>();
			menuMap.put("Menu", rootMenu);
			menuMaps.add(menuMap);

			Element menuElement = (Element) resNodes.item(i);
			NodeList menuChildren = menuElement.getChildNodes();

			for (int j = 0; j < menuChildren.getLength(); j++) {
				Node n = (Node) menuChildren.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					if (n.getNodeName().equals(ParserConstants.SUBMENU)) {
						createSubmenu((Element) n, i);
					} else if (n.getNodeName().equals(ParserConstants.MENU_ITEM_MAIN)) {
						createMenuItemMain((Element) n, i);
					} else if (n.getNodeName().equals(ParserConstants.ROLES)) {
						createRoles((Element) n, i);
					}
				}
			}
		}


	}

	/**
	 * Roles connected to the menu structure.
	 * @param n
	 * @param i
	 */
	private void createRoles(Element n, int i) {
		NodeList nroles = n.getElementsByTagName(ParserConstants.ROLE);

		for (int j = 0; j < nroles.getLength(); j++) {
			Element element1 = (Element) nroles.item(j);
			String roleName = XMLParserUtils
					.getCharacterDataFromElement(element1);
			rootMenus.get(i).getRoles().add(roleName);
		}
	}


	/**
	 * Creates menu item on the main menu bar
	 * @param n
	 * @param i
	 */
	private void createMenuItemMain(Element n, int i) {

		// Menuitemi u rootu menija
		NodeList nmenuname = n.getElementsByTagName(ParserConstants.MENU_NAME);
		NodeList nformname = n.getElementsByTagName(ParserConstants.FORM_NAME);
		NodeList nactivate = n.getElementsByTagName(ParserConstants.ACTIVATE);
		NodeList npaneltype = n.getElementsByTagName(ParserConstants.PANEL_TYPE);

		String menuName = XMLParserUtils
				.getCharacterDataFromElement((Element) nmenuname.item(0));
		String formName = XMLParserUtils
				.getCharacterDataFromElement((Element) nformname.item(0));
		String activate = XMLParserUtils
				.getCharacterDataFromElement((Element) nactivate.item(0));
		String panelType = XMLParserUtils
				.getCharacterDataFromElement((Element) npaneltype.item(0));



		DjangoMenuItem tMenuItem;
		tMenuItem = new DjangoMenuItem();
		tMenuItem.setMenuName(menuName);
		tMenuItem.setFormName(formName);
		tMenuItem.setActivate(activate);
		PanelType ptr = null;
		if (!panelType.equals("?"))
		try {
			ptr = PanelTypeResolver.getType(panelType);
			tMenuItem.setPanelType(ptr.name().toString());
			
		} catch (PanelTypeParsingException e) {
			// issue with the parsed paneltype
			// probalby it is a Separator
			e.printStackTrace();
		}
		tMenuItem.setParent(rootMenus.get(i));
		rootMenus.get(i).getChildren().add(tMenuItem);

	}

	/**
	 * Creates Submenu.
	 * @param n
	 * @param i
	 */
	private void createSubmenu(Element n, int i) {
		NodeList nsubmenuparent = n.getElementsByTagName(ParserConstants.SUBMENU_PARENT);
		NodeList nsubmenuname = n.getElementsByTagName(ParserConstants.SUBMENU_NAME);
		NodeList ntmenuitem = n.getElementsByTagName(ParserConstants.MENU_ITEM);

		String submenuParent = XMLParserUtils
				.getCharacterDataFromElement((Element) nsubmenuparent.item(0));
		String submenuName = XMLParserUtils
				.getCharacterDataFromElement((Element) nsubmenuname.item(0));

		DjangoSubMenu parentMenu = menuMaps.get(i).get(submenuParent);
		DjangoSubMenu tempSubmenu = new DjangoSubMenu(submenuName);
		if (parentMenu != null) {
			tempSubmenu.setParent(parentMenu);
			parentMenu.getChildren().add(tempSubmenu);
		}
		menuMaps.get(i).put(submenuName, tempSubmenu);

		for (int k = 0; k < ntmenuitem.getLength(); k++) {
			Element element2 = (Element) ntmenuitem.item(k);
			NodeList nmenuname = element2.getElementsByTagName(ParserConstants.MENU_NAME);
			NodeList nformname = element2.getElementsByTagName(ParserConstants.FORM_NAME);
			NodeList nactivate = element2.getElementsByTagName(ParserConstants.ACTIVATE);
			NodeList npaneltype = element2.getElementsByTagName(ParserConstants.PANEL_TYPE);

			String menuName = XMLParserUtils
					.getCharacterDataFromElement((Element) nmenuname.item(0));
			String formName = XMLParserUtils
					.getCharacterDataFromElement((Element) nformname.item(0));
			String activate = XMLParserUtils
					.getCharacterDataFromElement((Element) nactivate.item(0));
			String panelType = XMLParserUtils
					.getCharacterDataFromElement((Element) npaneltype.item(0));

			DjangoMenuItem tMenuItem;
			tMenuItem = new DjangoMenuItem();
			tMenuItem.setMenuName(menuName);
			tMenuItem.setFormName(formName);
			tMenuItem.setActivate(activate);
			// Check if the PanelType is valid
			PanelType ptr = null;
			try {
				ptr = PanelTypeResolver.getType(panelType);
			} catch (PanelTypeParsingException e) {
				e.printStackTrace();
			}
			tMenuItem.setPanelType(ptr.name().toString());
			tMenuItem.setParent(tempSubmenu);
			tempSubmenu.getChildren().add(tMenuItem);
		}

	}

}

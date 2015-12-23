package com.krogen.xmlParsers;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.krogen.main.DataContainer;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.static_names.Tags;
import com.krogen.xml_utils.XMLParserUtils;
/**
 * Parsing enumerations
 *
 */
public class EnumerationParser {

	private static String logPrefix = "ENUMERATION READER: ";

	public void parseEnumerations() {
		parseEnumeration(ParserConstants.staticModelPath + File.separator + ParserConstants.enumStaticFileName);
		parseEnumeration(ParserConstants.generatedModelPath + File.separator + ParserConstants.enumGeneratedFileName);
	}

	private void parseEnumeration(String filePath) {
		try {
			Document document = XMLParserUtils.parseXml(filePath);
			NodeList enumNodes = document.getElementsByTagName(Tags.ENUM);
			for(int i=0; i<enumNodes.getLength(); i++) {
				Node enumNode = enumNodes.item(i);
				Element enumElement = (Element)enumNode;
				String name = enumElement.getAttribute(Tags.NAME);
				String label = enumElement.getAttribute(Tags.LABEL);
				Enumeration enumeration = new Enumeration(name, label);
				NodeList valueNodes = enumElement.getElementsByTagName(Tags.VALUE);
				for(int j=0; j<valueNodes.getLength(); j++) {
					String value = valueNodes.item(j).getTextContent();
					if(value != null) {
						enumeration.add(value);
					}
				}
				DataContainer.getInstance().addEnumeration(name, enumeration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

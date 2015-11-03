package com.krogen.xml_readers;

import java.io.File;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.krogen.ejb.EntityHelper;
import com.krogen.exceptions.EntityAttributeNotFoundException;
import com.krogen.main.AppCache;
import com.krogen.model.ejb.ColumnAttribute;
import com.krogen.model.ejb.EntityBean;
import com.krogen.model.ejb.JoinColumnAttribute;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.repository_utils.RepositoryPathsUtil;
import com.krogen.static_names.Tags;
import com.krogen.xml_utils.XMLParserUtils;

/**
 * Util class that reads ejb entities specification from application repository
 * @author Milorad Filipovic
 */
public class EntityReader {
	
	protected static String generatedRepoPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String generatedModelPath 	= RepositoryPathsUtil.getGeneratedModelPath();
	protected static String mappingFileName 	= "xml-mapping.xml";
	private static EntityBean currentBean;
	
	private static String logPrefix = "ENTITY READER: ";
	
	/**
	 * Read mapping from xml-mapping.xml file (appRepo/generated/model/)
	 */
	public static void loadMappings() {
		AppCache.displayTextOnMainFrame(logPrefix + "Reading mapping file: " + mappingFileName, 0);
		Document doc = XMLParserUtils.parseXml(generatedRepoPath + File.separator + mappingFileName);
		if(doc != null) {
			NodeList propertyNodes = doc.getElementsByTagName(Tags.PROPERTY);
			for (int i=0; i<propertyNodes.getLength(); i++) {
				Element propertyElement = (Element)propertyNodes.item(i);
				String className = propertyElement.getAttribute(Tags.CLASS_NAME);
				String xmlFile = propertyElement.getAttribute(Tags.XML_FILE);
				AppCache.getInstance().addToCache(className, xmlFile);
				AppCache.displayTextOnMainFrame(logPrefix + "Mapping " + className + " --> " + xmlFile + ".xml", 0);
			}
		}else {
			AppCache.displayTextOnMainFrame(logPrefix +  "Error parsing mapping file " + mappingFileName, 1);
		}
	}
	
	/**
	 * Loads XML specification for given EJB class
	 * @param className
	 * @return {@code EntityBean} object
	 */
	public static EntityBean load(String className) {
		EntityBean bean = null;
		if(AppCache.getInstance().findEJBByClassName(className) != null) {
			return bean;
		}
		try {
			String xmlFileName = AppCache.getInstance().getXMLFileName(className);
			if(xmlFileName == null) {
				return null;
			}
			Document doc = XMLParserUtils.parseXml(generatedModelPath + File.separator +  xmlFileName + ".xml");
			AppCache.displayTextOnMainFrame(logPrefix +  "Fetching EJB info from " + xmlFileName + ".xml", 0);
			bean = getEntityBeanInfo(doc);
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
			AppCache.displayTextOnMainFrame("Error loading EJB class info from xml file for class: " + className, 1);
			return null;
		}
	}
	
	private static EntityBean getEntityBeanInfo(Document document) throws ClassNotFoundException {
		EntityBean bean = new EntityBean();
		// Each xml file contains one <entity> with basic ejb info specified as attributes
		NodeList ejbNodes = document.getElementsByTagName(Tags.EJB);
		Element ejbNode = (Element)ejbNodes.item(0);
		
		bean.setName(ejbNode.getAttribute(Tags.NAME));
		bean.setLabel(ejbNode.getAttribute(Tags.LABEL));
		bean.setEntityClass(Class.forName(ejbNode.getAttribute(Tags.CLASS_NAME)));
		
		System.out.println("[getEntityBeanInfo] " + bean.getName());
		currentBean = bean;
		
		//EJB class attributes are specified within <attributes> tag
		NodeList attributesTags = ejbNode.getElementsByTagName(Tags.ATTRIBUTES);
		Element attributesTag = (Element)attributesTags.item(0);
		
		//attribute type (column attribute or zoom attribute) is determined by tag names
		NodeList attributesChildren = attributesTag.getChildNodes();
		for(int i=0; i<attributesChildren.getLength(); i++) {
			Node node = attributesChildren.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element attributeElement = (Element)attributesChildren.item(i);
				String attributeType = attributeElement.getNodeName();
				if(attributeType.equals(Tags.COLUMN_ATTRIBUTE)) {
					bean.add(getColumnAttibuteInfo(attributeElement));
				}else if(attributeType.equals(Tags.JOIN_COLUMN_ATTRIBUTE)) {
					bean.add(getJoinColumnAttributeInfo(attributeElement));
				}
			}
		}
		return bean;
	}
	
	private static ColumnAttribute getColumnAttibuteInfo(Element attributeElement) {
		ColumnAttribute columnAttribute = new ColumnAttribute();
		
		columnAttribute.setLabel(attributeElement.getAttribute(Tags.LABEL));
		columnAttribute.setName(attributeElement.getAttribute(Tags.NAME));
		columnAttribute.setFieldName(attributeElement.getAttribute(Tags.FIELD_NAME));
		columnAttribute.setDataType(attributeElement.getAttribute(Tags.DATA_TYPE));
		columnAttribute.setLength(Integer.parseInt(attributeElement.getAttribute(Tags.LENGTH)));
		
		String keyString = attributeElement.getAttribute(Tags.KEY);
		if(keyString != null && (keyString.equals("true") || keyString.equals("false"))) {
			Boolean key = new Boolean(keyString);
			if(key != null) {
				columnAttribute.setKey(key);
			}
		}
		
		//TODO Generisanje i parsiranje za scale
		Attr enumerationAttr = attributeElement.getAttributeNode(Tags.ENUM);
		if(enumerationAttr != null) {
			String enumName = attributeElement.getAttribute(Tags.ENUM);
			Enumeration enumeration = AppCache.getInstance().findEnumerationByName(enumName);
			if(enumeration != null) {
				columnAttribute.setEnumeration(enumeration);
			}
		}
		
		String hiddenString = attributeElement.getAttribute(Tags.HIDDEN);
		if(hiddenString != null && (hiddenString.equals("true") || hiddenString.equals("false"))) {
			Boolean hidden = new Boolean(hiddenString);
			columnAttribute.setHidden(hidden);
		}
		
		//TODO Parsiranje DERIVED i CALCULATED OBELEZJA
		return columnAttribute;
	}
	
	private static JoinColumnAttribute getJoinColumnAttributeInfo(Element attributeElement) {
		JoinColumnAttribute jcAttribute = new JoinColumnAttribute();
		
		jcAttribute.setZoomedBy(attributeElement.getAttribute(Tags.ZOOMED_BY));
		jcAttribute.setName(attributeElement.getAttribute(Tags.NAME));
		jcAttribute.setLabel(attributeElement.getAttribute(Tags.LABEL));
		jcAttribute.setFieldName(attributeElement.getAttribute(Tags.FIELD_NAME));
		
		String lookupName = attributeElement.getAttribute(Tags.CLASS_NAME);
		if(lookupName != null) {
			try {
				jcAttribute.setLookupClass(Class.forName(lookupName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		//each zoomed column reference is specified in <column-ref> tag
		NodeList columnRefNodes = attributeElement.getElementsByTagName(Tags.COLUMN_REF);
		for(int i=0; i<columnRefNodes.getLength(); i++) {
			Node colRefNode = columnRefNodes.item(i);
			Element colRefELement = (Element)colRefNode;
			ColumnAttribute column = null;
			String attrName = colRefELement.getAttribute(Tags.NAME);
			column = lookupColumnAttribute(attrName, jcAttribute.getLookupClass());
			column.setLabel(colRefELement.getAttribute(Tags.LABEL));
			jcAttribute.add(column);
		}
		return jcAttribute;
	}
	
	private static ColumnAttribute lookupColumnAttribute(String attrName, Class<?> lookupClass) {
		System.out.println("[lookupColumnAttribute] " + attrName + ", " + lookupClass.getSimpleName());
		ColumnAttribute ca = null;
		AppCache appCache = AppCache.getInstance();
		EntityBean ejb = null;
		if ((ejb = appCache.findEJBByClassName(lookupClass.getName())) != null) {
			try {
				return (ColumnAttribute) EntityHelper.getAttribute(ejb,
						attrName);
			} catch (EntityAttributeNotFoundException e) {
				return null;
			}
		}
		String xmlFileName = appCache.getXMLFileName(lookupClass.getName());
		if (xmlFileName == null)
			return null;
		try {
			Document doc = XMLParserUtils.parseXml(generatedModelPath + File.separator +  xmlFileName + ".xml");
			
			//If class has join column onto itself, there is no need to parse XML and go trough EJB loading process again
			if(lookupClass.getName().equals(currentBean.getEntityClass().getName())) {
				ejb = currentBean;
			}else {
				ejb = getEntityBeanInfo(doc);
			}
			// TODO: izvuci samo potreban columnAttribute
			NodeList nodeList = doc.getElementsByTagName(Tags.COLUMN_ATTRIBUTE);
			Node node = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				Element el = (Element) node;
				String val = el.getAttribute(Tags.NAME);
				if (val.equals(attrName)) {
					return getColumnAttibuteInfo(el);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ca;
	}
}
package com.krogen.model.django;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.krogen.main.DataContainer;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.django.modelpy.DjangoModelField;
import com.krogen.model.django.modelpy.EntryTypesEnum;
import com.krogen.model.ejb.AbstractAttribute;
import com.krogen.model.ejb.ColumnAttribute;
import com.krogen.model.ejb.EjbClass;
import com.krogen.model.ejb.JoinColumnAttribute;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.model.panel.DjangoPanel;


/**
 * Adapter to create Django Ready data from the parsed model data
 * @author Tihomir Turzai
 *
 */
public class DjangoAdapter {


	//	public String getXMLFileName(String className) {
	//		return djangoMetaModel.getXmlMappingsMap().get(className);
	//	}

	public List<DjangoUrl> getDjangoUrls(){
		List<DjangoUrl> urls = new ArrayList<DjangoUrl>();
		Map<String, String> panels = DataContainer.getInstance().getPanelClassMap();

		for (Map.Entry<String, String> entry : panels.entrySet())
		{
			System.out.println(entry.getKey() + "/" + entry.getValue());
			urls.add(new DjangoUrl(entry.getValue(),entry.getValue()));
		}
		return urls;
	}

	public Map<String, String> getPanelClassMap(){
		return DataContainer.getInstance().getPanelClassMap();
	}

	public List<DjangoPanel> getPanels(){
		return DataContainer.getInstance().getPanels();
	}

	public DjangoSubMenu getDefaultMenu() {
		return DataContainer.getInstance().getDefaultMenu();
	}

	public DjangoSubMenu getMenuList(){
		// TODO: alter the menu generator here if needed
		if (DataContainer.getInstance().getMenuList().size() > 0 ){
			DjangoSubMenu menu = new DjangoSubMenu();
			menu.getChildren().addAll(DataContainer.getInstance().getMenuList());
			return menu;
		}
		else			
			return DataContainer.getInstance().getDefaultMenu(); 
	}
	public DjangoModel convertEjbToModelEntry(EjbClass ejbModel){

		DjangoModel djangoModel= new DjangoModel();

		djangoModel.setName(ejbModel.getName());
		djangoModel.setLabel(ejbModel.getLabel());
		List<AbstractAttribute> attributes = ejbModel.getAttributes();
		for (AbstractAttribute attribute : attributes){
			DjangoModelField djangoModelField= new DjangoModelField();

			djangoModelField.setLabel(attribute.getLabel());
			djangoModelField.setFieldName(attribute.getFieldName());
			djangoModelField.setHidden(attribute.getHidden());
			djangoModelField.setName(attribute.getName());

			if (attribute instanceof ColumnAttribute){				

				//	attribute.getDisabled();
				djangoModelField.setKey(((ColumnAttribute) attribute).getKey());								
				djangoModelField.setLength(((ColumnAttribute) attribute).getLength());
				djangoModelField.setRepresentative(((ColumnAttribute) attribute).getRepresentative());
				String dataType = ((ColumnAttribute) attribute).getDataType();
				if (((ColumnAttribute) attribute).getEnumeration() != null)
					djangoModelField.setEnumerationName(((ColumnAttribute) attribute).getEnumeration().getName());

				switch(dataType){
				case "java.lang.Long":
					djangoModelField.setEntryTypesEnum(EntryTypesEnum.INTEGERFIELD);
					break;
				case "java.lang.Boolean":
					djangoModelField.setEntryTypesEnum(EntryTypesEnum.BOOLEANFIELD);										
					break;
				case "java.lang.String:TextArea":	
					djangoModelField.setEntryTypesEnum(EntryTypesEnum.TEXTAREA);					
					break;
				case "java.lang.String":					
				default:
					djangoModelField.setEntryTypesEnum(EntryTypesEnum.CHARFIELD);					
				}

			}else
				if (attribute instanceof JoinColumnAttribute){			
					djangoModelField.setClassName(((JoinColumnAttribute) attribute).getLookupClass());		
					djangoModelField.setEntryTypesEnum(EntryTypesEnum.FOREIGNKEY);
					//In the kroki UI is not possible to set representative value for fields from other panels
					djangoModelField.setRepresentative(false);
				}
			djangoModel.getFieldsList().add(djangoModelField);

		}
		return djangoModel;
	}

	public Map<String,String> getClassnameModelMapping(){
		Map<String,String> resultMap = new HashMap<String,String>();
		HashMap<String,EjbClass> ejbClasses = DataContainer.getInstance().getEjbClasses();
		for (Map.Entry<String, EjbClass> entry : ejbClasses.entrySet())
		{
			resultMap.put(entry.getValue().getEntityClass(), entry.getValue().getName());
		}		
		return resultMap;
	}

	public List<DjangoModel> getModelList(){
		List<EjbClass> ejbClasses = getSortedModelList();
		List<DjangoModel> djangoModelList = new ArrayList<DjangoModel>();
		for (EjbClass entry : ejbClasses)
		{
			djangoModelList.add(convertEjbToModelEntry(entry));		
		}	
		return djangoModelList;
	}

	public List<Enumeration> getEnumerations() {
		Map<String, Enumeration> enumMap = DataContainer.getInstance().getEnumerations();
		List<Enumeration> list = new ArrayList<Enumeration>(enumMap.values());
		return list;
	}

	public Map<String,String> getPanelNameMap() {
		Map<String,String> panelNameMap = new HashMap<String,String>();

		List<DjangoPanel> panels = DataContainer.getInstance().getPanels();

		for (DjangoPanel panel : panels)
		{		
			panelNameMap.put(panel.getName(),panel.getLabel());
		}
		return panelNameMap;		
	}

	public List<EjbClass> getSortedModelList(){
		List<EjbClass> ejbList = new ArrayList<EjbClass>();
		for (Map.Entry<String, EjbClass> entry : DataContainer.getInstance().getEjbClasses().entrySet()){
			List<EjbClass> tempList = xxx(entry.getValue());
			for (EjbClass ejbRes : tempList){
				if (!ejbList.contains(ejbRes))
					ejbList.add(ejbRes);
			}
		}
		return ejbList;
	}
	private List<EjbClass> xxx(EjbClass ejb){
		//boolean lookup = false;
		List<EjbClass> ejbList = new ArrayList<EjbClass>();
		for (AbstractAttribute attribute: ejb.getAttributes()){
			if (attribute instanceof JoinColumnAttribute){
				//	lookup = true;
				ejbList.addAll(xxx(DataContainer.getInstance().getEjbClasses().get(((JoinColumnAttribute) attribute).getLookupClass())));
			}
		}
		ejbList.add(ejb);		
		return ejbList;
	}
	//	public List<>
	//	public Map<String, DjangoModel> getDjangoModels(){
	//		Map<String, String> mappings =  djangoMetaModel.getXmlMappingsMap();
	//		Map<String, DjangoModel> mappingModels = new HashMap<String, DjangoModel>();
	//		for (Map.Entry<String, String> mapping : mappings.entrySet())
	//		{
	//			System.out.println(mapping.getKey() + "/" + mapping.getValue());
	//			ModelEntry modelEntry = EntityReader.load(mapping.getKey());
	//			// new ModelEntry().
	//
	//			//mappingModels.put(mapping.getKey(),);
	//
	//			System.out.println(mapping.getKey() + "/" + mapping.getValue());
	//			
	//			//urls.add(new DjangoUrl(entry.getValue(),entry.getValue()));
	//		}
	//		return mappingModels;
	//
	//	}




	//	public ModelEntry findEJBByClassName(String className) {
	//		Iterator<ModelEntry> it = djangoMetaModel.getEntityBeans().iterator();
	//		ModelEntry ejb = null;
	//		while (it.hasNext()) {
	//			ejb = it.next();
	//			if (ejb.getEntityClass().getName().equals(className)) {
	//				return ejb;
	//			}
	//		}
	//		return null;
	//	}

}

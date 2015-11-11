package com.krogen.model.django;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krogen.main.DataContainer;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.django.modelpy.DjangoModelField;
import com.krogen.model.django.modelpy.EntryTypesEnum;
import com.krogen.model.ejb.AbstractAttribute;
import com.krogen.model.ejb.ColumnAttribute;
import com.krogen.model.ejb.EjbClass;
import com.krogen.model.menu.DjangoSubMenu;


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

	public DjangoSubMenu getDefaultMenu() {
		return DataContainer.getInstance().getDefaultMenu();
	}
	
	public DjangoModel convertEjbToModelEntry(EjbClass ejbModel){
		
		DjangoModel djangoModel= new DjangoModel();
		
		djangoModel.setName(ejbModel.getName());
		djangoModel.setLabel(ejbModel.getLabel());
		List<AbstractAttribute> attributes = ejbModel.getAttributes();
		for (AbstractAttribute attribute : attributes){
			if (attribute instanceof ColumnAttribute){				
				DjangoModelField djangoModelField= new DjangoModelField();
				
				attribute.getDisabled();
				djangoModelField.setLabel(attribute.getLabel());
				djangoModelField.setFieldName(attribute.getFieldName());
				djangoModelField.setKey(((ColumnAttribute) attribute).getKey());				
				djangoModelField.setHidden(attribute.getHidden());
				djangoModelField.setName(attribute.getName());
				djangoModelField.setLength(((ColumnAttribute) attribute).getLength());
				String dataType = ((ColumnAttribute) attribute).getDataType();
				
				switch(dataType){
				//case "java.lang.Long":
					
					//break;
				case "java.lang.String":					
				default:
					djangoModelField.setEntryTypesEnum(EntryTypesEnum.CHARFIELD);
					
				}
				
				djangoModel.getFieldsList().add(djangoModelField);
			}
			
		}
		return djangoModel;
	}
	
	public List<DjangoModel> getModelList(){
		List<EjbClass> ejbClasses = DataContainer.getInstance().getEjbClasses();
		 List<DjangoModel> djangoModelList = new ArrayList<DjangoModel>();
		for (EjbClass ejbClass : ejbClasses){
			djangoModelList.add(convertEjbToModelEntry(ejbClass));
		}			
		return djangoModelList;
	}
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

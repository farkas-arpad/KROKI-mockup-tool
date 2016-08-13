package com.krogen.generator.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.main.Application;
import com.krogen.main.DataContainer;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.django.modelpy.DjangoModelField;
import com.krogen.model.django.modelpy.EntryTypesEnum;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.panel.AdaptParentChildPanel;
import com.krogen.model.panel.AdaptStandardPanel;
import com.krogen.model.panel.DjangoPanel;
import com.krogen.static_names.Settings;

public class ViewPyPart extends Part {

	public ViewPyPart() {
		super();
	}
	
	@Override
	public void generate() throws Exception {
		List<DjangoPanel> panels = djangoAdapter.getPanels();
		List<AdaptParentChildPanel> pcPanels = DataContainer.getInstance().getParentChildPanel();
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();
		Map<String, String> panelClassMap = djangoAdapter.getPanelClassMap();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();

		context.clear();
	
		context.put("classes", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", DjangoConstants.MODULE_NAME);
		context.put("description", Settings.APP_DESCRIPTION);
		context.put("urls", djangoAdapter.getDjangoUrls());
		context.put("panels", panels);
		context.put("pcPanels", pcPanels);
		context.put("classnameModelMap", classnameModelMap);
		context.put("panelClassMap", panelClassMap);

		context.put("models", djangoModelList);
		context.put("enumerations", enumerations);

		Map<String, Map<String, List<String>>> pcForeingKeyMap = new HashMap<String, Map<String, List<String>>>();

		if (pcPanels.size() > 0) {
			for (AdaptParentChildPanel adaptParentChildPanel : pcPanels) {

				// if the parent-child panel has child no panels. skip the generation
				if (adaptParentChildPanel.getChildPanels().size() <= 0) {
					continue;
				}

				DjangoModel parentModel = null;
				List<DjangoModel> childModelList = new ArrayList<DjangoModel>();
				
				for (DjangoModel model : djangoModelList) {
					if (model.getLabel().equals(adaptParentChildPanel.getParentPanel().getLabel())) {
						parentModel = model;
					}
				}
				
				for (DjangoModel model : djangoModelList) {
					for (AdaptStandardPanel childPanel : adaptParentChildPanel.getChildPanels()) {
						if (model.getLabel().equals(childPanel.getLabel())) {
							childModelList.add(model);
						}
					}
				}
				
				Map<String, List<String>> fieldMap = new HashMap<String, List<String>>();
				
				for (DjangoModel childModel : childModelList){
					List<String> fieldListToSend = new ArrayList<String>();
					
					for (DjangoModelField field : childModel.getFieldsList()) {
						if (field.getEntryTypesEnum() == EntryTypesEnum.FOREIGNKEY) {
							String foreignKeyEntity = classnameModelMap.get(field.getClassName());
							if (foreignKeyEntity.equals(parentModel.getName())) {
								fieldListToSend.add(field.getFieldName());
							}
						}
					}
					
					fieldMap.put(childModel.getLabel(), fieldListToSend);
				}
				pcForeingKeyMap.put(adaptParentChildPanel.getLabel(), fieldMap);
			}
		}

		context.put("pcForeingKeyMap", pcForeingKeyMap);
		// TODO add ejb-url map

		generateWithProjectname(DjangoConstants.moduleDir, DjangoConstants.VIEWS_PY, context);
	}

}

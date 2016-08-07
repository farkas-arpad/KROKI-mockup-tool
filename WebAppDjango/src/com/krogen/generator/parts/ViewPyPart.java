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

		Map<String, String> pcForeingKeyMap = new HashMap<String, String>();

		if (pcPanels.size() > 0) {
			for (AdaptParentChildPanel adaptParentChildPanel : pcPanels) {

				int p1Level = adaptParentChildPanel.getPanels().get(0).getLevel();
				int p2Level = adaptParentChildPanel.getPanels().get(1).getLevel();

				AdaptStandardPanel parentPanel = null;
				AdaptStandardPanel childPanel = null;

				DjangoModel parentModel = null;
				DjangoModel childModel = null;

				//the panel with lower hierarchy is the parent one
				if (p1Level < p2Level) {
					parentPanel = adaptParentChildPanel.getPanels().get(0);
					childPanel = adaptParentChildPanel.getPanels().get(1);
				} else {
					parentPanel = adaptParentChildPanel.getPanels().get(1);
					childPanel = adaptParentChildPanel.getPanels().get(0);
				}

				for (DjangoModel model : djangoModelList) {
					if (model.getLabel().equals(parentPanel.getLabel())) {
						parentModel = model;
					}
					if (model.getLabel().equals(childPanel.getLabel())) {
						childModel = model;
					}
				}

				DjangoModelField fieldToSend = null;

				for (DjangoModelField field : childModel.getFieldsList()) {
					if (field.getEntryTypesEnum() == EntryTypesEnum.FOREIGNKEY) {
						String foreignKeyEntity = classnameModelMap.get(field.getClassName());
						if (foreignKeyEntity.equals(parentModel.getName())) {
							fieldToSend = field;
						}
					}
				}
				pcForeingKeyMap.put(adaptParentChildPanel.getLabel(), fieldToSend.getFieldName());
			}
		}

		context.put("pcForeingKeyMap", pcForeingKeyMap);
		// TODO add ejb-url map

		generateWithProjectname(DjangoConstants.moduleDir, DjangoConstants.VIEWS_PY, context);
	}

}

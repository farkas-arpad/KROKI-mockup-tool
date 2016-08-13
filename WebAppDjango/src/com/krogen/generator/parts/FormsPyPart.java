package com.krogen.generator.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.main.Application;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.model.panel.DjangoPanel;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of forms.py file
 */
public class FormsPyPart extends Part {

	public FormsPyPart() {
		super();
	}
	
	@Override
	public void generate() throws Exception {
		DjangoSubMenu mainMenu = djangoAdapter.getDefaultMenu();
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();
		List<DjangoPanel> panels = djangoAdapter.getPanels();

		context.clear();
		context.put("forms", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", DjangoConstants.MODULE_NAME);
		context.put("classnameModelMap", classnameModelMap);
		context.put("menu", mainMenu);
		context.put("models", djangoModelList);
		context.put("enumerations", enumerations);
		context.put("panels", panels);

		generateWithProjectname(DjangoConstants.moduleDir, DjangoConstants.FORMS_PY, context);
	}

}

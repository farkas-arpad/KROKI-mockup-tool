package com.krogen.generator.parts;

import java.util.ArrayList;
import java.util.List;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.main.Application;
import com.krogen.main.DataContainer;
import com.krogen.model.panel.AdaptParentChildPanel;
import com.krogen.model.panel.DjangoPanel;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of urls.py file
 */
public class URLsPyPart extends Part {

	@Override
	public void generate() throws Exception {
		List<DjangoPanel> panels = djangoAdapter.getPanels();
		List<AdaptParentChildPanel> pcPanels = DataContainer.getInstance().getParentChildPanel();

		context.clear();
		context.put("imports", new ArrayList<String>());
		context.put("urls", djangoAdapter.getDjangoUrls());
		context.put("panels", panels);
		context.put("parentChildPanels", pcPanels);
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", DjangoConstants.MODULE_NAME);
		
		generateWithProjectname(DjangoConstants.moduleDir, DjangoConstants.URLS_PY, context);
	}

}

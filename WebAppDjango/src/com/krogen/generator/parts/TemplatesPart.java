package com.krogen.generator.parts;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.main.Application;
import com.krogen.main.DataContainer;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.model.panel.AdaptParentChildPanel;
import com.krogen.model.panel.AdaptStandardPanel;
import com.krogen.model.panel.DjangoPanel;

public class TemplatesPart extends Part {

	@Override
	public void generate() throws Exception {
		String srcDirString = DjangoConstants.dynamicTemplateDir;
		File srcDir = new File(DjangoConstants.dynamicTemplateDir);
		// clean up previously generated files
		FileUtils.cleanDirectory(srcDir);

		File tempDestDir = new File(DjangoConstants.moduleDir + File.separator + "templates");

		DjangoSubMenu mainMenu = djangoAdapter.getMenuList();
		context.clear();
		context.put("menu", mainMenu);
		context.put("projectname", Application.projectTitleRenamed);

		// generate navbar
		generateWithProjectname(srcDirString, DjangoConstants.NAVBAR_HTML, context);

		// generate list views
		List<DjangoPanel> panels = djangoAdapter.getPanels();
		Map<String, String> panelNameMap = djangoAdapter.getPanelNameMap();

		for (DjangoPanel panel : panels) {
			context.clear();

			context.put("projectname", Application.projectTitleRenamed);
			context.put("menu", mainMenu);
			context.put("panel", panel);
			context.put("panelNameMap", panelNameMap);
			generateWithProjectname(srcDirString, "list.ftl", panel.getName() + "_list.html", context);
			generateWithProjectname(srcDirString, "newObject.ftl", panel.getName() + "_new.html", context);
			generateWithProjectname(srcDirString, "object.ftl", panel.getName() + ".html", context);

		}

		// generate parentChild
		for (AdaptParentChildPanel adaptParentChildPanel : DataContainer.getInstance().getParentChildPanel()) {
			context.clear();

			AdaptStandardPanel parentPanel = adaptParentChildPanel.getParentPanel();
			List<AdaptStandardPanel> childPanels = adaptParentChildPanel.getChildPanels();

			context.put("projectname", Application.projectTitleRenamed);
			context.put("menu", mainMenu);
			context.put("panel", adaptParentChildPanel);
			context.put("parentPanel", parentPanel);
			context.put("childPanels", childPanels);
			generateWithProjectname(srcDirString, "parentChild.ftl", adaptParentChildPanel.getName() + "_pc.html",
					context);
		}

		FileUtils.copyDirectory(srcDir, tempDestDir);
	}

}

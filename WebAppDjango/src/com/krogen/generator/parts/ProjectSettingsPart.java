package com.krogen.generator.parts;

import java.io.File;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.main.Application;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of manage.py, __init__.py, wsgi.py, urls.py, custom_tags.py file
 */
public class ProjectSettingsPart extends Part {

	public ProjectSettingsPart() {
		super();
	}
	
	@Override
	public void generate() throws Exception {

		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", DjangoConstants.MODULE_NAME);

		// Generate the manage.py
		generateWithProjectname(DjangoConstants.projectDir + File.separator + Application.projectTitleRenamed, DjangoConstants.MANAGE_PY, context);
		// Generate the __init__.py file with the basic configurations
		// TODO put basic config into an external file
		generateWithProjectname(DjangoConstants.projectConfigDestDir, DjangoConstants.INIT_PY, context);
		// Generate settings.py
		generateWithProjectname(DjangoConstants.projectConfigDestDir, DjangoConstants.SETTINGS_PY, context);
		// Generate wsgi.py
		generateWithProjectname(DjangoConstants.projectConfigDestDir, DjangoConstants.WSGI_PY, context);
		// Generate urls.py
		generateWithProjectname(DjangoConstants.projectConfigDestDir, DjangoConstants.URLS_DEFAULT_FTL, DjangoConstants.URLS_PY, context);
		// Custom Tags
		generateWithProjectname(DjangoConstants.templatetagDir, DjangoConstants.CUSTOM_TAGS_PY, context);
		generateWithProjectname(DjangoConstants.templatetagDir, DjangoConstants.INIT_PY_DEFAULT_FTL, DjangoConstants.INIT_PY, context);
	}

}

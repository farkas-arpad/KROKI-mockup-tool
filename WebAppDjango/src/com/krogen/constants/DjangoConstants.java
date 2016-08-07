package com.krogen.constants;

import java.io.File;

import com.krogen.main.Application;

public class DjangoConstants {

	// file name constants
	public static final String MANAGE_PY = "manage.py";
	public static final String WSGI_PY = "wsgi.py";
	public static final String MODELS_PY = "models.py";
	public static final String VIEWS_PY = "views.py";
	public static final String FORMS_PY = "forms.py";
	public static final String URLS_PY = "urls.py";	
	public static final String INIT_PY = "__init__.py";
	public static final String SETTINGS_PY = "settings.py";
	public static final String CUSTOM_TAGS_PY = "custom_tags.py";
	public static final String CUSTOM_CODE_PY = "manualCode.py";

	public static final String URLS_DEFAULT_FTL = "urlsDefault.ftl";		
	public static final String INIT_PY_DEFAULT_FTL = "__init__Default.ftl";
	
	public static final String HOME_FTL = "home.ftl";		
	
	public static final String NAVBAR_HTML = "navbar.html";

	public static final String MODULE_NAME = "module";

	// destination paths
	// project will be generated into:
	public static final String projectDir =  Application.djangoProjectRootPath + File.separator + "generated";
	// module will be placed into:
	public static final String moduleDir =  projectDir + File.separator + Application.projectTitleRenamed + File.separator+ MODULE_NAME;
	//templatetags	
	public static final String templatetagDir =  moduleDir + File.separator+ "templatetags";
	// main configuration files will be generated into:
	public static final String projectConfigDestDir = projectDir + File.separator + Application.projectTitleRenamed + File.separator+Application.projectTitleRenamed;
	// static files will be copied into:
	public static final String staticDestDir = projectDir + File.separator + Application.projectTitleRenamed + File.separator+"static";
	// source paths
	public static final String resourcesSourceDir = Application.djangoProjectRootPath + File.separator+"src"+ File.separator + "resources";
	// ftl template source
	public static final String templateDir = resourcesSourceDir + File.separator + "freemarkertemplates";	
	// html template source
	public static final String djangoTemplateDir = resourcesSourceDir  +File.separator+"djangotemplates";
	// dynamic html template source
	public static final String dynamicTemplateDir = resourcesSourceDir  +File.separator+"dynamictemplates";	
	// static resources source
	public static final String staticSourceDir = resourcesSourceDir + File.separator + "staticdata" ;
	// custom code source
	public static final String customCodeDir = resourcesSourceDir  +File.separator+"customcode";	

}

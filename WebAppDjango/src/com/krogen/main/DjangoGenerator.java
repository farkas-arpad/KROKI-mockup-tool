package com.krogen.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.krogen.model.django.DjangoAdapter;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.model.panel.AdaptPanel;
import com.krogen.static_names.Settings;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Generating python code
 */
public class DjangoGenerator {


	// 	file name constants
	public static String MANAGE_PY = "manage.py";
	public static String WSGI_PY = "wsgi.py";
	public static String MODELS_PY = "models.py";
	public static String VIEWS_PY = "views.py";
	public static String FORMS_PY = "forms.py";
	public static String URLS_PY = "urls.py";	
	public static String INIT_PY = "__init__.py";
	public static String SETTINGS_PY = "settings.py";

	public static String URLS_DEFAULT_FTL = "urlsDefault.ftl";		
	public static String NAVBAR_HTML = "navbar.html";
	
	public static String MODULE_NAME = "module";
	public Configuration cfg;

	// destination paths
	// project will be generated into:
	private String projectDir =  Application.appRootPath + File.separator + "generated";
	// module will be placed into:
	private String moduleDir =  projectDir + File.separator + Application.projectTitleRenamed + File.separator+ MODULE_NAME;
	// main configuration files will be generated into:
	private String projectConfigDestDir = projectDir + File.separator + Application.projectTitleRenamed + File.separator+Application.projectTitleRenamed;
	// static files will be copied into:
	private String staticDestDir = projectDir + File.separator + Application.projectTitleRenamed + File.separator+"static";
	
	// source paths
	private String resourcesSourceDir = Application.appRootPath + File.separator+"src"+ File.separator + "resources";
	// ftl template source
	private String templateDir = resourcesSourceDir + File.separator + "freemarkertemplates";	
	// html template source
	private String djangoTemplateDir = resourcesSourceDir  +File.separator+"djangotemplates";
	// static resources source
	private String staticSourceDir = resourcesSourceDir + File.separator + "staticdata" ;
	
	private Template template;
	Map<String, Object> context = new HashMap<String, Object>();
	
	DjangoAdapter djangoAdapter = new DjangoAdapter();

	public DjangoGenerator() throws IOException {		
		cfg = new Configuration();		
		try {
			cfg.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			throw new IOException("Template folder not exists" + templateDir , e);
		}		
	}

	public void generate() throws IOException{
		// create base folders
		generateBasicFolderStructure();
		// project settings
		generateProjectSettings();		
		// component files
		generateModelsPy();
		generateFormsPy();
		generateViewsPy();
		generateURLsPy();
		generateStaticFiles();
		// directly tweak html files using freemarker
		generateTemplates();
		
		//copy custom code

	}

	protected void generateProjectSettings() throws IOException{
		context.clear();
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);

		// Generate the manage.py 
		generateWithProjectname(projectDir + File.separator + Application.projectTitleRenamed,MANAGE_PY, context);	
		// Generate the __init__.py file with the basic configurations 
		// TODO put basic config into an external file
		generateWithProjectname(projectConfigDestDir,INIT_PY, context);
		// Generate settings.py
		generateWithProjectname(projectConfigDestDir,SETTINGS_PY, context);		
		// Generate wsgi.py
		generateWithProjectname(projectConfigDestDir,WSGI_PY,context);		
		// Generate urls.py
		generateWithProjectname(projectConfigDestDir,URLS_DEFAULT_FTL,URLS_PY,context);	
		
	}

	protected void generateStaticFiles() throws IOException{		
		File srcDir = new File(staticSourceDir);
		File tempDestDir = new File(staticDestDir);		
		FileUtils.copyDirectory(srcDir, tempDestDir);		
	}
	
	protected void generateTemplates() throws IOException{	

		String srcDirString = djangoTemplateDir;
		File srcDir = new File(djangoTemplateDir);		
		File tempDestDir = new File(moduleDir +File.separator+"templates");		

		// TODO:
		// add multimeenu system
		DjangoSubMenu mainMenu = djangoAdapter.getDefaultMenu();
		context.clear();
		context.put("menu", mainMenu);

		// generate navbar
		generateWithProjectname(srcDirString,NAVBAR_HTML, context);

		// generate list views
		List<AdaptPanel> panels = djangoAdapter.getPanels();
		
		for (AdaptPanel panel : panels){
			context.clear();			
			context.put("menu", mainMenu);
			context.put("panel", panel);
			generateWithProjectname(srcDirString,"list.ftl",panel.getName() + "_list.html", context);
			generateWithProjectname(srcDirString,"newObject.ftl",panel.getName() + "_new.html", context);
			generateWithProjectname(srcDirString,"object.ftl",panel.getName() + ".html", context);

		}
		FileUtils.copyDirectory(srcDir, tempDestDir);		

	}	

	public void generateViewsPy()throws IOException{

		//AppCache.getInstance().getXmlMappings();
		List<AdaptPanel> panels = djangoAdapter.getPanels();
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		
		context.clear();
		context.put("classes", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);
		context.put("description", Settings.APP_DESCRIPTION);	
		context.put("urls", djangoAdapter.getDjangoUrls());		
		context.put("panels", panels);
		context.put("models", djangoModelList);
		
		generateWithProjectname(moduleDir,VIEWS_PY, context);
	}

	public void generateFormsPy()throws IOException{
		DjangoSubMenu mainMenu = djangoAdapter.getDefaultMenu();
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();

		context.clear();
		context.put("forms", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);

		context.put("menu", mainMenu);
		context.put("models", djangoModelList);

		generateWithProjectname(moduleDir,FORMS_PY, context);
	}

	public void generateURLsPy()throws IOException{

		context.clear();		
		context.put("imports", new ArrayList<String>());
		context.put("urls", djangoAdapter.getDjangoUrls());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);
		generateWithProjectname(moduleDir,URLS_PY, context);
	}

	public void generateModelsPy()throws IOException{
		// Map<String, DjangoModel> model = DjangoContainer.getInstance().getDjangoModels();

		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();

		context.clear();
		context.put("enumerations", enumerations);
		context.put("models", djangoModelList);

		generateWithProjectname(moduleDir,MODELS_PY, context);
	}

	/**
	 * Internal code generator code
	 * Used to create the two base files manage.py and wsgi.py
	 * @param templateName
	 * @throws IOException
	 */
	private void generateWithProjectname(String path, String destinationFileName, Map<String, Object> context) throws IOException{
		String templateName = destinationFileName.substring(0,destinationFileName.lastIndexOf(".")) +".ftl";		
		generateWithProjectname(path, templateName, destinationFileName, context);
	}
/**
 * Used to generate files using freemarker
 * @param path - destination path
 * @param templateName
 * @param destinationFileName
 * @param context
 * @throws IOException
 */
	private void generateWithProjectname(String path, String templateName,String destinationFileName, Map<String, Object> context) throws IOException{
		try{
			// set basic configuration
			template = cfg.getTemplate(templateName);
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			// put data to context
			Writer out = new OutputStreamWriter(new FileOutputStream(path + File.separator+destinationFileName));
			if (out != null) {
				template.process(context, out);
				out.flush();
			}					
		}
		catch (IOException e) {
			throw new IOException("Missing template  " + templateName + ".",
					e);
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the basic folder structure for the Django project
	 * @throws IOException
	 */
	protected void generateBasicFolderStructure() throws IOException{	
		createFolder(Paths.get(projectConfigDestDir));
		createFolder(Paths.get(moduleDir));		
	}

	private void createFolder(Path folderToCreate) throws IOException{
		if (!folderToCreate.toFile().exists())
			if (!folderToCreate.toFile().mkdirs()) {
				throw new IOException("Error during folder creation."
						+ folderToCreate);
			}
	}
}
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
import com.krogen.model.django.modelpy.DjangoModelField;
import com.krogen.model.django.modelpy.EntryTypesEnum;
import com.krogen.model.enumeration.Enumeration;
import com.krogen.model.menu.DjangoSubMenu;
import com.krogen.model.panel.AdaptParentChildPanel;
import com.krogen.model.panel.AdaptStandardPanel;
import com.krogen.model.panel.DjangoPanel;
import com.krogen.static_names.Settings;

import antlr.StringUtils;
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
	public static String CUSTOM_TAGS_PY = "custom_tags.py";
	public static String CUSTOM_CODE_PY = "manualCode.py";

	public static String URLS_DEFAULT_FTL = "urlsDefault.ftl";		
	public static String INIT_PY_DEFAULT_FTL = "__init__Default.ftl";		
	
	public static String NAVBAR_HTML = "navbar.html";
	
	public static String MODULE_NAME = "module";
	public Configuration cfg;

	// destination paths
	// project will be generated into:
	private String projectDir =  Application.djangoProjectRootPath + File.separator + "generated";
	// module will be placed into:
	private String moduleDir =  projectDir + File.separator + Application.projectTitleRenamed + File.separator+ MODULE_NAME;
	//templatetags	
	private String templatetagDir =  moduleDir + File.separator+ "templatetags";
	// main configuration files will be generated into:
	private String projectConfigDestDir = projectDir + File.separator + Application.projectTitleRenamed + File.separator+Application.projectTitleRenamed;
	// static files will be copied into:
	private String staticDestDir = projectDir + File.separator + Application.projectTitleRenamed + File.separator+"static";
	
	// source paths
	private String resourcesSourceDir = Application.djangoProjectRootPath + File.separator+"src"+ File.separator + "resources";
	// ftl template source
	private String templateDir = resourcesSourceDir + File.separator + "freemarkertemplates";	
	// html template source
	private String djangoTemplateDir = resourcesSourceDir  +File.separator+"djangotemplates";
	// dynamic html template source
	private String dynamicTemplateDir = resourcesSourceDir  +File.separator+"dynamictemplates";	
	// static resources source
	private String staticSourceDir = resourcesSourceDir + File.separator + "staticdata" ;
	// custom code source
	private String customCodeDir = resourcesSourceDir  +File.separator+"customcode";	

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
		copyCustomCode();
		copyStaticTemplates();
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
		// Custom Tags
		generateWithProjectname(templatetagDir, CUSTOM_TAGS_PY, context);
		generateWithProjectname(templatetagDir,INIT_PY_DEFAULT_FTL,INIT_PY,context);	
		
	}

	protected void generateStaticFiles() throws IOException{		
		File srcDir = new File(staticSourceDir);
		File tempDestDir = new File(staticDestDir);		
		FileUtils.copyDirectory(srcDir, tempDestDir);		
	}
	
	protected void generateTemplates() throws IOException{	
		
		String srcDirString = dynamicTemplateDir;
		File srcDir = new File(dynamicTemplateDir);		
		// clean up previously generated files
		FileUtils.cleanDirectory(srcDir);
		
		File tempDestDir = new File(moduleDir +File.separator+"templates");		

		DjangoSubMenu mainMenu = djangoAdapter.getMenuList();
		context.clear();
		context.put("menu", mainMenu);
		context.put("projectname", Application.projectTitleRenamed);
		
		// generate navbar
		generateWithProjectname(srcDirString,NAVBAR_HTML, context);

		// generate list views
		List<DjangoPanel> panels = djangoAdapter.getPanels();
		Map panelNameMap = djangoAdapter.getPanelNameMap();
		
		for (DjangoPanel panel : panels){
			context.clear();
			
			context.put("projectname", Application.projectTitleRenamed);
			context.put("menu", mainMenu);
			context.put("panel", panel);
			context.put("panelNameMap", panelNameMap);
			generateWithProjectname(srcDirString,"list.ftl",panel.getName() + "_list.html", context);
			generateWithProjectname(srcDirString,"newObject.ftl",panel.getName() + "_new.html", context);
			generateWithProjectname(srcDirString,"object.ftl",panel.getName() + ".html", context);

		}
		
		//generate parentChild
		List<AdaptParentChildPanel> parentChildPanels = DataContainer.getInstance().getParentChildPanel();
		
		for (AdaptParentChildPanel adaptParentChildPanel : parentChildPanels) {
			context.clear();
			
			int p1Level = adaptParentChildPanel.getPanels().get(0).getLevel();
			int p2Level = adaptParentChildPanel.getPanels().get(1).getLevel();
			
			AdaptStandardPanel parentPanel = null;
			AdaptStandardPanel childPanel = null;
			
			//TODO check again. not sure what does the higher of lower values mean
			if(p1Level < p2Level){
				parentPanel = adaptParentChildPanel.getPanels().get(0);
				childPanel = adaptParentChildPanel.getPanels().get(1);
			}else{
				parentPanel = adaptParentChildPanel.getPanels().get(1);
				childPanel = adaptParentChildPanel.getPanels().get(0);
			}
			
			context.put("projectname", Application.projectTitleRenamed);
			context.put("menu", mainMenu);
			context.put("panel", adaptParentChildPanel);
			context.put("parentPanel", parentPanel);
			context.put("childPanel", childPanel);
			generateWithProjectname(srcDirString,"parentChild.ftl", adaptParentChildPanel.getName() + "_pc.html", context);
		}
		
		FileUtils.copyDirectory(srcDir, tempDestDir);	
	}	

	private void copyCustomCode() throws IOException {
		File srcDir = new File(customCodeDir + File.separator + CUSTOM_CODE_PY);		
		File tempDestDir = new File(moduleDir);		

		FileUtils.copyFileToDirectory(srcDir, tempDestDir);
		
	}
	
	protected void copyStaticTemplates() throws IOException{
		File srcDir = new File(djangoTemplateDir);		
		File tempDestDir = new File(moduleDir +File.separator+"templates");		

		FileUtils.copyDirectory(srcDir, tempDestDir);
	}
	public void generateViewsPy()throws IOException{

		List<DjangoPanel> panels = djangoAdapter.getPanels();
		List<AdaptParentChildPanel> pcPanels = DataContainer.getInstance().getParentChildPanel();
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();
		Map<String, String> panelClassMap = djangoAdapter.getPanelClassMap();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();
		
		context.clear();
		context.put("classes", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);
		context.put("description", Settings.APP_DESCRIPTION);	
		context.put("urls", djangoAdapter.getDjangoUrls());		
		context.put("panels", panels);
		context.put("pcPanels", pcPanels);
		context.put("classnameModelMap", classnameModelMap);	
		context.put("panelClassMap", panelClassMap);					
		
		context.put("models", djangoModelList);
		context.put("enumerations", enumerations);
		
		Map<String, String> pcForeingKeyMap = new HashMap<String, String>();
		
		if(pcPanels.size() > 0){
			for (AdaptParentChildPanel adaptParentChildPanel : pcPanels) {

				int p1Level = adaptParentChildPanel.getPanels().get(0).getLevel();
				int p2Level = adaptParentChildPanel.getPanels().get(1).getLevel();

				AdaptStandardPanel parentPanel = null;
				AdaptStandardPanel childPanel = null;

				DjangoModel parentModel = null;
				DjangoModel childModel = null;

				//TODO check again. not sure what does the higher of lower values mean
				if(p1Level < p2Level){
					parentPanel = adaptParentChildPanel.getPanels().get(0);
					childPanel = adaptParentChildPanel.getPanels().get(1);
				}else{
					parentPanel = adaptParentChildPanel.getPanels().get(1);
					childPanel = adaptParentChildPanel.getPanels().get(0);
				}

				for (DjangoModel model : djangoModelList) {
					if(model.getLabel().equals(parentPanel.getLabel())){
						parentModel = model;
					}
					if(model.getLabel().equals(childPanel.getLabel())){
						childModel = model;
					}
				}

				DjangoModelField fieldToSend = null;

				for (DjangoModelField field : childModel.getFieldsList()) {
					if(field.getEntryTypesEnum() == EntryTypesEnum.FOREIGNKEY){
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
		//TODO add ejb-url map
		
		generateWithProjectname(moduleDir,VIEWS_PY, context);
	}

	public void generateFormsPy()throws IOException{
		DjangoSubMenu mainMenu = djangoAdapter.getDefaultMenu();
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();
		List<DjangoPanel> panels = djangoAdapter.getPanels();
		
		context.clear();
		context.put("forms", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);
		context.put("classnameModelMap", classnameModelMap);
		context.put("menu", mainMenu);
		context.put("models", djangoModelList);
		context.put("enumerations", enumerations);
		context.put("panels", panels);
		
		generateWithProjectname(moduleDir,FORMS_PY, context);
	}

	public void generateURLsPy()throws IOException{

		List<DjangoPanel> panels = djangoAdapter.getPanels();
		List<AdaptParentChildPanel> pcPanels = DataContainer.getInstance().getParentChildPanel();
		
		context.clear();		
		context.put("imports", new ArrayList<String>());
		context.put("urls", djangoAdapter.getDjangoUrls());
		context.put("panels", panels);		
		context.put("parentChildPanels", pcPanels);
		context.put("projectname", Application.projectTitleRenamed);
		context.put("modulename", MODULE_NAME);
		generateWithProjectname(moduleDir,URLS_PY, context);
	}

	public void generateModelsPy()throws IOException{	
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();
		
		context.clear();
		context.put("enumerations", enumerations);
		context.put("models", djangoModelList);
		context.put("classnameModelMap", classnameModelMap);

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
		FileUtils.deleteDirectory(new File(projectDir));		
		createFolder(Paths.get(projectConfigDestDir));
		createFolder(Paths.get(moduleDir));		
		createFolder(Paths.get(templatetagDir));	
	}

	private void createFolder(Path folderToCreate) throws IOException{
		if (!folderToCreate.toFile().exists())
			if (!folderToCreate.toFile().mkdirs()) {
				throw new IOException("Error during folder creation."
						+ folderToCreate);
			}
	}
}
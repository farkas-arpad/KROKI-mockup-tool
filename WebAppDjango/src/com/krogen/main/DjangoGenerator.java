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

import com.krogen.model.django.DjangoUrl;
import com.krogen.model.menu.AdaptSubMenu;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Parse xmls and Generate python code
 */
public class DjangoGenerator {


	// 	file name constants
	public static String MANAGE_PY = "manage";
	public static String WSGI_PY = "wsgi";
	public static String MODELS_PY = "models";
	public static String VIEWS_PY = "views";
	public static String FORMS_PY = "forms";
	public static String URLS_PY = "urls";
	public static String SETTINGS_PY = "settings";

	public Configuration cfg;

	// data paths
	private String projectDir =  Application.appRootPath + File.separator+"generated";
	private String templateDir = Application.appRootPath + File.separator+"src"+ File.separator+"com"+ File.separator + "krogen" + File.separator + "templates";
	private String staticSourceDir = Application.appRootPath + File.separator+"src"+ File.separator+"com"+ File.separator + "krogen" + File.separator + "resources"+ File.separator + "staticdata" ;
	private String djangoResources = Application.appRootPath + File.separator+"src"+ File.separator+"com"+ File.separator + "krogen" + File.separator + "resources";
	private String destDir = projectDir + File.separator+Application.projectTitleRenamed+File.separator+Application.projectTitleRenamed;
	private String staticDestDir = projectDir + File.separator+Application.projectTitleRenamed+ File.separator+"static";

	private Template template;
	Map<String, Object> context = new HashMap<String, Object>();

	//

	public DjangoGenerator() throws IOException {


		cfg = new Configuration();		
		try {
			cfg.setDirectoryForTemplateLoading(new File(templateDir));
		} catch (IOException e) {
			throw new IOException("Template folder not exists" + templateDir , e);
		}		
	}

	public void generate() throws IOException{
		generateBasicFolderStructure();
		generateInitPy();	
		generateManagePy();
		generateWsgiPy();
		generateModelsPy();
		generateFormsPy();
		generateViewsPy();
		generateURLsPy();
		generateSettingsPy();
		generateStaticFiles();
		generateTemplates();

	}

	protected void generateStaticFiles() throws IOException{		
		File srcDir = new File(staticSourceDir);
		File tempDestDir = new File(staticDestDir);		
		FileUtils.copyDirectory(srcDir, tempDestDir);		
	}
	protected void generateTemplates() throws IOException{		
		File srcDir = new File(djangoResources  +File.separator+"djangotemplates");
		File tempDestDir = new File(destDir +File.separator+"templates");		
		FileUtils.copyDirectory(srcDir, tempDestDir);		
	}
	/**
	 * Copy the empty __init.py file 
	 * @throws IOException
	 */
	protected void generateInitPy() throws IOException{
		File file = new File(destDir + File.separator+"__init__.py");
		if (!file.exists())
			file.createNewFile();

	}

	public void generateViewsPy()throws IOException{
		AdaptSubMenu mainMenu = AppCache.getInstance().getDefaultMenu();

		context.clear();
		context.put("classes", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("menu", mainMenu);

		generateWithProjectname(destDir,VIEWS_PY, context);
	}

	public void generateFormsPy()throws IOException{
		AdaptSubMenu mainMenu = AppCache.getInstance().getDefaultMenu();

		context.clear();
		context.put("classes", new ArrayList<String>());
		context.put("projectname", Application.projectTitleRenamed);
		context.put("menu", mainMenu);

		generateWithProjectname(destDir,FORMS_PY, context);
	}
	public void generateSettingsPy()throws IOException{
		context.clear();
		context.put("projectname", Application.projectTitleRenamed);

		generateWithProjectname(destDir,SETTINGS_PY, context);
	}
	public void generateURLsPy()throws IOException{
		// TODO 
		// get the links from the menu system??
		// get he links from the forms list
		AppCache cache = AppCache.getInstance();
		List<DjangoUrl> urls = new ArrayList<DjangoUrl>();
		Map<String, String> panels =AppCache.getInstance().getPanelClassMap();


		for (Map.Entry<String, String> entry : panels.entrySet())
		{
			System.out.println(entry.getKey() + "/" + entry.getValue());
			urls.add(new DjangoUrl(entry.getValue(),entry.getValue()));
		}

		context.clear();		
		context.put("imports", new ArrayList<String>());
		context.put("urls", urls);
		context.put("projectname", Application.projectTitleRenamed);

		generateWithProjectname(destDir,URLS_PY, context);
	}

	private void creatDjangoUrls(AdaptSubMenu menu){

		new ArrayList<DjangoUrl>();

	}
	public void generateModelsPy()throws IOException{
		context.clear();
		context.put("classes", new ArrayList<String>());

		generateWithProjectname(destDir,MODELS_PY, context);
	}
	/**
	 * Generate the manage.py file in the appropriate folder
	 * The only property needed to add here is the project name
	 * @throws IOException
	 */
	public void generateManagePy() throws IOException{
		context.clear();
		context.put("projectname", Application.projectTitleRenamed);

		generateWithProjectname(projectDir+File.separator+Application.projectTitleRenamed,MANAGE_PY, context);		
	}

	/**
	 * Generate the wsgi.py file in the appropriate folder
	 * The only property needed to add here is the project name
	 * @throws IOException
	 */
	public void generateWsgiPy() throws IOException{
		context.clear();
		context.put("projectname", Application.projectTitleRenamed);

		generateWithProjectname(destDir,WSGI_PY,context);		
	}

	/**
	 * Internal code generator code
	 * Used to create the two base files manage.py and wsgi.py
	 * @param templateName
	 * @throws IOException
	 */
	private void generateWithProjectname(String path, String templateName, Map<String, Object> context) throws IOException{
		try{
			// set basic configuration
			template = cfg.getTemplate(templateName +".ftl");
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			// put data to context
			Writer out = new OutputStreamWriter(new FileOutputStream(path+File.separator+templateName +".py"));
			if (out != null) {
				template.process(context, out);
				out.flush();
			}					
		}
		catch (IOException e) {
			throw new IOException("Missing template  " + templateName + ".",
					e);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates the basic folder structure for the Django project
	 * @throws IOException
	 */
	protected void generateBasicFolderStructure() throws IOException{
		Path folderToCreate = Paths.get(destDir);

		if (!folderToCreate.toFile().exists())
			if (!folderToCreate.toFile().mkdirs()) {
				throw new IOException("Greska pri kreiranju izlaznog direktorijuma "
						+ destDir);
			}
	}
}

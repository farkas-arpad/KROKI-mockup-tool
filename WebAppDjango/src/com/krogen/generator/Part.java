package com.krogen.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.krogen.constants.DjangoConstants;
import com.krogen.model.django.DjangoAdapter;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Generic representation parts which creates the django application
 */
public abstract class Part {

	protected Map<String, Object> context;

	protected Template template;
	protected Configuration cfg;

	protected DjangoAdapter djangoAdapter = new DjangoAdapter();
	
	public Part() {
		context = new HashMap<String, Object>();
		context.clear();
	}

	public abstract void generate() throws Exception;

	/**
	 * Internal code generator code Used to create the two base files manage.py
	 * and wsgi.py
	 * 
	 * @param templateName
	 * @throws IOException
	 */
	protected void generateWithProjectname(String path, String destinationFileName, Map<String, Object> context)
			throws IOException {
		String templateName = destinationFileName.substring(0, destinationFileName.lastIndexOf(".")) + ".ftl";
		generateWithProjectname(path, templateName, destinationFileName, context);
	}

	/**
	 * Used to generate files using freemarker
	 * 
	 * @param path - destination path
	 * @param templateName
	 * @param destinationFileName
	 * @param context
	 * @throws IOException
	 */
	protected void generateWithProjectname(String path, String templateName, String destinationFileName,
			Map<String, Object> context) throws IOException {
		Writer out = null;
		try {
			// set basic configuration
			cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setDirectoryForTemplateLoading(new File(DjangoConstants.templateDir));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);  
			template = cfg.getTemplate(templateName);
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			// put data to context
			out = new OutputStreamWriter(new FileOutputStream(path + File.separator + destinationFileName));
			if (out != null) {
				template.process(context, out);
				out.flush();
			}
		} catch (IOException e) {
			throw new IOException("Missing template  " + templateName, e);
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
	}
}

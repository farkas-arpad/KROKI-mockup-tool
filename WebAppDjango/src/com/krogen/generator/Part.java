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

/**
 * Generic representation parts which creates the django application
 */
public abstract class Part {

	protected Map<String, Object> context = new HashMap<String, Object>();

	protected Template template;
	protected Configuration cfg;

	protected DjangoAdapter djangoAdapter = new DjangoAdapter();

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
			cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(DjangoConstants.templateDir));
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

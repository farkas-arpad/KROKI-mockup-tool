package com.krogen.generator.parts;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * copying of static template files
 */
public class StaticTemplatePart extends Part {

	public StaticTemplatePart() {
		super();
	}
	
	@Override
	public void generate() throws Exception {
		File srcDir = new File(DjangoConstants.djangoTemplateDir);
		File tempDestDir = new File(DjangoConstants.moduleDir + File.separator + "templates");

		FileUtils.copyDirectory(srcDir, tempDestDir);
	}

}

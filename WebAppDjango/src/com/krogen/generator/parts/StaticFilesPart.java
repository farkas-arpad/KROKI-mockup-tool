package com.krogen.generator.parts;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * copying of static files
 */
public class StaticFilesPart extends Part {

	@Override
	public void generate() throws Exception {
		File srcDir = new File(DjangoConstants.staticSourceDir);
		File tempDestDir = new File(DjangoConstants.staticDestDir);		
		FileUtils.copyDirectory(srcDir, tempDestDir);	
	}

}

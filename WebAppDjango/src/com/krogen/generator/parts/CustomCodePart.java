package com.krogen.generator.parts;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of custom made code
 */
public class CustomCodePart extends Part {

	public CustomCodePart() {
		super();
	}
	
	@Override
	public void generate() throws Exception {
		File srcDir = new File(DjangoConstants.customCodeDir + File.separator + DjangoConstants.CUSTOM_CODE_PY);		
		File tempDestDir = new File(DjangoConstants.moduleDir);		

		FileUtils.copyFileToDirectory(srcDir, tempDestDir);
	}

}

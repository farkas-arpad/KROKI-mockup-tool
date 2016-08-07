package com.krogen.generator.parts;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;

public class HomePyPart extends Part {
	
	String srcDirString = DjangoConstants.djangoTemplateDir;
	
	@Override
	public void generate() throws Exception {
		
		context.clear();
		
		generateWithProjectname(srcDirString, "home.ftl", "home.html", context);

	}

}

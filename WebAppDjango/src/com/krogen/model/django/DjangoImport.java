package com.krogen.model.django;

public class DjangoImport {

	String module;
	String subModule;
		
	public DjangoImport(String module, String subModule) {
		super();
		this.module = module;
		this.subModule = subModule;
	}

	public String print(){		
		return "from "+module+" import "+subModule +"\n";
	}
	
}

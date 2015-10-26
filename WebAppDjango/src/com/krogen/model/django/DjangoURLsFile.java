package com.krogen.model.django;

import java.util.ArrayList;
import java.util.List;

public class DjangoURLsFile {

	List<DjangoImport> imports = new ArrayList<DjangoImport>();
	List<DjangoUrl> urls = new ArrayList<DjangoUrl>();

	public DjangoURLsFile() {		
		super();
		imports.add(new DjangoImport("django.conf.urls","patterns, include, url"));
		imports.add(new DjangoImport("ticket","views"));
		imports.add(new DjangoImport("django.contrib","admin"));
		
	}
}

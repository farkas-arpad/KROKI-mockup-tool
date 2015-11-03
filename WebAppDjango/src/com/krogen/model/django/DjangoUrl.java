package com.krogen.model.django;

public class DjangoUrl {

	String pattern;
	String view;
	
	public DjangoUrl(String pattern, String view) {
		super();
		this.pattern = pattern;
		this.view = view;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	} 
	
	
	 
}

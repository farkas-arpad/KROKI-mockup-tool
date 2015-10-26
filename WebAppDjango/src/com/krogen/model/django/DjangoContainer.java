package com.krogen.model.django;

import java.util.List;

public class DjangoContainer {

	static DjangoContainer instance = null;
	List<DjangoUrl> djangoUrlList;
	
	/**
	 * Singleton constructor.
	 * @return existing instance or creates new if none exist.
	 */
	public static DjangoContainer getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new DjangoContainer();
			return instance;
		}
	}
	
	
	public void translateData(){
		
	}
	
	public void writeData(){
		
	}
	
}

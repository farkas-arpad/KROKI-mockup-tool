package com.krogen.model.menu;

import java.util.ArrayList;

public class DjangoSubMenu extends DjangoMenu {
	
	private String name;
	private ArrayList<DjangoMenu> children;
	private ArrayList<String> roles;
	
	public DjangoSubMenu(String name) {
		this.name = name;
		this.children = new ArrayList<DjangoMenu>();
		this.roles = new ArrayList<String>();
	}
	
	public DjangoSubMenu() {
		this.children = new ArrayList<DjangoMenu>();
		this.roles = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<DjangoMenu> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<DjangoMenu> children) {
		this.children = children;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	
}

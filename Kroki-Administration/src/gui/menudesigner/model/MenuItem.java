package gui.menudesigner.model;

public class MenuItem extends Menu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3109657186274102017L;
	private String formName;
	private String menuName;
	private String activate;
	private String panelType;
	
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getPanelType() {
		return panelType;
	}
	public void setPanelType(String panelType) {
		this.panelType = panelType;
	}
	
	@Override
	public String toString() {
		return menuName;
	}

}

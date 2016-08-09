package com.krogen.model.panel;

import java.util.ArrayList;
import java.util.List;

public class AdaptParentChildPanel extends DjangoPanel {

	protected AdaptStandardPanel parentPanel;
	protected List<AdaptStandardPanel> childPanels = new ArrayList<AdaptStandardPanel>();
	
	public void addChildPanel(AdaptStandardPanel spanel) {
		childPanels.add(spanel);
	}
	
//	public AdaptStandardPanel findByLevel(Integer level) {
//		Iterator<AdaptStandardPanel> it = panels.iterator();
//		while(it.hasNext()) {
//			AdaptStandardPanel panel = it.next();
//			if(panel.getLevel().intValue() == level.intValue()) {
//				return panel;
//			}
//		}
//		return null;
//	}

	public List<AdaptStandardPanel> getChildPanels() {
		return childPanels;
	}

	public void setChildPanels(List<AdaptStandardPanel> childPanel) {
		this.childPanels = childPanel;
	}
	
	public void setParentPanel(AdaptStandardPanel parentPanel){
		this.parentPanel = parentPanel;
	}
	
	public AdaptStandardPanel getParentPanel(){
		return parentPanel;
	}
	
}

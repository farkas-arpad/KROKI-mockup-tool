package com.krogen.main;

import com.krogen.repository_utils.RepositoryPathsUtil;
import com.krogen.static_names.Settings;

public class Application {

	public static String PYTHON_PATH = "C:\\Python34\\Python.exe";	
	public static String projectTitleRenamed =   Settings.APP_TITLE.replace(" ", "_");

	public static String repositoryPath = RepositoryPathsUtil.getRepositoryRootPath();
	public static String appRootPath = RepositoryPathsUtil.getAppRootPath();

	
	public static void main(String[] args) {
		LogJFrame mainFrame = new LogJFrame();		
	}
}

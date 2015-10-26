package com.krogen.repository_utils;

import java.io.File;

public class RepositoryPathsUtil {

	/**
	 * Searches for the RepositoryRootPath
	 * @return
	 */
	public static String getRepositoryRootPath() {
		File f = new File(".");
		String tempPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator));
		File tempFile = new File(tempPath + File.separator + "ApplicationRepository");

		while(!tempFile.exists()){

			tempPath = tempPath.substring(0, tempPath.lastIndexOf(File.separator));		
			tempFile = new File(tempPath + File.separator + "ApplicationRepository");

		}
		return tempPath + File.separator + "ApplicationRepository";
	}

	public static String getAppRootPath() {
		File f = new File(".");
		return f.getAbsolutePath().substring(0,f.getAbsolutePath().lastIndexOf(File.separator));
	}

	public static String getStaticRepositoryPath() {
		return getRepositoryRootPath() + File.separator + "static";
	}

	public static String getGeneratedRepositoryPath() {
		return getRepositoryRootPath() + File.separator + "generated";
	}

	public static String getGeneratedModelPath() {
		return getGeneratedRepositoryPath() + File.separator + "model";
	}

	public static String getStaticModelPath() {
		return getStaticRepositoryPath() + File.separator + "model";
	}
}
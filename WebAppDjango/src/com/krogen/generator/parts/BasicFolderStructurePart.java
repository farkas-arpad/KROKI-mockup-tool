package com.krogen.generator.parts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of basic folder structure
 */
public class BasicFolderStructurePart extends Part {

	/**
	 * Creates the basic folder structure for the Django project
	 * 
	 * @throws IOException
	 */
	@Override
	public void generate() throws IOException {
		FileUtils.deleteDirectory(new File(DjangoConstants.projectDir));
		createFolder(Paths.get(DjangoConstants.projectConfigDestDir));
		createFolder(Paths.get(DjangoConstants.moduleDir));
		createFolder(Paths.get(DjangoConstants.templatetagDir));
	}

	private void createFolder(Path folderToCreate) throws IOException {
		if (!folderToCreate.toFile().exists())
			if (!folderToCreate.toFile().mkdirs()) {
				throw new IOException("Error during folder creation." + folderToCreate);
			}
	}

}

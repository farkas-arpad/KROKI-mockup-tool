package com.krogen.generator;

/**
 * Generating python code from parts. Each individual part of the Django
 * application will be generated in the respective {@link Part} file.
 */
public class DjangoGenerator {

	public void generate(Part partToGenerate) throws Exception {
		partToGenerate.generate();
	}

}
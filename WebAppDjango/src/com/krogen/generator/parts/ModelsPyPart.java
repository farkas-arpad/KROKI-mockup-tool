package com.krogen.generator.parts;

import java.util.List;
import java.util.Map;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.enumeration.Enumeration;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of models.py file
 */
public class ModelsPyPart extends Part {

	@Override
	public void generate() throws Exception {
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();

		context.clear();
		context.put("enumerations", enumerations);
		context.put("models", djangoModelList);
		context.put("classnameModelMap", classnameModelMap);

		generateWithProjectname(DjangoConstants.moduleDir, DjangoConstants.MODELS_PY, context);
	}
}

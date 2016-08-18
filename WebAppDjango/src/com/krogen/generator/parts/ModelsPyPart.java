package com.krogen.generator.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krogen.constants.DjangoConstants;
import com.krogen.generator.Part;
import com.krogen.model.django.modelpy.DjangoModel;
import com.krogen.model.django.modelpy.DjangoModelField;
import com.krogen.model.enumeration.Enumeration;

/**
 * Concrete implementation of a {@link Part} responsible for the
 * generation of models.py file
 */
public class ModelsPyPart extends Part {

	public ModelsPyPart() {
		super();
	}
	
	@Override
	public void generate() throws Exception {
		List<DjangoModel> djangoModelList = djangoAdapter.getModelList();
		List<Enumeration> enumerations = djangoAdapter.getEnumerations();
		Map<String, String> classnameModelMap = djangoAdapter.getClassnameModelMapping();
		
		//fill a map with the list of representative fields to handle them easier in the ftl
		Map<String, List<DjangoModelField>> representativeFieldMap = new HashMap<String, List<DjangoModelField>>();
		for (DjangoModel model : djangoModelList) {
			representativeFieldMap.put(model.getName(), new ArrayList<DjangoModelField>());
			
			for (DjangoModelField field : model.getFieldsList()) {
				if(field.getRepresentative()){
					representativeFieldMap.get(model.getName()).add(field);
				}
			}
		}
		
		context.clear();
		context.put("enumerations", enumerations);
		context.put("models", djangoModelList);
		context.put("classnameModelMap", classnameModelMap);
		context.put("representativeFieldMap", representativeFieldMap);

		generateWithProjectname(DjangoConstants.moduleDir, DjangoConstants.MODELS_PY, context);
	}
}

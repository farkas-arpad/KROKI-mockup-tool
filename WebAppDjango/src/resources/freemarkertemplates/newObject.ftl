   {% extends "base.html" %}
  {% load custom_tags %}
  
  {% block head_title%} {{block.super}} {% endblock %}
  
  {% block jumbotron_content %}
		<form class="form-horizontal" method="post" action="">	        			
	 	{% csrf_token%}
	 		<fieldset>
				<legend>${panel.entityBean.label}</legend>
				<div class="form-group">
					{% for field in ${panel.entityBean.name}Form %}
					{{ field.errors }}           		
           			<div class="col-sm-3"> {{ field.label_tag }}</div> 
           			<div class="col-sm-9">
           			{% if nexts|lookup:field.name != "" %}<div class="input-group">{% endif %} 
           			{{ field }} 
           			{% if nexts|lookup:field.name != "" %}
           			 <a class="input-group-addon" href="{% url nexts|lookup:field.name %}">
           			 <span class=" glyphicon glyphicon-share" aria-hidden="true">
           			 </span>
           			 </a>
           			 {% endif %} 
           			 {% if nexts|lookup:field.name != "" %}</div> {% endif %} 
           			 </div>
        			{% endfor %}
        		</div>
        		<div class="form-group">
					<div class="col-sm-9 col-sm-offset-3">
					{% if ${panel.name}.id == None %}
						<a class="btn btn-sm btn-default" href="{% url '${panel.name}_list' %}">Back</a>
					{% else %}
					<a class="btn btn-sm btn-default" href="{% url '${panel.name}' ${panel.name}.id %}">Back</a>
					{% endif %}
						<button type="submit" class="btn btn-sm btn-primary">Submit</button>
					</div>
				</div>
			</fieldset>
		</form>  
      
  {% endblock %}
  
  
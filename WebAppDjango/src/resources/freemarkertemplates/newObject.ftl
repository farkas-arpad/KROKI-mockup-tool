  {% extends "base.html" %}
  {% load crispy_forms_tags %}
  
  {% block head_title%} {{block.super}} {% endblock %}
  
  {% block jumbotron %}
	<div class="jumbotron">      
		<div class="row">
		<form class="form-horizontal" method="post" action="">	        			
	 	{% csrf_token%}
	 		<fieldset>
				<legend>${panel.entityBean.label}</legend>
				<div class="form-group">
					{% for field in ${panel.entityBean.name}Form %}
					{{ field.errors }}           		
           			<div class="col-sm-2"> {{ field.label_tag }}</div> <div class="col-sm-10">{{ field }} </div>
        			{% endfor %}
        		</div>
        		<div class="form-group">
					<div class="col-sm-10 col-sm-offset-2">
					{% if ${panel.name}.id == None %}
						<a class="btn btn-default" href="{% url '${panel.name}_list' %}">Back</a>
					{% else %}
					<a class="btn btn-default" href="{% url '${panel.name}' ${panel.name}.id %}">Back</a>
					{% endif %}
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</div>
			</fieldset>
		</form> 
        </div>
    </div>
      
  {% endblock %}
  
  
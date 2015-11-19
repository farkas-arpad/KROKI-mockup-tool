  {% extends "base.html" %}
  {% load crispy_forms_tags %}
  
  {% block head_title%} {{block.super}} {% endblock %}
  
  {% block jumbotron %}
   <!-- Main component for a primary marketing message or call to action -->
	<div class="jumbotron">      
		<div class="row">
			<div class="col-md-10"> 
			<form class="form-horizontal">	        			
	 		{% csrf_token%}
	 			<fieldset>
					<legend>${panel.entityBean.label}</legend>
					<div class="form-group">
						{% for field in ${panel.entityBean.name}Form %}
           				<div class="col-sm-2"> {{ field.label_tag }}</div> <div class="col-sm-10">{{ field }} </div>
        				{% endfor %}
        			</div>
        		</fieldset>
        	</form> 
        		<div class="col-sm-10 col-sm-offset-2">
        		 <a class="btn btn-default" href="{% url '${panel.name}_list' %}"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"> Back</span>	</a>													
					{% if editable == "true" %}
         			<form action="{% url '${panel.name}_edit' ${panel.name}_id %}" method="POST">
        		    {% csrf_token %}
					<input type="submit" class="btn btn-primary" value="Edit" />
           			</form>
           {% endif %}
           </div>
        </div>
    </div>
      
  {% endblock %}
  
  <!--
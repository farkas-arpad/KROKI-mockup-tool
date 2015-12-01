  {% extends "base.html" %}
  
  {% block head_title%} {{block.super}} {% endblock %}
  
  {% block jumbotron_content %}
  
 {% if messages %}
  		<div class="alert alert-success fade in">
  	<a href="#" class="close" data-dismiss="alert">&times;</a>
  
			{% for message in messages %}
    		{{ message }}
    		{% endfor %}
		</div>
	{% endif %}
	
   		<form class="form-horizontal">	        			
	 		{% csrf_token%}
	 			<fieldset>
					<legend>${panel.entityBean.label}</legend>
					<div class="form-group">
						{% for field in ${panel.entityBean.name}Form %}
           				<div class="col-sm-3"> {{ field.label_tag }}</div> <div class="col-sm-9">{{ field }} </div>
        				{% endfor %}
        			</div>
        		</fieldset>
        		<div class="form-group">
        			<div class="col-sm-9 col-sm-offset-3">
        				<a class="btn btn-sm btn-primary" href="{% url '${panel.name}_edit' ${panel.name}_id %}"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Submit</a>			
        			</div>
           		</div> 
        </form>         

      
  {% endblock %}
  {% extends "base.html" %}
  
  {% block head_title%} {{block.super}} {% endblock %}
  
  {% block page_title %} ${panel.entityBean.label} {% endblock %}
  {% block jumbotron_content %}
   <#assign special_functions = false> 
   <#if (panel.nextPanels?? && panel.nextPanels?has_content)>
 	<#assign special_functions = true>
 	</#if>
 	
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
					<div class="form-group">
						{% for field in ${panel.entityBean.name}Form %}
           				<div class="col-sm-3"> {{ field.label_tag }}</div> <div class="col-sm-9">{{ field }} </div>
        				{% endfor %}
        			</div>
        		</fieldset>
        		<div class="form-group">
        			<div class="col-sm-9 col-sm-offset-3">
        				<a class="btn btn-sm btn-default" href="{% url '${panel.name}_list' %}"> Back</a>
        				{% if editable == "true" %}
         				<a class="btn btn-sm btn-primary" href="{% url '${panel.name}_edit' ${panel.name}_id %}"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Edit</a>			
        		 		{% endif %}																							           		
           			</div>
           		</div> 
        </form>         
{% endblock %}
      
  {% block extra_functions %}
  
   <#if special_functions == true>
   <div class="col-md-3">	
   <#if panel.nextPanels?has_content >
   <h4>Lists of interest:</h4>   		
   <#list panel.nextPanels as nextFormUrl>
   <div class="row">
         <#if panelNameMap[nextFormUrl.panelId]??> 	
        	 <a class="btn-group btn-sm btn btn-primary" href="{% url '${nextFormUrl.panelId}_list' %}"> <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> ${panelNameMap[nextFormUrl.panelId]}</a>
       	 </#if>  	 
   </div>      
   </#list>    
   </#if>
   </#if>
   {% endblock %}
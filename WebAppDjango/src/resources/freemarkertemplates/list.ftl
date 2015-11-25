  {% extends "base.html" %}
  {% load crispy_forms_tags %}
  
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
	
  		       <fieldset>
              <legend>${panel.entityBean.label}</legend>     
              <nav class="navbar navbar-default">   
               <div class="container-fluid">
               <div class="navbar-header">
               <div class="btn-group" role="group" aria-label="..."> <a class="btn btn-info navbar-btn" href="{% url '${panel.name}_new' %}"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> New ${panel.entityBean.label}</a>  
              	<a class="btn btn-info navbar-btn" href="{% url '${panel.name}_list' %}"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Refresh</a>  
              	</div>
              	</div>
              	</div>
              	</nav>                
                  <table class="table" id="example">   
                  <thead>
                    <tr>
                    <#list panel.entityBean.attributes as attribute>
           			<th>${attribute.label}</th>
        			</#list>
        			</tr>
        			</thead>           			        
                    {% for ${panel.name} in ${panel.name}s %}
                  <tr>
                    <#list panel.entityBean.attributes as attribute>
                    <#if attribute.enumeration?? >
           			<td>{{ ${panel.name}.get_${attribute.fieldName}_display }}</td>
           			<#else>
           			<td>{{ ${panel.name}.${attribute.fieldName} }}</td>      
           			</#if>     			
        			</#list>
        			<td>        				
        			 <a class="btn btn-default" href="{% url '${panel.name}' ${panel.name}.id %}"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"> Details</span>	</a>					
						</td>
                       <td> {% if deletable == "true" %}
                         <form action="{% url '${panel.name}_delete' ${panel.name}.id %}" method="POST">
                         {% csrf_token %}
                        <button class="btn btn-danger" type="submit">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"> Delete</span>
						</button>
						</form>
						</td>
						
                         {% endif %}
                       </tr>                
                    {% endfor %}       
                    </table>                 
              </fieldset>       
  {% endblock %}
  
  {% block next_forms %}
     <#list panel.nextPanels as nextFormUrl>
        	 <a class="btn btn-info" href="{% url '${nextFormUrl.panelId}_list' %}"> <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> List of ${panelNameMap[nextFormUrl.panelId]} </a>      
     </#list>    		
  
  {% endblock %}
  
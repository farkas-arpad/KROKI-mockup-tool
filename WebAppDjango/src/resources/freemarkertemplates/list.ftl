  {% extends "base.html" %}  
  {% block head_title %} ${projectname} :: ${panel.entityBean.label} {% endblock %}
  
  {% block page_title %} ${panel.entityBean.label} {% endblock %}
  
  {% block jumbotron_content %}
 
  <#assign special_functions = false> 
   <#if (panel.nextPanels?? && panel.nextPanels?has_content) || (panel.standardOperations.operations??  && panel.standardOperations.operations?has_content)>
 	<#assign special_functions = true>
 	</#if>
 	
 	<#if special_functions == true>
 		<div class="col-md-9">		
 	<#else>
 		<div class="col-md-12">
 	</#if>
  	{% if messages %}
  		<div class="alert alert-success fade in">
  			<a href="#" class="close" data-dismiss="alert">&times;</a>
			{% for message in messages %}
    		{{ message }}
    		{% endfor %}
		</div>
	{% endif %}
<fieldset>
	 <nav class="navbar navbar-default">   
    	<div class="container-fluid">
        	<div class="navbar-header">
            	<div class="btn-group" role="group" aria-label="...">
                	{% if addable == "true" %}
                	<div class="col-md-6"> 
              		    <a class="btn btn-primary btn-sm navbar-btn" href="{% url '${panel.name}_new' %}"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> New ${panel.entityBean.label}</a>
              		</div>    
               		{% endif %}  
               		<div class="col-md-6"> 
              		    <a class="btn btn-primary btn-sm navbar-btn" href="{% url '${panel.name}_list' %}"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Refresh</a>
              		</div>  
              	</div>
            </div>
        </div>
    </nav>  
	
	<div class="dataTable_wrapper">	
			<table id="dataTables-example" class="table table-striped table-bordered table-hover" width="100%">  
				<thead>
					<tr>
						<#list panel.entityBean.attributes as attribute>
							<th style="min-width: 50px">${attribute.label}</th>
						</#list>
						<th></th>
						<th></th>
					</tr>						
				</thead>   
				<tfoot>
					<tr>
						<#list panel.entityBean.attributes as attribute>
							<th>${attribute.label}</th>
						</#list>
						<th></th>
						<th></th>
					</tr>					
				</tfoot>
			<tbody>				
				{% for ${panel.name} in ${panel.name}s %}
				<tr class="clickable-row" data-href="{% url '${panel.name}' ${panel.name}.id %}">
					<#list panel.entityBean.attributes as attribute>
					<#if attribute.enumeration?? >
					<td>{{ ${panel.name}.get_${attribute.fieldName}_display }}</td>
					<#else>
					<td>{{ ${panel.name}.${attribute.fieldName} }}</td>      
					</#if>     			
					</#list>
					<td class="center">        				
						<a class="btn btn-sm btn-default" href="{% url '${panel.name}' ${panel.name}.id %}"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Details</a>					
					</td>
					<td class="center">   
					{% if deletable == "true" %}                                        
						<form action="{% url '${panel.name}_delete' ${panel.name}.id %}" method="POST">
						{% csrf_token %}
							<button class="btn btn-sm btn-danger" type="submit">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete
							</button>
						</form>												
						{% endif %}
						</td>
					</tr>                
					{% endfor %}     
			</tbody>					
			</table> 
	</div>
	
</fieldset> 
</div>    
  {% endblock %}
  
  {% block extra_functions %}
  
   <#if special_functions == true>
   <div class="col-md-3">	
   <#if panel.nextPanels?has_content >
   <h4>Lists of interest:</h4>   		
   <#list panel.nextPanels as nextFormUrl>
   <div class="row">
         <#if panelNameMap[nextFormUrl.panelId]??> 	
        	 <p><a class="btn-group btn-sm btn btn-primary" href="{% url '${nextFormUrl.panelId}_list' %}"> <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> ${panelNameMap[nextFormUrl.panelId]}</a></p>
       	 </#if>  	 
   </div>      
   </#list>    
   </#if>
   
   <#if panel.standardOperations.operations?has_content ==true >   	
  <h4>Operations:</h4>
   <#list panel.standardOperations.operations as operation>
    <div class="row">         	
         	 <#if operation.type == 'VIEWREPORT'> 
         	<p> <a class="btn-group btn-sm btn btn-primary" href="{% url '${panel.name}_${operation.name}'%}">
         	 	<span class="glyphicon glyphicon-book" aria-hidden="true"></span> ${operation.label}
         	 </a></p>
         	 <#elseif operation.type == 'BUSINESSTRANSACTION'> 
         	 	<#if operation.parameters?has_content> 
         	 	       		 <p><a class="btn-group btn-sm btn btn-primary" href="{% url '${panel.name}_${operation.name}'%}"><span class="glyphicon glyphicon-briefcase" aria-hidden="true"></span> ${operation.label}</a></p>        
         	 	<#else>
         	 	<form action="{% url '${panel.name}_${operation.name}'%}" method="POST">
                   	{% csrf_token %}
                    	<button class="btn btn-sm btn-primary" type="submit"><span class="glyphicon glyphicon-briefcase" aria-hidden="true"></span> ${operation.label}</button>
					</form>	
         	  	</#if>         	 	
         	 </#if>   
   </div>
   </#list> 
   </#if>
   <#if panel.nextPanels?has_content >
   <h4>Additional operations:</h4>   		
   <#list panel.nextPanels as nextFormUrl>
   <div class="row">
         <#if panelNameMap[nextFormUrl.panelId]?? == false> 	      
       	  	 <p><a class="btn-group btn-sm btn btn-primary" href="{% url '${nextFormUrl.label}' %}"> <span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span> ${nextFormUrl.label}</a></p>     
         </#if>  	 
   </div>      
   </#list> 
   </#if> 
    </div>
   </#if>    
   
  {% endblock %}
  {% block js_extra %}
  {% load staticfiles %}
	<script src="{% static 'custom/list.js'%}"></script>  

  {% endblock %}
  
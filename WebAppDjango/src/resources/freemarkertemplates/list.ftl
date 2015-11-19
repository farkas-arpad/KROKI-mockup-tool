  {% extends "base.html" %}
  {% load crispy_forms_tags %}
  
  {% block head_title%} {{block.super}} {% endblock %}
  
  {% block jumbotron %}
   <!-- Main component for a primary marketing message or call to action -->
	<div class="jumbotron">      
	

   		       <fieldset>
              <legend>${panel.entityBean.label}</legend>        
              <div class="panel panel-default">
 			 <div class="panel-body">
              	 <a class="btn btn-large btn-info" href="{% url '${panel.name}_new' %}"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> New ${panel.entityBean.label}</a>  
              	<a class="btn btn-large btn-info" href="{% url '${panel.name}_list' %}"><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span> Refresh </a>  
             
              	 </div>
              	 </div>     
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
           			<td>{{ ${panel.name}.${attribute.fieldName} }}</td>
        			</#list>
        			<td>        				
        			 <a class="btn btn-default" href="{% url '${panel.name}' ${panel.name}.id %}"><span class="glyphicon glyphicon-pencil" aria-hidden="true"> Details</span>	</a>					
						</td>
                       <td> {% if deletable == "true" %}
                         <form action="{% url '${panel.name}_delete' ${panel.name}.id %}" method="POST">
                         {% csrf_token %}
                        <button class="btn btn-default" type="submit">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"> Delete</span>
						</button>
						</form>
						</td>
						
                         {% endif %}
                       </tr>                
                    {% endfor %}       
                    </table>                 
              </fieldset>
        </div>         
  {% endblock %}
  
  
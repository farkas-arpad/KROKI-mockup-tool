  {% extends "base.html" %}  
  {% block head_title %} ${projectname} :: ${panel.label} {% endblock %}
  
{% block jumbotron_content %}
    <div class="col-md-12">

    {% if messages %}
        <div class="alert alert-success fade in">
            <a href="#" class="close" data-dismiss="alert">&times;</a>
            {% for message in messages %}
            {{ message }}
            {% endfor %}
        </div>
    {% endif %}
    
    <!--parent panel-->
    <fieldset>
        <legend>${parentPanel.entityBean.label}</legend>     
        
        <nav class="navbar navbar-default">   
            <div class="container-fluid">
                <div class="navbar-header">
                    <div class="btn-group" role="group" aria-label="...">
                        {% if parentParams.addable == "true" %}
                        <div class="col-md-6"> 
                            <a class="btn btn-primary btn-sm navbar-btn" href="{% url '${parentPanel.name}_new' %}"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> New ${parentPanel.entityBean.label}</a>
                        </div>
                        {% endif %}  
                    </div>
                </div>
            </div>
        </nav>  
                                   
        <table class="table" id="example">   
            <thead>
                <tr>
                    <#list parentPanel.entityBean.attributes as attribute>
                        <th>${attribute.label}</th>
                    </#list>
                </tr>
            </thead>
                                        
            {% for ${parentPanel.name} in parentParams.${parentPanel.name}s %}
                <tr>
                
                    <#list parentPanel.entityBean.attributes as attribute>
                        <#if attribute.enumeration?? >
                            <td>{{ ${parentPanel.name}.get_${attribute.fieldName}_display }}</td>
                        <#else>
                            <td>{{ ${parentPanel.name}.${attribute.fieldName} }}</td>      
                        </#if>              
                    </#list>

                    <td>                        
                        <a class="btn btn-sm btn-default" href="{% url '${panel.name}'%}?id={{${parentPanel.name}.id}}"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Details</a>                   
                    </td>
                
                    <td>  
                        {% if parentParams.deletable == "true" %}                                        
                            <form action="{% url '${parentPanel.name}_delete' ${parentPanel.name}.id %}" method="POST">
                                {% csrf_token %}
                                <button class="btn btn-sm btn-danger" type="submit">
                                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete
                                </button>
                            </form>                                             
                        {% endif %}
                    </td>
                    
                </tr>                
            {% endfor %}
                       
        </table>                 
    </fieldset> 
</div>    
<!--//parent panel-->

<!--child panel-->
<div class="col-md-12">
    <fieldset>
        <legend>${childPanel.entityBean.label}</legend>  
           
        <nav class="navbar navbar-default">   
            <div class="container-fluid">
                <div class="navbar-header">
                    <div class="btn-group" role="group" aria-label="...">
                        {% if childParams.addable == "true" %}
                            <div class="col-md-6"> 
                                <a class="btn btn-primary btn-sm navbar-btn" href="{% url '${childPanel.name}_new' %}"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> New ${childPanel.entityBean.label}</a>
                            </div>
                        {% endif %}
                        <div class="col-md-6">   
                            <a class="btn btn-primary btn-sm navbar-btn" href="{% url '${panel.name}'%}?id="><span class="glyphicon" aria-hidden="true"></span> Show All </a>
                        </div>
                    </div>
                </div>
            </div>
        </nav> 
                                    
        <table class="table" id="example">   
            <thead>
                <tr>
                    <#list childPanel.entityBean.attributes as attribute>
                        <th>${attribute.label}</th>
                    </#list>
                </tr>
            </thead>
                                        
            {% for ${childPanel.name} in childParams.${childPanel.name}s %}
                <tr>
                
                    <#list childPanel.entityBean.attributes as attribute>
                        <#if attribute.enumeration?? >
                            <td>{{ ${childPanel.name}.get_${attribute.fieldName}_display }}</td>
                        <#else>
                            <td>{{ ${childPanel.name}.${attribute.fieldName} }}</td>      
                        </#if>              
                    </#list>

                    <td>                        
                        <a class="btn btn-sm btn-default" href="{% url '${childPanel.name}' ${childPanel.name}.id %}"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span> Details</a>                   
                    </td>
                    
                    <td>   
                        {% if deletable == "true" %}                                        
                            <form action="{% url '${childPanel.name}_delete' ${childPanel.name}.id %}" method="POST">
                                {% csrf_token %}
                                <button class="btn btn-sm btn-danger" type="submit">
                                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete
                                </button>
                            </form>                                             
                        {% endif %}
                    </td>
                </tr>                
                {% endfor %}       
        </table>                 
</fieldset> 
</div>  
<!--//child panel-->
{% endblock %}
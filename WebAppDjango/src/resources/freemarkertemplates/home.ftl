  {% extends "base.html" %}
  
  {% block head_title %} {{ projectname }} :: Dashboard {% endblock %}
  
  {% block page_title %} Dashboard {% endblock %}
  
  {% block jumbotron_content %}
{% for entity in list %}
    <div class="col-lg-3 col-md-6">
    <div class="panel panel-green">
        <div class="panel-heading">
            <div class="row">
                <div class="col-xs-3">
                    <i class="fa fa-tasks fa-5x"></i>
                </div>
                <div class="col-xs-9 text-right">
                    <div class="huge">{{entity.entity_size}}</div>
                    <div>{{entity.entity_name}}</div>
                </div>
            </div>
        </div>
        <a href="{% url entity.entity_url %}">
            <div class="panel-footer">
            <span class="pull-left">View Details</span>
            <span class="pull-right">
                <i class="fa fa-arrow-circle-right"></i>
            </span>
            <div class="clearfix"></div>
            </div>
        </a>
    </div>
  </div>
 {% endfor %}  
 
 {% endblock %}
 
 {% block js_extra %}
 <script>
	$('#side-menu li:nth-child(1) a:first-child').addClass('active');
 </script>
 {% endblock %}
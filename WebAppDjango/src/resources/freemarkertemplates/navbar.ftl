<!-- Static navbar -->
    <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">{{ projectname }}</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">          
            {% if request.user.is_authenticated %}
            <#list menu.children as submenu>
			<#if submenu.name??>				
				<#if submenu.children?has_content>
					<li class="dropdown">
            			<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${submenu.name} <span class="caret"></span></a>
              			<ul class="dropdown-menu">
							<@menugenerator menuList=submenu.children/>
						</ul>
					</li>
				</#if>
			<#else>
			<li><a href="#">${submenu.menuName}</a></li>           				
			</#if>			
			</#list>
            {% endif %}
          </ul>
          <ul class="nav navbar-nav navbar-right">
           {% if request.user.is_authenticated %}
            <li class="dropdown">
				<a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> <span class="glyphicon glyphicon-user" aria-hidden="true"></span>  {{ user.username }} <span class="caret"></span></a>
					<ul class="dropdown-menu">
				<li><a href="{% url 'logout' %}">
				Logout</a>
				</li>
				</ul>
				</li>           
          {% else %}
             <li class='active'> <a href="{% url 'login' %}">Login</a></li>
               {% endif %}
          
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

<#macro menugenerator menuList>
        <#if menuList??>
				<#foreach child in menuList>
					<#if child.name??>						
							<#if child.children?has_content>
							<li class="dropdown-submenu">
							<a tabindex="-1" href="#">${child.name}</a>
								<ul class="dropdown-menu">
								<@menugenerator menuList=child.children/>
								</ul>
							</li>
							</#if>							
					<#else>
					   <li><a href="{% url '${child.activate}_list' %}">${child.menuName}</a></li>
					</#if>
				</#foreach>			
        </#if>
</#macro>
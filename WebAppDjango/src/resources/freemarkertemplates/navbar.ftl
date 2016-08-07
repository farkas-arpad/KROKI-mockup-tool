		<!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/{{ projectname }}">{{ projectname }}</a>
			</div>
			<!-- /.navbar-header -->
			
			<!-- Top Menu Items -->
			<ul class="nav navbar-top-links navbar-right">
            {% if request.user.is_authenticated %}
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<i class="fa fa-user fa-fw"></i> {{ user.username }} <i class="fa fa-caret-down"></i></a>
					<ul class="dropdown-menu dropdown-user">
						<li><a href="{% url 'logout' %}">
						<i class="fa fa-sign-out fa-fw"></i> Logout</a>
						</li>
					</ul>
				</li>           
			{% else %}
             <li class='active'> <a href="{% url 'login' %}"> Login</a></li>
               {% endif %}
          
			</ul>
		<!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
		  
        <div class="navbar-default sidebar" role="navigation">
			<div class="sidebar-nav navbar-collapse">
				<ul class="nav" id="side-menu">										
				{% if request.user.is_authenticated %}
				<li>
					<a href="/{{ projectname }}"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
				</li>
				<#list menu.children as submenu>
				<#if submenu.name??>				
					<#if submenu.children?has_content>
						<li>	
							<a href="#"><i class="fa fa-table fa-fw"></i> ${submenu.name}<span class="fa arrow"></span></a>																				
							<ul class="nav nav-second-level">
								<@menugenerator menuList=submenu.children/>
							</ul>
						</li>
					</#if>
				<#else>
				<li><a href="{% url '${submenu.activate}' %}">${submenu.menuName}</a></li>           				
				</#if>			
				</#list>
				{% endif %}
				</ul>
			</div>
        </div>
		<!--/.nav-collapse -->      
    </nav>

<#macro menugenerator menuList>
        <#if menuList??>
				<#foreach child in menuList>
					<#if child.name??>						
							<#if child.children?has_content>
							<li>
							<a href="#">${child.name}<span class="fa arrow"></span></a>
								<ul class="nav nav-third-level">
								<@menugenerator menuList=child.children/>
								</ul>
							</li>
							</#if>							
					<#else>
						<#if child.menuName == 'Separator'>
							<li role="separator" class="divider"></li>
						<#else>
							<#if child.panelType == 'PARENTCHILDPANEL'>
								<li><a href="{% url '${child.activate}' %}">${child.menuName}</a></li>
							<#else>
					  			<li><a href="{% url '${child.activate}_list' %}">${child.menuName}</a></li>
					  		</#if>
					  	</#if>
					</#if>
				</#foreach>			
        </#if>
</#macro>
from django.conf.urls import patterns, include, url
from ${projectname} import views
<#list imports as import>
from ${import.module} import ${import.submodule}
</#list>

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # login / logout
    
    <#list urls as url>
    url(${url.pattern}, ${url.view}),   
    </#list>   
 	url(r'^test', views.testdefault),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^login/$', 'django.contrib.auth.views.login'),
    url(r'^logout/$', 'django.contrib.auth.views.logout'),
)

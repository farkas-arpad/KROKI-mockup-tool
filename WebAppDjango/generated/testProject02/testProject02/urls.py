from django.conf.urls import patterns, include, url
from django.conf.urls.static import static

from testProject02 import views

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # login / logout
    
    url(r'^$', 'testProject02.views.index'),
 	url(r'^test', views.testdefault),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^login/$', 'testProject02.views.login_user'),
    url(r'^logout/$', 'django.contrib.auth.views.logout'),
) 

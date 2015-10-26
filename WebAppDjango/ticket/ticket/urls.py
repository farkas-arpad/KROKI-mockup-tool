from django.conf.urls import patterns, include, url
from ticket import views

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'ticket.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
 	url(r'^test', views.testdefault),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^login/$', 'django.contrib.auth.views.login'),
    url(r'^logout/$', 'django.contrib.auth.views.logout'),
)

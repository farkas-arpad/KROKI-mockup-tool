from django.db import models

# Generated by KROKI
# http://www.kroki-mde.net/

from django.contrib.auth.models import User
    
class Login(models.Model):
	username = models.CharField(max_length = 60, blank=True, null=False)
	password = models.CharField(max_length = 60, blank=True, null=False)
	
	def __str__(self):
		return self.username
		
<#list classes as class>
class ${class.name}(models.Model):
<#list class.fields as field>
	${field.name} = field.type
</#list>
</#list>

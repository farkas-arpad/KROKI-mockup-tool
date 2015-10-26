from django.db import models

<#list classes as class>
class ${class.name}(models.Model):
<#list class.fields as field>
	${field.name} = field.type
</#list>
</#list>
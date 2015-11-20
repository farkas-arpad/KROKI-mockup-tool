from django.db import models
from django.core.urlresolvers import reverse

# Generated by KROKI
# http://www.kroki-mde.net/

from django.contrib.auth.models import User
    
class Login(models.Model):
	username = models.CharField(max_length = 60, blank=True, null=False)
	password = models.CharField(max_length = 60, blank=True, null=False)
	
	def __str__(self):
		return self.username

class Article(models.Model):
    title = models.CharField(max_length=200)
    pub_date = models.DateField()

    def get_absolute_url(self):
        return reverse('article-detail', kwargs={'pk': self.pk})
        		
class Author(models.Model):
    name = models.CharField(max_length=200)

    def get_absolute_url(self):
        return reverse('author-detail', kwargs={'pk': self.pk})
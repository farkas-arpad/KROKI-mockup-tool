# Generated by KROKI
# http://www.kroki-mde.net/

from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.shortcuts import render_to_response, render
from django.contrib.auth import authenticate, login, logout
from django.template import RequestContext
from django.contrib.auth.decorators import login_required
from .forms import LoginForm 

# homepage view
@login_required(login_url='/login/')
def index(request):
	projectname = "${projectname}"
	context = {
		"projectname": projectname 
	}
	return render(request,'home.html', context)

# logout function	
def logout_user(request):
	form = LoginForm()
	context = {
		"projectname": "${projectname}",
		"description": "${description}", 	
		"title": 'Login',
		"form" : form
    }
    
	logout(request)
	return render(request,'login.html', context)     

def auth_view(request):
    username = request.POST.get('username', '')
    password = request.POST.get('password', '')
    user = auth.authenticate(username = username, password = password)      

    if user is not None:
        auth.login(request, user)
        return HttpResponseRedirect(reverse('home'))
    else:
        return HttpResponseRedirect('/accounts/invalid')
            
def testdefault(request):
	return HttpResponse("Hello, world. You're at the test view.")

def login_user(request):   
	form = LoginForm()
	context = {
		"projectname": "${projectname}",
		"description": "${description}",  	
		"title": 'Login',
		"form" : form
    }
	username = password = ''
	if request.POST:
		username = request.POST['username']
		password = request.POST['password']

		user = authenticate(username=username, password=password)
		if user is not None:
			if user.is_active:
				login(request, user)
				return HttpResponseRedirect(reverse('index'))
	return render(request,'login.html', context)
	

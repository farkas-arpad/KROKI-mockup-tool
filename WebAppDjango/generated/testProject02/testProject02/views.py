from django.http import HttpResponse
from django.shortcuts import render_to_response
from django.contrib.auth import authenticate, login
from django.shortcuts import render

def index(request):
	projectname = 'testProject02'
	context = {
		"projectname": projectname 
	}
	return render(request,'base.html', context)


def testdefault(request):
	return HttpResponse("Hello, world. You're at the test view.")

def login_user(request):
	title = "Welecome"
	context = {
	"title": title
	}

	return render(request,'base.html', context)
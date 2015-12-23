
from django.http import HttpResponse

# test code for customCode         
def custom(request):
    return HttpResponse("Hello, world. You're at the test view.")

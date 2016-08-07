
# Generated by KROKI
# http://www.kroki-mde.net/

from django.core.urlresolvers import reverse
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render_to_response, render, redirect
from django.template import RequestContext
from django.db.models import Q
from reportlab.pdfgen import canvas
from django.db import connection
from reportlab.pdfgen import canvas
from io import BytesIO
from reportlab.platypus.doctemplate import SimpleDocTemplate
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import Paragraph, Spacer, Table, TableStyle
from reportlab.lib import colors

#generated imports
from ${modulename}.models import <#list models as model> ${model.name}<#if model_has_next == true>,</#if></#list>
from ${modulename}.forms import LoginForm<#list models as model>, ${model.name}Form, ${model.name}FormReadOnly</#list>

# homepage view
@login_required(login_url='login/')
def index(request):

    list = []
<#list panels as panel>
    entity_name = '${panel.entityBean.name}'
    entity_size = ${panel.entityBean.name}.objects.all().count()
    entity_url = '${panel.name}_list'
    list.append({'entity_name':entity_name, 'entity_size':entity_size, 'entity_url':entity_url})
</#list>    

    context = {
        "projectname": "${projectname}",
        "description": "${description}",
        "list": list,
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
 
# test link for sanity checks            
def testdefault(request):
	return HttpResponse("Hello, world. You're at the test view.")
	
# login function
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
			else:
				messages.error(request, "Sorry, that login was invalid. Please try again.")
		else:
			messages.error(request, "Sorry, that login was invalid. Please try again.")
	return render(request,'login.html', context)

<#list panels as panel>
@login_required(login_url='/${projectname}/login/')
def ${panel.name}_list(request): # panel_id
    context = RequestContext(request)
    ${panel.name}s = ${panel.entityBean.name}.objects.all()    # modelname.objects.all()
    return render_to_response('${panel.name}_list.html',{'addable' : '${panel.panelSettings.add}', 'deletable' : "${panel.panelSettings.delete}", "${panel.name}s" : ${panel.name}s,"projectname" : "${projectname}"},context)

@login_required(login_url='/${projectname}/login/')
def ${panel.name}_search(request):
	if request.method == 'POST':
		query = request.POST['q']
		if query == '':
			${panel.name}s = ${panel.entityBean.name}.objects.all()
		else:
		<#assign not_first = false>
			${panel.name}s = ${panel.entityBean.name}.objects.filter(<#list panel.entityBean.attributes as attribute><#if attribute.lookupClass?? == false && attribute.hidden == false><#if not_first == true> | </#if>Q(${attribute.name}=query)<#assign not_first = true></#if></#list>)	
	else:
		${panel.name}s = ${panel.entityBean.name}.objects.all()
	context = RequestContext(request)    
	return render_to_response('${panel.name}_list.html',{'addable' : '${panel.panelSettings.add}', 'deletable' : "${panel.panelSettings.delete}", "${panel.name}s" : ${panel.name}s,"projectname" : "${projectname}"},context)

@login_required(login_url='/${projectname}/login/')
def ${panel.name}(request, ${panel.name}_id):
	context = RequestContext(request)
	${panel.name}_form = ${panel.entityBean.name}FormReadOnly(instance=${panel.entityBean.name}.objects.get(pk=${panel.name}_id))	
	
	<#list panel.entityBean.attributes as attribute>
	<#if attribute.lookupClass??>
	# list of foreign keys
	${attribute.name}s = ${classnameModelMap[attribute.lookupClass]}.objects.all()
	</#if>
	</#list>
	return render_to_response('${panel.name}.html',{'${panel.entityBean.name}Form':  ${panel.name}_form, '${panel.name}_id': ${panel.name}_id, 'editable' : 'true', "projectname" : "${projectname}"<#list panel.entityBean.attributes as attribute><#if attribute.lookupClass??>, '${attribute.name}s' : ${attribute.name}s</#if></#list>}, context)

# render empty form 	
# if new object added redirect to listing
# if there is an error message display
@login_required(login_url='/${projectname}/login/')
def ${panel.name}_new(request):

	context = RequestContext(request)
	if request.method == 'POST':
		${panel.name}_form = ${panel.entityBean.name}Form(data=request.POST)
		if ${panel.name}_form.is_valid():
			${panel.name} = ${panel.name}_form.save()
			${panel.name}.save()
			messages.success(request,'Data successfully saved.');
			
			return redirect('${panel.name}_list')
		# previous workflow:	
		#	return render_to_response('${panel.name}_list.html',{"${panel.entityBean.name}Form" : ${panel.name}_form, "projectname" : "${projectname}"},context)    
		else: 
			messages.error(request, ${panel.name}_form.errors)
	else:
		${panel.name}_form = ${panel.entityBean.name}Form()
		<#assign lookup = false> 
		<#list panel.entityBean.attributes as attribute>		
		<#if attribute.lookupClass??>
		<#assign lookup = true> 
		
		# list of foreign keys
		${attribute.name}s = ${classnameModelMap[attribute.lookupClass]}.objects.all()
		</#if>
		</#list>
		<#if lookup == true>
		nexts = {		
		<#list panel.entityBean.attributes as attribute><#if attribute.lookupClass??>'${attribute.name}':${panelClassMap[attribute.lookupClass]}_list,</#if></#list>
		}
		</#if>
		
	return render_to_response('${panel.name}_new.html', {'${panel.entityBean.name}Form': ${panel.name}_form, "projectname" : "${projectname}"<#list panel.entityBean.attributes as attribute><#if attribute.lookupClass??>, '${attribute.name}s' : ${attribute.name}s, 'nexts' : nexts </#if></#list>}, context)

@login_required(login_url='/${projectname}/login/')   
def ${panel.name}_edit(request, ${panel.name}_id):
	context = RequestContext(request)
	${panel.name}FromDB = ${panel.entityBean.name}.objects.get(pk=${panel.name}_id)
	if request.method == 'POST':
		${panel.name}_form = ${panel.entityBean.name}Form(request.POST, instance=${panel.name}FromDB)
		if ${panel.name}_form.is_valid():
			${panel.name} = ${panel.name}_form.save()
			${panel.name}.save()
			
			<#list panel.entityBean.attributes as attribute>
			<#if attribute.lookupClass??>
			# list of foreign keys
			${attribute.name}s = ${classnameModelMap[attribute.lookupClass]}.objects.all()
			</#if>
			</#list> 
			messages.success(request,'Data successfully updated.');
			${panel.name}_form = ${panel.entityBean.name}FormReadOnly(instance=${panel.name}FromDB)	         
			return render_to_response('${panel.name}.html', {'${panel.entityBean.name}Form': ${panel.name}_form,'${panel.name}_id': ${panel.name}_id, 'editable' : 'true', "projectname" : "${projectname}"<#list panel.entityBean.attributes as attribute><#if attribute.lookupClass??>,'${attribute.name}s' : ${attribute.name}s</#if></#list>},context)
		else: 
			messages.error(request, ${panel.name}_form.errors)
			
	${panel.name}_form = ${panel.entityBean.name}Form(instance=${panel.name}FromDB)
	return render_to_response('${panel.name}_new.html', {'${panel.entityBean.name}Form': ${panel.name}_form,'${panel.name}_id': ${panel.name}_id, "projectname" : "${projectname}",}, context)

@login_required(login_url='/${projectname}/login/')   
def ${panel.name}_delete(request, ${panel.name}_id):
	context = RequestContext(request)
	${panel.name} = ${panel.entityBean.name}.objects.get(pk=${panel.name}_id)
	if request.method == 'POST':
		${panel.name}.delete();
        
	${panel.name}s = ${panel.entityBean.name}.objects.all()    
	return render_to_response('${panel.name}_list.html',{'addable' : '${panel.panelSettings.add}','deletable' : "${panel.panelSettings.delete}", "${panel.name}s" : ${panel.name}s, "projectname": "${projectname}"},context)
	
<#if panel.standardOperations.operations?has_content >   
<#list panel.standardOperations.operations as operation>
<#if operation.type == 'BUSINESSTRANSACTION'>
@login_required(login_url='/${projectname}/login/')  
def ${panel.name}_${operation.name}(request):
	if request.method == 'GET':
			print("To be implemented")
		# prepare form if there are attributes
	if request.method == 'POST':
		# set form if the proper attributes are set'
		<#if operation.parameters?has_content>		
		if ${panel.name}_${operation.name}_form.is_valid():
			c = connection.cursor()
			try:
				c.execute("BEGIN")
				c.callproc("${operation.name}", (<#list operation.parameters as parameter> </#list>))
				results = c.fetchall()
				c.execute("COMMIT")
			finally:
				c.close()
			print results
			messages.success(request,'Stored procedure results:' + str(results).strip('[]'));		
			return redirect('${panel.name}_list')
		<#else>
		c = connection.cursor()
		try:
			c.execute("BEGIN")
			c.callproc("${operation.name}")
			results = c.fetchall()
			c.execute("COMMIT")
		finally:
			c.close()
		print (results)
		messages.success(request,'Stored procedure results:' + str(results).strip('[]'));
		return redirect('${panel.name}_list')
		</#if>

<#else>
@login_required(login_url='/${projectname}/login/')  
def ${panel.name}_${operation.name}(request):
	# Create the HttpResponse object with the appropriate PDF headers.
	response = HttpResponse(content_type='application/pdf')  
	response['Content-Disposition'] = 'attachment; filename="${panel.name}_Report.pdf"'
	
	buffer = BytesIO()
	
	styles = getSampleStyleSheet()
	doc = SimpleDocTemplate(buffer)
	Catalog = []
	header = Paragraph("Report of ${panel.name}", styles['Heading1'])
	Catalog.append(header)
	style = styles['Normal']
	headings = (<#list panel.entityBean.attributes as attribute>'${attribute.label}'<#if attribute_has_next == true>,</#if></#list>)
	allproducts = [(<#list panel.entityBean.attributes as attribute><#if attribute.enumeration?? >p.get_${attribute.fieldName}_display<#else>p.${attribute.fieldName}</#if><#if attribute_has_next == true>,</#if></#list>) for p 
											in ${panel.entityBean.name}.objects.all()]
	t = Table([headings] + allproducts)
	t.setStyle(TableStyle(
               [('LINEBELOW', (0,0), (-1,0), 2, colors.red),
                ('BACKGROUND', (0, 0), (-1, 0), colors.pink)]))
	Catalog.append(t) 
	doc.build(Catalog)	
	
	pdf = buffer.getvalue()
	buffer.close()
	response.write(pdf)
	return response
</#if>
</#list> 
</#if>
</#list>

<#list pcPanels as pcPanel>
@login_required(login_url='/${projectname}/login/')  
def ${pcPanel.name}(request):
    context = RequestContext(request)
    
    id = request.GET.get('id', '')
     
    <#if pcPanel.panels[0].level < pcPanel.panels[1].level>
    ${pcPanel.panels[0].name}s = ${pcPanel.panels[0].entityBean.name}.objects.all()
    
    if id != '':
        #order_sts = ORDER.objects.filter(order_payment_2=id)
        ${pcPanel.panels[1].name}s = ${pcPanel.panels[1].entityBean.name}.objects.filter(${pcForeingKeyMap[pcPanel.label]}=id)
    else:
        ${pcPanel.panels[1].name}s = ${pcPanel.panels[1].entityBean.name}.objects.all()
    
    parentParams = {'addable' : '${pcPanel.panels[0].panelSettings.add}', 'deletable' : "${pcPanel.panels[0].panelSettings.delete}", "${pcPanel.panels[0].name}s" : ${pcPanel.panels[0].name}s}
    childParams = {'addable' : '${pcPanel.panels[1].panelSettings.add}', 'deletable' : "${pcPanel.panels[1].panelSettings.delete}", "${pcPanel.panels[1].name}s" : ${pcPanel.panels[1].name}s}
    <#else>
    ${pcPanel.panels[1].name}s = ${pcPanel.panels[1].entityBean.name}.objects.all()
    
    if id != '':
        ${pcPanel.panels[0].name}s = ${pcPanel.panels[0].entityBean.name}.objects.filter(${pcForeingKeyMap[pcPanel.label]}=id)
    else:
        ${pcPanel.panels[0].name}s = ${pcPanel.panels[0].entityBean.name}.objects.all()
    
    parentParams = {'addable' : '${pcPanel.panels[1].panelSettings.add}', 'deletable' : "${pcPanel.panels[1].panelSettings.delete}", "${pcPanel.panels[1].name}s" : ${pcPanel.panels[1].name}s}
    childParams = {'addable' : '${pcPanel.panels[0].panelSettings.add}', 'deletable' : "${pcPanel.panels[0].panelSettings.delete}", "${pcPanel.panels[0].name}s" : ${pcPanel.panels[0].name}s}
    </#if>
    
    #parentParams = {'addable' : '${pcPanel.panels[0].panelSettings.add}', 'deletable' : "${pcPanel.panels[0].panelSettings.delete}", "${pcPanel.panels[0].name}s" : ${pcPanel.panels[0].name}s}
    #childParams = {'addable' : '${pcPanel.panels[1].panelSettings.add}', 'deletable' : "${pcPanel.panels[1].panelSettings.delete}", "${pcPanel.panels[1].name}s" : ${pcPanel.panels[1].name}s}
    
    #return render_to_response('${pcPanel.name}_list.html', {"parentParams" : parentParams, "childParams": "childParams", "projectname" : "${projectname}"},context)
    return render_to_response('${pcPanel.name}_pc.html', {"parentParams" : parentParams, "childParams": childParams, "projectname" : "${projectname}"},context)

    #context = RequestContext(request)
    #return redirect('localhost:8000/youtube')
</#list>
	
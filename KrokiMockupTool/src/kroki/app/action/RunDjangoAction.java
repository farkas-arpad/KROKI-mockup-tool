package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.krogen.main.Application;
import com.krogen.main.DjangoGenerator;
import com.krogen.repository_utils.RepositoryPathsUtil;
import com.krogen.xmlParsers.MainParser;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ExportProjectToEclipseUML;
import kroki.app.export.ProjectExporter;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.ImageResource;
import kroki.app.utils.RunAnt;
import kroki.app.utils.StringResource;
import kroki.app.utils.uml.KrokiComponentOutputMessage;
import kroki.profil.subsystem.BussinesSubsystem;

/**
 * Action that runs selected project as a Django web application
 * @author Arpad Farkas
 */
public class RunDjangoAction extends AbstractAction {

	public RunDjangoAction() {
		putValue(NAME, "Run django web version");
		putValue(SMALL_ICON, new ImageIcon(ImageResource.getImageResource("action.rundjango.smallicon")));
		putValue(LARGE_ICON_KEY, new ImageIcon(ImageResource.getImageResource("action.rundjango.largeicon")));
		putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.rundjango.description"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		Thread thread = new Thread(new Runnable() {
			public void run() {

				BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
				
				if(proj == null) {
					//if no project is selected, inform user to select one
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				
				try{
					KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
					
					//TODO have no clue what this part does...
					if(proj.getEclipseProjectPath() != null) {
						if(!FileChooserHelper.checkDirectory(proj)) {
							KrokiMockupToolApp.getInstance().displayTextOutput("The selected project has associated Eclipse project path, but is seems to be missing or corrupted. Please review these settings in the project properties panel.", OutputPanel.KROKI_WARNING);
							return;
						}
					}
					
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					
					ProjectExporter exporter = new ProjectExporter(false);
					
					KrokiMockupToolApp.getInstance().displayTextOutput("Generating UML model...", OutputPanel.KROKI_RESPONSE);
//					exporter.getData(proj);
					exporter.generateAppAndRepo(proj, "Project exported OK!");
					exporter.writeProjectName(proj.getLabel(), proj.getProjectDescription());
					
//					File tempUMLFile = new File(tempDir.getAbsolutePath() + File.separator + jarName + ".uml");
//					new ExportProjectToEclipseUML(tempUMLFile, proj, true, true).exportToUMLDiagram(new KrokiComponentOutputMessage(), ExportProjectToEclipseUML.MESSAGES_FOR_CLASS, false);
//					exporter.export(tempDir, jarName, proj, "Project exported OK! Running project...");
					
				}catch(Exception e){
					
				}finally {
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				
				
				try {
					parseData();
					KrokiMockupToolApp.getInstance().displayTextOutput("Parsing finished", OutputPanel.KROKI_RESPONSE);
					initGenerator();
					KrokiMockupToolApp.getInstance().displayTextOutput("Application generated", OutputPanel.KROKI_RESPONSE);
					migration();
					KrokiMockupToolApp.getInstance().displayTextOutput("Data migration finished", OutputPanel.KROKI_RESPONSE);
					addAdminUser();
					KrokiMockupToolApp.getInstance().displayTextOutput("Default admin user added", OutputPanel.KROKI_RESPONSE);
					runApp();
					KrokiMockupToolApp.getInstance().displayTextOutput("Test server is running", OutputPanel.KROKI_RESPONSE);
				} catch (Exception e) {
					e.printStackTrace();
					KrokiMockupToolApp.getInstance().displayTextOutput("Error: " + e, OutputPanel.KROKI_ERROR);
				}
			}
		});
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
		
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void parseData(){
		MainParser parser = new MainParser();
		parser.parseData();	
	}

	private void initGenerator() throws IOException {
		DjangoGenerator generator = new DjangoGenerator();
		generator.generate();
	}

	private void migration() throws Exception {
		// starting up the app

//		String manageFile = Application.PYTHON_PATH + " " + Application.djangoProjectRootPath + File.separator + "generated" + File.separator + 
//				Application.projectTitleRenamed + File.separator + "manage.py migrate";

//		KrokiMockupToolApp.getInstance().displayTextOutput("Starting " + manageFile, OutputPanel.KROKI_RESPONSE);

		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.djangoProjectRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","migrate");
		processBuilder.redirectErrorStream(true);
		Process p = processBuilder.start();
	}

	public void addAdminUser() throws Exception {
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.djangoProjectRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","createsuperuser --username=admin --email=admin@example.com");
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.INHERIT);
		processBuilder.start();
	}

	public void runApp() throws Exception {
		// starting up the app
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.djangoProjectRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","runserver");
		processBuilder.redirectErrorStream(true);

		processBuilder.redirectOutput(Redirect.INHERIT);
		Process p = processBuilder.start();
		
		KrokiMockupToolApp.getInstance().displayTextOutput("Starting internal server on port 8000", OutputPanel.KROKI_RESPONSE);
	}
}

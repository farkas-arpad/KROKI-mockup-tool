package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.krogen.generator.DjangoGenerator;
import com.krogen.generator.parts.BasicFolderStructurePart;
import com.krogen.generator.parts.CustomCodePart;
import com.krogen.generator.parts.FormsPyPart;
import com.krogen.generator.parts.HomePyPart;
import com.krogen.generator.parts.ModelsPyPart;
import com.krogen.generator.parts.ProjectSettingsPart;
import com.krogen.generator.parts.StaticFilesPart;
import com.krogen.generator.parts.StaticTemplatePart;
import com.krogen.generator.parts.TemplatesPart;
import com.krogen.generator.parts.URLsPyPart;
import com.krogen.generator.parts.ViewPyPart;
import com.krogen.main.Application;
import com.krogen.xmlParsers.MainParser;

import kroki.app.KrokiMockupToolApp;
import kroki.app.export.ProjectExporter;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.FileChooserHelper;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
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

				//get the selected project
				BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
				
				//if no project is selected, inform user to select one
				if(proj == null) {
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				
				try{
					//logging onto the kroki app console
					KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", 0);
					
					//TODO have no clue what this part does...
					if(proj.getEclipseProjectPath() != null) {
						if(!FileChooserHelper.checkDirectory(proj)) {
							KrokiMockupToolApp.getInstance().displayTextOutput("The selected project has associated Eclipse project path, but is seems to be missing or corrupted. Please review these settings in the project properties panel.", OutputPanel.KROKI_WARNING);
							return;
						}
					}
					
					//change cursor to wait
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					
					//initialize the project exporter. false means it is not a swing app
					ProjectExporter exporter = new ProjectExporter(false);
					
					//logging onto the kroki app console
					KrokiMockupToolApp.getInstance().displayTextOutput("Generating UML model...", OutputPanel.KROKI_RESPONSE);
					
					exporter.generateDjangoRequirements(proj);
					//set the project name into the property files
					exporter.writeProjectName(proj.getLabel(), proj.getProjectDescription());
					
				}catch(Exception e){
					KrokiMockupToolApp.getInstance().displayTextOutput("Unexpected error: " + e, OutputPanel.KROKI_ERROR);
					e.printStackTrace();
				}finally {
					//change cursor back to normal
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				
				try {
					//parsing the data from the generated files
					parseData();
					//logging onto the kroki app console
					KrokiMockupToolApp.getInstance().displayTextOutput("Parsing finished", OutputPanel.KROKI_RESPONSE);
					generateDjangoProject();
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
		
		//change cursor back to normal
		KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	/**
	 * initialize the parser that reads the data from the files generated before
	 * @throws Exception 
	 */
	private void parseData() throws Exception{
		MainParser parser = new MainParser();
		parser.parseData();	
	}

	/**
	 * Initiates and starts the generation of the Django project
	 * @throws Exception 
	 */
	private void generateDjangoProject() throws Exception {
		DjangoGenerator generator = new DjangoGenerator();
		generator.generate(new BasicFolderStructurePart());
		generator.generate(new ProjectSettingsPart());
		generator.generate(new ModelsPyPart());
		generator.generate(new FormsPyPart());
		generator.generate(new ViewPyPart());
		generator.generate(new URLsPyPart());
		generator.generate(new StaticFilesPart());
		generator.generate(new HomePyPart());
		generator.generate(new TemplatesPart());
		generator.generate(new CustomCodePart());
		generator.generate(new StaticTemplatePart());
	}

	/**
	 * Try to make django migration if it is possible without user interaction or asks for further intructions
	 * @throws Exception
	 */
	private void migration() throws Exception {
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.djangoProjectRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","migrate");
		processBuilder.redirectErrorStream(true);
		processBuilder.start();
	}

	/**
	 * Creates a Django superuser
	 * 
	 * @throws Exception
	 */
	public void addAdminUser() throws Exception {
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.djangoProjectRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","createsuperuser --username=admin --email=admin@example.com");
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.INHERIT);
		processBuilder.start();
	}
	
	/**
	 * Starts the generated Django app
	 * 
	 * @throws Exception
	 */
	public void runApp() throws Exception {
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.djangoProjectRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","runserver");
		processBuilder.redirectErrorStream(true);

		processBuilder.redirectOutput(Redirect.INHERIT);
		processBuilder.start();
		
		KrokiMockupToolApp.getInstance().displayTextOutput("Starting internal server on port 8000", OutputPanel.KROKI_RESPONSE);
	}
}

package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.krogen.main.Application;
import com.krogen.main.DjangoGenerator;
import com.krogen.repository_utils.RepositoryPathsUtil;
import com.krogen.xmlParsers.MainParser;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;

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
		
		RepositoryPathsUtil.getDjangoProjectPath();

		Thread thread = new Thread(new Runnable() {
			public void run() {

				//TODO project name
				//				KrokiMockupToolApp.getInstance().displayTextOutput("Exporting project '" + proj.getLabel() + "'. Please wait...", OutputPanel.KROKI_RESPONSE);

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

		String manageFile = Application.PYTHON_PATH + " " + Application.djangoProjectRootPath + File.separator + "generated" + File.separator + 
				Application.projectTitleRenamed + File.separator + "manage.py makemigrations";

		KrokiMockupToolApp.getInstance().displayTextOutput("Starting " + manageFile, OutputPanel.KROKI_RESPONSE);

		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.appRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","makemigrations");
		processBuilder.redirectErrorStream(true);
		Process p = processBuilder.start();
	}

	public void addAdminUser() throws Exception {
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.appRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","createsuperuser --username=admin --email=admin@example.com");
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.INHERIT);
		processBuilder.start();
	}

	public void runApp() throws Exception {
		// starting up the app
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.appRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","runserver");
		processBuilder.redirectErrorStream(true);

		processBuilder.redirectOutput(Redirect.INHERIT);
		Process p = processBuilder.start();
		
		KrokiMockupToolApp.getInstance().displayTextOutput("Starting internal server on port 8000", OutputPanel.KROKI_RESPONSE);
	}
}

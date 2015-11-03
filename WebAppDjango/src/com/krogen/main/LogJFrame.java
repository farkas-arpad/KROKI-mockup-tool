package com.krogen.main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ProcessBuilder.Redirect;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.krogen.static_names.Settings;
import com.krogen.xml_readers.MenuReader;
import com.krogen.xml_readers.PanelReader;

/**
 * Parse xml, generate python code, start up the project 
 * @author Tihomir Turzai
 *
 */
public class LogJFrame extends JFrame {

	private JScrollPane scrollPane;
	private JTextPane statusPane;
	private Process process;
	private static final long serialVersionUID = 1L;

	public LogJFrame() {
		super();
		setTitle(Settings.APP_TITLE + " [Server log]");
		setMaximumSize(new Dimension(850, 400));
		setMinimumSize(new Dimension(850, 400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadIcon();	
		Application.setMainFrame(this);
		init();
	}	

	private void init() {
		scrollPane = new JScrollPane();
		statusPane = new JTextPane();
		statusPane.setEditable(false);
		statusPane.setFont(new Font("Monospaced",Font.PLAIN,12));
		statusPane.setForeground(Color.white);
		statusPane.setBackground(Color.black);

		LogJFrame.this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					// TODO stop application
					process.destroy();
					System.exit(ABORT);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
					System.exit(ABORT);
				}
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});

		scrollPane.setViewportView(statusPane);
		add(scrollPane, BorderLayout.CENTER);
		setVisible(true);

		try {
			parseData();
			displayText("Parsing finished.", 0);
		} catch (Exception e1) {
			e1.printStackTrace();
			displayStackTrace(e1);
		}
		try {
			initGenerator();
			displayText("Application generated.", 0);
		} catch (Exception e1) {
			e1.printStackTrace();
			displayStackTrace(e1);
		}
		try {
			migration();
			displayText("Data migration finished.", 0);
		} catch (Exception e1) {
			e1.printStackTrace();
			displayStackTrace(e1);
		}
		try {
			runApp();
			displayText("Test server is running.", 0);
		} catch (Exception e1) {
			e1.printStackTrace();
			displayStackTrace(e1);
		}

	}


	private void parseData(){
		MenuReader.load();
		PanelReader.loadMappings();
	}
	private void initGenerator() {

		try {
			DjangoGenerator generator = new DjangoGenerator();
			generator.generate();
		} catch (IOException e) {
			e.printStackTrace();
			displayStackTrace(e);
		}
	}


	/**
	 * Set 
	 */
	private void parseXML() {
		MenuReader.load();

	}
	
	/**
	 * Prepare log window icon
	 */
	public void loadIcon() {
		String icoPath = com.krogen.repository_utils.RepositoryPathsUtil.getStaticRepositoryPath() + File.separator + Settings.ICONS_DIR + File.separator + Settings.WEB_MAINFRAME_ICON;
		icoPath = icoPath.replaceAll("/", "\\\\");
		BufferedImage icoImg;
		try {
			icoImg = ImageIO.read(new File(icoPath));
			setIconImage(icoImg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays text for logging purposes.
	 * @param text Text to be shown in frame
	 * @param type Message type: 0 - info, 1 - error, 2 - warning
	 */
	public void displayText(String text, int type) {
		StyledDocument document = statusPane.getStyledDocument();
		SimpleAttributeSet set = new SimpleAttributeSet();
		statusPane.setCharacterAttributes(set, true);
		SimpleDateFormat formatter = new SimpleDateFormat(Settings.DATE_TIME_SECONDS_FORMAT);
		Date today = new Date();
		String prefix = "[" + formatter.format(today) + "] ";
		StyleConstants.setForeground(set, Color.white);
		if(type == 1) {
			StyleConstants.setForeground(set, Color.red);
			prefix += "[ERROR] ";
		}
		if(type == 2) {
			StyleConstants.setForeground(set, Color.yellow);
			prefix += "[WARINIG] ";
		}
		try {
			document.insertString(document.getLength(), prefix + text + "\n", set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		statusPane.setCaretPosition(statusPane.getDocument().getLength());
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
	}

	/**
	 * Displays exception stack trace as error message
	 * @param e
	 */
	public void displayStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktrace = sw.toString();
		displayText(stacktrace, 1);
	}
	
	
	public void migration() throws Exception {
		// starting up the app
		// TODO redirect output to log window
		System.out.println("start "+Application.PYTHON_PATH+" "+ Application.appRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py");

		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.appRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","migrate");
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(Redirect.INHERIT);
	//	Process process = processBuilder.start();
	//	process.waitFor();
	//	displayText("Starting internal server on port 8000", 0);
	}
	
	public void runApp() throws Exception {
		// starting up the app
		// TODO redirect output to log window
		ProcessBuilder processBuilder = new ProcessBuilder("cmd","/k","start "+Application.PYTHON_PATH+" "+ Application.appRootPath+File.separator+"generated"+File.separator+Application.projectTitleRenamed+File.separator+"manage.py","runserver");
		processBuilder.redirectErrorStream(true);

		processBuilder.redirectOutput(Redirect.INHERIT);
		process = processBuilder.start();

		displayText("Starting internal server on port 8000", 0);
	}
}

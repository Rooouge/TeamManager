package teammanager.gui;

import jutils.gui.JUFrame;
import teammanager.TeamManager;

@SuppressWarnings("serial")
public class MainWindow extends JUFrame {
	
	public MainWindow() throws Exception {
		super(TeamManager.VERSION);
		
		content = new MainWindowContent();
		GUI.content = (MainWindowContent) content;
		
		this.setContentPane(content);
		this.pack();		
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}

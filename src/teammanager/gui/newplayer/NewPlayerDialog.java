package teammanager.gui.newplayer;

import javax.swing.JDialog;

import teammanager.gui.GUI;

@SuppressWarnings("serial")
public class NewPlayerDialog extends JDialog {

	private NewPlayerContent content;
	
	
	public NewPlayerDialog() {
		this.setTitle("Create a new player");
		
		content = new NewPlayerContent();
		
		this.setContentPane(content);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(GUI.window);
		this.setVisible(true);
	}
	
}

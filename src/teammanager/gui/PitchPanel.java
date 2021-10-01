package teammanager.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import jutils.config.Images;
import jutils.gui.TransparentPanel;
import teammanager.players.Player;
import teammanager.teams.Teams;

@SuppressWarnings("serial")
public class PitchPanel extends TransparentPanel {

	private static final ImageIcon PITCH_IMAGE = Images.getImageIcon("pitch-512x");
	
	private Teams teams;
	private boolean drawTeams;
	
	
	public PitchPanel() throws Exception {
		super();
		
		drawTeams = false;
		
		this.setPreferredSize(new Dimension(PITCH_IMAGE.getIconWidth(), PITCH_IMAGE.getIconHeight()));
	}
	
	
	public void setDrawTeams(boolean value) throws Exception {
		if(teams == null && value) 
			teams = Teams.getInstance();
		
		drawTeams = value;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setFont(GUI.FONT_GUI);
		g.drawImage(PITCH_IMAGE.getImage(), 0, 0, null);
		
		if(drawTeams) {
			for(Player player : teams.getTeams()[0].getPlayers()) {
				GUI.drawPlayer(g, player, false, PITCH_IMAGE, 1, GUI.TEAM_0_COLOR);
			}
			for(Player player : teams.getTeams()[1].getPlayers()) {
				GUI.drawPlayer(g, player, true, PITCH_IMAGE, 1, GUI.TEAM_1_COLOR);
			}
		}
	}
	
}

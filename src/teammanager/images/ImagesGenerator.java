package teammanager.images;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import teammanager.gui.GUI;
import teammanager.players.Player;
import teammanager.teams.Team;
import teammanager.teams.Teams;

public class ImagesGenerator {	
	
	public static void generateImage(File file) throws Exception {
		Team[] teams = Teams.getInstance().getTeams();
		ImageIcon bg = Images.PITCH;
		
		BufferedImage img = new BufferedImage(bg.getIconWidth(), bg.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		
		bg.paintIcon(null, g, 0, 0);
		
		g.setFont(GUI.FONT_IMAGE);
		for(Player player : teams[0].getPlayers()) {
			GUI.drawPlayer(g, player, false, bg, 2, GUI.RED_TEAM);
		}
		for(Player player : teams[1].getPlayers()) {
			GUI.drawPlayer(g, player, true, bg, 2, GUI.BLUE_TEAM);
		}
		
		ImageIO.write(img, "png", file);
	}
	
}

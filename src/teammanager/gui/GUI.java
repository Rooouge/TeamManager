package teammanager.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import jutils.config.Config;
import jutils.gui.Colors;
import teammanager.gui.newplayer.NewPlayerDialog;
import teammanager.players.Player;
import teammanager.players.Roles;

public class GUI {

	public static final Font FONT_IMAGE = new Font("Sans Serif", Font.BOLD, Integer.parseInt(Config.getValue("font-size")));
	public static final Font FONT_GUI = FONT_IMAGE.deriveFont(FONT_IMAGE.getSize2D()/2f);
	
	public static MainWindow window;
	public static MainWindowContent content;
	public static NewPlayerDialog newPlayerDialog;
	
	public static final Color WHITE_TEXT = Color.white;
	public static final Color RED_TEXT = new Color(255, 64, 64);
	public static final Color GREEN_TEXT = new Color(64, 255, 64);
	public static final Color GRAY_TEXT = Colors.gray(128);
	
	
	public static void createNewWindow() throws Exception {
		if(window != null)
			window.dispose();
		window = new MainWindow();
	}
	
	public static void createNewPlayerDialog() {
		if(newPlayerDialog != null)
			newPlayerDialog.dispose();
		
		newPlayerDialog = new NewPlayerDialog();
	}
	
	
	public static void drawPlayer(Graphics g, Player player, boolean invert, ImageIcon bg, int scale) {
		Roles role = player.getActiveRole();
		
		int x = invert ? bg.getIconWidth() - scale*role.getX() : scale*role.getX();
		int y = invert ? bg.getIconHeight() - scale*role.getY() : scale*role.getY();
		
		int bound = scale*2 + (scale == 2 ? -1 : 0);
		
		for(int i = -bound; i <= bound; i++) {
			for(int j = -bound; j <= bound; j++) {
				boolean checkCorners = (i == -bound && j == -bound) || (i == -bound && j == bound) || (i == bound && j == -bound) || (i == bound && j == bound);
				
				if(!checkCorners)
					GUI.drawCenteredString(g, player.getName().toUpperCase(), x+i, y+j, Colors.GRAY_48);
			}
		}
		
		GUI.drawCenteredString(g, player.getName().toUpperCase(), x, y, Color.white);
	}
	
	public static void drawCenteredString(Graphics g, String text, int x, int y, Color color) {
		Color previous = g.getColor();
		g.setColor(color);
		
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
	    int dx = x - (metrics.stringWidth(text)) / 2;
	    int dy = y - ((metrics.getHeight()) / 2) + metrics.getAscent();
	    
	    g.drawString(text, dx, dy);
	    g.setColor(previous);
	}
	
}

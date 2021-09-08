package teammanager.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import jutils.asserts.Assert;
import jutils.asserts.AssertException;
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
	
	public static final Color TEAM_0_COLOR;
	public static final Color TEAM_1_COLOR;
	
	
	static {
		String[] rgb0 = Config.getValue("team-0-color").split(",");
		String[] rgb1 = Config.getValue("team-1-color").split(",");
		
		try {
			Assert.isTrue(rgb0.length == 3, "Color must be specified in the format \"R,G,B\" (0-255");
			Assert.isTrue(rgb1.length == 3, "Color must be specified in the format \"R,G,B\" (0-255");
		} catch (AssertException e) {
			e.printStackTrace();
		}
		
		TEAM_0_COLOR = new Color(Integer.parseInt(rgb0[0]), Integer.parseInt(rgb0[1]), Integer.parseInt(rgb0[2]));
		TEAM_1_COLOR = new Color(Integer.parseInt(rgb1[0]), Integer.parseInt(rgb1[1]), Integer.parseInt(rgb1[2]));
	}
	
	
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
	
	
	public static void drawPlayer(Graphics g, Player player, boolean invert, ImageIcon bg, int scale, Color color) {
		Roles role = player.getActiveRole();
		
		int x = invert ? bg.getIconWidth() - scale*role.getX() : scale*role.getX();
		int y = invert ? bg.getIconHeight() - scale*role.getY() : scale*role.getY();
		
		int bound = scale*2 + (scale == 2 ? -1 : 0);
		float alphaFraction = bound*2/48;
		
		for(int i = -bound; i <= bound; i++) {
//			System.out.println(0);
			for(int j = -bound; j <= bound; j++) {
//				System.out.println(1);
				boolean checkCorners = (i == -bound && j == -bound) || (i == -bound && j == bound) || (i == bound && j == -bound) || (i == bound && j == bound);
				
				if(!checkCorners) {
//					float alpha = Math.max(alphaFraction * (-i), alphaFraction * (-j));
//					if(alpha < 0)
//						alpha = 0;
					
					GUI.drawCenteredString(g, player.getName().toUpperCase(), x+i, y+j, Colors.GRAY_48, 0);
				}
			}
		}
		
		GUI.drawCenteredString(g, player.getName().toUpperCase(), x, y, color, 0);
	}
	
	public static void drawCenteredString(Graphics g, String text, int x, int y, Color color, float alpha) {
		Color previous = g.getColor();
		
		float[] colorComponents = color.getColorComponents(null);
		
//		g.setColor(new Color(colorComponents[0], colorComponents[1], colorComponents[2], alpha));
		g.setColor(color);
		
	    FontMetrics metrics = g.getFontMetrics(g.getFont());
	    int dx = x - (metrics.stringWidth(text)) / 2;
	    int dy = y - ((metrics.getHeight()) / 2) + metrics.getAscent();
	    
	    g.drawString(text, dx, dy);
	    g.setColor(previous);
	}
	
}

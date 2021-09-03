package teammanager;

import jutils.config.Config;
import jutils.log.Log;
import teammanager.gui.GUI;
import teammanager.players.Players;

public class TeamManager {
	
	public static final String VERSION = "TeamManager " + Config.getValue("app-version");
	
	
	public static void main(String[] args) {
		Log.system("Started " + VERSION);
		try {
			setup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void setup() throws Exception {
		Players.init();
		
		GUI.createNewWindow();
	}
}

package teammanager.xml;

import java.io.File;

import jutils.config.Config;
import jutils.config.Files;

public class XMLUtils {

	public static final String DEFAULT_SAVE_FOLDER = Config.getValue("default-save-folder");
	public static final String DEFAULT_OPEN_FOLDER = Config.getValue("default-open-folder");
	
	public static final File XML_PLAYERS = Files.getFile("players.xml");
	
}

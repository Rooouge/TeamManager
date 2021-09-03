package teammanager.players;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import jutils.asserts.Assert;
import jutils.log.Log;
import lombok.Getter;
import teammanager.xml.XMLUtils;

@Getter
public class Players {

	private static Players instance;
	
	
	private List<Player> allPlayers;
	private List<Player> goalkeepers;
	private List<Player> movementPlayers;
	
	private Random random;
	
	
	public Players() throws Exception {
		random = new Random();
		
		parseXML();
		
	}
	
	/*
	 * Initialization
	 */
	
	private void parseXML() throws Exception {
		SAXReader reader = SAXReader.createDefault();
		
		File playersFile = XMLUtils.XML_PLAYERS;
		Assert.isTrue(playersFile.exists(), "File " + playersFile.getAbsolutePath() + " doesn't exists");
		
		allPlayers = new LinkedList<>();
		goalkeepers = new LinkedList<>();
		movementPlayers = new LinkedList<>();
		
		Node playersNodes = reader.read(playersFile).selectSingleNode("players");
		// Parsing players
		for(Node playerNode : playersNodes.selectNodes("player")) {
			String originalName = playerNode.valueOf("@name");
			List<Roles> mainRoles = Roles.getRolesFromAcronyms(playerNode.selectSingleNode("mainroles").getText());
			List<Roles> otherRoles = Roles.getRolesFromAcronyms(playerNode.selectSingleNode("otherroles").getText());
			double value = Double.parseDouble(playerNode.selectSingleNode("value").getText());
			
			Player player = new Player(originalName, mainRoles, otherRoles, value);
			allPlayers.add(player);
			if(player.isGoalkeeper())
				goalkeepers.add(player);
			else
				movementPlayers.add(player);
		}
	}
	
	/*
	 * Getter
	 */
	
	public Player getPlayerByName(String name) {
		for(Player player : allPlayers) {
			if(name.equalsIgnoreCase(player.getName()))
				return player;
		}
		
		return null;
	}
	
	/*
	 * Statics
	 */
	
	public static Players getInstance() throws Exception {
		if(instance == null)
			instance = new Players();
		
		return instance;
	}
	
	public static void init() throws Exception {
		Log.system("[Initializing players]");
		getInstance();
		Log.system("[Players initialized]");
	}
	
}

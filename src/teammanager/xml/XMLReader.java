package teammanager.xml;

import java.io.File;
import java.util.List;

import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import jutils.asserts.Assert;
import teammanager.players.Player;
import teammanager.players.Players;
import teammanager.teams.Team;
import teammanager.teams.Teams;

public class XMLReader {

	public static Team[] readXML(File file) throws Exception {
		SAXReader reader = SAXReader.createDefault();
		Node teamsNode = reader.read(file).selectSingleNode("teams");
		
		List<Node> teamNodes = teamsNode.selectNodes("team");
		Assert.isTrue(teamNodes.size() == 2, "Founded " + teamNodes.size() + " <team> nodes (expected 2)");
		Team[] teams = new Team[teamNodes.size()];
		
		Players players = Players.getInstance();
		
		for(int i = 0; i < teamNodes.size(); i++) {
			Node teamNode = teamNodes.get(i);
			Team team = new Team(teamNode.valueOf("@name"));
			
			List<Node> playerNodes = teamNode.selectNodes("player");
			Assert.isTrue(playerNodes.size() == Teams.PLAYERS_PER_TEAM, "Founded " + teamNodes.size() + " <team> nodes (expected " + Teams.PLAYERS_PER_TEAM + ")");
			
			for(int j = 0; j < playerNodes.size(); j++) {
				Node playerNode = playerNodes.get(j);
				
				Player player = players.getPlayerByName(playerNode.getText());
				player.setChecked(true);
				team.addPlayer(player, j);
			}
			
			teams[i] = team;
		}
		
		return teams;
	}
}

package teammanager.teams;

import jutils.asserts.Assert;
import jutils.asserts.AssertException;
import jutils.log.Log;
import lombok.Getter;
import teammanager.players.Player;
import teammanager.players.Roles;

@Getter
public class Team {

	private String name;
	private Player[] players;
	
	
	public Team(String name) {
		this.name = name;
		players = new Player[Teams.PLAYERS_PER_TEAM];
	}
	
	/*
	 * Operations
	 */
	
	public Player getPlayer(int index) {
		/*
		 * 0 = GK
		 * 1 = LEFT CB
		 * 2 = RIGHT CB
		 * 3 = RW
		 * 4 = CM
		 * 5 = LW
		 * 6 = ST
		 */
		return players[index];
	}
	
	
	
	public void addPlayer(Player player, int index) throws AssertException {
		Assert.isTrue(currentSize() < Teams.PLAYERS_PER_TEAM, "Team already full");
		
		Roles role = Roles.getRoleFromIndex(index);
		player.setActiveRole(role);
		player.setActive(true);
		
		players[index] = player;
		Log.info("[" + name + "] Added player " + player.getName() + " as " + role);
	}
	
	public int currentSize() {
		int c = 0;
		for(int i = 0; i < players.length; i++) {
			if(players[i] != null)
				c++;
		}
		
		return c;
	}
	
	public boolean isFull() {
		return players.length == Teams.PLAYERS_PER_TEAM;
	}
	
	public boolean isRoleFree(Roles role) {
		return players[role.getIndex()] == null;
	}
	
	public Roles getFirstFreeRole() {
		int i;
		for(i = 0; i < players.length; i++) {
			if(players[i] == null)
				return Roles.getRoleFromIndex(i);
		}
		
		return Roles.getRoleFromIndex(players.length);
	}
	
	public double getValue() {
		double value = 0.0;
		
		for(Player player : players) {
			if(player != null)
				value += player.getValue();
		}
		
		return value;
	}
	
	public void reset() {
		for(Player player : players) {
			if(player == null)
				break;
			
			player.setActive(false);
		}
		
		players = new Player[Teams.PLAYERS_PER_TEAM];
	}
	
	
	public void logTeam() {
		Log.info("- Team '" + name + "' (Current size: " + players.length + ", Value: " + getValue() + ")");
		for(Player player : players) {
			Log.info("  [" + player.getActiveRole().getAcronym() + "] " + player.getName() + " (" + player.getValue() + ")");
		}
	}
}

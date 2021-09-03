package teammanager.teams;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import jutils.asserts.AssertException;
import jutils.config.Config;
import jutils.log.Log;
import lombok.Getter;
import lombok.Setter;
import teammanager.Utils;
import teammanager.players.Player;
import teammanager.players.Roles;

public class Teams {

	public static final int PLAYERS_PER_TEAM = Integer.parseInt(Config.getValue("team-size"));
	public static final double BALANCE_FACTOR = Double.parseDouble(Config.getValue("team-balance-factor"));
	private static Teams instance;
	
	@Getter
	private Team[] teams;
	private Random random;
	private List<Player> originalGoalkeepers;
	private List<Player> originalMovementPlayers;
	
	@Getter
	@Setter
	private boolean created;
	
	
	public Teams() throws Exception {
		teams = new Team[2];
		teams[0] = new Team("Squadra ROSSA");
		teams[1] = new Team("Squadra BLU");
		
		random = new Random();
		created = false;
	}
	
	public void initByXML(Team[] teams) {
		this.teams = teams;
		created = true;
	}
	
	/*
	 * Teams creation
	 */
	
	public void makeTeams(List<Player> goalkeepers, List<Player> movementPlayers) throws Exception {
		while(goalkeepers.size() < 2) {
			goalkeepers.add(movementPlayers.remove(random.nextInt(movementPlayers.size())));
		}
		while(goalkeepers.size() > 2) {
			movementPlayers.add(goalkeepers.remove(random.nextInt(goalkeepers.size())));
		}

		Collections.shuffle(goalkeepers);
		Collections.shuffle(movementPlayers);
		
		originalGoalkeepers = new LinkedList<>();
		originalMovementPlayers = new LinkedList<>();		
		for(Player player : goalkeepers) {
			originalGoalkeepers.add(player);
		}
		for(Player player : movementPlayers) {
			originalMovementPlayers.add(player);
		}
		
		
		Log.info("Check 1: " + goalkeepers.size());
		
		boolean isBalanced;
		do {
			addGoalkeepers(goalkeepers);
			addMovementPlayers(movementPlayers);
			
			isBalanced = isBalanced();
			if(!isBalanced) {
				resetTeams();
				resetLists(goalkeepers, movementPlayers);
			}
				
		} while(!isBalanced);
		
		created = true;
	}

	private void addGoalkeepers(List<Player> goalkeepers) throws AssertException {
		teams[0].addPlayer(goalkeepers.remove(0), Roles.GOALKEEPER.getIndex());
		teams[1].addPlayer(goalkeepers.remove(0), Roles.GOALKEEPER.getIndex());
	}
	
	private void addMovementPlayers(List<Player> movementPlayers) throws AssertException {
		Team team;
		
		for(int i = 0; i < PLAYERS_PER_TEAM*2 - 2; i++) {
			team = i % 2 == 0 ? teams[0] : teams[1];
			
			Player player;
			Roles role;
			boolean flag = false;
			
			int attempt = 0;
			int maxAttempt = 3;
			
			do {
				player = movementPlayers.get(random.nextInt(movementPlayers.size()));
				List<Roles> mainRoles = Utils.cloneRolesList(player.getMainRole());
				List<Roles> otherRoles = Utils.cloneRolesList(player.getOtherRoles());
				
				do {
					role = mainRoles.remove(random.nextInt(mainRoles.size()));	
					flag = team.isRoleFree(role);
				} while(!flag && !mainRoles.isEmpty());
				
				if(!flag && !otherRoles.isEmpty()) {
					do {
						role = otherRoles.remove(random.nextInt(otherRoles.size()));
						flag = team.isRoleFree(role);
					} while(!flag && !otherRoles.isEmpty());
				}
				
				attempt++;
				if(attempt == maxAttempt) {
					flag = true;
					role = team.getFirstFreeRole();
				}
			} while(!flag);
			
			movementPlayers.remove(player);
			team.addPlayer(player, role.getIndex());
		}
	}
	
	private boolean isBalanced() {
		return Math.abs(teams[0].getValue() - teams[1].getValue()) <= BALANCE_FACTOR;
	}
	
	public void resetTeams() {
		teams[0].reset();
		teams[1].reset();
	}
	
	private void resetLists(List<Player> goalkeepers, List<Player> movementPlayers) {
		for(Player player : originalGoalkeepers) {
			goalkeepers.add(player);
		}
		for(Player player : originalMovementPlayers) {
			movementPlayers.add(player);
		}
	}
	
	/*
	 * Statics
	 */
	
	public static Teams getInstance() throws Exception {
		if(instance == null)
			instance = new Teams();
		
		return instance;
	}
	
}

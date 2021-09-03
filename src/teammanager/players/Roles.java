package teammanager.players;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

	GOALKEEPER("GK", 0, 169, 35),
	LEFT_CENTRE_BACK("CB", 1, 238, 92),
	RIGHT_CENTRE_BACK("CB", 2, 100, 92),
	LEFT_WING("LW", 3, 278, 150),
	RIGHT_WING("RW", 4, 60, 150),
	CENTRE_MIDFIELDER("CM", 5, 169, 150),
	STRIKER("ST", 6, 169, 210);
	
	
	private String acronym;
	private int index;
	private int x;
	private int y;
	
	
	public static List<Roles> getRolesFromAcronyms(String acronymsList) {
		if(acronymsList.equalsIgnoreCase("all")) {
			List<Roles> all = new ArrayList<>(Roles.values().length);
			for(Roles role : Roles.values()) {
				if(!role.equals(GOALKEEPER))
					all.add(role);
			}
			return all;
		}
		
		List<Roles> roles = new LinkedList<>();
		
		for(String acronym : acronymsList.split(";")) {
			for(Roles role : Roles.values()) {
				if(acronym.equalsIgnoreCase("cb")) {
					roles.add(LEFT_CENTRE_BACK);
					roles.add(RIGHT_CENTRE_BACK);
				}
				else if(acronym.equalsIgnoreCase(role.acronym))
					roles.add(role);
			}
		}
		
		return roles;
	}
	
	public static Roles getRoleFromIndex(int index) {
		for(Roles role : Roles.values()) {
			if(role.index == index)
				return role;
		}
		
		return null;
	}
	
	public static List<String> getRolesDistinctAcronyms() {
		List<String> acronyms = new ArrayList<>();
		
		for(Roles role : Roles.values()) {
			if(!acronyms.contains(role.acronym))
				acronyms.add(role.acronym);
		}
		
		return acronyms;
	}
	
	@Override
	public String toString() {
		return name() + " (" + acronym + ", " + index + ")";
	}
	
}

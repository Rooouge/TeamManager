package teammanager;

import java.util.LinkedList;
import java.util.List;

import teammanager.players.Roles;

public class Utils {

	public static final String XML = "xml";
	public static final String PNG = "png";
	
	
	public static List<Roles> cloneRolesList(List<Roles> original) {
		List<Roles> newList = new LinkedList<>();
		
		for(Roles role : original) {
			newList.add(role);
		}
		
		return newList;
	}
}

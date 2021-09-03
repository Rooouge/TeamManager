package teammanager.players;

import java.util.List;

import javax.swing.ImageIcon;

import jutils.log.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import teammanager.images.Images;

@Getter
@ToString
public class Player {

	private String originalName;
	private String name;
	private List<Roles> mainRole;
	private List<Roles> otherRoles;
	@Setter
	private Roles activeRole;
	@Setter
	private boolean active;
	@Setter
	private boolean checked;
	private double value;
	private ImageIcon icon;
	
	
	public Player(String originalName, List<Roles> mainRole, List<Roles> otherRoles, double value) {
		this.originalName = originalName;
		this.mainRole = mainRole;
		this.otherRoles = otherRoles;
		this.value = value;
		
		active = false;
		
		name = originalName.substring(0, 1).toUpperCase() + originalName.substring(1).toLowerCase();
//		Log.info("- Created " + this);
		Log.info("- Created player '" + name + "'");
		
		if(isGoalkeeper())
			icon = Images.GLOVE;
		else
			icon = Images.SHOE;
	}
	
	
	public boolean isGoalkeeper() {
		return mainRole.contains(Roles.GOALKEEPER);
	}
	
}

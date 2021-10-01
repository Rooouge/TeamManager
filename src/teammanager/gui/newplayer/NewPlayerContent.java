package teammanager.gui.newplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jutils.asserts.Assert;
import jutils.asserts.AssertException;
import jutils.gui.ColoredTitledBorder;
import jutils.gui.Colors;
import jutils.gui.Content;
import jutils.gui.EmptyBorder;
import jutils.gui.TransparentPanel;
import jutils.random.Utils;
import jutils.strings.Strings;
import teammanager.gui.GUI;
import teammanager.players.Roles;
import teammanager.xml.XMLWriter;

@SuppressWarnings("serial")
public class NewPlayerContent extends Content {
	
	private JTextField nameField;
	private JTextField valueField;
	private JCheckBox[] mainRoles;
	private JCheckBox[] otherRoles;
	
	
	public NewPlayerContent() {
		super(Colors.GRAY_48);
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(new EmptyBorder(10), new ColoredTitledBorder(Colors.CYAN, "PLAYER DATA", 2)), 
				new EmptyBorder(10))
		);
		
		this.setLayout(new BorderLayout(0, 15));
		
		this.add(nameAndValuePanel(), BorderLayout.NORTH);
		this.add(rolesPanel(), BorderLayout.CENTER);
		this.add(buttonPanel(), BorderLayout.SOUTH);
	}
	
	/*
	 * North
	 */
	
	private JPanel nameAndValuePanel() {
		nameField = new JTextField(10);
		valueField = new JTextField(5);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setForeground(Color.white);
		JLabel valueLabel = new JLabel("Value: ");
		valueLabel.setForeground(Color.white);
		
		
		TransparentPanel namePanel = new TransparentPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		
		TransparentPanel valuePanel = new TransparentPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		valuePanel.add(valueLabel);
		valuePanel.add(valueField);
		
		
		TransparentPanel panel = new TransparentPanel(new BorderLayout(0, 5));
		panel.add(namePanel, BorderLayout.NORTH);
		panel.add(valuePanel, BorderLayout.SOUTH);
				
		return panel;
	}
	
	/*
	 * Center
	 */
	
	private JPanel rolesPanel() {
		mainRoles = buildCheckBoxes();
		otherRoles = buildCheckBoxes();
		JPanel mainRolesPanel = buildCheckBoxesPanel(mainRoles, "MAIN ROLES");
		JPanel otherRolesPanel = buildCheckBoxesPanel(otherRoles, "OTHER ROLES");
		
		TransparentPanel panel = new TransparentPanel(new BorderLayout(25, 0));
		panel.add(mainRolesPanel, BorderLayout.WEST);
		panel.add(otherRolesPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	private JCheckBox[] buildCheckBoxes() {
		List<String> roles = Roles.getRolesDistinctAcronyms();
		JCheckBox[] boxes = new JCheckBox[roles.size()];
		
		for(int i = 0; i < roles.size(); i++) {
			JCheckBox box = new JCheckBox(roles.get(i));
			
			box.setForeground(Color.white);
			box.setBackground(null);
			
			boxes[i] = box;
		}
		
		return boxes;
	}
	
	private JPanel buildCheckBoxesPanel(JCheckBox[] boxes, String labelText) {
		JLabel label = new JLabel(labelText);
		label.setForeground(Colors.gray(128));
		
		TransparentPanel panel = new TransparentPanel(new GridLayout(boxes.length + 1, 1, 0, 2));
		panel.add(label);
		for(JCheckBox box : boxes) {
			panel.add(box);
		}
		
		return panel;
	}
	
	/*
	 * South
	 */
	
	private JPanel buttonPanel() {
		JButton button = new JButton("Create");
		button.addActionListener(e -> {
			try {
				Assert.isTrue(XMLWriter.writeNewPlayerToXML(makeXMLNode()), "Failed to write new player");
				
				Utils.beep();
				JOptionPane.showMessageDialog(this, "Succesfully created player '" + nameField.getText() + "'!", "Create player", JOptionPane.INFORMATION_MESSAGE);
				
				GUI.newPlayerDialog.dispose();
				GUI.createNewWindow();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		TransparentPanel panel = new TransparentPanel(new FlowLayout());
		panel.add(button);
		
		return panel;
	}
	
	/*
	 * Operations
	 */
	
	private String makeXMLNode() throws AssertException {
		// Checking params
		
		String name = nameField.getText();
		String valueString = valueField.getText();
		
		Assert.notNull(name);
		Assert.isNotEmpty(name);
		
		Assert.notNull(valueString);
		Assert.isNotEmpty(valueString);
		double value = Double.parseDouble(valueString);
		
		boolean flag = false;
		for(JCheckBox box : mainRoles) {
			if(box.isSelected()) {
				flag = true;
				break;
			}
		}
		Assert.isTrue(flag);
		
		// Building XML node
		
		String mainRolesString = "";
		for(JCheckBox box : mainRoles) {
			if(box.isSelected()) {
				mainRolesString += box.getText() + ";";
			}
		}
		if(mainRolesString.endsWith(";"))
			mainRolesString = Strings.cutLastChars(mainRolesString, 1);
		
		String otherRolesString = "";
		for(JCheckBox box : otherRoles) {
			if(box.isSelected()) {
				otherRolesString += box.getText() + ";";
			}
		}
		if(otherRolesString.endsWith(";"))
			otherRolesString = Strings.cutLastChars(otherRolesString, 1);
		
		return "\t<player name=\"" + name + "\">\n"
				+ "\t\t<mainroles>" + mainRolesString + "</mainroles>\n"
				+ "\t\t<otherroles>" + otherRolesString + "</otherroles>\n"
				+ "\t\t<value>" + value + "</value>\n"
				+ "\t</player>";
	}
	
}

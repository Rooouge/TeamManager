package teammanager.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import jutils.asserts.Assert;
import jutils.config.Images;
import jutils.gui.ColoredPanel;
import jutils.gui.ColoredTitledBorder;
import jutils.gui.Colors;
import jutils.gui.Content;
import jutils.gui.EmptyBorder;
import jutils.gui.IconOnlyButton;
import jutils.gui.TransparentPanel;
import jutils.log.Log;
import teammanager.Utils;
import teammanager.images.ImagesGenerator;
import teammanager.players.Player;
import teammanager.players.Players;
import teammanager.teams.Teams;
import teammanager.xml.XMLReader;
import teammanager.xml.XMLUtils;
import teammanager.xml.XMLWriter;

@SuppressWarnings("serial")
public class MainWindowContent extends Content {
	
	private Players players;
	private PitchPanel pitchPanel;
	private JLabel countLabel;
	private JCheckBox[] checkBoxes;
	private int checkBoxesSelected;
	private boolean checkBoxFlag;
	
	
	public MainWindowContent() throws Exception {
		super(Colors.GRAY_48);
		this.setBorder(new EmptyBorder(10));
		players = Players.getInstance();
		
		pitchPanel = new PitchPanel();
		
		this.setLayout(new BorderLayout(0, 25));
		this.add(buttonsPanel(), BorderLayout.NORTH);
		this.add(playersPanel(), BorderLayout.WEST);
		this.add(pitchPanel, BorderLayout.EAST);
	}
	
	
	private JPanel buttonsPanel() {
		IconOnlyButton newPlayer = new IconOnlyButton(Images.getImageIcon("new-player"));
		newPlayer.addActionListener(e -> GUI.createNewPlayerDialog());
		
		IconOnlyButton makeTeams = new IconOnlyButton(Images.getImageIcon("make-teams"));
		makeTeams.addActionListener(e -> {
			try {
				Log.system("Creating teams...");
				Teams teams = Teams.getInstance();
				teams.resetTeams();
				
				List<Player> goalkeepers = new LinkedList<>();
				List<Player> movementPlayers = new LinkedList<>();
				
				for(Player player : players.getAllPlayers()) {
//					Log.info(player.getName() + " - " + player.isActive());
					if(player.isChecked()) {
						if(player.isGoalkeeper())
							goalkeepers.add(player);
						else
							movementPlayers.add(player);
					}
				}
				
				int tot = Teams.PLAYERS_PER_TEAM*2;
				if(goalkeepers.size() + movementPlayers.size() != tot) {
					jutils.random.Utils.beep();
					JOptionPane.showMessageDialog(this, "You must select " + tot + " players", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Log.info("Check 0: " + goalkeepers.size());
				teams.makeTeams(goalkeepers, movementPlayers);
				Log.system("Teams created!");
				
				pitchPanel.setDrawTeams(true);
				GUI.window.repaint();
				jutils.random.Utils.beep();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		IconOnlyButton saveImage = new IconOnlyButton(Images.getImageIcon("save-image"));
		saveImage.addActionListener(e -> saveFile(Utils.PNG));
		
		IconOnlyButton saveXML = new IconOnlyButton(Images.getImageIcon("save-xml"));
		saveXML.addActionListener(e -> saveFile(Utils.XML));
		
		IconOnlyButton openXML = new IconOnlyButton(Images.getImageIcon("open-xml"));
		openXML.addActionListener(e -> {
			try {
				File file = openXMLFile();
				Assert.notNull(file);
				
				Teams teams = Teams.getInstance();
				Log.info("Parsing teams...");
				teams.initByXML(XMLReader.readXML(file));
				setCheckBoxesByXML();
				Log.system("Teams parsed!");
				
				pitchPanel.setDrawTeams(true);
				GUI.window.repaint();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		TransparentPanel leftPanel = new TransparentPanel(new GridLayout(1, 2, 15, 0));
		leftPanel.add(newPlayer);
		leftPanel.add(makeTeams);
		
		
		TransparentPanel rightPanel = new TransparentPanel(new GridLayout(1, 3, 15, 0));
		rightPanel.add(saveImage);
		rightPanel.add(saveXML);
		rightPanel.add(openXML);
		
		
		Border border = BorderFactory.createCompoundBorder(new ColoredTitledBorder(Colors.CYAN, "OPERATIONS", 2), new EmptyBorder(10));
		TransparentPanel panel = new TransparentPanel(new BorderLayout(10, 0), border);
		panel.add(leftPanel, BorderLayout.WEST);
		panel.add(rightPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel playersPanel() {
		List<Player> playersList = players.getAllPlayers();
		checkBoxes = new JCheckBox[playersList.size()];
		ColoredPanel checkBoxesPanel = new ColoredPanel(null, new GridLayout(playersList.size() + 2, 1, 0, 2));
		checkBoxesSelected = 0;
		checkBoxFlag = false;
		
		JLabel playersLabel = new JLabel("PLAYERS");
		playersLabel.setForeground(GUI.GRAY_TEXT);
		checkBoxesPanel.add(playersLabel);
		
		countLabel = new JLabel("Selected: 0");
		countLabel.setForeground(GUI.GRAY_TEXT);
		
		for(int i = 0; i < playersList.size(); i++) {
			Player player = playersList.get(i);
			
			JCheckBox box = new JCheckBox(player.getName(), player.getIcon());
			box.setForeground(GUI.WHITE_TEXT);
			box.setBackground(null);
			box.addItemListener(e -> {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					if(checkBoxesSelected < 14) {
						checkBoxesSelected++;
						box.setForeground(GUI.GREEN_TEXT);
						countLabel.setForeground(GUI.GRAY_TEXT);
						player.setChecked(true);
					} else {
						jutils.random.Utils.beep();
						countLabel.setForeground(GUI.RED_TEXT);
						checkBoxFlag = true;
						((JCheckBox) e.getItemSelectable()).setSelected(false);
					}
					
					
				} else if(e.getStateChange() == ItemEvent.DESELECTED) {
					if(!checkBoxFlag) {
						checkBoxesSelected--;
						box.setForeground(GUI.WHITE_TEXT);
						countLabel.setForeground(GUI.GRAY_TEXT);
						player.setChecked(false);
					} else {
						checkBoxFlag = false;
					}
				}
				repaintCountLabel();
			});
			
			
			checkBoxes[i] = box;
			checkBoxesPanel.add(box);
		}
		
		checkBoxesPanel.add(countLabel);
		
		TransparentPanel panel = new TransparentPanel(new FlowLayout());
		panel.add(checkBoxesPanel);
		
		return panel;
	}
	
	public void setCheckBoxesByXML() {
		for(JCheckBox box : checkBoxes) {
			box.setSelected(players.getPlayerByName(box.getText()).isChecked());
		}
		
		checkBoxesSelected = Teams.PLAYERS_PER_TEAM*2;
		countLabel.setBackground(GUI.GRAY_TEXT);
		repaintCountLabel();
	}
	
	private void repaintCountLabel() {
		countLabel.setText("Selected: " + checkBoxesSelected);
		this.repaint();
	}
	
	
	private void saveFile(String extension) {
		try {
			if(!Teams.getInstance().isCreated()) {
				JOptionPane.showMessageDialog(this, "Teams not created yet! You need to create or import teams before saving...", "Save teams", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			File defaultFolder = new File(XMLUtils.DEFAULT_SAVE_FOLDER);
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save " + extension.toUpperCase() + " file");
			if(defaultFolder.exists() && defaultFolder.isDirectory())
				fileChooser.setCurrentDirectory(defaultFolder);
			
			int result = fileChooser.showSaveDialog(this);
			
			if(result == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getAbsolutePath();
				if(!fileName.endsWith("." + extension))
					fileName += "." + extension;
				
				File file = new File(fileName);
				
				switch (extension) {
				case Utils.XML:
					XMLWriter.writeXML(file);
					break;
				case Utils.PNG:
					ImagesGenerator.generateImage(file);
					break;
				default:
					break;
				}
				
				
				jutils.random.Utils.beep();
				JOptionPane.showMessageDialog(this, "Succesfully created file '" + file.getName() + "'", extension.toUpperCase() + " Creation", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch(Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private File openXMLFile() {
		File defaultFolder = new File(XMLUtils.DEFAULT_OPEN_FOLDER);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open XML file");
		if(defaultFolder.exists() && defaultFolder.isDirectory())
			fileChooser.setCurrentDirectory(defaultFolder);
		
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
}

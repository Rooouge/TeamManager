package teammanager.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jutils.asserts.Assert;
import jutils.log.Log;
import teammanager.players.Player;
import teammanager.teams.Team;
import teammanager.teams.Teams;

public class XMLWriter {

	public static void writeXML(File file) throws Exception {
		Log.system("Creating XML file...");
		
		Teams teams = Teams.getInstance();
		Document doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.newDocument();
		
		Element root = doc.createElement("teams");
		
		for(Team team : teams.getTeams()) {
			Element teamElem = doc.createElement("team");
			teamElem.setAttribute("name", team.getName());
			teamElem.setAttribute("value", "" + team.getValue());
			
			for(Player player : team.getPlayers()) {
				Assert.notNull(player, "The current player is null");
				
				Element playerElem = doc.createElement("player");
				playerElem.setTextContent(player.getOriginalName());
				
				teamElem.appendChild(playerElem);
			}
			
			root.appendChild(teamElem);
		}
		
		doc.appendChild(root);
		
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		
		Transformer tr = tf.newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		
		tr.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(file)));
		
		Log.system("Created XML file: '" + file.getAbsolutePath() + "'");
	}
	
	public static boolean writeNewPlayerToXML(String newPlayerNode) throws Exception {
		File file = XMLUtils.XML_PLAYERS;
		File temp = new File(file.getParentFile().getAbsolutePath() + "/temp");
		
		try (
			BufferedReader br = new jutils.files.BufferedReader(file);
			BufferedWriter bw = new jutils.files.BufferedWriter(temp);
		) {
			String line;
			
			while((line = br.readLine()) != null) {
				if(line.equals("</players>"))
					bw.append(newPlayerNode + "\n");
				
				bw.append(line + "\n");
			}
		}
		
		Files.delete(file.toPath());
		return temp.renameTo(file);
	}
}

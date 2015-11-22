/**
 * 
 */
package kth.id2209.curator;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author pradeeppeiris
 *
 */
public class CuratorAgent extends Agent {

	private static final Logger log = Logger.getLogger(CuratorAgent.class.getName());
	
	private Map<String, Artifact> artifacts = new HashMap<String, Artifact> ();
	
	private CuratorGui gui;
	
	protected void setup() {
		log.info("Initailize Curator Agent");
		gui = new CuratorGuiImpl();
		gui.setAgent(this);
		gui.show();
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Publish-curator"); 
		sd.setName(getLocalName()+"-Publish-curator"); 
		dfd.addServices(sd);
		
		try {
			log.info("Register Profiler Agent");
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			log.severe("Error in Profile Agent register: " + e.getMessage());
		}
		
		addBehaviour(new ArtifactRequest());
	}
	
	public void updateArtifacts(String id, String name, String creator, 
									String dateCreate, String placeCreate, String genre) {
		addBehaviour(new ArtifactManager(id, name, creator, dateCreate, placeCreate, genre));
	}
	
	private class ArtifactRequest extends CyclicBehaviour {
		private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				log.info("Receive Artifact Rrequest");
				
				ACLMessage reply = msg.createReply();
				reply.setContent(getArtifacts());
				myAgent.send(reply);
			} else {
				block();
			}
		}
		
		private String getArtifacts() {
			StringBuilder sb = new StringBuilder();
			for(Artifact a : artifacts.values()) {
				sb.append(a.toString()).append(",");
			}
			return sb.toString();
		}
		
	}
	
	private class ArtifactManager extends OneShotBehaviour {

		private Artifact artifact;
		
		private ArtifactManager(String id, String name, String creator, 
				String dateCreate, String placeCreate, String genre) {
			this.artifact = new Artifact(id, name, creator, dateCreate, placeCreate, genre);
		}
		
		@Override
		public void action() {
			log.info("Add new Artifact");
			artifacts.put(artifact.id, artifact);
		}
		
	}
	
	private class Artifact {
		private String id;
		private String name;
		private String creator;
		private String dateCreate;
		private String placeCreate;
		private String genre;
		
		private Artifact(String id, String name, String creator, 
				String dateCreate, String placeCreate, String genre) {
			this.id = id;
			this.name = name;
			this.creator = creator;
			this.dateCreate = dateCreate;
			this.placeCreate = placeCreate;
			this.genre = genre;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(id).append(":").append(genre);
			
			return sb.toString();
		}
		
	}
	
}

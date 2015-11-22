/**
 * 
 */
package kth.id2209.tourguide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
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
public class TourGuideAgent extends Agent {
	
	private static final Logger log = Logger.getLogger(TourGuideAgent.class.getName());
	
	
	private List<AID> curatorAgents = new ArrayList<AID>();
	
	private Map<String, List<String>> intresetArtifacts = new HashMap<String, List<String>>();
	
	protected void setup() {
		log.info("Initailize TourGuide Agent");
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Publish-tourguide"); 
		sd.setName(getLocalName()+"-Publish-tourguide"); 
		dfd.addServices(sd);
		
		try {
			log.info("Register Tourguide Agent");
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			log.severe("Error in TourGuide Agent register: " + e.getMessage());
		}
		
		addBehaviour(new CuratorReply());
		addBehaviour(new RecommendationOffer());
		addBehaviour(new CuratorManager(this));
		
	}
	
	private class CuratorReply extends CyclicBehaviour {
		private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String reply = msg.getContent();
				log.info("Get CuratorReply " + msg.getContent());
				updateInterestCatalog(reply);
				
			} else {
				block();
			}
			
		}
		
		private void updateInterestCatalog(String reply) {
			String[] artifacts = reply.split(",");
			if(artifacts != null && artifacts.length > 0) {
				if(intresetArtifacts.containsKey(artifacts[1])) {
					intresetArtifacts.get(artifacts[1]).add(artifacts[0]);
				} else {
					List<String> items = new ArrayList<String>();
					items.add(artifacts[0]);
					intresetArtifacts.put(artifacts[1], items);
				}
			}
		}
		
	}
	
	private class RecommendationOffer extends CyclicBehaviour {
		private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String profileIntrest = msg.getContent();
				log.info("Request received: " + profileIntrest);
//				String recommendation = getRecommendation(profileIntrest);
//				log.info("LLLLLL: " + recommendation);
				
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setContent("TEst Recommenation");
				myAgent.send(reply);
				log.info(">>>>>>>>>> Message Sent: " + reply);
			} else {
				block();
			}
		}
		
		private String getRecommendation(String profileIntrest) {
			String[] intresets = profileIntrest.split(",");
			StringBuilder sb = new StringBuilder();
			
			for(String s : intresets) {
				if(s != null && s.length() > 0) {
					if(intresetArtifacts.containsKey(s)) {
						List<String> list = intresetArtifacts.get(s);
						for(String rec :  list) {
							sb.append(rec).append("\n");
						}
					}
				}
			}
			
			return sb.toString();
		}
		
	}
	
	private class CuratorManager extends TickerBehaviour {
		
		private CuratorManager(Agent agent) {
			super(agent, 5000);
		}
		
		@Override
		protected void onTick() {
			log.info("Search for CuratorAgent");
			DFAgentDescription template = new DFAgentDescription(); 
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Publish-curator");
			template.addServices(sd);
			
			try {
				DFAgentDescription[] result = DFService.search(myAgent, template);
				for (int i = 0; i < result.length; ++i) {
					if(curatorAgents.contains(result[i].getName())) {
						log.info("CuratorAgent " + result[i].getName() + " is already in Curator Catalog");
					} else {
						log.info("CuratorAgent " + result[i].getName() + " is added to Curator Catalog");
						curatorAgents.add(result[i].getName());
						myAgent.addBehaviour(new TourCatalogManager());
					}
				}
				
			} catch (FIPAException e) {
				log.severe("Error in Curator Agent search: " + e.getMessage());
			}
			
		}
		
	}

	private class TourCatalogManager extends OneShotBehaviour {

		@Override
		public void action() {
			log.info("Get Artifact details");
			ACLMessage cfp = new ACLMessage(ACLMessage.REQUEST);
			for(AID curator : curatorAgents) {
				cfp.addReceiver(curator);
			}

			cfp.setConversationId("curator-artifact");
			cfp.setReplyWith("cfp" + System.currentTimeMillis());
			myAgent.send(cfp);
		}
		
	}
	
	
}

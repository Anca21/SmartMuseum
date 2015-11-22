/**
 * 
 */
package kth.id2209.profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
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
public class ProfilerAgent extends Agent {

	private static final Logger log = Logger.getLogger(ProfilerAgent.class.getName());
	
	private ProfilerGui gui;
	
	private List<AID> tourGuideAgents = new ArrayList<AID>();
	
	private Profile userProfile;
	
	protected void setup() {
		log.info("Initailize Profiler Agent");
		
		log.info("Show Profiler GUI");
		gui = new ProfilerGuiImpl();
		gui.setAgent(this);
		gui.show();
		
		addBehaviour(new TourGuideManager(this));
		
	}
	
	public void tourRequest() {
		log.info("Tour request");
		addBehaviour(new TourRecommendation());
	}
	
	
	public void updateProfile(int age, String occupation, String gender, String[] intresets) {
		log.info("Update profile");
		addBehaviour(new ProfileManager(age, occupation, gender, intresets));
	}
	
	private class TourRecommendation extends Behaviour {
		private int step = 0;
//		private MessageTemplate mt;
		
		public TourRecommendation() {
			super(null);
		}
		
		@Override
		public void action() {
			switch (step) {
			case 0:
				log.info("Step 0. Propose for Tour Recommendation");
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for(AID tourAgent : tourGuideAgents) {
					cfp.addReceiver(tourAgent);
				}
				cfp.setContent(userProfile.getInterests());
				cfp.setConversationId("tour-recommend");
				cfp.setReplyWith("cfp" + System.currentTimeMillis());
				myAgent.send(cfp);
				
//				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("tour-recommend"),
//						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				
				step = 1;
				break;
			case 1:
				log.info("Step 1. Receive Tour Recommendation");
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
				ACLMessage reply = myAgent.receive(mt);
				log.info("XXXX " + reply);
				if (reply != null) {
					log.info("YYYY " + reply.getPerformative());
					System.out.println("Step 1,  Recommenation " + reply.getContent());
					
				} else {
					block();
				}
				step = 2;
				break;
				
			}
			
		}

		@Override
		public boolean done() {
			return step == 2;
		}
		
	}
	
	private class TourGuideManager extends TickerBehaviour {

		private TourGuideManager(Agent agent) {
			super(agent, 5000);
		}
		
		@Override
		protected void onTick() {
			log.info("Search for TourGuideAgent");
			DFAgentDescription template = new DFAgentDescription(); 
			ServiceDescription sd = new ServiceDescription();
			sd.setType("Publish-tourguide");
			template.addServices(sd);
			
			try {
				DFAgentDescription[] result = DFService.search(myAgent, template);
				for (int i = 0; i < result.length; ++i) {
					if(tourGuideAgents.contains(result[i].getName())) {
						log.info("TourGuideAgent " + result[i].getName() + " is already in list");
					} else {
						log.info("TourGuideAgent " + result[i].getName() + " is added to list");
						tourGuideAgents.add(result[i].getName());
					}
				}
			} catch (FIPAException e) {
				log.severe("Error in TourGuideAgent search: " + e.getMessage());
			}
			
			
		}
		
	}
	
	private class ProfileManager extends OneShotBehaviour {
		private Profile profile;
		
		private ProfileManager(int age, String occupation, String gender, String[] interests) {
			this.profile = new Profile(age, occupation, gender, interests);
		}
		
		@Override
		public void action() {
			userProfile = profile;
		}
		
	}
	
	private class Profile {
		private int age;
		private String occupation;
		private String gender;
		private String[] interests;
		
		private Profile(int age, String occupation, String gender, String[] interests) {
			this.age = age;
			this.occupation = occupation;
			this.gender = gender;
			this.interests = interests;
		}
		
		public String getInterests() {
			StringBuilder sb = new StringBuilder();
			for(String s : interests) {
				sb.append(s).append(",");
			}
			return sb.toString();
		}
		
	}
}

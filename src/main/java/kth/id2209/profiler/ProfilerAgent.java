/**
 * 
 */
package kth.id2209.profiler;

import java.util.logging.Logger;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * @author pradeeppeiris
 *
 */
public class ProfilerAgent extends Agent {

	private static final Logger log = Logger.getLogger(ProfilerAgent.class.getName());
	
	private ProfilerGui gui;
	
	protected void setup() {
		log.info("Initailize Profiler Agent");
		
		log.info("Show Profiler GUI");
		gui = new ProfilerGuiImpl();
		gui.setAgent(this);
		gui.show();
	}
	
	public void updateProfile(int age, String occupation, String gender, String[] intresets) {
		addBehaviour(new ProfileManager(age, occupation, gender, intresets));
	}
	
	private class ProfileManager extends OneShotBehaviour {
		private int age;
		private String occupation;
		private String gender;
		private String[] interests;
		
		private ProfileManager(int age, String occupation, String gender, String[] interests) {
			this.age = age;
			this.occupation = occupation;
			this.gender = gender;
			this.interests = interests;
		}
		
		@Override
		public void action() {
			log.info("Notify TourGuide Agents about profile change");
			
		}
		
	}
}

/**
 * 
 */
package bookTrading.seller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author pradeeppeiris
 *
 */
public class BookSellerAgent extends Agent {
	// The catalogue of books available for sale
	private Map catalogue = new HashMap();

	// The GUI to interact with the user
	private BookSellerGui myGui;

	/**
	 * Agent initializations
	 */
	protected void setup() {
		// Create and show the GUI
		myGui = new BookSellerGuiImpl();
		myGui.setAgent(this);
		myGui.show();

		// Add the behaviour serving calls for price from buyer agents
		addBehaviour(new CallForOfferServer());

		// Add the behaviour serving purchase requests from buyer agents
		addBehaviour(new PurchaseOrderServer());
	}

	protected void takeDown() {
		if (myGui != null) {
			myGui.dispose();
		}
		System.out.println("Seller-agent " + getAID().getName() + " terminating.");
	}

	public void putForSale(String title, int initPrice, int minPrice, Date deadline) {
		addBehaviour(new PriceManager(this, title, initPrice, minPrice, deadline));
	}

	private class PriceManager extends TickerBehaviour {
		private String title;
		private int minPrice, deltaP, initPrice;
		private long initTime, deadline, deltaT, currentPrice;

		private PriceManager(Agent a, String t, int ip, int mp, Date d) {
			super(a, 5000);
			title = t;
			initPrice = ip;
			currentPrice = initPrice;
			deltaP = initPrice - mp;
			deadline = d.getTime();
			initTime = System.currentTimeMillis();
		}

		public void onStart() {
			catalogue.put(title, this);
			super.onStart();
		}

		public void onTick() {
			System.out.println("PriceManager onTick ");
			long currentTime = System.currentTimeMillis();
			if (currentTime > deadline) {
				// Deadline expired 
				myGui.notifyUser("Cannot sell book "+title);
				 catalogue.remove(title);
				stop();
			} else {
				// Compute the current price
				long elapsedTime = currentTime - initTime;
				currentPrice = currentPrice - 5;//deltaP * (elapsedTime / deltaT);
				System.out.println(">>> Current Price " + currentPrice);
			}
		}
		
		public long getCurrentPrice() { 
			return currentPrice;
		}
	}
	
	private class PurchaseOrderServer extends CyclicBehaviour {
		private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println(">>> Receive Purchase Order");
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				myAgent.send(reply);
			} else {
				block();
			}
		}
	}
	
	private class CallForOfferServer extends CyclicBehaviour {
		private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
		public void action() {
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				PriceManager pm = (PriceManager) catalogue.get(title);
				if (pm != null) {
					System.out.println(">>> The requested book is available for sale. current price " + pm.getCurrentPrice());
					reply.setPerformative(ACLMessage.PROPOSE);
				    reply.setContent(String.valueOf(pm.getCurrentPrice()));
				} else {
					System.out.println(">>> The requested book is not available for sale.");
					reply.setPerformative(ACLMessage.REFUSE);
				}
				myAgent.send(reply);
			} else {
				block();
			}
		}
	}
}


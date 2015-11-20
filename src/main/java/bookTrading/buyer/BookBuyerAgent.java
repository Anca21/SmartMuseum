/**
 * 
 */
package bookTrading.buyer;

import java.util.Date;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author pradeeppeiris
 *
 */
public class BookBuyerAgent extends Agent {
	// The list of known seller agents
	private Vector<AID> sellerAgents = new Vector<AID>();

	// The GUI to interact with the user
	private BookBuyerGui myGui;

	/**
	 * Agent initializations
	 */
	protected void setup() {
		// Printout a welcome message
		System.out.println("Buyer-agent " + getAID().getName() + " is ready.");

		// Get names of seller agents as arguments
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; ++i) {
				AID seller = new AID((String) args[i], AID.ISLOCALNAME);
				System.out.println("Add Seller-agent " + seller.getName());

				sellerAgents.addElement(seller);
			}
		}

		// Show the GUI to interact with the user
		myGui = new BookBuyerGuiImpl();
		myGui.setAgent(this);
		myGui.show();

	}

	/**
	 * Agent clean-up
	 */
	protected void takeDown() {
		// Dispose the GUI if it is there
		if (myGui != null) {
			myGui.dispose();
		}

		// Printout a dismissal message
		System.out.println("Buyer-agent " + getAID().getName() + "terminated.");
	}

	/**
	 * This method is called by the GUI when the user inserts a new book to buy
	 * 
	 * @param title
	 *            The title of the book to buy
	 * @param maxPrice
	 *            The maximum acceptable price to buy the book
	 * @param deadline
	 *            The deadline by which to buy the book
	 */
	public void purchase(String title, int maxPrice, Date deadline) {
		System.out.println("purchase book " + title);
		addBehaviour(new PurchaseManager(this, title, maxPrice, deadline));
	}

	private class PurchaseManager extends TickerBehaviour {
		private String title;
		private int maxPrice;
		private long deadline, initTime, deltaT;
		private long acceptablePrice;

		private PurchaseManager(Agent a, String t, int mp, Date d) {
			super(a, 5000);
			title = t;
			maxPrice = mp;
			acceptablePrice = mp;
			deadline = d.getTime();
			initTime = System.currentTimeMillis();
			deltaT = deadline - initTime;
		}

		public void onTick() {
			System.out.println("PurchaseManager onTick ");
			long currentTime = System.currentTimeMillis();
			if (currentTime > deadline) {
				myGui.notifyUser("Cannot buy book " + title);
				stop();
			} else {
				// Compute the currently acceptable price and start a
				// negotiation
//				long elapsedTime = currentTime - initTime;
				acceptablePrice = acceptablePrice + 5;// * (elapsedTime / deltaT);
				 myAgent.addBehaviour(new BookNegotiator(title, acceptablePrice, this));

//				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
//				for (int i = 0; i < sellerAgents.size(); ++i) {
//					// Send this message to all seller agents
//					cfp.addReceiver(sellerAgents.get(i));
//				}
//				cfp.setContent(title);
//				myAgent.send(cfp);

			}
		}
	}

	private class BookNegotiator extends Behaviour {
		private String title;
		private long maxPrice;
		private PurchaseManager manager;
		private AID bestSeller;
		private long bestPrice;
		private int repliesCnt = 0;

		private MessageTemplate mt;
		private int step = 0;

		public BookNegotiator(String t, long p, PurchaseManager m) {
			super(null);
			title = t;
			maxPrice = p;
			bestPrice = p;
			manager = m;
		}

		public void action() {
			switch (step) {
			case 0:
				System.out.println(">>> Step 0 ");
				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < sellerAgents.size(); ++i) {
					cfp.addReceiver(sellerAgents.get(i));
				}
				cfp.setContent(title);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp" + System.currentTimeMillis());
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;

			case 1:
				System.out.println(">>> Step 1 ");
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					System.out.println(">>> Step 1,  reply not null ");
					// Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						System.out.println(">>> Step 1,  is an offer ");
						// This is an offer
						int price = Integer.parseInt(reply.getContent());
						System.out.println(">>> Step 1,  offer price " + price + " best price " + bestPrice);
						if (bestSeller == null || price < bestPrice) {
							// This is the best offer at present 
							bestPrice = price;
							System.out.println(">>> Step 1,  best seller " + reply.getSender());
							
							bestSeller = reply.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= sellerAgents.size()) {
						// We received all replies
						step = 2;
					}
				} else {
					block();
				}
				break;
			case 2:
				System.out.println(">>> Step 2 ");
				if (bestSeller != null && bestPrice <= maxPrice) {
					System.out.println(">>>  bestPrice " + bestPrice + " maxPrice" + maxPrice);
					ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestSeller);
					order.setContent(title);
					order.setConversationId("book-trade");
					order.setReplyWith("order" + System.currentTimeMillis());
					myAgent.send(order);
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
							MessageTemplate.MatchInReplyTo(order.getReplyWith()));
					step = 3;
				} else {
					System.out.println("<<<<<  bestPrice " + bestPrice + " maxPrice" + maxPrice);
					step = 4;
				}
				break;
			case 3:
				System.out.println(">>> Step 3 ");
				reply = myAgent.receive(mt);
				if (reply != null) {
					System.out.println(">>> Step 3 recived inform");
					if (reply.getPerformative() == ACLMessage.INFORM) {
						myGui.notifyUser("Book " + title + " successfully purchased. Price = " + bestPrice);
						manager.stop();
					}
					step = 4;

				} else {
					block();
				}
				break;
			}

		}

		public boolean done() {
			return step == 4;
		}
	}
}

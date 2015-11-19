/**
 * 
 */
package bookTrading.buyer;

import java.util.Date;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * @author pradeeppeiris
 *
 */
public class BookBuyerAgent extends Agent {
	// The list of known seller agents
	private Vector sellerAgents = new Vector();
	
	// The GUI to interact with the user 
	private BookBuyerGui myGui;
	
	/**
	   * Agent initializations
	   */
	protected void setup() {
		// Printout a welcome message 
		System.out.println("Buyer-agent "+ getAID().getName()+" is ready.");
		
		// Get names of seller agents as arguments 
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; ++i) {
				AID seller = new AID((String) args[i], AID.ISLOCALNAME);
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
	* This method is called by the GUI when the user inserts a new 
	* book to buy
	* @param title The title of the book to buy
	* @param maxPrice The maximum acceptable price to buy the book 
	* @param deadline The deadline by which to buy the book
	*/
	public void purchase(String title, int maxPrice, Date deadline) {
		System.out.println("purchase book " + title);
		addBehaviour(new PurchaseManager(this, title, maxPrice, deadline));
	}
	
	private class PurchaseManager extends TickerBehaviour {
		private String title;
		private int maxPrice;
		private long deadline, initTime, deltaT;
		
		private PurchaseManager(Agent a, String t, int mp, Date d) {
			super(a, 1000);
			title = t;
			maxPrice = mp;
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
				// Compute the currently acceptable price and start a negotiation
				long elapsedTime = currentTime - initTime;
				long acceptablePrice = maxPrice * (elapsedTime / deltaT);
//				myAgent.addBehaviour(new BookNegotiator(title,
//					     acceptablePrice, this));
			}
		}
	}
}

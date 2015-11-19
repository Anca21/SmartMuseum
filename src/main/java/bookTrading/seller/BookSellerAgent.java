/**
 * 
 */
package bookTrading.seller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * @author pradeeppeiris
 *
 */
public class BookSellerAgent extends Agent {
	// The catalogue of books available for sale
	private Map catalogue = new HashMap();

	// The GUI to interact with the user
//	private BookSellerGui myGui;

	/**
	 * Agent initializations
	 */
	protected void setup() {
		// Create and show the GUI
//		myGui = new BookSellerGuiImpl();
//		myGui.setAgent(this);
//		myGui.show();

		// Add the behaviour serving calls for price from buyer agents
//		addBehaviour(new CallForOfferServer());

		// Add the behaviour serving purchase requests from buyer agents
//		addBehaviour(new PurchaseOrderServer());
	}

	protected void takeDown() {
//		if (myGui != null) {
//			myGui.dispose();
//		}
		System.out.println("Seller-agent " + getAID().getName() + " terminating.");
	}

	public void putForSale(String title, int initPrice, int minPrice, Date deadline) {
//		addBehaviour(new PriceManager(this, title, initPrice, minPrice, deadline);
	}

	private class PriceManager extends TickerBehaviour {
		private String title;
		private int minPrice, deltaP, initPrice;
		private long initTime, deadline, deltaT, currentPrice;

		private PriceManager(Agent a, String t, int ip, int mp, Date d) {
			super(a, 60000);
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
			long currentTime = System.currentTimeMillis();
			if (currentTime > deadline) {
				// Deadline expired myGui.notifyUser("Cannot sell book "+title);
				// catalogue.remove(title);
				stop();
			} else {
				// Compute the current price
				long elapsedTime = currentTime - initTime;
				currentPrice = initPrice - deltaP * (elapsedTime / deltaT);
			}
		}
		
		public long getCurrentPrice() { 
			return currentPrice;
		}
	}
}


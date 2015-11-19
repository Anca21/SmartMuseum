/**
 * 
 */
package bookTrading.buyer;

/**
 * @author pradeeppeiris
 *
 */
public interface BookBuyerGui {
	
	void setAgent(BookBuyerAgent a);

	void show();

	void hide();

	void dispose();
	
	void notifyUser(String message);
}
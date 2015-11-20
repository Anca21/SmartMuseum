/**
 * 
 */
package bookTrading.buyer;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author pradeeppeiris
 *
 */
public class BookBuyerGuiImpl implements BookBuyerGui {

	private static final int APP_WINDOW_HEIGHT = 200;
	private static final int APP_WINDOW_WIDTH = 300;
	private static final String APP_TITLE = "Buyer";
	
	private BookBuyerAgent buyerAgent;
	
	private Frame mainFrame;
	
	Label notificationLbl = new Label();
	
	@Override
	public void setAgent(BookBuyerAgent a) {
		this.buyerAgent = a;
	}

	@Override
	public void show() {
		System.out.println("Show UI");
		launch();
		
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyUser(String message) {
		notificationLbl.setText(message);
		
	}

	@Override
	public void dispose() {
		System.out.println(">>>>>>  dispose UI");
		
	}

	private void launch() {
		mainFrame = new Frame("Buyer");
		mainFrame.setSize(APP_WINDOW_WIDTH, APP_WINDOW_HEIGHT);
		mainFrame.setLayout(new GridLayout(4, 2, 5, 5));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		Label bookToBueLbl = new Label("Book to Buy");
		mainFrame.add(bookToBueLbl);
		
		final TextField bookToBuy = new TextField();
		mainFrame.add(bookToBuy);
		
		Label maxPriceLbl = new Label("Max Price");
		mainFrame.add(maxPriceLbl);
		
		final TextField maxPrice = new TextField();
		mainFrame.add(maxPrice);

		Button button = new Button("Buy");
		mainFrame.add(button);
		
		mainFrame.add(notificationLbl);
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					buyerAgent.purchase(bookToBuy.getText(), Integer.valueOf(maxPrice.getText()), new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-22"));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		mainFrame.setVisible(true);
	}
}

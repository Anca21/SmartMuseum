/**
 * 
 */
package bookTrading.seller;

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
public class BookSellerGuiImpl implements BookSellerGui {

	private static final int APP_WINDOW_HEIGHT = 200;
	private static final int APP_WINDOW_WIDTH = 300;
	private static final String APP_TITLE = "Buyer";
	
	private BookSellerAgent sellerAgent;
	
	private Frame mainFrame;
	
	@Override
	public void setAgent(BookSellerAgent a) {
		this.sellerAgent = a;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		System.out.println(">>>>>>  dispose UI");
		
	}

//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		primaryStage.setTitle(APP_TITLE);
//		primaryStage.setResizable(false);
//		GridPane gridPane = new GridPane();
//		gridPane.setVgap(5);
//		gridPane.setHgap(5);
//		
//		Label bookToBueLbl = new Label("Book to Buy");
//		gridPane.add(bookToBueLbl, 1, 1); 
//		
//		final TextField bookToBuy = new TextField();
//		gridPane.add(bookToBuy, 2, 1); 
//		
//		Label maxPriceLbl = new Label("Max Price");
//		gridPane.add(maxPriceLbl, 1, 2); 
//		
//		final TextField maxPrice = new TextField();
//		gridPane.add(maxPrice, 2, 2); 
//		
//		Label deadlineLbl = new Label("Deadline");
//		gridPane.add(deadlineLbl, 1, 3); 
//		
//		final TextField deadline = new TextField();
//		gridPane.add(deadline, 2, 3); 
//		
//		final BookBuyerAgent iBuyerAgent = this.buyerAgent;
//		System.out.println("LLLLLL " + this.buyerAgent);
//		Button button = new Button("Buy");
//		button.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				try {
//					System.out.println("MMM " + bookToBuy.getText() + "  " + iBuyerAgent);
//					iBuyerAgent.purchase(bookToBuy.getText(), Integer.valueOf(maxPrice.getText()), 
//							new SimpleDateFormat("yyyy-MM-dd").parse(deadline.getText()));
//				} catch (NumberFormatException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		gridPane.add(button, 1, 4, 2, 1); 
//		
//		Scene scene = new Scene(gridPane, APP_WINDOW_WIDTH, APP_WINDOW_HEIGHT);
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}

	private void launch() {
		mainFrame = new Frame("Seller");
		mainFrame.setSize(APP_WINDOW_WIDTH, APP_WINDOW_HEIGHT);
		mainFrame.setLayout(new GridLayout(3, 2, 5, 5));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		Label bookToSellLbl = new Label("Book to Sell");
		mainFrame.add(bookToSellLbl);
		
		final TextField bookToSell = new TextField();
		mainFrame.add(bookToSell);
		
		Label minPriceLbl = new Label("Min Price");
		mainFrame.add(minPriceLbl);
		
		final TextField minPrice = new TextField();
		mainFrame.add(minPrice);
		
		Label initPriceLbl = new Label("Init Price");
		mainFrame.add(initPriceLbl);
		
		final TextField initPrice = new TextField();
		mainFrame.add(initPrice);

		Button button = new Button("Sell");
		mainFrame.add(button);
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sellerAgent.putForSale(bookToSell.getText(), Integer.valueOf(initPrice.getText()), Integer.valueOf(minPrice.getText()), new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-21"));
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

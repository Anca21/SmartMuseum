/**
 * 
 */
package kth.id2209.profiler;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author pradeeppeiris
 *
 */
public class ProfilerGuiImpl implements ProfilerGui {
	private static final int APP_WINDOW_HEIGHT = 500;
	private static final int APP_WINDOW_WIDTH = 300;
	
	private ProfilerAgent agent;
	
	private Label recommendedTour = new Label();
	
	@Override
	public void setAgent(ProfilerAgent agent) {
		this.agent = agent;
	}

	@Override
	public void show() {
		launch();
	}

	@Override
	public void updateTourSuggestions() {
		// TODO Auto-generated method stub
		
	}
	
	private void launch() {
		Frame mainFrame = new Frame("Profiler-" + agent.getLocalName());
		mainFrame.setSize(APP_WINDOW_WIDTH, APP_WINDOW_HEIGHT);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		
		mainFrame.setLayout(new GridLayout(0, 2, 5, 5));
		
		mainFrame.add(new Label("Age"));
		final TextField age = new TextField();
		mainFrame.add(age);
		
		mainFrame.add(new Label("Occupation"));
		final TextField occupation = new TextField();
		mainFrame.add(occupation);
		
		mainFrame.add(new Label("Gender"));
		final TextField gender = new TextField();
		mainFrame.add(gender);
		
		mainFrame.add(new Label("Intrest"));
		final List intrest = new List(4, true);
		intrest.add("Portrait Paintings");
		intrest.add("Landscape Paintings");
		intrest.add("Photography");
		intrest.add("Fantacy");
		mainFrame.add(intrest);
		 
		Button profileUpdate = new Button("Update Profile");
		mainFrame.add(profileUpdate);
		profileUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				agent.updateProfile(Integer.valueOf(age.getText()), occupation.getText(), gender.getText(), intrest.getSelectedItems());
			}
			
		});
		
		Button tourRequest = new Button("Tour Request");
		mainFrame.add(tourRequest);
		tourRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				agent.tourRequest();
			}
		});
		
		mainFrame.add(recommendedTour);
		
		mainFrame.setVisible(true);
	}

}

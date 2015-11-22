/**
 * 
 */
package kth.id2209.curator;

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
public class CuratorGuiImpl implements CuratorGui {
	private static final int APP_WINDOW_HEIGHT = 500;
	private static final int APP_WINDOW_WIDTH = 300;
	
	private CuratorAgent agent;
	
	@Override
	public void setAgent(CuratorAgent agent) {
		this.agent = agent;
	}

	@Override
	public void show() {
		launch();
	}

	@Override
	public void addArtifact() {
		// TODO Auto-generated method stub
		
	}
	
	private void launch() {
		Frame mainFrame = new Frame("Curator-" + agent.getLocalName());
		mainFrame.setSize(APP_WINDOW_WIDTH, APP_WINDOW_HEIGHT);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		
		mainFrame.setLayout(new GridLayout(0, 2, 5, 5));
		
		mainFrame.add(new Label("Id"));
		final TextField id = new TextField();
		mainFrame.add(id);
		
		mainFrame.add(new Label("Name"));
		final TextField name = new TextField();
		mainFrame.add(name);
		
		mainFrame.add(new Label("Creator"));
		final TextField creator = new TextField();
		mainFrame.add(creator);
		
		mainFrame.add(new Label("Date of Create"));
		final TextField dateCreate = new TextField();
		mainFrame.add(dateCreate);
		
		mainFrame.add(new Label("Place of Create"));
		final TextField placeCreate = new TextField();
		mainFrame.add(placeCreate);
		
		mainFrame.add(new Label("Genre"));
		final List genre = new List(1, true);
		genre.add("Portrait Paintings");
		genre.add("Landscape Paintings");
		genre.add("Photography");
		genre.add("Fantacy");
		mainFrame.add(genre);
		 
		Button button = new Button("Add Artifact");
		mainFrame.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				agent.updateArtifacts(id.getText(), name.getText(), creator.getText(), 
						dateCreate.getText(), placeCreate.getText(), genre.getSelectedItem());
			}
			
		});
		mainFrame.setVisible(true);
	}

}

package test;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class MyAgent extends Agent {
	protected void setup() {
		System.out.println("Adding waker behaviour");
		addBehaviour(new TickerBehaviour(this, 10000) {
			protected void onTick() { // perform operation X
				System.out.println("perform operation X");
			}
			
		});
	}

}

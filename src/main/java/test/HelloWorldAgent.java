package test;

import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HelloWorldAgent extends Agent {

	public void setup() {
//		System.out.println("Hello. My name is "+getLocalName());
		// Printout a welcome message
		System.out.println("Hello World. I’m an agent!"); 
		System.out.println("My local-name is "+ getAID().getLocalName()); 
		System.out.println("My GUID is "+getAID().getName()); 
		System.out.println("My addresses X are:");
		Iterator it = getAID().getAllAddresses();
		while (it.hasNext()) {
			System.out.println("- "+it.next()); 
		}
		
		
		System.out.println("My arguments are:"); Object[] args = getArguments();
		if (args != null) {
			for (int i = 0; i < args.length; ++i) { 
				System.out.println("- "+args[i]);
			} 
		}
		
//		addBehaviour(new CyclicBehaviour() {
//			public void action() {
//				ACLMessage msgRx = receive();
//				if (msgRx != null) {
//					System.out.println(msgRx);
//					ACLMessage msgTx = msgRx.createReply();
//					msgTx.setContent("Hello!");
//					send(msgTx);
//				} else {
//					block();
//				}
//			}
//		});
	}

		
}

/**
 * @author wiz-rd/mvm
 * @version 1.0
 */

package main;

import javax.swing.JFrame;

public class Main {
	/**
	 * @param args not used
	 */
	public static void main(String[] args) { // just initiates and starts all other code, really
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("TerraHail");
		
		SimWindow simWin = new SimWindow();
		window.add(simWin);
		window.pack();
		
		window.setSize(1000, 1000);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setResizable(false);
		
		simWin.startSimThread();
		
	}

}

package connection;

import gui.MainFrame;
import database.DatabaseHandler;

public class MainClass {

	public static void main(String[] args) {
		DatabaseHandler.getInstance("root", "root");
		MainFrame mainFrame = MainFrame.getInstance();
		mainFrame.setVisible(true);
	}
}
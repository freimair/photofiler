package itemfiler;
import itemfiler.model.Database;
import itemfiler.ui.MainWindow;

import java.io.File;

public class Photomanager {

	public Photomanager() {
		// TODO Auto-generated constructor stub
	}
	
	public void open(String path) {
		Database.init(path);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Photomanager DUT = new Photomanager();
		DUT.open(new File(".").getAbsolutePath() + "/../playground/database");

		MainWindow window = new MainWindow();
		window.open();

		Database.closeConnection();
	}

}

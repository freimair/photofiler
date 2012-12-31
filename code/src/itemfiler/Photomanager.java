package itemfiler;
import itemfiler.model.Database;
import itemfiler.ui.MainWindow;

import java.io.File;

public class Photomanager {

	public static File home = new File(new File(".").getAbsolutePath()
			+ "/../playground/");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Database.init(home.getAbsolutePath() + File.separatorChar
				+ "database.h2.db");

		MainWindow window = new MainWindow();
		window.open();

		Database.closeConnection();
	}

}

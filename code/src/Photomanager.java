import java.io.File;


public class Photomanager {

	private Database database;

	public Photomanager() {
		// TODO Auto-generated constructor stub
	}
	
	public void open(String path) {
		database = new Database(path);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Photomanager DUT = new Photomanager();
		DUT.open(new File(".").getAbsolutePath() + "/../playground/database");

		Database.closeConnection();
	}

}

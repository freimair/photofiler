import java.sql.SQLException;
import java.util.List;


public class Item {
	// ######### STATICS #########
	public static void add(String path) {
		try {
			Database.execute("INSERT INTO objects (path) VALUES ('" + path
					+ "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<String> getAll() {
		try {
			return Database.getStringList("SELECT path FROM objects");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

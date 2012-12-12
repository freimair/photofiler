package itemfiler.model;

import java.sql.SQLException;
import java.util.List;


public class Tag {
	// ######### STATICS #########
	public static void create(String name) {
		try {
			Database.execute("INSERT INTO tags (name) VALUES ('" + name + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<String> getFilteredStrings(String string) {
		try {
			return Database
					.getStringList("SELECT name FROM tags WHERE name LIKE '"
							+ string + "%'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

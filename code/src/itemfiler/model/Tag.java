package itemfiler.model;

import java.sql.SQLException;
import java.util.List;


public class Tag {
	// ######### STATICS #########
	public static void create(String name) {
		String tagname = "";
		for (String current : name.split("-")) {

			// accumulate tag name one by one
			if (!tagname.equals(""))
				tagname += "-";
			tagname += current;

			// check if tag exists
			try {
				Database.getString("SELECT name FROM tags WHERE name = '"
						+ tagname + "'");
			} catch (SQLException e) {
				// if not create
				try {
					Database.execute("INSERT INTO tags (name) VALUES ('"
							+ tagname
							+ "')");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
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

	public static void rename(String oldName, String newName) {
		try {
			List<String> affectedTags = Database
					.getStringList("SELECT name FROM tags WHERE name LIKE '"
							+ oldName + "%'");
			for (String current : affectedTags)
				Database.execute("UPDATE tags SET name='"
						+ current.replaceFirst(oldName, newName)
						+ "' WHERE name = '" + current + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

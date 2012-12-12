package itemfiler.model;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Item {

	private static Map<Integer, Item> cache = new HashMap<Integer, Item>();

	// ######### STATICS #########
	public static void create(String path) {
		try {
			Database.execute("INSERT INTO objects (path) VALUES ('" + path
					+ "')");

			// add to cache
			int newId = Database.getInteger("SELECT MAX(oid) FROM objects");
			cache.put(newId, new Item(newId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Collection<Item> getAll() {
		updateCache();

		return cache.values();
	}

	private static void updateCache() {
		try {
			List<Integer> allIds = Database
					.getIntegerList("SELECT oid FROM objects");
			allIds.removeAll(cache.keySet());

			for (int currentId : allIds)
				cache.put(currentId, new Item(currentId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ####### NON-STATICS #######
	private int id;
	private File path;

	private Item(int currentId) {
		id = currentId;
		try {
			path = new File(
					Database.getString("SELECT path FROM objects WHERE oid = "
							+ currentId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		try {
			String tmp = Database
					.getString("SELECT name FROM objects WHERE oid = "
					+ id);
			return null == tmp ? path.getName() : tmp;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}

	public void setName(String newName) {
		try {
			Database.execute("UPDATE objects SET name='" + newName
					+ "' WHERE oid='" + id + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

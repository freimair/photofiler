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
			int newId = Database.getInteger("SELECT MAX(oid) FROM photos");
			cache.put(newId, new Item(newId, new File(path)));
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
				cache.put(
						currentId,
						new Item(
								currentId,
								new File(
										Database.getString("SELECT path FROM objects WHERE oid = "
												+ currentId))));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ####### NON-STATICS #######
	private int id;
	private File path;

	private Item(int currentId, File file) {
		id = currentId;
		path = file;
	}

	public String getName() {
		return path.getName();
	}
}

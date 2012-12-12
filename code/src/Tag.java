import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tag {
	// ######### STATICS #########
	private static Map<Integer, Tag> cache = new HashMap<Integer, Tag>();

	public static void create(String name) {
		try {
			Database.execute("INSERT INTO tags (name) VALUES ('" + name + "')");

			// add to cache
			int newId = Database.getInteger("SELECT MAX(tid) FROM tags");
			cache.put(newId, new Tag(newId, name));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Collection<Tag> getAll() {
		updateCache();

		return cache.values();
	}

	private static void updateCache() {
		try {
			List<Integer> allIds = Database
					.getIntegerList("SELECT tid FROM tags");
			allIds.removeAll(cache.keySet());

			for (int currentId : allIds)
				cache.put(
						currentId,
						new Tag(
								currentId,
 Database
								.getString("SELECT name FROM tags WHERE tid = "
										+ currentId)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ####### NON-STATICS #######

	public Tag(int currentId, String string) {
		// TODO Auto-generated constructor stub
	}
}

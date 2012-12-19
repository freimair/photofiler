package itemfiler.model;

import itemfiler.Photomanager;
import itemfiler.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Item {

	private static Map<Integer, Item> cache = new HashMap<Integer, Item>();

	// ######### STATICS #########
	public static void create(String path) {
		try {
			// move it to internal storage
			File file = new File(path);
			File target = new File(Photomanager.home.getAbsolutePath()
					+ File.separatorChar
					+ "data"
					+ File.separatorChar + file.getName());

			if (!target.getParentFile().exists())
				target.getParentFile().mkdirs();

			Files.move(file.toPath(), target.toPath());

			Database.execute("INSERT INTO objects (path) VALUES ('"
					+ FileUtils.getRelativePath(Photomanager.home, target)
					+ "')");

			// add to cache
			int newId = Database.getInteger("SELECT MAX(oid) FROM objects");
			cache.put(newId, new Item(newId));
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Collection<Item> getAll() {
		updateCache();

		return cache.values();
	}

	public static Collection<Item> getFiltered(Collection<String> tags,
			boolean includeUntagged, boolean includeTrash) {

		updateCache();

		try {
			List<Integer> ids = new ArrayList<>();
			if (0 > tags.size()) {
				String sql = "SELECT oid FROM objects_tags JOIN tags ON objects_tags.tid=tags.tid";
				if (0 < tags.size())
					sql += " WHERE";
				for (String current : tags)
					sql += " tags.name LIKE '" + current + "%'";

				ids.addAll(Database.getIntegerList(sql));
			}

			if (includeUntagged)
				ids.addAll(Database
						.getIntegerList("SELECT oid FROM objects WHERE oid IS NOT (SELECT oid FROM objects_tags)"));

			if(includeTrash)
				ids.addAll(Database
						.getIntegerList("SELECT oid FROM objects WHERE trash IS TRUE"));

			HashMap<Integer, Item> result = new HashMap<Integer, Item>(cache);
			result.keySet().retainAll(ids);
			return result.values();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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

	public void addTags(List<String> tags) {
		for (String current : tags)
			addTag(current);
	}

	public void addTag(String tag) {
		try {
			int tid = Database.getInteger("SELECT tid FROM tags WHERE name='"
					+ tag
					+ "'");
			Database.execute("INSERT INTO objects_tags (oid, tid) VALUES ("
					+ id + "," + tid + ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getTags() {
		try {
			return Database
					.getStringList("SELECT name FROM tags JOIN objects_tags ON tags.tid=objects_tags.tid WHERE objects_tags.oid='"
							+ id + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void removeTag(String tag) {
		int tid;
		try {
			tid = Database.getInteger("SELECT tid FROM tags WHERE name='" + tag
					+ "'");
			Database.execute("DELETE FROM objects_tags WHERE tid='" + tid + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void moveToTrash() {
		try {
			Database.execute("UPDATE objects SET trash = TRUE WHERE oid='" + id
					+ "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

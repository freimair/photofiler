package itemfiler.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Filter {
	private Collection<String> tags;
	private boolean includeUntagged = false;
	private boolean includeTrash = false;

	public Filter(Collection<String> tags, boolean includeUntagged,
			boolean includeTrash) {
		this.tags = tags;
		this.includeUntagged = includeUntagged;
		this.includeTrash = includeTrash;
	}

	public Filter() {
		this.tags = new ArrayList<>();
	}

	Map<String, Collection<String>> getTags() {
		Map<String, Collection<String>> result = new HashMap<>();
		String start = "";
		for(String current : tags) {
			String currentStart = current.split("-", 0)[0];
			if (!start.equals(currentStart))
				result.put(currentStart, new ArrayList<String>());

			result.get(currentStart).add(current);
		}

		return result;

	}

	public boolean isIncludeUntagged() {
		return includeUntagged;
	}

	public boolean isIncludeTrash() {
		return includeTrash;
	}

	public boolean isShowAll() {
		if (!tags.isEmpty())
			return false;
		if (includeUntagged)
			return false;
		if (includeTrash)
			return false;
		return true;
	}
}

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

	public Collection<String> getDates() {
		Collection<String> result = new HashMap<>(getByRootElement())
				.get("date");
		return null == result ? new ArrayList<String>() : result;
	}

	public Map<String, Collection<String>> getTags() {
		HashMap<String, Collection<String>> result = new HashMap<>(
				getByRootElement());
		result.keySet().remove("date");
		return result;
	}

	private Map<String, Collection<String>> getByRootElement() {
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

package itemfiler.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Filter {
	private Collection<String> includeTags;
	private Collection<String> excludeTags;
	private boolean includeUntagged = false;
	private boolean includeTrash = false;

	public Filter() {
		this.includeTags = new ArrayList<>();
		this.excludeTags = new ArrayList<>();
	}

	public Filter(List<String> include, List<String> exclude) {
		this.includeTags = include;
		this.excludeTags = exclude;

		this.includeUntagged = includeTags.contains("untagged");
		this.includeTrash = includeTags.contains("trash");
	}

	// public Collection<String> getDates() {
	// Collection<String> result = new HashMap<>(getByRootElement())
	// .get("date");
	// return null == result ? new ArrayList<String>() : result;
	// }

	// private Map<String, Collection<String>> getByRootElement() {
	// Map<String, Collection<String>> result = new HashMap<>();
	// String start = "";
	// for (String current : includeTags) {
	// String currentStart = current.split("-", 0)[0];
	// if (!start.equals(currentStart))
	// result.put(currentStart, new ArrayList<String>());
	//
	// result.get(currentStart).add(current);
	// }
	//
	// return result;
	//
	// }

	public Collection<String> getIncludedTags() {
		return includeTags;
	}

	public Collection<String> getExcludedTags() {
		return excludeTags;
	}

	public boolean isIncludeUntagged() {
		return includeUntagged;
	}

	public boolean isIncludeTrash() {
		return includeTrash;
	}

	public boolean isShowAll() {
		if (!includeTags.isEmpty())
			return false;
		if (includeUntagged)
			return false;
		if (includeTrash)
			return false;
		if (!excludeTags.isEmpty())
			return false;
		return true;
	}
}

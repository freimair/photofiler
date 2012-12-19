package itemfiler.model;

import java.util.ArrayList;
import java.util.Collection;

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

	public Collection<String> getTags() {
		return tags;
	}

	public boolean isIncludeUntagged() {
		return includeUntagged;
	}

	public boolean isIncludeTrash() {
		return includeTrash;
	}
}

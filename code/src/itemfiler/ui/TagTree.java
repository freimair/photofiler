package itemfiler.ui;

import itemfiler.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TagTree extends Composite {

	private Tree tree;

	public TagTree(Composite parent, int style) {
		super(parent, style);

		this.setLayout(new FillLayout());
		tree = new Tree(this, SWT.BORDER | style);

		tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (SWT.CHECK == e.detail) {
					// maintain childrens checked state
					setChildrensCheckedState(((TreeItem) e.item).getChecked(),
							((TreeItem) e.item));

					// maintain parents checked state
					maintainParentsCheckedState(
							((TreeItem) e.item).getChecked(),
							((TreeItem) e.item));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		refresh();
	}

	private void setChildrensCheckedState(boolean checked, TreeItem item) {
		for (TreeItem current : item.getItems())
			setChildrensCheckedState(checked, current);

		item.setChecked(checked);
	}

	private void maintainParentsCheckedState(boolean checked, TreeItem item) {
		try {
			if (checked)
				for (TreeItem current : item.getParentItem().getItems())
					if (!current.getChecked())
						return;

			item.getParentItem().setChecked(checked);
			maintainParentsCheckedState(checked, item.getParentItem());
		} catch (NullPointerException e) {
			// reached the tree's root
		}
	}

	public void refresh() {
		// clean
		for (TreeItem current : tree.getItems())
			current.dispose();

		List<Tag> sorted = new ArrayList<>(Tag.getAll());
		Collections.sort(sorted, new Comparator<Tag>() {

			@Override
			public int compare(Tag o1, Tag o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});

		build(null, "", sorted);

	}

	/**
	 * @param parent
	 * @param currentPrefix
	 * @param tags
	 *            alphabetically ASC sorted list of tags.
	 */
	private void build(TreeItem parent, String currentPrefix, List<Tag> tags) {
		try {
			for (;;)
				if (tags.get(0).getName().startsWith(currentPrefix)) {
					String[] tmp = tags.get(0).getName()
							.substring(currentPrefix.length()).split("-");

					TreeItem item;
					if (null == parent)
						item = new TreeItem(tree, SWT.NONE);
					else
						item = new TreeItem(parent, SWT.NONE);
					item.setText(tmp[0]);

					if (1 == tmp.length)
						tags.remove(0);
					build(item, currentPrefix + tmp[0] + "-", tags);
				} else
					return;
		} catch (IndexOutOfBoundsException e) {
		}

	}
}

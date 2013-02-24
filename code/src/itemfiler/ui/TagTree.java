package itemfiler.ui;

import itemfiler.model.Filter;
import itemfiler.model.Tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TagTree extends Refreshable {

	private Tree tree;
	private MainWindow mainWindow;

	public TagTree(Composite parent, int style, MainWindow main) {
		super(parent, style);

		mainWindow = main;

		this.setLayout(new FillLayout());
		tree = new Tree(this, SWT.BORDER | style);
		tree.setData(this);

		tree.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (SWT.CHECK == e.detail) {
					TreeItem item = (TreeItem) e.item;

					// we have to negate the checked state answer because the
					// checked state is updated before we get here (the box was
					// checked when we clicked it)
					if (!item.getChecked()) {
						item.setChecked(!item.getGrayed());
						item.setGrayed(!item.getGrayed());
					}

					// maintain childrens checked state
					setChildrensCheckedState(new CheckedState(
							item.getChecked(), item.getGrayed()), item);

					// maintain parents checked state
					maintainParentsCheckedState(
							new CheckedState(item.getChecked(), item
									.getGrayed()), item);

					List<String> include = new ArrayList<>();
					gatherCheckedItems(null, include);

					List<String> exclude = new ArrayList<>();
					gatherBarredItems(null, exclude);

					mainWindow.setFilter(new Filter(include, exclude, include
							.remove("untagged"), include.remove("trash")));
					mainWindow.refresh((Refreshable) tree.getData());
				}
			}
		});

		final Menu contextMenu = new Menu(parent);
		MenuItem item1 = new MenuItem(contextMenu, SWT.PUSH);
		item1.setText("rename");
		item1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				RenameTagDialog dialog = new RenameTagDialog(getShell(), tree
						.getSelection()[0]);
				dialog.setBlockOnOpen(true);
				dialog.open();
				refresh();
			}
		});

		MenuItem item2 = new MenuItem(contextMenu, SWT.PUSH);
		item2.setText("move");
		item2.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				MoveTagDialog dialog = new MoveTagDialog(getShell(), tree
						.getSelection()[0]);
				dialog.setBlockOnOpen(true);
				dialog.open();
				refresh();
			}
		});

		parent.setMenu(contextMenu);

		tree.addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(MenuDetectEvent e) {
				contextMenu.setVisible(true);

			}
		});

		refresh();
	}

	private void gatherCheckedItems(TreeItem node, List<String> result) {
		try {
			if (node.getChecked())
				result.add(rebuildTagName(node));
			else
				for (TreeItem current : node.getItems())
					gatherCheckedItems(current, result);
		} catch (NullPointerException e) {
			// process root elements
			for (TreeItem current : tree.getItems())
				gatherCheckedItems(current, result);
		}
	}

	private void gatherBarredItems(TreeItem node, List<String> result) {
		try {
			if (node.getGrayed())
				result.add(rebuildTagName(node));
			else
				for (TreeItem current : node.getItems())
					gatherBarredItems(current, result);
		} catch (NullPointerException e) {
			// process root elements
			for (TreeItem current : tree.getItems())
				gatherBarredItems(current, result);
		}
	}

	private void setChildrensCheckedState(CheckedState checkedState,
			TreeItem item) {
		for (TreeItem current : item.getItems())
			setChildrensCheckedState(checkedState, current);

		item.setChecked(checkedState.getChecked());
		item.setGrayed(checkedState.getBarred());
	}

	private void maintainParentsCheckedState(CheckedState checkedState,
			TreeItem item) {
		try {
			if (checkedState.getChecked()) {
				for (TreeItem current : item.getParentItem().getItems())
					if (!current.getChecked())
						return;

				item.getParentItem().setChecked(checkedState.getChecked());
				maintainParentsCheckedState(checkedState, item.getParentItem());
			}
			if (checkedState.getBarred()) {
				for (TreeItem current : item.getParentItem().getItems())
					if (!current.getGrayed())
						return;

				item.getParentItem().setGrayed(checkedState.getBarred());
				maintainParentsCheckedState(checkedState, item.getParentItem());
			}
		} catch (NullPointerException e) {
			// reached the tree's root
		}
	}

	public void refresh() {
		// clean
		for (TreeItem current : tree.getItems())
			current.dispose();

		List<String> sorted = new ArrayList<>(Tag.getFilteredStrings(""));
		Collections.sort(sorted);

		build(null, "", sorted);

		if ((getStyle() & SWT.CHECK) > 0) {
			TreeItem dateTreeItem = new TreeItem(tree, SWT.NONE);
			dateTreeItem.setText("date");
			for (int year = 2005; year < Calendar.getInstance().get(
					Calendar.YEAR); year++) {
				TreeItem yearTreeItem = new TreeItem(dateTreeItem, SWT.NONE);
				yearTreeItem.setText(Integer.toString(year));
				for (int month = 1; month <= 12; month++) {
					TreeItem monthTreeItem = new TreeItem(yearTreeItem,
							SWT.NONE);
					monthTreeItem.setText(Integer.toString(month));
				}
			}

			TreeItem tmp = new TreeItem(tree, SWT.NONE);
			tmp.setText("untagged");

			tmp = new TreeItem(tree, SWT.NONE);
			tmp.setText("trash");
		}

	}

	/**
	 * @param parent
	 * @param currentPrefix
	 * @param tags
	 *            alphabetically ASC sorted list of tags.
	 */
	private void build(TreeItem parent, String currentPrefix, List<String> tags) {
		try {
			for (;;)
				if (tags.get(0).startsWith(currentPrefix)) {
					String[] tmp = tags.get(0)
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

	public List<String> getSelection() {
		List<String> result = new ArrayList<>();
		for (TreeItem current : tree.getSelection())
			result.add(rebuildTagName(current));
		return result;
	}

	private String rebuildTagName(TreeItem item) {
		if (null == item.getParentItem())
			return item.getText();
		else
			return rebuildTagName(item.getParentItem()) + "-" + item.getText();
	}
}

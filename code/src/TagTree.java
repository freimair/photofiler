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

		for(Tag current : Tag.getAll()) {
			TreeItem item0 = new TreeItem(tree, 0);
			item0.setText(current.getSimpleName());
		}
	}
}

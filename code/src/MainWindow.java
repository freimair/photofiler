import java.io.File;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class MainWindow extends ApplicationWindow {

	private Composite listComposite;

	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addToolBar(SWT.NONE);
	}

	protected Control createContents(Composite parent) {
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new FillLayout());

		final Tree tree = new Tree(container, SWT.BORDER | SWT.CHECK);

		for (int loopIndex1 = 0; loopIndex1 < 5; loopIndex1++) {
			TreeItem item0 = new TreeItem(tree, 0);
			item0.setText("Level 0 Item " + loopIndex1);
			for (int loopIndex2 = 0; loopIndex2 < 5; loopIndex2++) {
				TreeItem item1 = new TreeItem(item0, 0);
				item1.setText("Level 1 Item " + loopIndex2);
				for (int loopIndex3 = 0; loopIndex3 < 5; loopIndex3++) {
					TreeItem item2 = new TreeItem(item1, 0);
					item2.setText("Level 2 Item " + loopIndex3);
				}
			}
		}
		
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

		ScrolledComposite scrolledListComposite = new ScrolledComposite(
				container, SWT.V_SCROLL);
		scrolledListComposite.setExpandHorizontal(true);
		scrolledListComposite.setExpandVertical(true);

		listComposite = new Composite(scrolledListComposite, SWT.NONE);
		scrolledListComposite.setContent(listComposite);

		listComposite.setLayout(new RowLayout(SWT.VERTICAL));

		listComposite.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				Rectangle r = listComposite.getParent().getClientArea();
				((ScrolledComposite) listComposite.getParent())
						.setMinSize(listComposite.computeSize(r.width,
								SWT.DEFAULT));
			}
		});

		refresh();

		return container;
	}

	@Override
	protected Control createToolBarControl(Composite parent) {
		ToolBar toolbar = (ToolBar) super.createToolBarControl(parent);

		ToolItem addButton = new ToolItem(toolbar, SWT.PUSH);
		addButton.setText("add");
		addButton.setToolTipText("add a photo");
		addButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN
						| SWT.MULTI);
				dialog.setFilterExtensions(new String[] { "*.jpg;*.jpeg" });
				if (!"".equals(dialog.open())) {
					for(String current : dialog.getFileNames())
						Item.add(dialog.getFilterPath() + File.separatorChar + current);
					refresh();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return toolbar;
	}

	public void refresh() {
		for (Control current : listComposite.getChildren())
			current.dispose();

		for (String current : Item.getAll()) {
			Label label = new Label(listComposite, SWT.NONE);
			label.setText(current);
		}

		listComposite.redraw();
		getShell().layout();
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
	};

}

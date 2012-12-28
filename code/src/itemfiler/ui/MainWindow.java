package itemfiler.ui;

import itemfiler.model.Filter;
import itemfiler.model.Item;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class MainWindow extends ApplicationWindow {

	private ObjectList objectList;
	private DetailsArea detailsArea;

	private Set<Refreshable> refreshables = new HashSet<Refreshable>();
	private Set<Item> selected = new HashSet<Item>();
	private Filter filter;
	private boolean filterIncludeUntagged;
	private boolean filterIncludeTrash;

	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addToolBar(SWT.NONE);
	}

	protected Control createContents(Composite parent) {
		getShell().setSize(800, 800);
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new FillLayout());

		refreshables.add(new TagTree(container, SWT.CHECK, this));

		refreshables.add(new ObjectList(container, SWT.BORDER, this));

		detailsArea = new DetailsArea(container, SWT.NONE, this);
		refreshables.add(detailsArea);

		container.layout();
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
				dialog.setFilterExtensions(new String[] { "*.jpg;*.jpeg;*.JPG" });
				if (!"".equals(dialog.open())) {
					for(String current : dialog.getFileNames())
						Item.create(dialog.getFilterPath() + File.separatorChar + current);
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
		refresh(null);
	}

	public void refresh(Refreshable except) {
		for (Refreshable current : refreshables)
			if (!current.equals(except))
				current.refresh();
	}

	public void setSelected(Item selected) {
		this.selected.clear();
		addSelected(selected);
	}

	public void addSelected(Item selected) {
		this.selected.add(selected);
		detailsArea.refresh();
	}

	public void removeSelected(Item item) {
		this.selected.remove(item);
		detailsArea.refresh();
	}

	public Set<Item> getSelected() {
		return selected;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Filter getFilter() {
		return filter != null ? filter : new Filter();
	}
}

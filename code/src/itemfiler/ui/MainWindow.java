package itemfiler.ui;

import itemfiler.model.Filter;
import itemfiler.model.Item;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class MainWindow extends ApplicationWindow {

	private Set<Refreshable> refreshables = new HashSet<Refreshable>();
	private ObjectList objectList;
	private DetailsArea detailsArea;
	private TagTree tagTree;

	private Set<Item> selected = new HashSet<Item>();
	private Filter filter;

	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addToolBar(SWT.NONE);
	}

	protected Control createContents(Composite parent) {
		getShell().setSize(800, 800);
		final Composite container = (Composite) super.createContents(parent);

		tagTree = new TagTree(container, SWT.CHECK, this);
		refreshables.add(tagTree);

		final Sash leftSash = new Sash (container, SWT.VERTICAL);

		objectList = new ObjectList(container, SWT.BORDER, this);
		refreshables.add(objectList);

		final Sash rightSash = new Sash(container, SWT.VERTICAL);

		detailsArea = new DetailsArea(container, SWT.NONE, this);
		refreshables.add(detailsArea);
		container.setLayout(new FormLayout());

		FormData tagTreeFormData = new FormData();
		tagTreeFormData.left = new FormAttachment(0, 0);
		tagTreeFormData.right = new FormAttachment(leftSash);
		tagTreeFormData.top = new FormAttachment(0, 0);
		tagTreeFormData.bottom = new FormAttachment(100, 0);
		tagTree.setLayoutData(tagTreeFormData);

		final FormData leftSashData = new FormData();
		leftSashData.left = new FormAttachment(25, 0);
		leftSashData.top = new FormAttachment(0, 0);
		leftSashData.bottom = new FormAttachment(100, 0);
		leftSash.setLayoutData(leftSashData);

		FormData objectListFormData = new FormData();
		objectListFormData.left = new FormAttachment(leftSash);
		objectListFormData.right = new FormAttachment(rightSash);
		objectListFormData.top = new FormAttachment(0, 0);
		objectListFormData.bottom = new FormAttachment(100, 0);
		objectList.setLayoutData(objectListFormData);

		final FormData rightSashData = new FormData();
		rightSashData.left = new FormAttachment(75, 0);
		rightSashData.top = new FormAttachment(0, 0);
		rightSashData.bottom = new FormAttachment(100, 0);
		rightSash.setLayoutData(rightSashData);

		FormData detailsAreaFormData = new FormData();
		detailsAreaFormData.left = new FormAttachment(rightSash);
		detailsAreaFormData.right = new FormAttachment(100, 0);
		detailsAreaFormData.top = new FormAttachment(0, 0);
		detailsAreaFormData.bottom = new FormAttachment(100, 0);
		detailsArea.setLayoutData(detailsAreaFormData);

		final int limit = 100;
		leftSash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle sashRect = leftSash.getBounds();
				Rectangle shellRect = container.getClientArea();
				int right = shellRect.width - sashRect.width - limit;
				e.x = Math.max(Math.min(e.x, right), limit);
				if (e.x != sashRect.x) {
					leftSashData.left = new FormAttachment(e.x * 100
							/ leftSash.getParent().getBounds().width, 0);
					container.layout();
				}
			}
		});

		rightSash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle sashRect = rightSash.getBounds();
				Rectangle shellRect = container.getClientArea();
				int right = shellRect.width - sashRect.width - limit;
				e.x = Math.max(Math.min(e.x, right), limit);
				if (e.x != sashRect.x) {
					rightSashData.left = new FormAttachment(e.x * 100
							/ rightSash.getParent().getBounds().width, 0);
					container.layout();
				}
			}
		});

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

		ToolItem imageViewerButton = new ToolItem(toolbar, SWT.PUSH);
		imageViewerButton.setText("ImageViewer");
		imageViewerButton.setToolTipText("show/hide the ImageViewer window");
		imageViewerButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ImageViewer.show();
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

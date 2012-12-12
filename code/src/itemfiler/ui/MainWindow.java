package itemfiler.ui;
import itemfiler.model.Item;

import java.io.File;

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

	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addToolBar(SWT.NONE);
	}

	protected Control createContents(Composite parent) {
		getShell().setSize(800, 800);
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new FillLayout());

		new TagTree(container, SWT.CHECK);

		objectList = new ObjectList(container, SWT.BORDER);

		detailsArea = new DetailsArea(container, SWT.NONE, objectList);
		objectList.setDetailsArea(detailsArea);

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
				dialog.setFilterExtensions(new String[] { "*.jpg;*.jpeg" });
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
		objectList.refresh();
	}
}

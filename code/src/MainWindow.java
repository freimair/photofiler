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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class MainWindow extends ApplicationWindow {

	private Composite listComposite;
	private TagTree tagTree;

	public MainWindow() {
		super(null);
		setBlockOnOpen(true);
		addToolBar(SWT.NONE);
	}

	protected Control createContents(Composite parent) {
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new FillLayout());

		tagTree = new TagTree(container, SWT.NONE);

		ScrolledComposite scrolledListComposite = new ScrolledComposite(
				container, SWT.V_SCROLL);
		scrolledListComposite.setExpandHorizontal(true);
		scrolledListComposite.setExpandVertical(true);

		listComposite = new Composite(scrolledListComposite, SWT.NONE);
		scrolledListComposite.setContent(listComposite);

		listComposite.setLayout(new RowLayout());

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

		for (String current : Item.getAll())
			new ListItem(listComposite, SWT.NONE, current);

		listComposite.redraw();
		getShell().layout();
	}

}

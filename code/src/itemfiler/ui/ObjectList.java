package itemfiler.ui;

import itemfiler.model.Item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;


public class ObjectList extends Refreshable {

	private Composite listComposite;
	private MainWindow mainWindow;

	public ObjectList(Composite parent, int style, MainWindow mainWindow) {
		super(parent, style);

		this.mainWindow = mainWindow;

		this.setLayout(new FillLayout());

		ScrolledComposite scrolledListComposite = new ScrolledComposite(this,
				SWT.V_SCROLL);
		scrolledListComposite.setExpandHorizontal(true);
		scrolledListComposite.setExpandVertical(true);

		listComposite = new Composite(scrolledListComposite, SWT.NONE);
		scrolledListComposite.setContent(listComposite);

		listComposite.setLayout(new RowLayout());
		listComposite.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));

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
	}

	public void refresh() {
		for (Control current : listComposite.getChildren())
			current.dispose();

		RowData layoutData = new RowData(100, 60);

		for (final Item current : Item.getFiltered(mainWindow.getFilter())) {
			final ListItem tmp = new ListItem(listComposite, SWT.NONE, current);
			tmp.setLayoutData(layoutData);
			try {
				if (mainWindow.getSelected().contains(current))
					tmp.setSelected(true);
			} catch (NullPointerException e) {
				// nothing selected
			}
			tmp.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent e) {
					if (tmp.getSelected())
						mainWindow.removeSelected(current);
					else if ((e.stateMask & SWT.CTRL) > 0)
						mainWindow.addSelected(current);
					else
						mainWindow.setSelected(current);
					mainWindow.refresh();
				}
			});
		}

		listComposite.layout();
		this.layout();
	}
}

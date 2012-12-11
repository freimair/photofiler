import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


public class ObjectList extends Composite {

	private Composite listComposite;
	private String selected;
	private DetailsArea myDetailsArea;

	public ObjectList(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new FillLayout());

		ScrolledComposite scrolledListComposite = new ScrolledComposite(this,
				SWT.V_SCROLL);
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
	}

	public void refresh() {
		for (Control current : listComposite.getChildren())
			current.dispose();

		for (final String current : Item.getAll()) {
			ListItem tmp = new ListItem(listComposite, SWT.NONE, current);
			tmp.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent e) {
					selected = current;
					myDetailsArea.refresh();
				}
			});
		}

		listComposite.redraw();
		getShell().layout();
	}

	public String getSelected() {
		return selected;
	}

	public void setDetailsArea(DetailsArea detailsArea) {
		myDetailsArea = detailsArea;
	}

}

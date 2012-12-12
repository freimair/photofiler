package itemfiler.ui;
import itemfiler.model.Item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;


public class ListItem extends Composite {

	private Label iconLabel;
	private Label nameLabel;

	public ListItem(Composite parent, int style, Item current) {
		super(parent, style | SWT.TRANSPARENT);
		this.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		this.setLayout(new GridLayout(2, false));

		iconLabel = new Label(this, SWT.NONE);
		iconLabel.setImage(Display.getCurrent().getSystemImage(
				SWT.ICON_QUESTION));
		iconLabel.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		nameLabel = new Label(this, SWT.NONE);
		nameLabel.setText(current.getName());
		nameLabel.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
	}

	@Override
	public void addMouseListener(MouseListener listener) {
		super.addMouseListener(listener);

		iconLabel.addMouseListener(listener);
		nameLabel.addMouseListener(listener);
	}

}

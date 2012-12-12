package itemfiler.ui;

import org.eclipse.swt.widgets.Composite;

public abstract class Refreshable extends Composite {

	public Refreshable(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void refresh();
}

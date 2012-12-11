import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class ListItem extends Composite {

	public ListItem(Composite parent, int style, String path) {
		super(parent, style | SWT.BORDER);
		this.setLayout(new RowLayout());
		this.setLayoutData(new RowData(250, 100));

		Label label = new Label(this, SWT.NONE);
		label.setText(path);
	}

}

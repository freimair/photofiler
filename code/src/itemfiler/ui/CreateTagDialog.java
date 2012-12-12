package itemfiler.ui;
import itemfiler.model.Tag;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class CreateTagDialog extends Dialog {

	private Text newTag;

	protected CreateTagDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		newTag = new Text(container, SWT.BORDER);
		
		return container;
	}

	@Override
	protected void okPressed() {
		Tag.create(newTag.getText());

		super.okPressed();
	}

}

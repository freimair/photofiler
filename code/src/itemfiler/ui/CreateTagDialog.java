package itemfiler.ui;
import itemfiler.model.Tag;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
		container.setLayout(new GridLayout(1, false));

		Label newTagLabel = new Label(container, SWT.NONE);
		newTagLabel.setText("enter new tag name:");
		newTag = new Text(container, SWT.BORDER);
		newTag.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return container;
	}

	@Override
	protected void okPressed() {
		Tag.create(newTag.getText());

		super.okPressed();
	}

}

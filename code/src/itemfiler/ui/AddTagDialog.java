package itemfiler.ui;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class AddTagDialog extends Dialog {

	private TagTree tagTree;

	protected AddTagDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setMinimumSize(SWT.DEFAULT, 300);

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Label tagTreeLabel = new Label(container, SWT.NONE);
		tagTreeLabel.setText("tags:");

		tagTree = new TagTree(container, SWT.MULTI);
		tagTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.INTERNAL_ID, "new", false);
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void okPressed() {

		super.okPressed();
	}

	protected void newPressed() {
		CreateTagDialog dialog = new CreateTagDialog(getShell());
		dialog.open();
		tagTree.refresh();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.INTERNAL_ID == buttonId)
			newPressed();
		else
			super.buttonPressed(buttonId);
	}
}

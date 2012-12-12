import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class AddTagDialog extends Dialog {

	private TagTree tagTree;

	protected AddTagDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		tagTree = new TagTree(container, SWT.MULTI);

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

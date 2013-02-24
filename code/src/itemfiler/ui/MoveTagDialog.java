package itemfiler.ui;

import itemfiler.model.Tag;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

public class MoveTagDialog extends Dialog {

	private TreeItem fromTreeItem;
	private TagTree tagTree;

	protected MoveTagDialog(Shell parentShell, TreeItem current) {
		super(parentShell);
		fromTreeItem = current;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("move tag");
		getShell().setMinimumSize(300, 300);

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Label fromLabel = new Label(container, SWT.NONE);
		fromLabel.setText("move " + fromTreeItem.getText() + " to");

		tagTree = new TagTree(container, SWT.NONE, null);
		tagTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.INTERNAL_ID, "move to root",
				false);
		super.createButtonsForButtonBar(parent);
	}


	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.INTERNAL_ID == buttonId)
			moveToRootPressed();
		else
			super.buttonPressed(buttonId);
	}

	@Override
	protected void okPressed() {
		save(tagTree.getSelection().get(0));
		super.okPressed();
	}

	protected void moveToRootPressed() {
		save("");
		okPressed();
	}

	private void save(String targetTag) {
		TreeItem current = fromTreeItem;
		String fullTagName = "";
		do {
			fullTagName = current.getText() + "-" + fullTagName;
		} while (null != (current = current.getParentItem()));
		fullTagName = fullTagName.substring(0, fullTagName.length() - 1);
		
		String newFullTagName = targetTag;
		if (!"".equals(newFullTagName))
			newFullTagName += "-";
		newFullTagName += fromTreeItem.getText();
		
		Tag.rename(fullTagName, newFullTagName);
	}
}

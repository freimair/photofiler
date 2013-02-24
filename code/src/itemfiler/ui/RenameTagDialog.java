package itemfiler.ui;

import itemfiler.model.Tag;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class RenameTagDialog extends Dialog {

	private TreeItem fromTreeItem;
	private Text toText;

	protected RenameTagDialog(Shell parentShell, TreeItem current) {
		super(parentShell);
		fromTreeItem = current;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("rename tag");
		getShell().setMinimumSize(400, SWT.DEFAULT);

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		Label fromLabel = new Label(container, SWT.NONE);
		fromLabel.setText("current path");
		Text fromText = new Text(container, SWT.BORDER);
		fromText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fromText.setText(fromTreeItem.getText());
		fromText.setEnabled(false);

		Label toLabel = new Label(container, SWT.NONE);
		toLabel.setText("new path");
		toText = new Text(container, SWT.BORDER);
		toText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toText.setFocus();

		return container;
	}

	@Override
	protected void okPressed() {
		if(0 == toText.getText().length()) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR
					| SWT.OK);
			messageBox.setMessage("new Tagname is empty!");
			messageBox.open();
			return;
		}

		TreeItem current = fromTreeItem;
		String fullTagName = "";
		do {
			fullTagName = current.getText() + "-" + fullTagName;
		} while (null != (current = current.getParentItem()));
		fullTagName = fullTagName.substring(0, fullTagName.length() - 1);
		
		String newFullTagName = "";
		try {
			newFullTagName = fullTagName.substring(0,
					fullTagName.lastIndexOf("-"))
					+ "-";
		} catch (StringIndexOutOfBoundsException e) {
		}
		newFullTagName += toText.getText();
		
		Tag.rename(fullTagName, newFullTagName);

		super.okPressed();
	}

}

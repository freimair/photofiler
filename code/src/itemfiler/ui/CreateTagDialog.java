package itemfiler.ui;
import itemfiler.model.Tag;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class CreateTagDialog extends Dialog {

	private Text newTag;
	private List existingTagsList;

	protected CreateTagDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setMinimumSize(SWT.DEFAULT, 200);

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Label newTagLabel = new Label(container, SWT.DROP_DOWN);
		newTagLabel.setText("enter new tag name:");
		existingTagsList = new List(container, SWT.BORDER);
		existingTagsList.setLayoutData(new GridData(GridData.FILL_BOTH));
		existingTagsList.setEnabled(false);
		newTag = new Text(container, SWT.BORDER);
		newTag.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newTag.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (("" + e.character).matches("[a-zA-Z0-9-]")
						|| e.character == SWT.BS || e.character == SWT.DEL)
					refresh();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (SWT.BS == e.character)
					newTag.setSelection(newTag.getSelection().x - 1, newTag
							.getText().length());
				if ('-' == e.character)
					refresh();
			}
		});

		refresh();
		newTag.setFocus();

		return container;
	}

	@Override
	protected void okPressed() {
		Tag.create(newTag.getText());

		super.okPressed();
	}

	public void refresh() {
		String text = newTag.getText();
		String[] tags = Tag.getFilteredStrings(text).toArray(new String[] {});
		existingTagsList.setItems(tags);
		if (0 < tags.length)
			newTag.setText(tags[0]);
		else
			newTag.setText(text);
		newTag.setSelection(text.length(), newTag.getText().length());
	}

}

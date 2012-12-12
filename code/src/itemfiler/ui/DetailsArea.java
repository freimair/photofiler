package itemfiler.ui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class DetailsArea extends Composite {

	private ObjectList list;
	private Text nameLabel;
	private Composite tagsContainer;

	public DetailsArea(Composite parent, int style, ObjectList objectList) {
		super(parent, style | SWT.BORDER);
		list = objectList;

		this.setLayout(new GridLayout(1, false));

		Label nameLabelLabel = new Label(this, SWT.NONE);
		nameLabelLabel.setText("Name: ");
		nameLabel = new Text(this, SWT.BORDER);
		nameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label tagsContainerLabel = new Label(this, SWT.NONE);
		tagsContainerLabel.setText("Tags: ");
		tagsContainer = new Composite(this, SWT.NONE);
		tagsContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		tagsContainer.setLayout(layout);
		Button addTagButton = new Button(this, SWT.PUSH);
		addTagButton.setText("add tags");
		addTagButton.setToolTipText("add new tags");
		addTagButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				AddTagDialog dialog = new AddTagDialog(getShell());
				dialog.open();
			}
		});
		this.layout();
	}

	public void refresh() {
		nameLabel.setText(list.getSelected().getName());

		// cleanup
		for (Control current : tagsContainer.getChildren())
			current.dispose();
		
		for (int i = 0; i < 3; i++) {
			Text tmplabel = new Text(tagsContainer, SWT.BORDER);
			tmplabel.setText("tag " + i);
			tmplabel.setEditable(false);
			tmplabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			Button tmpbutton = new Button(tagsContainer, SWT.PUSH);
			tmpbutton.setText("x");
			tmpbutton.setToolTipText("remove tag");
		}

		tagsContainer.layout();
		this.layout();
	}

}

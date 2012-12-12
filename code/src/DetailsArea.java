import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


public class DetailsArea extends Composite {

	private ObjectList list;
	private Label nameLabel;
	private Composite tagsContainer;

	public DetailsArea(Composite parent, int style, ObjectList objectList) {
		super(parent, style | SWT.BORDER);
		list = objectList;

		this.setLayoutData(new GridData(200, SWT.DEFAULT));
		this.setLayout(new RowLayout(SWT.VERTICAL));

		nameLabel = new Label(this, SWT.NONE);
		tagsContainer = new Composite(this, SWT.BORDER);
		tagsContainer.setLayoutData(new RowData(175, 200));
		tagsContainer.setLayout(new GridLayout(2, false));
		Button addTagButton = new Button(this, SWT.PUSH);
		addTagButton.setText("+");
		addTagButton.setToolTipText("add new tags");
		this.layout();
	}

	public void refresh() {
		nameLabel.setText(list.getSelected().getName());

		// cleanup
		for (Control current : tagsContainer.getChildren())
			current.dispose();
		
		for (int i = 0; i < 3; i++) {
			Label tmplabel = new Label(tagsContainer, SWT.NONE);
			tmplabel.setText("tag " + i);
			Button tmpbutton = new Button(tagsContainer, SWT.PUSH);
			tmpbutton.setText("x");
			tmpbutton.setToolTipText("remove tag");
		}

		tagsContainer.layout();
		this.layout();
	}

}

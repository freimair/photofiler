package itemfiler.ui;

public class CheckedState {

	private boolean checked;
	private boolean barred;

	public CheckedState(boolean checked, boolean barred) {
		this.checked = checked;
		this.barred = barred;
	}

	public boolean getChecked() {
		return checked;
	}

	public boolean getBarred() {
		return barred;
	}
}

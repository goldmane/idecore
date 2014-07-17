package com.salesforce.ide.executeanonymous.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.salesforce.ide.core.project.ForceProjectException;

public class ExecuteAnonymousEditor extends EditorPart {

	public ExecuteAnonymousEditor() throws ForceProjectException {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		this.setSite(site);
		this.setInput(input);
		this.setPartName("Execute Anonymous");
	}

	

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		Button b = new Button(parent, SWT.None);
		b.setText("Testing");
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isDirty() { return false; }

	@Override
	public boolean isSaveAsAllowed() { return false; }

	@Override
	public void doSave(IProgressMonitor monitor) {}

	@Override
	public void doSaveAs() {}
}

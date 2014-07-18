package com.salesforce.ide.executeanonymous.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
		this.setSite(site);
		this.setInput(input);
		this.setPartName("Execute Anonymous");
	}

	@Override
	public void createPartControl(Composite parent) {
		createCoolBar(parent);
	}
	
	private void createCoolBar(Composite parent){
		
		Group g = new Group(parent, SWT.SHADOW_ETCHED_IN);
		g.setText("Group Name");

		SashForm form = new SashForm(parent,SWT.HORIZONTAL);
		form.setLayout(new FillLayout());
		
		Composite child1 = new Composite(form,SWT.NONE);
		child1.setLayout(new FillLayout());
		new Label(child1,SWT.NONE).setText("Label in pane 1");
		
		Composite child2 = new Composite(form,SWT.NONE);
		child2.setLayout(new FillLayout());
		new Button(child2,SWT.PUSH).setText("Button in pane2");

		Composite child3 = new Composite(form,SWT.NONE);
		child3.setLayout(new FillLayout());
		new Label(child3,SWT.PUSH).setText("Label in pane3");
		
		form.setWeights(new int[] {30,40,30});
	}

	@Override
	public void setFocus() {}
	@Override
	public boolean isDirty() { return false; }
	@Override
	public boolean isSaveAsAllowed() { return false; }
	@Override
	public void doSave(IProgressMonitor monitor) {}
	@Override
	public void doSaveAs() {}
}

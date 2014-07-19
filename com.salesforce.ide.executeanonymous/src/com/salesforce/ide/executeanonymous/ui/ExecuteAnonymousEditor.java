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
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.salesforce.ide.core.project.ForceProjectException;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.RowLayout;

public class ExecuteAnonymousEditor extends EditorPart {
	
	CoolBar coolBar = null;
	CoolItem coolItem = null;
	Composite cmpCoolBarContents = null;
	Label lblSnippets = null;
	Combo cboSnippets = null;
	Button btnSaveSnippet = null;

	public ExecuteAnonymousEditor() throws ForceProjectException {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		
		StringBuilder partName = new StringBuilder("Execute Anonymous");
		if(input instanceof IFileEditorInput)
			partName.append(" - " + ((IFileEditorInput)input).getFile().getName());
		
		this.setPartName(partName.toString());
	}

	@Override
	public void createPartControl(Composite parent) {
		
		//Composite composite = new Composite(parent, SWT.NONE);
		//composite.setLayout(new GridLayout(1, false));
		parent.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(parent, SWT.BORDER_SOLID | SWT.VERTICAL);
		//SashForm sashForm = new SashForm(composite, SWT.BORDER_SOLID | SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setBounds(0, 0, 55, 15);
		lblNewLabel_1.setText("New Label");
		
		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new GridLayout(4, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		composite_3.setBounds(0, 0, 64, 64);
		
		Combo combo = new Combo(composite_3, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(composite_3, SWT.NONE);
		btnNewButton.setSize(75, 25);
		btnNewButton.setText("New Button");
		new Label(composite_3, SWT.NONE);
		
		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnNewButton_1.setBounds(0, 0, 75, 25);
		btnNewButton_1.setText("New Button");
		
		StyledText styledText = new StyledText(composite_1, SWT.BORDER);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel_2 = new Label(composite_2, SWT.NONE);
		lblNewLabel_2.setText("New Label");
		
		StyledText styledText_1 = new StyledText(composite_2, SWT.BORDER);
		styledText_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setWeights(new int[] {1, 1});
		
	}
	
	private void createCoolBar(Composite parent){
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

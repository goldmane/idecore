package com.salesforce.ide.ui.views.executeanonymous;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ExecuteAnonymousSnippetSaveDialog extends TitleAreaDialog {
	
	protected ExecuteAnonymousController controller = null;

	public ExecuteAnonymousSnippetSaveDialog(ExecuteAnonymousController controller, Shell parentShell){
		super(parentShell);
		
		this.controller = controller;
	}

	@Override
	public void create() {
		super.create();
		
		this.setTitle("Snippet Save As");
		this.setMessage("Choose how to save this snippet.", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.None);
		GridData containerData = new GridData(GridData.FILL_BOTH);
		container.setData(containerData);
		
	    Composite cmpExisting = new Composite(container, SWT.None);
		
	    Button cbExisting = new Button(cmpExisting, SWT.RADIO);
		cbExisting.setText("Existing");
		
		Combo cboExisting = new Combo(cmpExisting, SWT.DROP_DOWN);
		
		return area;
	}
}

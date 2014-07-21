package com.salesforce.ide.executeanonymous.ui.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.salesforce.ide.executeanonymous.ui.ExecuteAnonymousEditor;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class ExecuteAnonymousSaveDialog extends TitleAreaDialog {
	
	protected Composite cmpContainer = null;
	protected Composite cmpInner = null;
	protected Composite cmpExisting = null;
	protected Composite cmpControls;
	protected Button rbExisting = null;
	protected Combo cboExistingSnippets = null;
	protected Button rbNew = null;
	protected Text txtNewSnippetName = null;

	public ExecuteAnonymousSaveDialog(Shell parentShell) {
		super(parentShell);
	}
	/**
	 * @wbp.parser.constructor
	 */
	public ExecuteAnonymousSaveDialog(ExecuteAnonymousEditor editor){
		this(editor.getSite().getShell());
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Execute Anonymous Save");
		setMessage("Choose how to save the current source");
		
		cmpContainer = new Composite(parent, SWT.None);
		cmpContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cmpContainer.setLayout(new GridLayout(1, false));
		
		cmpInner = new Composite(cmpContainer, SWT.None);
		cmpInner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		cmpInner.setLayout(new GridLayout(1, false));
		
		cmpControls = new Composite(cmpInner, SWT.NONE);
		cmpControls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_cmpControls = new GridLayout(2, false);
		gl_cmpControls.verticalSpacing = 20;
		cmpControls.setLayout(gl_cmpControls);
		
		rbExisting = new Button(cmpControls, SWT.RADIO);
		rbExisting.setText("Existing");
		rbExisting.setEnabled(false);
		
		cboExistingSnippets = new Combo(cmpControls, SWT.NONE);
		cboExistingSnippets.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cboExistingSnippets.setEnabled(false);
		
		rbNew = new Button(cmpControls, SWT.RADIO);
		rbNew.setBounds(0, 0, 90, 16);
		rbNew.setText("New");
		
		txtNewSnippetName = new Text(cmpControls, SWT.BORDER);
		txtNewSnippetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtNewSnippetName.setBounds(0, 0, 76, 21);
		
		return cmpContainer;
	}

	private void initialize(){
	}
	
	public boolean isExistingSelected(){
		if(rbExisting != null)
			return rbExisting.getSelection();
		return false;
	}

	public String getNewSnippetName(){
		if(txtNewSnippetName != null)
			return txtNewSnippetName.getText();
		return "";
	}

}

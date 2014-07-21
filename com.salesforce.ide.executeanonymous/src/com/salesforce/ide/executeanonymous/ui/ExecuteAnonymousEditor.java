package com.salesforce.ide.executeanonymous.ui;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.salesforce.ide.core.internal.context.ContainerDelegate;
import com.salesforce.ide.core.internal.utils.LoggingInfo;
import com.salesforce.ide.core.internal.utils.Utils;
import com.salesforce.ide.core.project.ForceProject;
import com.salesforce.ide.core.project.ForceProjectException;
import com.salesforce.ide.core.remote.apex.ExecuteAnonymousResultExt;
import com.salesforce.ide.executeanonymous.objects.Snippet;
import com.salesforce.ide.executeanonymous.objects.SnippetCollection;
import com.salesforce.ide.ui.views.LoggingComposite;

public class ExecuteAnonymousEditor extends EditorPart {
	
	private static Logger logger = Logger.getLogger(ExecuteAnonymousEditor.class);
	
	final String SNIPPETS_COMBO_SELECT = "Select a snippet...";
	
	protected Composite cmpContainer = null;
	
	protected Composite cmpExecuteAnonymousProps = null;
	protected Composite cmpActiveProject = null;
	protected Label lblActiveProject = null;
	protected Label lblActiveProjectName = null;
	protected LoggingComposite cmpLogging = null;
	
	protected SashForm sashForm = null;
	
	protected Composite cmpSource = null;
	protected Label lblSource = null;
	protected Composite cmpSourceControls = null;
	protected Label lblSnippets = null;
	protected Combo cboSnippets = null;
	protected Button btnSaveSnippet = null;
	protected Button btnExecute = null;
	protected StyledText txtSource = null;
	
	protected Composite cmpResults = null;
	protected Label lblResults = null;
	protected StyledText txtResults = null;
	
	private IFile currentFile = null;
	private ForceProject currentProject = null;
	private boolean isDirty = false;
	
	private SnippetCollection snippets = null;
	private JAXBContext snippetsContext = null;
	private Marshaller snippetsMarshaller = null;
	private Unmarshaller snippetsUnmarshaller = null;
	
	String lastSnippetSelection = "";

	public ExecuteAnonymousEditor() throws ForceProjectException {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		
		StringBuilder partName = new StringBuilder("Execute Anonymous - " + input.getName());
		this.setPartName(partName.toString());
		
		if(input instanceof IFileEditorInput)
			currentFile = ((IFileEditorInput)input).getFile();
	}
	
	private boolean initialize(){
		if(currentFile == null) 
			return false;
		
		IProject project = currentFile.getProject();
		
		if(project == null) 
			return false;
		currentProject = 
				ContainerDelegate.getInstance().getServiceLocator().getProjectService().getForceProject(project);
		if(Utils.isEmpty(currentProject.getEndpointServer())){
			currentProject = null;
			Utils.openError("Invalid Project Type", "Invalid project type. Expecting a Force.com project.");
			return false;
		}
		
		lblActiveProjectName.setText(currentProject.getProject().getName());
		cmpLogging.enable(currentProject.getProject());
		
		btnExecute.setEnabled(false);
		btnSaveSnippet.setEnabled(false);
		
		//set up JAXB
		try{
			snippetsContext = JAXBContext.newInstance(SnippetCollection.class);
			snippetsMarshaller = snippetsContext.createMarshaller();
			snippetsMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			snippetsUnmarshaller = snippetsContext.createUnmarshaller();
		}catch(JAXBException je){
			return false;
		}
		
		//read file
		snippets = new SnippetCollection();
		try{
			snippets = (SnippetCollection)snippetsUnmarshaller.unmarshal(currentFile.getLocation().makeAbsolute().toFile());
			resetSnippetsCombo();
		}catch(JAXBException readFileException){
			Utils.openError(readFileException, "Error reading file.", "An error occurred.");
		}
		
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		cmpExecuteAnonymousProps = new Composite(parent, SWT.NONE);
		GridLayout gl_cmpExecuteAnonymousProps = new GridLayout(3, false);
		gl_cmpExecuteAnonymousProps.marginHeight = 0;
		cmpExecuteAnonymousProps.setLayout(gl_cmpExecuteAnonymousProps);
		cmpExecuteAnonymousProps.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		createActiveProjectComposite(cmpExecuteAnonymousProps);
		createLoggingComposite(cmpExecuteAnonymousProps);
		
		sashForm = new SashForm(parent, SWT.BORDER_SOLID | SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		cmpSource = new Composite(sashForm, SWT.NONE);
		cmpSource.setLayout(new GridLayout(1, false));
		
		lblSource = new Label(cmpSource, SWT.NONE);
		lblSource.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblSource.setBounds(0, 0, 55, 15);
		lblSource.setText("Source");
		
		cmpSourceControls = new Composite(cmpSource, SWT.NONE);
		GridLayout gl_cmpSourceControls = new GridLayout(4, false);
		gl_cmpSourceControls.marginWidth = 0;
		cmpSourceControls.setLayout(gl_cmpSourceControls);
		cmpSourceControls.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		cmpSourceControls.setBounds(0, 0, 64, 64);
		
		//lblSnippets = new Label(cmpSourceControls, SWT.None);
		//lblSnippets.setText("Snippets:");
		
		createSnippetsCombo(cmpSourceControls);
		createSaveButton(cmpSourceControls);
		createExecuteButton(cmpSourceControls);
		
		txtSource = new StyledText(cmpSource, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		txtSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtSource.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				if(txtSource != null && btnExecute != null){
					boolean notEmpty = Utils.isNotEmpty(txtSource.getText());
					btnExecute.setEnabled(notEmpty);
					btnSaveSnippet.setEnabled(notEmpty);
				}
			}
		});
		
		cmpResults = new Composite(sashForm, SWT.NONE);
		cmpResults.setLayout(new GridLayout(1, false));
		
		lblResults = new Label(cmpResults, SWT.NONE);
		lblResults.setText("Results");
		
		txtResults = new StyledText(cmpResults, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		txtResults.setBackground(new Color(Display.getCurrent(), 240, 240, 240));
		txtResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		sashForm.setWeights(new int[] {1, 1});
		
		if(!initialize())
			parent.setEnabled(false);
	}
	
	private void createActiveProjectComposite(Composite parent){
		cmpActiveProject = new Composite(parent, SWT.None);
		cmpActiveProject.setLayoutData(new GridData(SWT.BEGINNING));
		GridLayout gl_cmpActiveProject = new GridLayout(3, false);
		gl_cmpActiveProject.marginHeight = 0;
		gl_cmpActiveProject.verticalSpacing = 0;
		cmpActiveProject.setLayout(gl_cmpActiveProject);
		
		lblActiveProject = new Label(cmpActiveProject, SWT.None);
		lblActiveProject.setLayoutData(new GridData(SWT.BEGINNING));
		lblActiveProject.setText("Active Project:");
		
		lblActiveProjectName = new Label(cmpActiveProject, SWT.NONE);
		FontData fd = lblActiveProjectName.getFont().getFontData()[0];
		Font f = new Font(this.getSite().getShell().getDisplay(), new FontData(fd.getName(), fd.getHeight(), SWT.BOLD));
		lblActiveProjectName.setFont(f);
		lblActiveProjectName.setLayoutData(new GridData(SWT.BEGINNING));
		lblActiveProjectName.setText("n/a");
		new Label(cmpActiveProject, SWT.NONE);
	}
	
	private void createLoggingComposite(Composite parent){
		cmpLogging = new LoggingComposite(
			parent, 
			ContainerDelegate.getInstance().getServiceLocator().getLoggingService(),
			SWT.NONE,
			false,
			LoggingInfo.SupportedFeatureEnum.ExecuteAnonymous
		);
		GridLayout gridLayout = (GridLayout) cmpLogging.getLayout();
		gridLayout.marginHeight = 0;
	}

	private void createSnippetsCombo(Composite parent){
		cboSnippets = new Combo(cmpSourceControls, SWT.DROP_DOWN | SWT.READ_ONLY);
		cboSnippets.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cboSnippets.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				Combo cbo = (Combo)e.widget;
				int selectedIndex = cbo.getSelectionIndex();
				if(selectedIndex >= 0){
					String selectedText = cbo.getItem(selectedIndex);
					if(!selectedText.equals(lastSnippetSelection) && !selectedText.equals(SNIPPETS_COMBO_SELECT)){
						if(!Utils.isEmpty(txtSource.getText())){
							if(!Utils.openConfirm("Overwrite Warning", "This will overwrite the current source content. Are you sure?")){
								cboSnippets.setText(lastSnippetSelection);
								return;
							}
						}
						//replace source with selected snippet
						Snippet chosen = snippets.getBySnippetName(selectedText);
						if(chosen != null)
							txtSource.setText(chosen.getCode());
						
						lastSnippetSelection = selectedText;
					}
				}
			}
		});
	}
	
	private void createExecuteButton(Composite parent){
		btnExecute = new Button(cmpSourceControls, SWT.NONE);
		btnExecute.setImage(null);
		GridData gd_btnExecute = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		gd_btnExecute.horizontalIndent = 30;
		btnExecute.setLayoutData(gd_btnExecute);
		btnExecute.setBounds(0, 0, 75, 25);
		btnExecute.setText("Execute Anonymous");
		btnExecute.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				final String code = txtSource.getText();
				WorkspaceJob job = new WorkspaceJob("Execute Anonymous"){
					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor)
							throws CoreException {
						try {
							monitor.beginTask("Send code to Tooling API", 3);
							//set up (1)
							monitor.subTask("Set up");
							monitor.worked(1);
							//api call (2)
							monitor.subTask("Call API");
							ExecuteAnonymousResultExt results = ContainerDelegate.getInstance().getServiceLocator()
									.getApexService().executeAnonymous(code, currentProject.getProject());
							monitor.worked(1);
							//results (3)
							monitor.subTask("Process Results");
							handleExecuteAnonymousResults(results);
							monitor.worked(1);
						} catch (Exception e2) {
							Utils.openError("Execute Anonymous", e2.getMessage());
						} finally{
							monitor.done();
						}
						return Status.OK_STATUS;
					}
				};
				job.schedule();
			}
		});
	}
	
	private void handleExecuteAnonymousResults(final ExecuteAnonymousResultExt results){
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				StringBuilder output = new StringBuilder();
				if(results.getCompiled()){
					if(results.getSuccess()){
						output.append("Anonymous execution was successful.\n\n");
						if(results.getDebugInfo() != null){
							output.append(results.getDebugInfo().getDebugLog());
						}
					}else{
						Utils.openError("Error Occurred", results.getExceptionMessage());
						output.append("DEBUG LOG\n");
						if(results.getDebugInfo() != null){
							output.append(results.getDebugInfo().getDebugLog());
						}
					}
				}else{
					output.append("Compile error at line " + 
							results.getLine() + " column " +
							results.getColumn() + "\n" + 
							results.getCompileProblem());
					
				}
				if(output.length() > 0)
					txtResults.setText(output.toString());
			}
		});
	}
	
	private void createSaveButton(Composite parent){
		btnSaveSnippet = new Button(parent, SWT.NONE);
		btnSaveSnippet.setImage(null);
		btnSaveSnippet.setSize(75, 25);
		btnSaveSnippet.setText("Save");
		btnSaveSnippet.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					/*ExecuteAnonymousSaveDialog dialog = 
							new ExecuteAnonymousSaveDialog(ExecuteAnonymousEditor.this);
					dialog.create();
					int dialogResult = dialog.open();
					if(dialogResult == Window.OK){
						setIsDirty(true);
					}*/
					
					Snippet s = new Snippet();
					s.setName("Test " + (snippets.size() + 1));
					s.setCode("System.debug(\"Hello world " + snippets.size() + ".\");");
					snippets.add(s);
					
					setIsDirty(true);
				} catch (Exception e2) {
					//logger.error(e2.getMessage());
					e2.printStackTrace();
				}
			}
		});
	}
	
	private void resetSnippetsCombo(){
		if(snippets == null)
			return;
		cboSnippets.removeAll();
		if(snippets.size() > 0){
			for(Snippet s : snippets.getOrdered()){
				cboSnippets.add(s.getName());
			}
			cboSnippets.add(SNIPPETS_COMBO_SELECT, 0);
			cboSnippets.select(0);
		}
		lastSnippetSelection = "";
	}
	
	private final void setIsDirty(boolean isDirty){
		if(this.isDirty == isDirty)
			return;
		this.isDirty = isDirty;
		this.firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			snippetsMarshaller.marshal(snippets, currentFile.getRawLocation().makeAbsolute().toFile());
			setIsDirty(false);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void setFocus() {}
	@Override
	public boolean isDirty() { return isDirty; }
	@Override
	public boolean isSaveAsAllowed() { return false; }
	@Override
	public void doSaveAs() {}

}

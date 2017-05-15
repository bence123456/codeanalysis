package com.bkonecsni.codeanalysis.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class CommonHandler extends AbstractHandler {
	
	private IWorkbenchWindow window;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if(editorPart != null) {
			IProject activeProject = getActiveProject(editorPart);
		    executeAction(activeProject);
		} else {
			showMessageBox();
		}
		return null;
	}

	private void showMessageBox() {
		MessageBox dialog = new MessageBox(window.getShell(), SWT.ICON_INFORMATION | SWT.OK);
		dialog.setText("Cannot perform selected action!");
		dialog.setMessage("Please first select a file from one of the projects!");
		dialog.open();
	}
	
	protected IProject getActiveProject(IEditorPart editorPart) {
		IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput() ;
		IFile file = input.getFile();
		return file.getProject();
	}
	
	protected abstract void executeAction(IProject activeProject);

	public IWorkbenchWindow getWindow() {
		return window;
	}
}

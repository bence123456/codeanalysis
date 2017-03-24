package com.bkonecsni.codeanalysis.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.bkonecsni.codeanalysis.visitors.FileVisitor;

public class ActionDelegate implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;
	
	@Override
	public void run(IAction arg0) {
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		
		if(editorPart  != null) {
			IProject activeProject = getActiveProject(editorPart);
		    listFiles(activeProject);
		} else {
			System.out.println("Please first select a file!");
			//Todo: popup window
		}	
	}

	private IProject getActiveProject(IEditorPart editorPart) {
		IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput() ;
		IFile file = input.getFile();
		return file.getProject();
	}

	protected void listFiles(IProject activeProject) {		
		System.out.println("Listing files from current project!");
		try {
			activeProject.accept(new FileVisitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(IWorkbenchWindow window) {	
		this.window = window;
	}
	
	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		System.out.println("selection changed! IAction: " + arg0 + ", " + arg0.getDescription() + "; ISelection: " + arg1);
	}

	@Override
	public void dispose() {
	}
}

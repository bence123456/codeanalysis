package com.bkonecsni.codeanalysis.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.bkonecsni.codeanalysis.visitors.CodeAnalysisVisitor;

public class CodeAnalysisAction extends AbstractAction {
	
	private boolean autoAnalysisEnabled = true;
	
	@Override
	public void init(IWorkbenchWindow window) {	
		super.init(window);
		
		addResourceChangeListener();
	}
	
	@Override
	protected void executeAction(IProject activeProject) {
		analyzeCode(activeProject);	
	}
	
	private void analyzeCode(IProject activeProject) {		
		System.out.println("Analyzing code!");
		try {
			activeProject.accept(new CodeAnalysisVisitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void addResourceChangeListener() {
		IResourceChangeListener listener = new IResourceChangeListener() {
			
			@Override
			public void resourceChanged(IResourceChangeEvent arg0) {
				if (autoAnalysisEnabled) {
					IEditorPart editorPart = getWindow().getActivePage().getActiveEditor();
					if(editorPart != null) {
						IProject activeProject = getActiveProject(editorPart);
						analyzeCode(activeProject);
					}
				}
			}
		};
				
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.PRE_BUILD);
	}
}

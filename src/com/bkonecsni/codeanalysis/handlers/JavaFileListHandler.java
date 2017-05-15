package com.bkonecsni.codeanalysis.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

import com.bkonecsni.codeanalysis.visitors.JavaFileVisitor;

public class JavaFileListHandler extends CommonHandler {
	
	@Override
	protected void executeAction(IProject activeProject) {
		listJavaFiles(activeProject);		
	}
	
	private void listJavaFiles(IProject activeProject) {		
		System.out.println("Listing java files from current project!");
		
		try {
			if (activeProject.getDescription().hasNature(JavaCore.NATURE_ID)) {
				activeProject.accept(new JavaFileVisitor());
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}

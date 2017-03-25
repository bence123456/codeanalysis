package com.bkonecsni.codeanalysis.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.bkonecsni.codeanalysis.visitors.JavaFileVisitor;

public class ListJavaFilesAction extends ListAllFilesAction {
	
	@Override
	protected void listFiles(IProject activeProject) {		
		System.out.println("Listing java files from current project!");
		try {
			activeProject.accept(new JavaFileVisitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}

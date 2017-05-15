package com.bkonecsni.codeanalysis.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.bkonecsni.codeanalysis.visitors.FileVisitor;

public class FileListHandler extends CommonHandler {

	@Override
	protected void executeAction(IProject activeProject) {
		listFiles(activeProject);
	}
	
	private void listFiles(IProject activeProject) {		
		System.out.println("Listing files from current project!");
		try {
			activeProject.accept(new FileVisitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}

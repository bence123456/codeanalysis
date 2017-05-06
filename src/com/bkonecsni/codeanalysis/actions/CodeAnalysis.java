package com.bkonecsni.codeanalysis.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.bkonecsni.codeanalysis.visitors.CodeAnalysisVisitor;

public class CodeAnalysis extends AbstractAction {
	
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
}

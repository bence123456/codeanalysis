package com.bkonecsni.codeanalysis.visitors;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class CodeAnalysisVisitor implements IResourceVisitor {
	
	@Override
	public boolean visit(IResource resource) throws JavaModelException, CoreException {
		String extension = resource.getFileExtension();
		if (extension != null && extension.equals("java")) {
			resource.deleteMarkers(ASTCodeAnalysisVisitor.MARKER_TYPE, true, 0);
			
			ASTNode node = createASTNode(resource);	
			node.accept(new ASTCodeAnalysisVisitor((CompilationUnit) node));			
		}
		return true;
	}
	
	private ASTNode createASTNode(IResource resource) throws JavaModelException {
		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);
		ICompilationUnit compUnit = JavaCore.createCompilationUnitFrom((IFile) resource);
		
		parser.setSource(compUnit);
		parser.setResolveBindings(true);
		
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		parser.setCompilerOptions(options);
	
		return parser.createAST(new NullProgressMonitor());	
	}
}

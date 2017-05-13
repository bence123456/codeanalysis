package com.bkonecsni.codeanalysis.actions;

import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.bkonecsni.codeanalysis.visitors.ASTCodeAnalysisVisitor;

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
		try {
			if (activeProject.getDescription().hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(activeProject);
				IFolder sourceFolder = activeProject.getFolder("src/main/java");
				IFolder testFolder = activeProject.getFolder("src/test/java");
	
				IPackageFragmentRoot sourcePackageFragmentRoot = javaProject.getPackageFragmentRoot(sourceFolder);
				IPackageFragmentRoot testPackageFragmentRoot = javaProject.getPackageFragmentRoot(testFolder);
				
				analyzePackageFragmentRoot(sourcePackageFragmentRoot);
				analyzePackageFragmentRoot(testPackageFragmentRoot);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void analyzePackageFragmentRoot(IPackageFragmentRoot packageFragmentRoot) throws JavaModelException, CoreException {
		for (IJavaElement javaElement : packageFragmentRoot.getChildren()) {
			IPackageFragment packageFragment = (IPackageFragment) javaElement;
			ICompilationUnit[] compilationUnits = packageFragment.getCompilationUnits();
			
			for (ICompilationUnit compilationUnit : compilationUnits) {
				compilationUnit.getResource().deleteMarkers(ASTCodeAnalysisVisitor.MARKER_TYPE, true, 0);
				ASTNode node = createASTNode(compilationUnit);
				node.accept(new ASTCodeAnalysisVisitor((CompilationUnit) node));
			}
		}
	}
	
	private ASTNode createASTNode(ICompilationUnit compUnit) throws JavaModelException {
		ASTParser parser = ASTParser.newParser(org.eclipse.jdt.core.dom.AST.JLS8);		
		parser.setSource(compUnit);
		parser.setResolveBindings(true);
		
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		parser.setCompilerOptions(options);
	
		return parser.createAST(new NullProgressMonitor());	
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

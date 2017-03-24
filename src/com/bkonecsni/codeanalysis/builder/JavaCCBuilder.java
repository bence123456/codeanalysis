package com.bkonecsni.codeanalysis.builder;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.bkonecsni.codeanalysis.visitors.XmlDeltaVisitor;
import com.bkonecsni.codeanalysis.visitors.XmlResourceVisitor;

public class JavaCCBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.bkonecsni.codeanalysis.com.bkonecsni.javaCCBuilder";
	private static final String MARKER_TYPE = "com.bkonecsni.codeanalysis.xmlProblem";

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Scanning for and compiling JavaCC source files...", 0);
		
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	protected void clean(IProgressMonitor monitor) throws CoreException {
		// delete markers set and files created
		getProject().deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		getProject().accept(new XmlResourceVisitor(MARKER_TYPE));
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		delta.accept(new XmlDeltaVisitor(MARKER_TYPE));
	}
}
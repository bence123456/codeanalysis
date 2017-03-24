package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

public class FileVisitor implements IResourceVisitor{
	
	@Override
	public boolean visit(IResource resource) {
		if (resource instanceof IFile) {
			System.out.println(resource.getName());
		}
		return true;
	}
}
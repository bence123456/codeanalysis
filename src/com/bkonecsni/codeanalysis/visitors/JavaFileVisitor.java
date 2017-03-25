package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

public class JavaFileVisitor implements IResourceVisitor{
	
	@Override
	public boolean visit(IResource resource) {
		//TODO: print methods and fields
		String extension = resource.getFileExtension();
		if (extension != null && extension.equals("java")) {
			System.out.println(resource.getName());
		}
		return true;
	}
}
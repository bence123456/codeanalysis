package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

public class JavaFileVisitor implements IResourceVisitor{
	
	@Override
	public boolean visit(IResource resource) {
		//TODO: print methods and fields
		if (resource.getFileExtension().equals("java")) {
			System.out.println(resource.getName());
		}
		return true;
	}
}
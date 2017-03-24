package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.bkonecsni.codeanalysis.builder.XmlChecker;

public class XmlDeltaVisitor implements IResourceDeltaVisitor {
	
	private XmlChecker xmlChecker;
	
	public XmlDeltaVisitor(String markerType) {
		this.xmlChecker = new XmlChecker(markerType);
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
			checkXML(resource);
			break;
		case IResourceDelta.REMOVED:
			break;
		case IResourceDelta.CHANGED:
			checkXML(resource);
			break;
		}
		return true;
	}
	
	private void checkXML(IResource resource) {
		xmlChecker.checkXML(resource);
	}
}

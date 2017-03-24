package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

import com.bkonecsni.codeanalysis.builder.XmlChecker;

public class XmlResourceVisitor implements IResourceVisitor {
	
	private XmlChecker xmlChecker;
	
	public XmlResourceVisitor(String markerType) {
		this.xmlChecker = new XmlChecker(markerType);
	}
	
	@Override
	public boolean visit(IResource resource) {
		checkXML(resource);
		return true;
	}
	
	private void checkXML(IResource resource) {
		xmlChecker.checkXML(resource);
	}
}
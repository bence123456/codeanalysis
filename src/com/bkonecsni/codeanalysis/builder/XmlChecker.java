package com.bkonecsni.codeanalysis.builder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.xml.sax.SAXException;

import com.bkonecsni.codeanalysis.builder.XMLErrorHandler;

public class XmlChecker {

	private SAXParserFactory parserFactory;
	private String markerType;
	
	public XmlChecker(String markerType) {
		this.markerType = markerType;
	}
	
	public void checkXML(IResource resource) {
		if (resource instanceof IFile && resource.getName().endsWith(".xml")) {
			IFile file = (IFile) resource;
			deleteMarkers(file);
			XMLErrorHandler reporter = new XMLErrorHandler(file, markerType);
			try {
				getParser().parse(file.getContents(), reporter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(markerType, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
	}

	private SAXParser getParser() throws ParserConfigurationException, SAXException {
		if (parserFactory == null) {
			parserFactory = SAXParserFactory.newInstance();
		}
		return parserFactory.newSAXParser();
	}

}

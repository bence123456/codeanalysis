package com.bkonecsni.codeanalysis.searchrequestors;

import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.SourceMethod;

public class MethodSearchRequestor extends SearchRequestor {

	@Override
	public void acceptSearchMatch(SearchMatch match) {
		SourceMethod sourceMethod = (SourceMethod) match.getElement();
		System.out.println("-method- " + sourceMethod.getElementName());
	}
}
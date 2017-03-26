package com.bkonecsni.codeanalysis.searchrequestors;

import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.SourceField;

public class FieldSearchRequestor extends SearchRequestor {

	@Override
	public void acceptSearchMatch(SearchMatch match) {
    	SourceField sourceField = (SourceField) match.getElement();
		System.out.println("-field- " + sourceField.getElementName());	
	}
}
package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import com.bkonecsni.codeanalysis.searchrequestors.FieldSearchRequestor;
import com.bkonecsni.codeanalysis.searchrequestors.MethodSearchRequestor;

public class JavaFileVisitor implements IResourceVisitor{
	
	@Override
	public boolean visit(IResource resource) {
		String extension = resource.getFileExtension();
		if (extension != null && extension.equals("java")) {
			System.out.println("\n" + resource.getName());
			
			searcAndListMembers(resource, IJavaSearchConstants.FIELD, new FieldSearchRequestor());
			searcAndListMembers(resource, IJavaSearchConstants.METHOD, new MethodSearchRequestor());		
		}
		return true;
	}

	private void searcAndListMembers(IResource resource, int searchConstant, SearchRequestor requestor) {
		IJavaElement javaElement = JavaCore.create(resource);
		
		SearchPattern pattern = SearchPattern.createPattern("*", searchConstant, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {javaElement});
		
		SearchEngine searchEngine = new SearchEngine();
		SearchParticipant[] searchParticipants = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
		try {
			searchEngine.search(pattern, searchParticipants, scope, requestor, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
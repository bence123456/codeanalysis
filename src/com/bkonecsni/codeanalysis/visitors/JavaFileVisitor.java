package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import com.bkonecsni.codeanalysis.ProblemTypes;

public class JavaFileVisitor implements IResourceVisitor{
	
	@Override
	public boolean visit(IResource resource) {
		//TODO: print methods and fields
		String extension = resource.getFileExtension();
		if (extension != null && extension.equals("java")) {
			System.out.println(resource.getName());
			
			IJavaElement javaElement = JavaCore.create(resource);
			
		    SearchPattern pattern = SearchPattern.createPattern("*", IJavaSearchConstants.FIELD, IJavaSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
	        IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {javaElement});

	        SearchRequestor requestor = new SearchRequestor() {
	            @Override
	            public void acceptSearchMatch(SearchMatch match) {
	                System.out.println(match.getElement());
	            }
	        };
	        
	        SearchEngine searchEngine = new SearchEngine();
	        SearchParticipant[] searchParticipants = new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() };
			try {
				searchEngine.search(pattern, searchParticipants, scope, requestor, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}		
			
			createMarker(resource);
		}
		return true;
	}

	private void createMarker(IResource resource) {
		try {
			IMarker marker = resource.createMarker("com.bkonecsni.codeanalysis.codeanalysisproblems");
			marker.setAttribute(IMarker.MESSAGE, ProblemTypes.MISSING_NULL_CHECK.getDescription());
		    marker.setAttribute("problemType", ProblemTypes.MISSING_NULL_CHECK.name());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
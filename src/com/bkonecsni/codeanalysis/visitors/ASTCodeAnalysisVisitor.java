package com.bkonecsni.codeanalysis.visitors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTCodeAnalysisVisitor extends ASTVisitor {
	
	private CompilationUnit compUnit;
	
	public static final String MARKER_TYPE = "com.bkonecsni.codeanalysis.codeanalysisproblems";
		
	ASTCodeAnalysisVisitor(CompilationUnit compUnit) {
		this.compUnit = compUnit;
	}
	
    @Override
    public boolean visit(VariableDeclarationFragment node) {    	
    	System.out.print("Node type: " + node.getNodeType() + "\n");
    	System.out.println("Node: " + node + "  at row: " + compUnit.getLineNumber(node.getStartPosition()));
    	
    	return true;
    }
    
    public boolean visit(MethodInvocation node) {          
        Expression expression = node.getExpression();

        if(expression instanceof Name) {
            Name name = (Name) expression;
            IBinding binding = name.resolveBinding();
            
	        IResource resource = binding.getJavaElement().getResource();
	        String warningText = createWarningText(node, binding);
	        int lineNumber = compUnit.getLineNumber(node.getStartPosition());
	        
	        createMarker(resource, warningText, lineNumber);
	            
	        System.out.println(warningText);
        }        
        return true;
    }

    private String createWarningText(MethodInvocation node, IBinding binding) {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("Calling the method '");
    	sb.append(node.getName().getFullyQualifiedName());
    	sb.append("' on "+ getVariableDeclarationTypeString(binding) + " variable ");
    	sb.append(getVariableTypeString(binding) + " " + binding.getName());
    	sb.append(" may result in a NPE");
    	
    	return sb.toString();
    }
    
    private String getVariableDeclarationTypeString(IBinding binding) {
        IJavaElement javaElement = binding.getJavaElement();
        
        if (javaElement.getElementType() == 8) {
        	return "field";
        } else {
        	return "local";
        }
    }
    
    private String getVariableTypeString(IBinding binding) {
        IJavaElement javaElement = binding.getJavaElement();
        
        String handlerIdentifierString = javaElement.getHandleIdentifier();
        int lastIndexOfBackslash = handlerIdentifierString.lastIndexOf("/");
        int lastIndexOfComma = handlerIdentifierString.lastIndexOf(";");
        
        return handlerIdentifierString.substring(lastIndexOfBackslash + 1, lastIndexOfComma);
    }
    
	private void createMarker(IResource resource, String text, int lineNumber) {
		try {			
			IMarker marker = resource.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, text);
		    marker.setAttribute(IMarker.LOCATION, "line" + lineNumber);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	

    
}

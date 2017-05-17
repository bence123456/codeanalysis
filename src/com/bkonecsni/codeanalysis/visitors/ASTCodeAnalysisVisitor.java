package com.bkonecsni.codeanalysis.visitors;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.bkonecsni.codeanalysis.NodeKey;

public class ASTCodeAnalysisVisitor extends ASTVisitor {
	
	private CompilationUnit compUnit;
	
	private Map<NodeKey, Integer> nodeKeyPositionMap = new HashMap<NodeKey, Integer>();
		
	public static final String MARKER_TYPE = "com.bkonecsni.codeanalysis.codeanalysisproblems";
		
	public ASTCodeAnalysisVisitor(CompilationUnit compUnit) {
		this.compUnit = compUnit;
	}
    
    @Override
    public void preVisit(ASTNode node) {
    	if (node instanceof NullLiteral) {
    		checkForNullVariableDeclarations(node);
    	} else if (node instanceof IfStatement) {
    		checkForNullAssignmentsInIfStatement((IfStatement) node);
    	} else if (node instanceof Assignment) {
    		checkForNullAssignments((Assignment) node);
    	}
    }
    
    @Override
    public boolean visit(MethodInvocation node) { 
        Expression expression = node.getExpression();

        if(expression instanceof Name) {
            Name name = (Name) expression;
            IBinding binding = name.resolveBinding();
            String variableName = binding.getName();
            
            if (variableMayBeNull(variableName, node.getParent().getParent(), name.getStartPosition())) {
		        IResource resource = binding.getJavaElement().getResource();
		        String warningText = createWarningText(node, binding, variableName);
		        int lineNumber = compUnit.getLineNumber(node.getStartPosition());
		        
		        createMarker(resource, warningText, lineNumber);
            }
        }
        return true;
    }

    private void checkForNullVariableDeclarations(ASTNode node) {
    	ASTNode parent = node.getParent();
    	
    	if (parent instanceof VariableDeclarationFragment) {
    		VariableDeclarationFragment variableDeclParent = (VariableDeclarationFragment) parent;
    		ASTNode superNode = variableDeclParent.getParent().getParent();
    		
    		if (superNode instanceof Block) {
    			String name = variableDeclParent.getName().toString();
        		NodeKey nodeKey = new NodeKey(name, (Block) superNode);
        		nodeKeyPositionMap.put(nodeKey, parent.getStartPosition());
    		}	
    	}
    }
    
    private void checkForNullAssignmentsInIfStatement(IfStatement node) {
    	Statement thenStatement = node.getThenStatement();
    	
    	if (thenStatement instanceof Block) {
    		for (Object statement : ((Block) thenStatement).statements()) {
    			if (statement instanceof ExpressionStatement) {    				
    				Expression  expression = ((ExpressionStatement) statement).getExpression();
    				if (expression instanceof Assignment) {
    					Assignment assignment = (Assignment) expression;
    					updateMapBasedOnLhsOrRhs(assignment, assignment.getLeftHandSide(), node.getParent());
    					updateMapBasedOnLhsOrRhs(assignment, assignment.getRightHandSide(), node.getParent());
    				}
    			}
    		}
    	}
    }
    
    private void checkForNullAssignments(Assignment node) {
   		updateMapBasedOnLhsOrRhs(node, node.getRightHandSide(), node.getParent().getParent());
    }
    
    private void updateMapBasedOnLhsOrRhs(Assignment assignment, Expression expression, ASTNode blockNode) {
		String name = assignment.getLeftHandSide().toString();
		Block block = blockNode instanceof Block ? (Block) blockNode : (Block) blockNode.getParent();
		NodeKey nodeKey = new NodeKey(name, block);
    	
    	if (expression.getNodeType() == ASTNode.NULL_LITERAL) {
			nodeKeyPositionMap.put(nodeKey, expression.getStartPosition());
		} else {
			if (nodeKeyPositionMap.containsKey(nodeKey)) {
				nodeKeyPositionMap.remove(nodeKey);
			}
		}
    }
    
    private boolean variableMayBeNull(String variableName, ASTNode node, int pos) {
    	Block block = node instanceof Block ? (Block) node : (Block) node.getParent();
    	for (NodeKey nodeKey : nodeKeyPositionMap.keySet()) {
    		if (nodeKey.getName().equals(variableName) && nodeKey.getBlock().equals(block) && nodeKeyPositionMap.get(nodeKey) < pos) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    private String createWarningText(MethodInvocation node, IBinding binding, String variableName) {  	
    	String methodName = node.getName().getFullyQualifiedName();
    	String declType = getVariableDeclarationTypeString(binding);
    	String variableType = getVariableTypeString(binding);
    	
    	return MessageFormat.format("Calling the method \"{0}\" on {1} {2} {3} may result in a NPE", 
    			methodName, declType, variableType, variableName);
    }
    
    private String getVariableDeclarationTypeString(IBinding binding) {
        IJavaElement javaElement = binding.getJavaElement();
        
        if (javaElement.getElementType() == 8) {
        	return "field ";
        } else {
        	return "local variable ";
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

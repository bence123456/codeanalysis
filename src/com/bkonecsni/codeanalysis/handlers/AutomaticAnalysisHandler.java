package com.bkonecsni.codeanalysis.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;

public class AutomaticAnalysisHandler extends AbstractHandler {

	public static boolean AUTO_ANALYSIS_ENABLED = false; 
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	    Event selectionEvent = (Event) event.getTrigger();
	    MenuItem item = (MenuItem) selectionEvent.widget;		
		
		AUTO_ANALYSIS_ENABLED = item.getSelection();
		return null;
	}

}

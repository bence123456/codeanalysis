package com.bkonecsni.codeanalysis;

public enum ProblemTypes {
	
	MISSING_NULL_CHECK("A null check is missing!"),
	CONSTRUCTOR_RECURSION("Constructor recursion detected!");

	private String description;
	
	ProblemTypes(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}

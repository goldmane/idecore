package com.salesforce.ide.executeanonymous.objects;

public class Snippet {
	
	private String name = "";
	private String code = "";
	
	public Snippet() {
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}

}

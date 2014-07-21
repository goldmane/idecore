package com.salesforce.ide.executeanonymous.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.salesforce.ide.core.internal.utils.Utils;

@XmlRootElement(name="snippets")
@XmlSeeAlso({Snippet.class})
public class SnippetCollection extends ArrayList<Snippet> {
	private static final long serialVersionUID = -937557505758296705L;

	@XmlElement(name="snippet")
	public List<Snippet> getOrdered(){
		Collections.sort(this, new Comparator<Snippet>(){
			@Override
			public int compare(Snippet arg0, Snippet arg1) {
				return String.CASE_INSENSITIVE_ORDER.compare(arg0.getName(), arg1.getName());
			}
		});
		return this;
	}
	
	public Snippet getBySnippetName(String name){
		for(Snippet s : this){
			if(Utils.isEqual(s.getName(), name, true))
				return s;
		}
		return null;
	}
}

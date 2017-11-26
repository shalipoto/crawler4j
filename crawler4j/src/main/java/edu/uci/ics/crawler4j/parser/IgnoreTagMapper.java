package edu.uci.ics.crawler4j.parser;

import java.util.HashSet;
import java.util.Set;

import org.apache.tika.parser.html.HtmlMapper;

public class IgnoreTagMapper implements HtmlMapper {

    private static final Set<String> DISCARDABLE_ELEMENTS = new HashSet<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
    add("STYLE");
    add("SCRIPT");
    }};

    
    @Override
    public String mapSafeElement(String name) {
        return name.toLowerCase();
    }

    @Override
    public boolean isDiscardElement(String name) {
        return DISCARDABLE_ELEMENTS.contains(name);
    }

    @Override
    public String mapSafeAttribute(String elementName, String attributeName) {
        return attributeName.toLowerCase();
    }
}

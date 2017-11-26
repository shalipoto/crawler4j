package edu.uci.ics.crawler4j.parser;

import java.util.HashSet;
import java.util.Set;

import org.apache.tika.parser.html.HtmlMapper;

/**
 * This class allows for html tag elements to be 
 * discarded in the final output as the user needs
 * 
 * @author saleem halipoto
 * 
 * code used from examples located at:
 * {@link https://github.com/apache/tika/blob/master/tika-parsers/src/main/java/org/apache/tika/parser/html/DefaultHtmlMapper.java}
 * {@link https://stackoverflow.com/questions/2041778/how-to-initialize-hashset-values-by-construction}
 */
public class IgnoreTagMapper implements HtmlMapper {

	// hashset holds all html elements to discard from final parsing output
    private static final Set<String> DISCARDABLE_ELEMENTS = new HashSet<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
	/*
	 * This is a technique which creates an anonymous inner class which has an
	 *  instance initializer which adds Strings to itself when an instance is created.
	 *  Its a bit hacky but prevents the need for a initializer method.
	 *  There may be performance issues at scale for this technique
	 *  that should be investigated.
	 */
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

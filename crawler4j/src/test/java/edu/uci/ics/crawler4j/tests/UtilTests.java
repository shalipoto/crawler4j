package edu.uci.ics.crawler4j.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.crawler4j.util.Util;

public class UtilTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		StringBuilder myFile = new StringBuilder("myFile");
		String extension = new String("txt");
		myFile = Util.AppendExtensionToFilename(myFile, extension);
		assertEquals("Adding file extesion failed", "myFile.txt", myFile.toString());
	}

	@Test
		public void test1() {		
		String stringNotGoodForFileName = new String("@%._/// |!  \\_\\Free $$ Credit ** Score&Is*this\"a\"question???");
		String newName = Util.NormalizeStringForFilename(stringNotGoodForFileName);
		assertEquals("The string normalization failed", "%%._%%%_%%__%_%Free_$$_Credit_%%_Score&Is%this%a%question%%%", newName);
		stringNotGoodForFileName = new String("&This is an example of a <tag>tag text here</tag>");
		newName = Util.NormalizeStringForFilename(stringNotGoodForFileName);
		assertEquals("The string normalization failed", "&This_is_an_example_of_a_%tag%tag_text_here%%tag%", newName);
		stringNotGoodForFileName = new String("._This is an example of a dot at the beginning");
		newName = Util.NormalizeStringForFilename(stringNotGoodForFileName);
		assertEquals("The string normalization failed", "%_This_is_an_example_of_a_dot_at_the_beginning", newName);
		stringNotGoodForFileName = new String(".._This is an example of two dots at the beginning");
		newName = Util.NormalizeStringForFilename(stringNotGoodForFileName);
		assertEquals("The string normalization failed", "%._This_is_an_example_of_two_dots_at_the_beginning", newName);
	}
}

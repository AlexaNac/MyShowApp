package junitTests;

import static org.junit.Assert.*;

import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;

import org.junit.Test;

import database.DatabaseOperations;


public class JUnitTests {

	@Test
	public void test() {
		String shows = new String();
		String arg = "DROP TABLE shows";
		shows = DatabaseOperations.getDetails(arg);
		shows = DatabaseOperations.getDetails("Arrow");
		assertEquals(shows != null, true);
	}
}

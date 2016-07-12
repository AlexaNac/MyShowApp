package junitTests;

import static org.junit.Assert.*;
import org.junit.Test;
import database.DatabaseHandler;
import java.sql.Connection;

public class JunitConnectionTest {

	@Test
	public void test() {
		Connection connection = null;
		connection = DatabaseHandler.preliminaries();
		assertEquals(connection != null, true);
	}

}

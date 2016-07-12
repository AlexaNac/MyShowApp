package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

	// JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";
    
    //  Database attributes
    private static String USER = "root";
    private static String PASS = "root";
    private static final String DB_NAME = "MyShowsDB";
    
    static Connection conn;
    
    private static DatabaseHandler instance;
    
    private DatabaseHandler(String USER, String PASS) {
        this.USER = USER;
        this.PASS = PASS;
        
        if (createDatabase() == true){
            createTables();
        }
    }

    
    public static DatabaseHandler getInstance(String USER, String PASS) {
        if(instance == null){
            instance = new DatabaseHandler(USER,PASS);
        }
        return instance;
    }

    /**
     * Register JDBC driver and open a connection
     * @return 
     */
    public static  Connection preliminaries() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException se) {
            // Handle errors for JDBC   
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
    }

    private void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    /**
     * Create the database
     *
     * @return true if the database has been created, otherwise return false
     */
    private boolean createDatabase() {
        preliminaries();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            String sql = "CREATE DATABASE " + DB_NAME;
            stmt.executeUpdate(sql);

        }  catch (SQLException sqlException) {
            //if the database already exist
            if (sqlException.getErrorCode() == 1007) {
                closeConnection();
                return false;
            } else {
                closeConnection();
                sqlException.printStackTrace();
                return false;
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                closeConnection();
                return false;
            }
        }
        closeConnection();
        return true;
    }
    private void createTables() {
        preliminaries();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            String sqlQuery = "USE "+ DB_NAME;
            stmt.executeUpdate(sqlQuery);
                      
            //create USER_TABLE
            sqlQuery = "CREATE TABLE USER "
                    + "(username VARCHAR(255) not NULL, "
                    + " password VARCHAR(255) not NULL, "
                    + " PRIMARY KEY ( username ))";
            stmt.executeUpdate(sqlQuery);
            
            // create SHOWS_TABLE
            sqlQuery = "CREATE TABLE SHOWS "
            		+ "(show_id INTEGER not NULL, "
                    + " showname VARCHAR(255) not NULL, "
                    + " details VARCHAR(2000) not NULL, "
                    + " PRIMARY KEY ( show_id ))";
            stmt.executeUpdate(sqlQuery);
            
         // create show_HISTORY
            sqlQuery = "CREATE TABLE HISTORY "
                    + "(id INTEGER not NULL, "
            		+ " show_id INTEGER not NULL, "
            		+ " user VARCHAR(255) not NULL,"
            		+ " ranking INTEGER, "
                    + " status VARCHAR(255) not NULL,"
                    + " PRIMARY KEY ( id ), "
                    + " FOREIGN KEY (user) REFERENCES "
                    + DB_NAME + ".USER(username) "
                    + " ON DELETE CASCADE ON UPDATE CASCADE, "
                    + " FOREIGN KEY (show_id) REFERENCES "
                    + DB_NAME + ".SHOWS(show_id) "
                    + " ON DELETE CASCADE ON UPDATE CASCADE) ";
            stmt.executeUpdate(sqlQuery);
            
        } catch (SQLException ex) {
            closeConnection();
            ex.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                closeConnection();
            }
        }
        closeConnection();
    }
    
}

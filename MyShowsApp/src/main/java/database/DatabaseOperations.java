package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import com.mysql.cj.fabric.xmlrpc.base.Array;

import connection.User;

public class DatabaseOperations {
		private static final String DB_NAME = "MyShowsDB"; 	
		private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		private static final String DB_URL = "jdbc:mysql://localhost/"+DB_NAME;
		    
	    //  Database attributes
	    private static String USER = "root";
	    private static String PASS = "root";
	    private static Connection conn;
	    
	    private static void closeConnection() {
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	    }
	    
	    private static void preliminaries() {
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
	    }
	    
	    public static String insertUser(User p){
	    	preliminaries();
	        String result = "OK";
	    	java.sql.PreparedStatement preparedStatement = null;
	        try {
	           
	            String sql = "INSERT INTO user"
	            		+ " (username, password) "
	            		+ " values ( ? , ? );";
	            preparedStatement = conn.prepareStatement(sql);
	            preparedStatement.setString(1, p.getUsername());
	            preparedStatement.setString(2, p.getPassword());
	            preparedStatement.execute(); 
	          

	        }catch(SQLIntegrityConstraintViolationException e){
	        	//e.printStackTrace();
	        	result = "Username already exists.";
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (preparedStatement != null) {
	                	 preparedStatement.close();
	                }
	            } catch (SQLException se2) {
	                closeConnection();
	                return result;
	            }
	        }
	        closeConnection();
	        return result;
		}
	    public static String insertShowHistory(User p,String showname,String status,int rating){
	    	preliminaries();
			ResultSet res = null;
			ResultSet res_id = null;
			ResultSet res_show = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	String result=null;
	    	String sql = new String();
	    	int id=0;
	    	String id_show = new String();	
            try {   
            	sql = "select max(id) from history";
            	preparedStatement = conn.prepareStatement(sql);
            	res_id = preparedStatement.executeQuery();
            	while(res_id.next()){
            		id=res_id.getInt("max(id)");
            		id++;
            	}
            	preparedStatement.close();
            	
            	sql = "select show_id from shows where showname=?";
            	preparedStatement = conn.prepareStatement(sql);
            	preparedStatement.setString(1, showname);
            	
            	res_show= preparedStatement.executeQuery();
            	while(res_show.next()){
            		id_show=res_show.getString("show_id");
            	}
            	preparedStatement.close();
            	
            	sql = "INSERT INTO history (id, show_id, user, ranking, status) values ( ?, ?, ?, ?, ?);";
				
            	preparedStatement = conn.prepareStatement(sql);
            	
		        preparedStatement.setInt(1, id);
				preparedStatement.setString(2, id_show);
				preparedStatement.setString(3, p.getUsername());
				preparedStatement.setInt(4, rating );
		        preparedStatement.setString(5, status);
		        
		        preparedStatement.execute(); 	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            try {
	                if (preparedStatement != null) {
	                	 preparedStatement.close();
	                }
	            } catch (SQLException se2) {
	                closeConnection();
	                return result;
	            }
	        }
	        closeConnection();
	        return result;
		}
	    public static String getDetails(String showname){
	    	preliminaries();
			ResultSet res = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	String result=new String(); 
	    	result += showname.toUpperCase();
	    	result += "\n\n";
            try {            	
            	String sql = "select details from shows where showname=?";
            	preparedStatement = conn.prepareStatement(sql);
            	preparedStatement.setString(1,showname);
		        res = preparedStatement.executeQuery(); 	
		        while(res.next()){
		        	result+=res.getString("details");
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} finally {
	            try {
	                if (preparedStatement != null) {
	                	 preparedStatement.close();
	                }
	            } catch (SQLException se2) {
	                closeConnection();
	                return result;
	            }
	        }
	        closeConnection();
	        return result;
		}   
	    public static void deleteShow(User p,String showname){
	    	preliminaries();
			ResultSet res = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	String result=new String(); 
	    	String sql = "select show_id from shows where showname=?";
	    	String show_id=new String();
            try {  
            	preparedStatement=conn.prepareStatement(sql);
    	    	preparedStatement.setString(1,showname);
    	    	res=preparedStatement.executeQuery();
				while(res.next())
					show_id = res.getString("show_id");
    	    	preparedStatement.close();
    	    	
    	    	//--------------------------------------------------
    	    	
            	sql = "delete from history where user=? and show_id=?";
            	preparedStatement = conn.prepareStatement(sql);
            	preparedStatement.setString(1,p.getUsername());
            	preparedStatement.setString(2,show_id);
            	preparedStatement.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            try {
	                if (preparedStatement != null) {
	                	 preparedStatement.close();
	                }
	            } catch (SQLException se2) {
	                closeConnection();
	            }
	        }
	        closeConnection();
		}   
	    public static ArrayList<String> getAddShows(User p){
	    	preliminaries();
			ResultSet res = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	ArrayList<String> result = new ArrayList<String>();
	    	ArrayList<String> result2 = new ArrayList<String>();
	    	try{
		    	String sql = "select show_id from shows where show_id not in ( select show_id from history where user=?)";
		    	preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, p.getUsername());
		        res = preparedStatement.executeQuery(); 
		        while(res.next()){
		        		result.add(res.getString("show_id"));
		        }
				sql = "select showname from shows where show_id=?";
		        for(int i=0; i<result.size();i++){
		        	preparedStatement.close();
		        	preparedStatement = conn.prepareStatement(sql);
		        	preparedStatement.setString(1,result.get(i));
			        res = preparedStatement.executeQuery(); 
			        while(res.next())
			        	result2.add(res.getString("showname"));
		        }
	    	}
	    	catch (Exception e) {
		           e.printStackTrace();
		        } finally {
		            try {
		                if (preparedStatement != null) {
		                	 preparedStatement.close();
		                }
		            } catch (SQLException se2) {
		                closeConnection();
		            }
		        }
		        closeConnection();
		    return result2;
	    }
	    
	    public static ArrayList<String> getUserShows(User p){
	    	preliminaries();
			ResultSet res = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	ArrayList<String> result = new ArrayList<String>();
	    	ArrayList<String> result2 = new ArrayList<String>();
	    	try{
		    	String sql = "select show_id from history where user=?";
		    	preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, p.getUsername());
		        res = preparedStatement.executeQuery(); 
		        while(res.next()){
		        		result.add(res.getString("show_id"));
		        }
				sql = "select showname from shows where show_id=?";
		        for(int i=0; i<result.size();i++){
		        	preparedStatement.close();
		        	preparedStatement = conn.prepareStatement(sql);
		        	preparedStatement.setString(1,result.get(i));
			        res = preparedStatement.executeQuery(); 
			        while(res.next())
			        	result2.add(res.getString("showname"));
		        }
	    	}
	    	catch (Exception e) {
		           e.printStackTrace();
		        } finally {
		            try {
		                if (preparedStatement != null) {
		                	 preparedStatement.close();
		                }
		            } catch (SQLException se2) {
		                closeConnection();
		            }
		        }
		        closeConnection();
		    return result2;
	    }
	    public static ArrayList<String> getTinder(User p,String showname){
	    	preliminaries();
	    	int id = 0, ranking = 0;
			ResultSet res = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	ArrayList<String> result = new ArrayList<String>();
	    	try{
		    	String sql = "select show_id from shows where showname=?";
		    	preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, showname);
		        res = preparedStatement.executeQuery(); 
		        while(res.next()){
		        	 id = res.getInt("show_id");
		        }
	        	preparedStatement.close();

				sql = "select ranking from history where show_id=? and user=?";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setInt(1,id);
		        preparedStatement.setString(2, p.getUsername());
			    res = preparedStatement.executeQuery(); 
			    while(res.next()){
			    	ranking = res.getInt("ranking");
		        }
	        	preparedStatement.close();

	        	sql = "select user from history where show_id=? and ranking=? and user != ?";
	        	preparedStatement = conn.prepareStatement(sql);
	        	preparedStatement.setInt(1, id);
	        	preparedStatement.setInt(2, ranking);
	        	preparedStatement.setString(3, p.getUsername());
	        	res = preparedStatement.executeQuery();
	        	while(res.next()){
	        		result.add(res.getString("user"));
	        	}
	    	}
	    	catch (Exception e) {
		           e.printStackTrace();
		        } finally {
		            try {
		                if (preparedStatement != null) {
		                	 preparedStatement.close();
		                }
		            } catch (SQLException se2) {
		                closeConnection();
		            }
		        }
		        closeConnection();
		    return result;
	    }
	    
	    public static String searchUser(User p){
			preliminaries();
			ResultSet res = null;
	    	java.sql.PreparedStatement preparedStatement = null;
	    	String result=null;
	        try {
	           
	            String sql = "select username,password from user where username=?";
	            		
	            preparedStatement = conn.prepareStatement(sql);
	            preparedStatement.setString(1, p.getUsername());
	            res = preparedStatement.executeQuery(); 
	            
				int i = 0;
				while(res.next()){
					i++;
					if(res.getString(2).trim().toLowerCase().equals(p.getPassword())){
						result = "OK";
					}else{
						result = "WRONG PASSWORD";	
					}
				}
				if(i == 0){
					result = "WRONG USERNAME";
				}
				  

	        }  catch (Exception e) {
	           e.printStackTrace();
	        } finally {
	            try {
	                if (preparedStatement != null) {
	                	 preparedStatement.close();
	                }
	            } catch (SQLException se2) {
	                closeConnection();
	            }
	        }
	        closeConnection();
	        return result;
	    }
	    
}

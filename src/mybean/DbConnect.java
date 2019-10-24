/*
 * Created on 2005-11-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mybean;
import java.sql.*;
/**
 * @author zhanggan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DbConnect {

	String username = "zhanggan";
	String password = "zhanggan";
	String url="jdbc:mysql://localhost/website";
	Connection sqlConn = null; 
	Statement sqlStmt = null; 
	ResultSet rs = null;
	public DbConnect(){
		try{
			Class.forName ( "org.gjt.mm.mysql.Driver" );
			System.out.println ( "MySQL Driver Found" );
			sqlConn= java.sql.DriverManager.getConnection (url,username,password);
			sqlConn.setAutoCommit(true);
			sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY); 
			
		}
		catch (Exception e){
			System.err.println("Connect ERROR:"+e.getMessage());	
		}
	}
	public ResultSet executeQuery(String SqlCmd){
		try{
			rs = sqlStmt.executeQuery(SqlCmd);
		}
		catch (SQLException ex)
		{
			System.err.println("Query ERROR:"+ex.getMessage());
		}
		return rs;
	}
	public int executeUpdate(String SqlCmd){
		int affectRows = -1;
		try{
			affectRows = sqlStmt.executeUpdate(SqlCmd);
		}
		catch (SQLException ex)
		{
			System.err.println("Update ERROR:"+ex.getMessage());
		}
		return affectRows;
	}	
}

/*
 * Created on 2005-11-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 *  * V101 ZHANGGAN 2006/05/07 -New MySQL-connector used on windowsXP.
 *  * V102 zhanggan 2008/06/24 -Use ini file for db connect.
 */

package mybean;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
/**
 * @author zhanggan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DbConnect1 {

	private String username = "zhanggan";
	private String password = "zhanggan";
	private String url="jdbc:mysql://localhost/website";
	private String strCustFile="mydatasource.prop";
	private String strClass = "com.mysql.jdbc.Driver";
	private final String MYSQL_USER_NAME="mysql.user.name";
	private final String MYSQL_PASSWORD="mysql.password";
	private final String MYSQL_URL="mysql.url";
	private final String MYSQL_DRIVER_CLASS="mysql.driver.class";
	
	
	Connection sqlConn = null; 
	public DbConnect1() throws ClassNotFoundException, SQLException{
		getCustomerSetting();											//V102
      Class.forName ( strClass );
		//Class.forName ( "org.gjt.mm.mysql.Driver" );        //V101 -
		//System.out.println ( "MySQL Driver Found" );
		Properties p = new Properties();
		p.setProperty("user",username);
		p.setProperty("password",password);
		p.setProperty("useUnicode","true");
		p.setProperty("characterEncoding","gb2312");
		
		sqlConn= java.sql.DriverManager.getConnection (url,p);
		sqlConn.setAutoCommit(true);
	}
	public Connection getCnnct(){
		return sqlConn;
	}
	public void close() throws SQLException
	{
		sqlConn.close();
	}
	//V102 start
	/**
	 * To get the setting by config file myconf.prop.
	 *
	 */
	public void getCustomerSetting(){
		File f = null;
		FileInputStream fis = null;
		try{
			f = new File(strCustFile);
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			username = p.getProperty(MYSQL_USER_NAME, "zhanggan");
			password = p.getProperty(MYSQL_PASSWORD,"zhanggan");
			url = p.getProperty(MYSQL_URL,"jdbc:mysql://localhost/website");
			strClass = p.getProperty(MYSQL_DRIVER_CLASS,"com.mysql.jdbc.Driver");
		}
		catch(Exception e){
			System.out.println("DbConnect1.getCustomerSetting,Exception="+e.getMessage());
		}
		finally{
			if(fis!=null){
				try{
					fis.close();
					}
				catch(Exception e){}
			}
		}
	}
	//V102 end.
}

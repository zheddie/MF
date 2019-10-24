package mybean;

import java.util.*;
import java.sql.*;
import java.sql.Statement;
/*
 * Created on 2005-11-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyTranslateMessage {
	Hashtable<String,String> engTable = new Hashtable<String,String>();
	Hashtable<String,String> chnTable = new Hashtable<String,String>();
	Connection con = null;
	Statement stmtVoc = null;
	public MyTranslateMessage() throws ClassNotFoundException, SQLException {

		DbConnect1 dbCnnct = new DbConnect1();
		con  = dbCnnct.getCnnct();
		stmtVoc = con.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);
		initHashtable("english");
		initHashtable("chinese");
	}
	private void initHashtable(String strLang) throws SQLException{
		ResultSet rstVoc = stmtVoc.executeQuery("select name,message from mf_vocabulary where language =\""+
				strLang +
				"\"");
		rstVoc.last();
		int iRows = rstVoc.getRow();
		if (iRows>0)
		{
			rstVoc.first();
			for(int i =0 ;i<iRows;i++){
				String name = rstVoc.getString("name");
				String message = rstVoc.getString("message");
				if (strLang.equals("english")) engTable.put(name,message);
				if (strLang.equals("chinese")) chnTable.put(name,message);
				rstVoc.next();
			}
		}
		rstVoc.close();
	}
	public String getMessage(String strLang ,String strIdntfr){
		if (strLang.equals("english")) 
			return (String)engTable.get(strIdntfr);
		if (strLang.equals("chinese"))
		{
			if (chnTable.get(strIdntfr) ==null)
				return (String)engTable.get(strIdntfr);
			return (String)chnTable.get(strIdntfr);
		}
		return "";
	}
}

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import mybean.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
public class MyFavoriteManager extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4103405306425858464L;

	/**
	 * Constructor of the object.
	 */
	public MyFavoriteManager() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String strInputPass = "nopassword";
		String strConfirmPass = "";
		StringBuffer strHtml = new StringBuffer();
		int iUserStatus = -1;
		ParamProcess pp = new ParamProcess();
		String strLang = pp.getParam(request,"language","english") ;
//		if ("中文".equals(strLang)) 
//		{
//			strLang = "chinese";
//		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		//if("chinese".equals(strLang)) response.setContentType("text/html;charset=gb2312");
		//if("english".equals(strLang)) response.setContentType("text/html");
		if(request.getParameter("passwd") != null)
			strInputPass = request.getParameter("passwd");
		if(request.getParameter("confirmpasswd") != null)
			strConfirmPass = request.getParameter("confirmpasswd");			
		String strInputUser = pp.getParam(request,"login","Your@Mail.box");

		PrintWriter out = response.getWriter();
		try{
			DbConnect1 dbCnnct = new DbConnect1();
			Connection  con = dbCnnct.getCnnct();
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

			String sqlCmd = "select userid,status,password from mf_user where" +
				" username=\"" +
				strInputUser +
				"\"";
			
			ResultSet rst = stmt.executeQuery(sqlCmd);
			rst.last();
			int iCount = rst.getRow();
			if(iCount < 0 )
			{
				strHtml.append("Please Refresh your page!");
			}
			else
			{			
				iUserStatus = rst.getInt("status");

				if (iUserStatus == 1 && strInputPass.equals(strConfirmPass)){
					//strHtml.append("Password:"+strInputPass +"---ConfirmPass="+strConfirmPass+"<br>");
					sqlCmd = "update mf_user set status=0,password=\"" +
						strConfirmPass +"\" "+
						"where userid=\"" + rst.getString("userid") +"\"";
					stmt.executeUpdate(sqlCmd);
				}
				if(! strInputPass.equals(rst.getString("password")))
				{
				
					MyTranslateMessage trs = new MyTranslateMessage();
					
					strHtml.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
					strHtml.append(" \n" +
							"\n<HTML>"+
							" \n <HEAD><TITLE>My FavoriteSite Manager</TITLE></HEAD>"+
							"  \n<BODY>");
	
					
					strHtml.append("\n<center>"+
							trs.getMessage(strLang,"managerinfo") +
							" \n<br><form id=\"mgrlogin\" name=\"mgrlogin\" method=\"post\" action=\""+
							"MyFavoriteManager"+
							"\">"+
							"\n<table border=0 ><tr><td>"+
							trs.getMessage(strLang,"username")+
							"\n</td>"+
							"\n<td><input type=\"text\" id=\"login\" name=\"login\" value=\""+
							strInputUser +
							"\"></td>"+
							"</tr>");
					
					strHtml.append("<tr><td>"+
							trs.getMessage(strLang,"password")+
							"</td>"+
							"\n<td><input type=\"password\" id=\"passwd\" name=\"passwd\" ></td>"+
							"</tr>\n");
					if(iUserStatus == 1){
						//New User ,need twice password for confirm
						strHtml.append("<tr><td>"+
								trs.getMessage(strLang,"passwordconfirm")+
								"</td>"+
								"\n<td><input type=\"password\" id=\"confirmpasswd\" name=\"confirmpasswd\" ></td>"+
								"</tr>\n");						
					}
					strHtml.append("<tr><td><input type=\"submit\" id=\"submit\" name=\"submit\" value=\""+
							trs.getMessage(strLang,"login")+
							"\"</td><td></td>"+
							"</tr>\n</table></form>"+
							"  \n</center>");
	
				}
				else
				{
					String strUserID = rst.getString("userid");
					//out.println("LOGIN SUCCESSFUL!");
					HttpSession session= request.getSession();
					session.setMaxInactiveInterval(300);
					session.setAttribute("mfusername",strInputUser);
					session.setAttribute("mfuserid",strUserID);
					//session.setAttribute("mfusername",strInputUser);
					//session.setAttribute("mfuserid",strUserID);
					//Jump to modify screen.
					response.sendRedirect( "MFWebtypeModify");
				}
			}
			strHtml.append("\n</BODY></HTML>");
			out.println(strHtml);
			rst.close();
			stmt.close();
			out.flush();
			out.close();
		}
		catch(Exception e)
		{
			out.println("ERROR:"+e.getMessage());
			StackTraceElement [] steAll = e.getStackTrace();
			for(int so =0 ;so <steAll.length;so++)
			{
				out.println(steAll[so].getClassName());
				out.println(":"+steAll[so].getMethodName());
				out.println(String.valueOf(steAll[so].getLineNumber()));
				out.println("<br>");
			}
			
		}		
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

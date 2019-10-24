/*
 * Created on 2005-11-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V100 zhanggan 20061124 -Add new field "visible" for webtype.
 */
package myservlet;

import java.io.*;
import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybean.DbConnect1;
import mybean.MyTranslateMessage;
import mybean.ParamProcess;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MFWebtypeModify extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1483864115937033087L;

	/**
	 * Constructor of the object.
	 */
	public MFWebtypeModify() {
		super();
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

		doPost(request,response);
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
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String strUserName = "";
		StringBuffer sbHtml = new StringBuffer();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		ParamProcess pp = new ParamProcess();
		//MyTranslateMessage trs = new MyTranslateMessage();
		String strLang = pp.getParam(request,"language","english") ;		
		HttpSession session = request.getSession(false);
		try{
			MyTranslateMessage trs = new MyTranslateMessage();			
			if (session == null || session.getAttribute("mfusername") ==null)
			{
				out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
				out.println("<HTML>");
				out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
				out.println("  <BODY><center>");
				out.println(trs.getMessage(strLang,"sessionexpired")+"<br>");
				out.println("<a href = \"MyFavoriteManager\">" +
						trs.getMessage(strLang,"promptlogin")+
						"</a><br>");
				out.println(" </center> </BODY>");
				out.println("</HTML>");
			}
			else
			{
				strUserName= (String) session.getAttribute("mfusername");

				DbConnect1 dbCnnct = new mybean.DbConnect1();
				Connection sqlConn= dbCnnct.getCnnct();
				Statement sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);
				//V100 >>
				//String sqlCmd = " select b.displayorder displayorder ,c.idntfr idntfr,c.displayid displayid"
				String sqlCmd = " select b.displayorder displayorder ,c.idntfr idntfr,c.displayid displayid ,b.visible visible"
				//V100 <<
					+" from mf_user a ,mf_user_webtype b ,mf_webtype c"
					+" where a.userid = b.useridntfr"
					+" and a.username =\""+strUserName+"\""
					+" and b.webtypeidntfr=c.idntfr "
					+" order by b.displayorder";
				ResultSet sqlRst = sqlStmt.executeQuery(sqlCmd);
				sqlRst.last();
				String PropIn ="";
				if(sqlRst.getRow()>0)
				{
					int iCnt = sqlRst.getRow();
					sqlRst.first();
					for(int k =0;k<iCnt;k++){
						//V100 >>
						//PropIn=PropIn+sqlRst.getString(1)+"*"+sqlRst.getString(2)+"*"+sqlRst.getString(3)+",";
						PropIn=PropIn+sqlRst.getString(1)+"*"+sqlRst.getString(2)+"*"+sqlRst.getString(3)+"*"+sqlRst.getString(4)+",";
						//V100 <<
						sqlRst.next();
					}
					PropIn  = PropIn.substring(0,PropIn.length()-1);
				}
				sqlRst.close();
				sqlStmt.close();
				sbHtml.append("<script language=\"javascript\" >");
				sbHtml.append("<!--\n ");
				sbHtml.append("var PropIn = \"" +
						PropIn +
						"\"; \n");
				sbHtml.append("--></script>");
				//FileReader f = new FileReader("./WebTypeMaintenance.html");
				ServletContext sc = session.getServletContext();
				String strFileName  = null;
				if ("english".equals(strLang))
				{
					strFileName = "/htmltmpl/WebTypeMaintenance.en.html";
				}
				else if ("chinese".equals(strLang))
				{
					strFileName = "/htmltmpl/WebTypeMaintenance.ch.html";
				}
				String strUrl = sc.getRealPath(strFileName);
				File f = new File(strUrl);
				FileInputStream is = new FileInputStream(f);
				byte [] bLine = new byte[4096];
				
				//DataInputStream dis = new DataInputStream(is);
				//BufferedReader bf = new BufferedReader() ;
				//String strLine = null;
				int iCnt =0;
				while((iCnt = is.read(bLine,0,bLine.length)) > 0)
				{
					sbHtml.append(new String(bLine,0,iCnt));
				}
				
				is.close();
				out.println(sbHtml);
			}
		}			
		catch ( Exception e ) {
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

		out.flush();
		
		out.close();
	}

}

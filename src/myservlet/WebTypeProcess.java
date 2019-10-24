package myservlet;
import mybean.ParamProcess;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import mybean.DbConnect1;
//import mybean.MyTranslateMessage;

/*
 * Created on 2005-11-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V101   2006/05/04   ---The sqlRst need to be closed only for insert process.
 * V102   zhanggan 20061124 --Add new field to control the visibility of webtype.
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WebTypeProcess extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4680915313845267212L;

	/**
	 * Constructor of the object.
	 */
	public WebTypeProcess() {
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
		StringBuffer sbHtml= new StringBuffer();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//HttpSession hs= request.getSession();
		String MASK = "0000000000";
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		sbHtml.append("<HTML>");
		sbHtml.append("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		sbHtml.append("  <BODY>");
		
		try{
			String propout = request.getParameter("propout");
			String propin = request.getParameter("propin");
			sbHtml.append(propout+"<br>");
			if(! propout.equals(propin))
			{
				String [] arrType = propout.split(",");
				ParamProcess pp = new ParamProcess();
				//String strUserName = pp.getParam(request,"mfusername","") ;
				String strUserID = pp.getParam(request,"mfuserid","") ;
				//MyTranslateMessage trs = new MyTranslateMessage();
				DbConnect1 dbCnnct = new mybean.DbConnect1();
				Connection sqlConn= dbCnnct.getCnnct();
				Statement sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY); 
				ResultSet sqlRst = null;
				String sqlCmd =null;
				for(int i = 0;i<arrType.length;i++){
					sbHtml.append("ERROR:"+arrType[i]);
					String [] arrField = arrType[i].split("\\*");
					
					if (arrField[1].equals("#")){
						sqlCmd = "select max(idntfr) max_idntfr from mf_webtype";
						sqlRst = sqlStmt.executeQuery(sqlCmd);
						sqlRst.first();
						int iMaxWebIdntf = Integer.parseInt(sqlRst.getString("max_idntfr"));					
						String strWebIdntfr=String.valueOf(++iMaxWebIdntf);
						strWebIdntfr = MASK.substring(0,MASK.length()  -strWebIdntfr.length())+strWebIdntfr ;
						sqlCmd = "select max(idntfr) max_idntfr from mf_user_webtype";
						sqlRst = sqlStmt.executeQuery(sqlCmd);
						sqlRst.first();
						int iMaxUserWebIdntf = Integer.parseInt(sqlRst.getString("max_idntfr"));					
						String strUserWebIdntfr=String.valueOf(++iMaxUserWebIdntf);
						strUserWebIdntfr = MASK.substring(0,MASK.length()  -strUserWebIdntfr.length())+strUserWebIdntfr ;
						sqlCmd = "insert into mf_webtype(userid,idntfr,displayid,status,startdate) values(\"\",\"" +
								strWebIdntfr+
								"\",\"" +
								arrField[2] +
								"\",0,curdate()" +
								")";
						sbHtml.append(sqlCmd+"<br>");
						sqlStmt.executeUpdate(sqlCmd);
						//V102 >>
						//sqlCmd = "insert into mf_user_webtype(useridntfr,webtypeidntfr,startdate,idntfr,displayorder) values(\"" +
						sqlCmd = "insert into mf_user_webtype(useridntfr,webtypeidntfr,startdate,idntfr,displayorder,visible) values(\"" +
						//V102 <<
								strUserID +"\",\"" +
								strWebIdntfr +"\",curdate(),\"" +
								strUserWebIdntfr+"\","+
								arrField[0]+
								","+arrField[3]+          //V102 ++
								")";
						sbHtml.append(sqlCmd+"<br>");
						sqlStmt.executeUpdate(sqlCmd);
					}
					else
					{
						if (!arrField[0].equals("-1")){
	
							sqlCmd = "update mf_webtype set displayid=\"" +
								arrField[2]+
								"\" where idntfr =\"" +
								arrField[1] +"\"";
							sbHtml.append(sqlCmd+"<br>");
							sqlStmt.executeUpdate(sqlCmd);
	
						}
						sqlCmd = "update mf_user_webtype set displayorder=\"" +
							arrField[0]+
							"\" ,visible=" +arrField[3] +       //V102 ++
							" where webtypeidntfr =\"" +
							arrField[1] +"\" and useridntfr=\"" +
							strUserID+"\"";
						sbHtml.append(sqlCmd+"<br>");					
						sqlStmt.executeUpdate(sqlCmd);
					}
				}
				//sqlRst.close();          V101 -
				//V101 >>
				if (sqlRst != null) sqlRst.close();
				//V101<<
				sqlStmt.close();
			}
			response.sendRedirect( "MFWebsiteModify");
		}
		catch(Exception e){
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
		
		sbHtml.append("  </BODY>");
		sbHtml.append("</HTML>");
		out.println(sbHtml);
		out.flush();
		out.close();
		
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

/*
 * Created on 2005-12-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V101   2006/02/15   ---The sqlRst need to be closed only for insert process.
 * V102 zhanggan 20060914 --Add content field in myfavorite table to store some private infor for one site.
 * V103 zhanggan 20061124 --Add new field to control the visibility of website.
 */
package myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import mybean.DbConnect1;
//import mybean.MyTranslateMessage;
//import mybean.ParamProcess;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WebSiteProcess extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8308394963248406212L;

	/**
	 * Constructor of the object.
	 */
	public WebSiteProcess() {
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
				//ParamProcess pp = new ParamProcess();
				//String strUserName = pp.getParam(request,"mfusername","") ;
				//String strUserID = pp.getParam(request,"mfuserid","") ;
				//MyTranslateMessage trs = new MyTranslateMessage();
				DbConnect1 dbCnnct = new mybean.DbConnect1();
				Connection sqlConn= dbCnnct.getCnnct();
				Statement sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY); 
				ResultSet sqlRst = null;
				String sqlCmd =null;
				for(int i = 0;i<arrType.length;i++){
					String [] arrField = arrType[i].split("\\*");
					if (arrField[2].equals("#")){
						sqlCmd = "select max(idntfr) max_idntfr from myfavorite";
						sqlRst = sqlStmt.executeQuery(sqlCmd);
						sqlRst.first();
						int iMaxSiteIdntf = Integer.parseInt(sqlRst.getString("max_idntfr"));					
						String strSiteIdntfr=String.valueOf(++iMaxSiteIdntf);
						strSiteIdntfr = MASK.substring(0,MASK.length()  -strSiteIdntfr.length())+strSiteIdntfr ;
						sqlCmd = "select max(idntfr) max_idntfr from mf_webtype_site";
						sqlRst = sqlStmt.executeQuery(sqlCmd);
						sqlRst.first();
						int iMaxWebSiteIdntf = Integer.parseInt(sqlRst.getString("max_idntfr"));					
						String strWebSiteIdntfr=String.valueOf(++iMaxWebSiteIdntf);
						strWebSiteIdntfr = MASK.substring(0,MASK.length()  -strWebSiteIdntfr.length())+strWebSiteIdntfr ;
						//V102 >>
						/*sqlCmd = "insert into myfavorite (userid,webtype,startdate,url,displayid,idntfr) values(\"\",\"\",curdate(),\"" +
								arrField[3] +"\",\"" +
								arrField[4] +"\",\"" +
								strSiteIdntfr+
								"\")";*/
						sqlCmd = "insert into myfavorite (userid,webtype,startdate,url,displayid,content,idntfr) values(\"\",\"\",curdate(),\"" +
							arrField[3] +"\",\"" +
							arrField[4] +"\",\"" +
							arrField[5] +"\",\"" +
							strSiteIdntfr+
							"\")";						
						//V102 <<
						sbHtml.append(sqlCmd+"<br>");
						sqlStmt.executeUpdate(sqlCmd);
						//V103 >>
						//sqlCmd = "insert into mf_webtype_site(webtypeidntfr,siteidntfr,startdate,idntfr,displayorder) values(\"" +
						sqlCmd = "insert into mf_webtype_site(webtypeidntfr,siteidntfr,startdate,idntfr,displayorder,visible) values(\"" +
						//V103 <<
								arrField[0] +"\",\"" +
								strSiteIdntfr+"\",curdate(),\"" +
								strWebSiteIdntfr+"\","+
								arrField[1]+" ,"+
								arrField[6]+    //V103 ++
								")";
						sbHtml.append(sqlCmd+"<br>");
						sqlStmt.executeUpdate(sqlCmd);
					}
					else
					{
						if (!arrField[1].equals("-1")){
							//V102 >>
							/*sqlCmd = "update myfavorite set displayid=\"" +
								arrField[4]+"\" ,url=\"" +
								arrField[3]+"\" where idntfr =\"" +
								arrField[2] +"\"";*/
							sqlCmd = "update myfavorite set displayid=\"" +
								arrField[4]+"\" ,content=\"" +
								arrField[5]+"\" ,url=\"" +
								arrField[3]+"\" where idntfr =\"" +
								arrField[2] +"\"";
							//V102 <<
							sbHtml.append(sqlCmd+"<br>");
							sqlStmt.executeUpdate(sqlCmd);
	
						}
						sqlCmd = "update mf_webtype_site set displayorder=\"" +
							arrField[1]+"\" " +
							" , visible ="+
							arrField[6] +
							" where siteidntfr =\"" +
							arrField[2] +"\" and webtypeidntfr = \"" +
							arrField[0] +"\"";
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
			response.sendRedirect( "MyFavorite");
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

}

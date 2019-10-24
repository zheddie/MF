import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybean.*;
/*
 * Created on 2005-11-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V100 zhanggan 20060921 -Add Applet as one control for each URL.
 * V101 zhanggan 20061124 -webtype & website add a new field called visible ,0:visible ,others: invisible.
 * V102 zhanggan 20070104 -Add count for each click on one site.
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyFavorite extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public MyFavorite() {
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
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(new Date());
        String strWekNum = String.valueOf(nowCal.get(Calendar.DAY_OF_WEEK)-1);
        String strWekName = "week" +strWekNum ;
        String strWekColor = null;
        if (strWekName.equals("week1")){
			strWekColor = "color=\"red\" size=\"4\"";
		}
        else
        	strWekColor = "color=\"gray\" size=\"4\"";

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");		
		String strInputUser = "";
//		String strInputPass="";
		
//		String strInputUserParam = "";
		String strInputPassParam="";
		
//		String sqlQuery ="";
//		String WebTypeIdntfr ="";
		int i=0,j=0;
		String [] Colors= new String[2] ;
		Colors[0] = "#E0F4F8";
		Colors[1] = "#ffffff";
		StringBuffer sbHtml = new StringBuffer();
		
		Connection sqlConn = null; 
		
		Statement sqlStmt = null; 
		
		ResultSet sqlRst = null;
		
		Cookie myCk = null;
		String sqlCmd = null;
		String MASK = "0000000000";
		ParamProcess pp = new ParamProcess();
		PrintWriter out = response.getWriter();
		strInputPassParam = request.getParameter("password");
		
		HttpSession session = request.getSession(false);
		if(session!=null) session.setMaxInactiveInterval(0);
		String strLang = pp.getParam(request,"language","english") ;

		
		//if("chinese".equals(strLang)) response.setContentType("text/html;charset=gb2312");
		//if("english".equals(strLang)) response.setContentType("text/html");
		sbHtml.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		myCk = new Cookie("language",strLang);
		myCk.setMaxAge(1*24*60*60);
		response.addCookie(myCk);
		try {
			DbConnect1 dbCnnct = new mybean.DbConnect1();
			sqlConn= dbCnnct.getCnnct();
//			As WAS didn't support DS , so didn't give JDBC support here.Following code is just a sample to
//			to use JDBC. Not sure it can work.
//			 			InitialContext ctx=new InitialContext();
//			 			DataSource ds=(DataSource)ctx.lookup("java:comp/env/jndi/MYDB1");
//			sqlConn = ds.getConnection();			
			
			sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY); 
					
			strInputUser = pp.getParam(request,"login","Your@Mail.box") ;

			myCk = new Cookie("login",strInputUser);
			myCk.setMaxAge(1*24*60*60);
			response.addCookie(myCk);
			//sbHtml.append("Addcook:"+strInputUser+"<hr>");
			if(strInputPassParam ==null) strInputPassParam = "";
			sqlCmd = "select viewpassword from mf_user where username=\"" +
					strInputUser +
					"\" ";
			sqlRst = sqlStmt.executeQuery(sqlCmd);
			sqlRst.last();
			int iC = sqlRst.getRow();
			String strPass = "";
			if (iC == 0){
				sqlCmd = "select max(userid) from mf_user ;";
				sqlRst = sqlStmt.executeQuery(sqlCmd);
				sqlRst.first();
				int iMaxWebSiteIdntf = Integer.parseInt(sqlRst.getString(1));;
				String strNewID=String.valueOf(++iMaxWebSiteIdntf);
				strNewID = MASK.substring(0,MASK.length()  -strNewID.length())+strNewID ;
				sqlCmd = "insert into mf_user (username,password,startdate,status,userid,viewpassword) values(\""+
						strInputUser +"\",\"\",curdate(),1,\"" +  //Status ==1 ,stands for no password set for this user.
						strNewID+"\",\"" +
						strInputPassParam+
						"\");";
				sqlStmt.executeUpdate(sqlCmd);
				strPass = strInputPassParam;
			}
			else
			{
				strPass = sqlRst.getString("viewpassword");
			}
			MyTranslateMessage trs = new MyTranslateMessage();
			
			sbHtml.append("\n<html><head> <title>"
					+ trs.getMessage(strLang,"maintitle")
					+"</title></head> "
					+"<body><table border=0 width=\"100%\"><tr><td width=\"85%\" align=\"center\">" +
					"<font  color=gray size=\"8\">"
					+ trs.getMessage(strLang,"title1")
					+ trs.getMessage(strLang,"version")
					+"</font></td><td width=\"15%\" align=\"right\"><table border=0><tr><td><font  " +
					strWekColor +
					" >" +
					trs.getMessage(strLang,strWekName) +
					"</font></td></tr></table></td></tr></table>"
					+"\n<hr><TABLE>"
					+"<tr><form name=\"login\" method =\"post\" action=\"MyFavorite\">"
					+"\n <td>"
					+ trs.getMessage(strLang,"username")
					+"</td><td><input name=\"login\" type=\"text\" id=\"login\" value=\""
					+strInputUser
					+"\"\n></td>"
					+"<td>"
					+ trs.getMessage(strLang,"password")
					+"</td><td><input name=\"password\" type=\"password\" "
					+"id=\"password\" value=\""
					+ strInputPassParam 
					+ "\"></td><td><input type=\"submit\" name=\"submit\" value=\""
					+ trs.getMessage(strLang,"refresh")
					+"\"></td>	"
					+"	</form>	<form name=\"manager\" method=\"post\" action=\"MyFavoriteManager\">" +
					"<td><input type=\"submit\" name=\"mgrsubmit\" value=\""
					+ trs.getMessage(strLang,"manager")
					+"\">"+
					"</td></form><form name=\"lgenglish\" method=\"post\" action=\""
					+ "MyFavorite"
					+"\"><td><input type=\"submit\"  name=\"language\" value=\"english\"></td></form>" +
					"<form name=\"lgchinese\" method=\"post\" action=\""
					+ "MyFavorite"
					+"\"><td><input type=\"submit\"  name=\"language\" value=\"ÖÐÎÄ\"></td></form></tr></TABLE>"
					+"</center>"
					+"\n<table border=\"1\" width=\"100%\" bordercolorlight=\"#CC99FF\" cellpadding=\"2\" bordercolordark=\"#FFFFFF\" cellspacing=\"0\"> ");
			if (!strPass.equals(strInputPassParam)){
				sbHtml.append("<tr align=\"center\"><td>" +
					trs.getMessage(strLang,"passwordprotected1")+	
					"</td></tr>");
				sbHtml.append("<tr align=\"center\"><td>" +
						trs.getMessage(strLang,"passwordprotected2")+
						"</td></tr>");
			}
			else//Password Correct.
			{
				
				//sqlCmd = " select a.displayid webtype ,b.displayid website,b.url url" +  //V100 --
				sqlCmd = " select a.displayid webtype ,b.displayid website,b.url url,b.idntfr idntfr" +   //V100 ++
					" from mf_webtype a ,myfavorite b  ,mf_user c ,mf_user_webtype d ,mf_webtype_site e" +
					" where b.idntfr = e.siteidntfr"+
					" and c.userid = d.useridntfr"+
					" and d.webtypeidntfr = a.idntfr"+
					" and e.webtypeidntfr = d.webtypeidntfr"+
					" and c.username =\""+strInputUser+"\"" +
					" and d.displayorder >=0 " +
					" and e.displayorder >=0 "+
					//V101 >>
					" and d.visible = 0 "+
					" and e.visible = 0 "+
					//V101 <<
					" order by d.displayorder ,e.displayorder";
				sqlRst = sqlStmt.executeQuery(sqlCmd);
				boolean loopflag = true;
				sqlRst.last();
				int iRows = sqlRst.getRow();
				if (iRows >0)
				{
					sqlRst.absolute(1);
					String strWebType = sqlRst.getString("webtype");
					String strPreType = "";
					//int iLine = 0;
					
					while (loopflag) 
					{ 
						if(! strWebType.equals(strPreType))
						{
							sbHtml.append("\n<tr bgcolor=\"");
							sbHtml.append(Colors[++j%2]);
							sbHtml.append("\">\n<td width=\"15%\" ><h4>");
							sbHtml.append(strWebType);
							sbHtml.append("\n</h4></td><td><table width=\"100%\" cellpadding=\"2\">");
							//iLine = 0;
							strPreType  = strWebType;
						}
						while(strWebType.equals(strPreType)&& loopflag)
						{
							sbHtml.append("\n<tr>");
							for (i = 0 ;i<4 ;i++) 
							{
								if (loopflag&& strWebType.equals(strPreType))
								{
									//V100 >>
									
									sbHtml.append("\n<td width=\"25%\" align=\"left\"><a target=_blank href=");
									sbHtml.append(sqlRst.getString("url"));
									sbHtml.append(">\n");
									sbHtml.append(sqlRst.getString("website"));
									sbHtml.append("\n</a></td> ");
									/*Tamporarily remove the Applet ,as the memory issue of it.
									sbHtml.append("\n<td width=\"25%\" align=\"left\">");
									sbHtml.append("<APPLET code=\"Applet/WebSite.class\" width=\"200\" height=\"25\">");
									sbHtml.append("<PARAM NAME=\"idntfr\" VALUE=\""+sqlRst.getString("idntfr")+"\">");
									sbHtml.append("<PARAM NAME=\"displayid\" VALUE=\""+sqlRst.getString("website")+"\">");
									sbHtml.append("<PARAM NAME=\"url\" VALUE=\""+sqlRst.getString("url")+"\">");
									sbHtml.append("<PARAM NAME=\"backcolor\" VALUE=\""+Colors[j%2]+"\">");
									sbHtml.append("</APPLET></td> ");
									*/
									//V100 <<
									if (! sqlRst.next())
									{
										loopflag = false;
									}
									else
										strWebType = sqlRst.getString("webtype");

								}
								else
								{
									sbHtml.append("\n<td></td>\n");
								}
							}
							sbHtml.append("\n</tr>");					
							
						}
						sbHtml.append("\n</table></td></tr>");
					}
				}
				else
				{
					sbHtml.append("<tr align=\"center\"><td>" +
							trs.getMessage(strLang,"nocontent1")
							+"</td></tr>");
					sbHtml.append("<tr align=\"center\"><td>" +
							trs.getMessage(strLang,"nocontent2")
							+ "</td></tr>");
				}
			}
			sbHtml.append("\n</table></body>");
			sqlRst.close();
			sqlStmt.close ();
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
		sbHtml.append("</html>");				
//////////////////////////////////////////////////////////////		
		
		out.println(sbHtml);

		out.flush();
		out.close();
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

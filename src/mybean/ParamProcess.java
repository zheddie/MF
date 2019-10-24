package mybean;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * Created on 2005-11-27
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
public class ParamProcess {
	public String getParam(HttpServletRequest req,String strParam,String strDefault){

		String strB = "";
		if( req.getParameter(strParam) == null )
		{
			Cookie[] UserCookies = req.getCookies();
			if (UserCookies != null)
			{
				if(UserCookies.length >0)
				{
					for(int i =0;i<UserCookies.length;i++)
					{
						if(UserCookies[i].getName().equalsIgnoreCase(strParam)) 
						{
							strB = UserCookies[i].getValue();
						}

					}
				}
			}
		}
		else
			strB = (String) req.getParameter(strParam) ;
		if (strB=="")
		{	
			HttpSession session = req.getSession();
			if(session.getAttribute(strParam)!=null) strB = (String)session.getAttribute(strParam);
		}
		if(strB =="") strB = strDefault;
		if ("中文".equals(strB)) 
		{
			strB = "chinese";
		}		
		return strB;
	}
}

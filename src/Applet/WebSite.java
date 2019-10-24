/*
 * Created on 2006-9-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Applet;
import java.awt.*; 
import java.applet.*; 
import java.awt.event.*; 
import java.net.*;
//import netscape.javascript.*; 
//import netscape.javascript.JSException;

public class WebSite extends Applet 
implements ActionListener, MouseListener
    { 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1053421597467674786L;

	private PopupMenu popup;
	String strURL ;
	String strDisplayid;
	String strBackColor;
	int intSizeX;
	int intSizeY;
	public void init(){
		//lb = new Label(strLbl);
		
		MenuItem mi; 
		Integer intW = new Integer(getParameter("width"));
		Integer intH = new Integer(getParameter("height"));
		intSizeX = intW.intValue();
		intSizeY = intH.intValue();
		strURL = getParameter("url");
		strDisplayid = getParameter("displayid"); 
		strBackColor = getParameter("backcolor");
		URL baseUrl = getDocumentBase();
		String strHost = baseUrl.getHost();
		int iPort = baseUrl.getPort();
		
		if (strURL.trim().substring(0,1).equals("/")){
			if (iPort != 80)
				strURL = "http://"+strHost+":"+String.valueOf(iPort)+strURL;
			else
				strURL = "http://"+strHost+strURL;
		}
		else if (strURL.trim().substring(0,2).equals("./")){
			strURL = baseUrl.toString()+strURL.trim().substring(1,strURL.trim().length());
		}		
		//add(lb); 
		popup = new PopupMenu("Prop"); 
		
		mi = new MenuItem(strDisplayid); 
		mi.addActionListener(this); 
		mi.setEnabled(false);
		popup.add(mi); 

		popup.addSeparator(); 
		mi = new MenuItem(strURL); 
		mi.addActionListener(this); 
		popup.add(mi); 
		
		addMouseListener(this); 
		add(popup); // add popup menu to applet 
		
		enableEvents(AWTEvent.MOUSE_EVENT_MASK); 
		
		setSize(intSizeX, intSizeY); 		
	}
	public void mouseClicked(MouseEvent e) { 
	} 
	
	public void mouseEntered ( MouseEvent e ) {
		Cursor cs = new Cursor(Cursor.HAND_CURSOR);
		setCursor(cs);
	} 
	
	public void mouseExited ( MouseEvent e ) { 
	} 
	
	public void mousePressed ( MouseEvent e ) 
	{ 
	int   input_modifiers = e.getModifiers ( ); 
	if ((input_modifiers & MouseEvent.BUTTON3_MASK) != 0 || (input_modifiers & MouseEvent.BUTTON2_MASK) != 0) { 
		popup.show(e.getComponent(), e.getX(), e.getY()); 
	}
	if ((input_modifiers & MouseEvent.BUTTON1_MASK) != 0)
	{
		
		//getAppletContext().showDocument(strURL,"_blank");
		try {
			
			getAppletContext().showDocument
			(new URL(strURL),"_blank");
		}
		catch (Exception ex) {}
	}
	//super.processMouseEvent(e); 
	e.consume(); 
	} 
	
	public void mouseReleased ( MouseEvent e ) { 
	} 
	
	public void actionPerformed(ActionEvent e) { 
	String command = e.getActionCommand(); 
	
	if (command.equals(strURL)) {
	//	JSObject win=JSObject.getWindow(this);
	//	win.eval("alert(\"This alert comes from Java!\")");
		try {

			getAppletContext().showDocument
			(new URL(strURL),"_blank");
		}
		catch (Exception ex) {}
	}else if (command.equals(strDisplayid)) {
	
	}
	} 
   public void paint(Graphics g){ 
    
    g.setColor(Color.decode(strBackColor));
    g.fillRect(1,1,intSizeX-2,intSizeY-2);
    g.setColor(Color.gray);
    g.drawRect(0,0,intSizeX-1,intSizeY-1);
    g.setColor(new Color(134,134,187));
    Font fOld = getFont();
    g.setFont(new Font(fOld.getFontName(),fOld.getStyle(),intSizeY-4));
    int intX = (intSizeX - strDisplayid.length() * (intSizeY-4)/2)/2;
    if (intX < 0 ) intX = 0;
    g.drawString(strDisplayid,intX,intSizeY-2);
   } 


} 

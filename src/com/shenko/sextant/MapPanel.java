package com.shenko.sextant;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class MapPanel extends JPanel {

	// Remember, game coordinates from log and F11 panel have weird signing
	// CenterViewOnCoordinates() & CoordinateToPixel() 
	// account for this, so pass them game-proper coordinates
	//
	//               ^
	//               |
	//      X+       |     X-
	//      Z-       |     Z-
	//               |
	//               |
	// <-------------+------------->
	//               |
	//               | 
	//      X+       |    X-
	//      Z+       |    Z+
	//               |
	//               \
	//

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Window OurWindow;

	private int mapX, mapY, mouseX, mouseY; //(x,y) are the map coords
	private int width = 8341, height = 6595;
	BufferedImage img;
	private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	static int startX, startY;

	private GridPoint PlayerLocation, MarkLocation, MarkCoordinates, PortLocation;

	private ImageIcon ShipIcon, PortIcon, MarkIcon;

	private boolean StartUp = true;
	private boolean Panning = false;

	private javax.swing.Timer Timer;

	private List<Port> Ports;

	public int ItemID; //itemid to show
	public String toShow="none"; //none, production, price //TODO this and renderPrices

	//private Graphics g; //this is awful practice. Set by the overloaded refresh method.

	public MapPanel(BufferedImage img, Window inWindow) {
		OurWindow = inWindow;

		mapX = 0;
		mapY = 0;
		this.img = img;
		PlayerLocation = new GridPoint();
		MarkLocation = new GridPoint();
		MarkCoordinates = new GridPoint();
		PortLocation = new GridPoint();

		ItemID=0;

		ShipIcon = new ImageIcon(getClass().getResource("/shipicon.png"));
		PortIcon = new ImageIcon(getClass().getResource("/porticon.png"));
		MarkIcon = new ImageIcon(getClass().getResource("/markicon.png"));

		Ports = new CopyOnWriteArrayList<Port>();

		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				//super.mousePressed(me);
				Panning = true;
				mouseX = me.getXOnScreen();
				mouseY = me.getYOnScreen();
				//newY = y;

				CheckForClickOnPort( new GridPoint(me.getX(), me.getY()) );
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				//super.mouseReleased(me);
				Panning = false;
				//x = newX;
				//y = newY;
				//mouseX = me.getXOnScreen();
				//mouseY = me.getYOnScreen();
			}

			@Override
			public void mouseDragged(MouseEvent me) {
				int newMouseX, newMouseY;

				newMouseX=me.getXOnScreen();
				newMouseY=me.getYOnScreen();
				//super.mouseDragged(me);
				/*if (Panning)
                {

                	newX = x + me.getXOnScreen() * -2;
                	newY = y + me.getYOnScreen() * -2;                	
                	CenterViewOnPixel( newX, newY );
                }*/
				if (Panning)
				{
					mapX=mapX+(-mouseX+newMouseX)*2;
					mapY=mapY+(-mouseY+newMouseY)*2;
					CenterViewOnPixel(mapX, mapY);
				}

				//.getViewport().setViewPosition(new Point(scrollPane.getViewport().getViewPosition().x + deltaX, scrollPane.getViewport().getViewPosition().y + deltaY));
				//mouseX=newMouseX;
				//mouseY=newMouseY;

				//  repaint();
			}           

		}; 

		addMouseListener(ma);
		addMouseMotionListener(ma);

		LoadPorts();

		// Set up and start timer to check mouseover
		ActionListener Task = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {

				//repaint();
				CheckPortsForMouseover();
			}        	
		};

		Timer = new javax.swing.Timer(20, Task);
		Timer.start();
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		//g=grphcs;
		Graphics2D g2d = (Graphics2D) grphcs;
		Component C = null;

		//super.repaint();

		//turn on some nice effects
		applyRenderHints(g2d);

		// Get location for player ship icon
		GridPoint Ship = CoordinateToPixel(PlayerLocation.X, PlayerLocation.Y);

		// Draw map image
		g2d.drawImage(img, 0, 0, null);

		// Draw ports
		RenderPorts(grphcs);

		// Draw player location icon
		ShipIcon.paintIcon(C, grphcs, Ship.X - 16, Ship.Y - 16);
		//grphcs.drawString("hello", Ship.X + 16, Ship.Y + 16);

		RenderPrices(grphcs);

		RenderMissions(grphcs);

		if(!MarkLocation.isNull && !PlayerLocation.isNull)
		{
			drawRichLine(grphcs, Ship, MarkLocation, Color.gray);
		}

		if(!MarkLocation.isNull && !PortLocation.isNull)
		{
			drawRichLine(grphcs, PortLocation, MarkLocation, Color.gray);
		}

		// Draw marked icon if available
		if (MarkLocation.X != 0 && MarkLocation.Y != 0)
		{
			MarkIcon.paintIcon(C, grphcs, MarkLocation.X - 16, MarkLocation.Y - 16);
		}

		//TODO paint port prices

	}

	public void MarkLocation(int inX, int inY)
	{
		inX=-inX;
		inY=-inY;
		GridPoint Mark = CoordinateToPixel(inX, inY);

		MarkLocation.SetLoc(Mark.X, Mark.Y);
		MarkCoordinates.SetLoc(inX, inY);
		sextant.println("Marked location X:" + MarkCoordinates.X + " Z:" + MarkCoordinates.Y);

		CenterViewOnCoordinates(inX, inY);

		repaint();

	}

	public void CheckPortsForMouseover()
	{
		Point MousePos = getMousePosition();

		if (MousePos != null)
		{
			for (Port p : Ports)
			{
				GridPoint PortLoc = CoordinateToPixel( p.x, p.y );
				//	 PortLoc.X = PortLoc.X + mapX;
				//	 PortLoc.Y = PortLoc.Y + mapY;

				if ( (MousePos.x > PortLoc.X - 8 && MousePos.x < PortLoc.X + 8) 
						&& (MousePos.y > PortLoc.Y - 8 && MousePos.y < PortLoc.Y + 8) )
				{		 
					if (OurWindow != null)
					{
						OurWindow.MouseOverPort(p, new GridPoint(MousePos.x, MousePos.y));
					}
				}
				else
				{
					if (OurWindow != null)
					{
						OurWindow.EndMouseOverPort();
					}
				}
			}
		}
	}

	public void CheckForClickOnPort(GridPoint ClickLocation)
	{
		for (Port p : Ports)
		{
			GridPoint PortLoc = CoordinateToPixel( p.x, p.y );
			//	 PortLoc.X = PortLoc.X + mapX;
			//	 PortLoc.Y = PortLoc.Y + mapY;

			if ( (ClickLocation.X > PortLoc.X - 8 && ClickLocation.X < PortLoc.X + 8) 
					&& (ClickLocation.Y > PortLoc.Y - 8 && ClickLocation.Y < PortLoc.Y + 8) )
			{		 
				if (OurWindow != null)
				{
					OurWindow.ClickedOnPort(p, new GridPoint(ClickLocation.X, ClickLocation.Y));
				}
			}
		}    	
	}

	public void CheckForClickOnMark(GridPoint ClickLocation)
	{
		if ( (ClickLocation.X > MarkLocation.X - 16 && ClickLocation.X < MarkLocation.X + 16) 
				&& (ClickLocation.Y > MarkLocation.Y - 16 && ClickLocation.Y < MarkLocation.Y + 16) )
		{		 
			MarkLocation.SetLoc(0, 0);

		}    	
	}

	public void RenderPorts(Graphics g)
	{		 
		// Iterate ports and render icon at their locations
		for (Port p : Ports)
		{
			GridPoint PortLoc = CoordinateToPixel( p.x, p.y );
			PortLoc.X = PortLoc.X - 8;
			PortLoc.Y = PortLoc.Y - 8;
			PortIcon.paintIcon(this, g, PortLoc.X, PortLoc.Y);
		}
	}

	public void RenderMissions(Graphics g)
	{
		missionList missions = sextant.missions;
		for(int i=0;i<missions.size(); i++)
		{
			//get info
			int x=missions.x.get(i);
			int y=missions.y.get(i);
			int rank=missions.rank.get(i);
			int xp=missions.xp.get(i);
			int gold=missions.gold.get(i);

			//draw shit

			String toPrint = Integer.toString(rank); 

			GridPoint point = CoordinateToPixel(-x,-y);

			outlineText(g, toPrint, point.X, point.Y, Color.red, new Font("Arial", Font.PLAIN, 32));

			GridPoint Ship = CoordinateToPixel(PlayerLocation.X, PlayerLocation.Y);


			drawRichLine(g, Ship.X, Ship.Y, point.X+6, point.Y-15, Color.red);

			//sextant.println(point.X+ " "+ point.Y+ " "+ Ship.X+ " "+ Ship.Y);

		}
	}


	public void RenderPrices(Graphics g)
	{		 
		String toPrint=null;
		// Iterate ports and render icon at their locations
		if(ItemID>0)
		{
			sextant.println("renderPrices itemid:"+ItemID);
			sextant.println("rendering item "+ItemID+ " prices. . .");

			for (Port p : Ports)
			{
				GridPoint PortLoc = CoordinateToPixel( p.x, p.y );
				if(toShow=="item")
				{
					sale mySale = new sale();
					mySale = p.getSale(ItemID);
					if(mySale!=null)
					{
						toPrint = mySale.shortString();
					}
					else
					{
						toPrint="none";
					}
					//sextant.println(p.name+ " "+ toPrint);

					outlineText(g, toPrint, PortLoc.X+32, PortLoc.Y+6, Color.blue, new Font("Arial", Font.BOLD, 12));
				}
				else if(toShow =="production")
				{
					production myProduction;
					myProduction = p.getProduction(ItemID);
					if(myProduction!=null)
					{
						toPrint = myProduction.shortString();
					}
					else
					{
						toPrint="none";
					}
					//sextant.println(p.name+ " "+ toPrint);
					sextant.println(toPrint);
					if(toPrint.substring(0,1).compareTo("-")==0)
							{
						outlineText(g, toPrint, PortLoc.X+32, PortLoc.Y+6, Color.red, new Font("Arial", Font.BOLD, 18));	
							}
					else if(toPrint=="none")
					{
						outlineText(g, toPrint, PortLoc.X+32, PortLoc.Y+6, Color.blue, new Font("Arial", Font.BOLD, 12));
					}
					else
					{
						outlineText(g, toPrint, PortLoc.X+32, PortLoc.Y+6, Color.green, new Font("Arial", Font.BOLD, 18));
					}
					
				}

				g.drawLine(PortLoc.X+32, PortLoc.Y+6, PortLoc.X, PortLoc.Y);
			}
		}
	}



	public void LoadPorts()
	{
		// Iterate through sextant.portshash and set up an arraylist
		Iterator<Integer> portIterator = sextant.portsHash.keySet().iterator();
		while(portIterator.hasNext())
		{
			Integer key = portIterator.next();
			Port Port = sextant.portsHash.get(key);
			Ports.add(Port);
		}
	}

	public TextLayout GetTextForPort(Port p, Graphics g)
	{
		// Constructs the string to be displayed when mousing over the given port
		Font font = new Font("TimesRoman", Font.PLAIN, 18);
		FontRenderContext frc = ((Graphics2D) (g)).getFontRenderContext();
		String PortString = p.name + System.getProperty("line.separator") + p.ID;

		TextLayout Layout = new TextLayout(PortString, font, frc);

		return Layout;

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public static void applyRenderHints(Graphics2D g2d) {
		g2d.setRenderingHints(textRenderHints);
		g2d.setRenderingHints(imageRenderHints);
		g2d.setRenderingHints(renderHints);
	}

	public void SetPlayerLocation(int inX, int inZ)
	{
		// Sets the player location and centers the map
		PlayerLocation.SetLoc(inX, inZ);

		if (StartUp)
		{
			StartUp = false;
			// First load of player location we center the map
			CenterViewOnCoordinates(inX, inZ);
		}
		else
		{
			// This will change later to just update boat location
			CenterViewOnCoordinates(inX, inZ);
		}
		repaint();
	}

	public void CenterViewOnPlayer()
	{
		CenterViewOnCoordinates(PlayerLocation.X, PlayerLocation.Y);
	}

	public void CenterViewOnMarker()
	{
		if (MarkCoordinates.X != 0 && MarkCoordinates.Y != 0)
		{
			CenterViewOnCoordinates(MarkCoordinates.X, MarkCoordinates.Y);
		}
	}

	public GridPoint CoordinateToPixel(int inX, int inZ)
	{
		// Convert game coordinates to pixel values for the map
		GridPoint Out = new GridPoint();

		Out.SetX( (int)((inX * -1 + 840000) * 0.0049946) );
		Out.SetY( (int)((inZ + 820000) * 0.0049962) );

		return Out;
	}

	public void CenterViewOnCoordinates(int inX, int inZ)
	{
		// Center the map image at the given game coordinates
		GridPoint Pixel = CoordinateToPixel(inX, inZ);

		mapX = Pixel.X - (int)getVisibleRect().getWidth()/2;  // + 200;
		mapY = Pixel.Y - (int)getVisibleRect().getHeight()/2;  // + 200;

		Rectangle visible = getVisibleRect();
		visible.y = mapY; 
		visible.x = mapX; 

		scrollRectToVisible(visible);
		System.out.println("setting visible(coord) to "+mapX+" "+mapY);


		//repaint();
	}

	public void CenterViewOnPixel(int inX, int inY)
	{
		// Center the map image at the given pixel coordinates
		mapX = inX * -1 + 200;
		mapY = inY * -1 + 200;

		Rectangle visible = getVisibleRect();
		//        Rectangle bounds = getBounds();
		//
		//        visible.y = (bounds.height - visible.height) / 2; 
		//        visible.x = (bounds.width - visible.width) / 2; 
		//
		visible.y = mapY; 
		visible.x = mapX; 
		if(mapX>0 && mapY>0)
		{
			scrollRectToVisible(visible); 
		}
		sextant.println("setting visible to "+mapX+" "+mapY);

		//getParent().getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum());

		//repaint();
	}

	public boolean CoordinateIsOnScreen(int inX, int inZ)
	{
		// Returns true if the given game coordinates are viewable on the map pane givem
		// image panning
		GridPoint Pixel = CoordinateToPixel(inX, inZ);

		if ( (Pixel.X > mapX - 200 && Pixel.X < mapX + 200) && (Pixel.Y > mapY - 200 && Pixel.Y < mapY + 200) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void CenterViewOnCoordinates(String sX, String sY) {
		int x=0, y=0;
		try {
			x=-Integer.parseInt(sX);
			y=-Integer.parseInt(sY);

			CenterViewOnCoordinates(x,y);
			//TODO place breadcrumb
		} catch (NumberFormatException e) {
			// nice input, asshole.
			e.printStackTrace();
		}

	}

	public void MarkLocation(String sX, String sY) { //with strings!
		int x=0, y=0;
		double dX=Double.parseDouble(sX);
		double dY=Double.parseDouble(sY);
		x=(int)dX;
		y=(int)dY;
		MarkLocation(x,y);
	}

	public void displayItem(int ItemID)
	{
		this.ItemID=ItemID;
		sextant.println("displaying item on map: "+ItemID);
		repaint();
	}

	public void outlineText(Graphics g, String text, int x, int y, Color c, Font f)
	{
		GridPoint loc = new GridPoint();
		loc.X=x;
		loc.Y=y;
		g.setFont(f);
		g.setColor(Color.white); //outline
		g.drawString(text, loc.X+1, loc.Y+1);
		g.drawString(text, loc.X+1, loc.Y-1);
		g.drawString(text, loc.X-1, loc.Y+1);
		g.drawString(text, loc.X-1, loc.Y-1);

		g.setColor(c);
		g.drawString(text, loc.X, loc.Y);

	}

	public void SetPlayerLocation(String sX, String sY) {
		int x=0, y=0;
		double dX=Double.parseDouble(sX);
		double dY=Double.parseDouble(sY);
		x=(int)dX;
		y=(int)dY;
		SetPlayerLocation(-x,-y);

	}

	public void drawRichLine(Graphics g, int x1, int y1, int x2, int y2, Color c)
	{

		g.setColor(c); //outline
		g.drawLine(x1, y1, x2, y2);
		double dist, hdg;
		String sDist, sHdg;
		dist = Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
		hdg = Math.atan2((y2-y1),(x2-x1)); //get the initial angle
		hdg = hdg*(180/Math.PI); //to degrees
		hdg=hdg-270; //fix the orientation
		if(hdg<0)
		{
			hdg=hdg+360;
		}
		if(hdg>360)
		{
			hdg=hdg-360;
		}
		sDist=String.valueOf((int)dist);
		sHdg=String.valueOf((int)hdg);
		outlineText(g, sDist+"@"+sHdg, (x1+x2)/2-30, (y1+y2)/2, c,  new Font("TimesRoman", Font.PLAIN, 14));

	}

	public void drawRichLine(Graphics g, GridPoint gp1, GridPoint gp2, Color c)
	{
		drawRichLine(g, gp1.X, gp1.Y, gp2.X, gp2.Y, c);
	}

	public void centerViewOnPort(int x, int y)
	{
		CenterViewOnCoordinates(x,y);
		PortLocation.SetLoc(x, y);
	}
}
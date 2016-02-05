package com.shenko.sextant;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private int mapX, mapY, mouseX, mouseY; //(x,y) are the map coords
    private int width = 400, height = 400;
    BufferedImage img;
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    static int startX, startY;
    
    private GridPoint PlayerLocation;
    
    private ImageIcon ShipIcon, PortIcon;
    
    private boolean StartUp = true;
    private boolean Panning = false;
    
    private javax.swing.Timer Timer;
    
    private List<Port> Ports;

    public MapPanel(BufferedImage img) {
        mapX = 0;
        mapY = 0;
        this.img = img;
        PlayerLocation = new GridPoint();

        ShipIcon = new ImageIcon(getClass().getResource("/shipicon.png"));
        PortIcon = new ImageIcon(getClass().getResource("/porticon.png"));
        
        Ports = new CopyOnWriteArrayList<Port>();
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                //super.mousePressed(me);
                Panning = true;
                mouseX = me.getXOnScreen();
                mouseY = me.getYOnScreen();
                //newY = y;
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
        });

        addMouseMotionListener(new MouseMotionAdapter() {
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
                	mapX=mapX+(mouseX-newMouseX)*2;
                	mapY=mapY+(mouseY-newMouseY)*2;
                	CenterViewOnPixel(mapX, mapY);
                }
                
                
                //mouseX=newMouseX;
                //mouseY=newMouseY;
                
                
               
              //  repaint();
            }           
            
        }); 
        
        LoadPorts();
        
        ActionListener Task = new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				CheckPortsForMouseover();
			}        	
        };
        
        Timer = new javax.swing.Timer(20, Task);
        Timer.start();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
 //       super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        Component C = null;
        
        //turn on some nice effects
        applyRenderHints(g2d);
        
        // Get location for player ship icon
        GridPoint Ship = CoordinateToPixel(PlayerLocation.X, PlayerLocation.Y);

        // Draw map image
        g2d.drawImage(img, mapX, mapY, null);
        
        // Draw ports
        RenderPorts(grphcs);
        
        // Draw player location icon
        ShipIcon.paintIcon(C, grphcs, Ship.X - 16 + mapX, Ship.Y - 16 + mapY);
        
    //    CheckPortsForMouseover();
    }
    
    public void CheckPortsForMouseover()
    {
    	Point MousePos = getMousePosition();
    	
    	if (MousePos != null)
    	{
	    	for (Port p : Ports)
	    	{
				 GridPoint PortLoc = CoordinateToPixel( p.x, p.y );
				 PortLoc.X = PortLoc.X + mapX;
				 PortLoc.Y = PortLoc.Y + mapY;
				 
				 if ( (MousePos.x > PortLoc.X - 8 && MousePos.x < PortLoc.X + 8) 
						 && (MousePos.y > PortLoc.Y - 8 && MousePos.y < PortLoc.Y) )
				 {
					 // Mouse cursor is over this port yay
					 System.out.println("MOUSE OVER " + p.name );
					 TextLayout PortText = GetTextForPort(p, this.getGraphics());
					 repaint();
					 PortText.draw((Graphics2D) this.getGraphics(), MousePos.x, MousePos.y);
				 }
	    	}
    	}
    }
    
    public void UpdatePanel()
    {
    	while (true)
    	{
    		if (getMousePosition() != null)
    		{
    			CheckPortsForMouseover();
    		}
    		
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void RenderPorts(Graphics g)
    {
		 Iterator<Integer> portIterator = sextant.portsHash.keySet().iterator();
		 while(portIterator.hasNext())
		 {
			 Integer key = portIterator.next();
			 Port Port = sextant.portsHash.get(key);
			 
			 GridPoint PortLoc = CoordinateToPixel( Port.x, Port.y );
			 PortLoc.X = PortLoc.X - 8 + mapX;
			 PortLoc.Y = PortLoc.Y - 8 + mapY;
		//	 Ports.add(Port);
			 PortIcon.paintIcon(this, g, PortLoc.X, PortLoc.Y);
		 }
    }
    
    public void LoadPorts()
    {
		 Iterator<Integer> portIterator = sextant.portsHash.keySet().iterator();
		 while(portIterator.hasNext())
		 {
			 Integer key = portIterator.next();
			 Port Port = sextant.portsHash.get(key);
			 
			 GridPoint PortLoc = CoordinateToPixel( Port.x, Port.y );
			 PortLoc.X = PortLoc.X - 8 + mapX;
			 PortLoc.Y = PortLoc.Y - 8 + mapY;
			 Ports.add(Port);
		 }
    }
    
    public TextLayout GetTextForPort(Port p, Graphics g)
    {
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
    	System.out.println("Set player loc in mappanel");
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
    	
    	mapX = Pixel.X * -1 + 200;
    	mapY = Pixel.Y * -1 + 200;
    	
    	repaint();
    }
    
    public void CenterViewOnPixel(int inX, int inY)
    {
    	// Center the map image at the given pixel coordinates
    	mapX = inX * -1 + 200;
    	mapY = inY * -1 + 200;
    	
    	repaint();
    }
    
    public boolean CoordinateIsOnScreen(int inX, int inZ)
    {
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
    


}

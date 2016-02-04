package com.shenko.sextant;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class MapPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private int x, y;
    private int width = 400, height = 400;
    BufferedImage img;
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    static int startX, startY;
    
    private GridPoint PlayerLocation;
    
    private ImageIcon ShipIcon;
    
    private boolean StartUp = true;

    public MapPanel(BufferedImage img) {
        x = 0;
        y = 0;
        this.img = img;
        PlayerLocation = new GridPoint();
        
        URL IconURL = getClass().getResource("/shipicon.png");
        ShipIcon = new ImageIcon(IconURL);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                super.mousePressed(me);
                startX = me.getX();
                startY = me.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                super.mouseDragged(me);

                if (me.getX() < startX) {//moving image to right
                    x -= 2;
                } else if (me.getX() > startX) {//moving image to left
                    x += 2;
                }

                if (me.getY() < startY) {//moving image up
                    y -= 2;
                } else if (me.getY() > startY) {//moving image to down
                    y += 2;
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        Component C = null;
        
        //turn on some nice effects
        applyRenderHints(g2d);
        
        // Get location for player ship icon
        GridPoint Ship = CoordinateToPixel(PlayerLocation.X, PlayerLocation.Y);

        // Draw map image
        g2d.drawImage(img, x, y, null);
        
        // Draw player location icon
        ShipIcon.paintIcon(C, grphcs, Ship.X - 16 + x, Ship.Y - 16 + y);
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
    	
    	x = Pixel.X * -1 + 200;
    	y = Pixel.Y * -1 + 200;
    	
    	repaint();
    }
    
    public void CenterViewOnPixel(int inX, int inY)
    {
    	// Center the map image at the given pixel coordinates
    	x = inX * -1 + 200;
    	y = inY * -1 + 200;
    	
    	repaint();
    }
    

}
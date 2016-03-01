package com.shenko.sextant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class MapTooltip extends JPanel {//  JPopupMenu{//   

	// A JPanel that encapsulates all information drawn on the map when player
	// hovers mouse over a known port
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8915473516161124745L;
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
 	
	private Port Port;
	private int H, H2, W;
	
	public int Height = 50;
	public int Width = 50;
	
	private TextLayout HeaderText, ProductionHeader, ConsumeHeader;
	private ArrayList<TextLayout> ProductionText, ConsumeText;
	private Font HeaderFont, ProductionFont;
	private FontRenderContext frc;
	
	public MapTooltip()
	{	
	//	Graphics g = this.getGraphics();
	//	frc = ((Graphics2D) g).getFontRenderContext();
		
		ProductionText = new ArrayList<TextLayout>();
		ConsumeText = new ArrayList<TextLayout>();
		
		HeaderFont = new Font("Arial", Font.PLAIN, 14);
		ProductionFont = new Font("Arial", Font.PLAIN, 7);			
	}
	
	public void SetPort(Port inPort)
	{	
		if (inPort != Port)
		{
			sextant.println("SetPort " + inPort.ID);
			Port = inPort;		
		
			ProductionText.clear();
			ConsumeText.clear();
			
	//		PopulateProductionText();
			
			setVisible(true);
			setMinimumSize(new Dimension(50, 50));
			setPreferredSize(new Dimension(100, 100));
			setMaximumSize(new Dimension(100, 100));	
			setLocation(new Point(10,10)); //let's try this out.
			repaint();
		}
	}
	
	private void PopulateProductionText()
	{
		int Production = 0;
		int Consumption = 0;
		
		for (production p : Port.productionArray)
		{
			// Iterate production entries
			// Need a function to compare ItemID to human readable names
			if (p.quantity > 0)
			{
				ProductionText.add( new TextLayout(p.ItemID + " x" + p.quantity / 24, ProductionFont, frc) );
				Production++;
			}	
			else
			{
				ConsumeText.add( new TextLayout(p.ItemID + " x" + p.quantity / 24, ProductionFont, frc) );
				Consumption++;
			}
		}		
		
		// Now go through the array and find the widest line
		// Use this to calculate width of the entire tooltip
		int i = 0;
		int LineWidth = 0;
		int WTest = 0;
		for (TextLayout t : ProductionText)
		{
			int ThisLine = (int) (ProductionText.get(i).getBounds().getWidth() + ConsumeText.get(i).getBounds().getWidth() + H*2);
			int LineW = (int) ProductionText.get(i).getBounds().getWidth();
			
			if (ThisLine > LineWidth)
			{
				LineWidth = ThisLine;
			}
			
			if (LineW > WTest)
			{
				WTest = LineW;
			}
		}
		
		H2 = (int) ProductionText.get(0).getBounds().getHeight();
		
		if (ProductionText.size() > ConsumeText.size())
		{
			Height = (H * 2) + (H2 * ProductionText.size());
		}
		else
		{
			Height = (H * 2) + (H2 * ConsumeText.size());
		}
		
        ProductionHeader = new TextLayout("Produces per Day", ProductionFont, frc);
        ConsumeHeader = new TextLayout("Consumes per Day", ProductionFont, frc);
		
		Width = LineWidth;
		W = WTest;
	}

    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	Graphics2D g2d = (Graphics2D) g;
        applyRenderHints(g2d);
        
        sextant.println("Painting tooltip");
        if (frc == null)
        {
        	// Fuck this stupid font render context
        	sextant.println("Generating FRC");
        	frc = g2d.getFontRenderContext();
        }
        
        if (ProductionText == null || ProductionText.isEmpty())
        {
        	sextant.println("Populating production data");
        	PopulateProductionText();
        } 
        
		HeaderText = new TextLayout(Port.name, HeaderFont, frc);
		H = (int) HeaderText.getBounds().getHeight();
		
        // Draw background rectangle
        g.setColor(new Color(1f, 1f, 1f, 1f));
        g.fillRoundRect(0, 0, Width, Height, 8, 8);
        
        g.setColor(new Color(0f, 0f, 0f, 1f));
        
        // Draw port name
        HeaderText.draw(g2d, H/2, H/2);
        
        // Draw production text   
        ProductionHeader.draw(g2d, H/2, H*2);
        ConsumeHeader.draw(g2d, W, H*2);
        
        int DrawY = H*2 + H2;
        int i=0;
        for (TextLayout p : ProductionText)
        {
        	p.draw(g2d, H/2, DrawY * i);
        	i++;
        }
        
        DrawY = H*2;
        i=0; 
        for (TextLayout c : ConsumeText)
        {
        	c.draw(g2d, W, DrawY * i);
        	i++;
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
    	if (Width == 0 || Height == 0)
    	{
    		Width = 50;
    		Height = 50;
    	}
        return new Dimension(Width, Height);
    }


    public static void applyRenderHints(Graphics2D g2d) {
        g2d.setRenderingHints(textRenderHints);
        g2d.setRenderingHints(imageRenderHints);
        g2d.setRenderingHints(renderHints);
    }
}

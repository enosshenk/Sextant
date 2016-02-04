package com.shenko.sextant;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

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

    public MapPanel(BufferedImage img) {
        x = -2000;
        y = -3000;
        this.img = img;

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

        //turn on some nice effects
        applyRenderHints(g2d);

        g2d.drawImage(img, x, y, null);
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
}

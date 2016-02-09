package com.shenko.sextant;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JToolBar;
import javax.swing.JTree;

import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

import java.awt.Insets;

import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Button;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Window extends JFrame {

	private JPanel contentPane;
	private MapPanel MapPanel;
	private MapPanel MapPanel_1;
	
	private MapTooltip MapTooltip;
	
	//this is where everything that needs to get updated goes.
	public JLabel lblPlayerName;
	public JLabel lblFileStatus;
	public JTextField lblX;
	public JTextField lblY;
	public JButton goXY;
	/**
	 * Create the frame.
	 */
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sextant");
		setSize(800,600);
		setResizable(false);
//		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// System dropdown menu
		JMenu mnNewMenu = new JMenu("System");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmLoadLog = new JMenuItem("Update Log File");
		mntmLoadLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sextant.reloadLog();
			}
		});
		mnNewMenu.add(mntmLoadLog);
		
		JMenuItem mntnSyncDB = new JMenuItem("Update Tree");
		mntnSyncDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//reload tree
			}
		});
		mnNewMenu.add(mntnSyncDB);
		
		JMenuItem mntnCherno = new JMenuItem("Cherno Special");
		mntnCherno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sextant.reloadLog(1); //reload with cherno's fucking SSD directory
			}
		});
		mnNewMenu.add(mntnCherno);
		
		JMenuItem mntnCherno2 = new JMenuItem("MoreAlive Special");
		mntnCherno2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sextant.reloadLog(2); //reload with cherno's (morealivethandead) fucking SSD directory
			}
		});
		mnNewMenu.add(mntnCherno2);
		
		JMenu mnAdmin = new JMenu("Admin");
		mnNewMenu.add(mnAdmin);
		
		JMenuItem mntmFilldb = new JMenuItem("FillDB");
		mntmFilldb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sextant.mySql.fillDB();
			}
		});
		mnAdmin.add(mntmFilldb);
		
		// Map menu dropdown stuff
		JMenu MapMenu = new JMenu("Map");
		menuBar.add(MapMenu);
		
		JMenuItem MapMenuCenterOnPlayer = new JMenuItem("Center on Me");
		MapMenuCenterOnPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MapPanel_1.CenterViewOnPlayer();
			}
		});
		MapMenu.add(MapMenuCenterOnPlayer);		
		
		JMenuItem MapMenuCenterOnMarker = new JMenuItem("Center on My Marker");
		MapMenuCenterOnMarker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MapPanel_1.CenterViewOnMarker();
			}
		});
		MapMenu.add(MapMenuCenterOnMarker);		
		
		JLabel lblNewLabel = new JLabel("       ");
		menuBar.add(lblNewLabel);
		
		
		lblPlayerName = new JLabel("PlayerName");
		menuBar.add(lblPlayerName);
		
		JLabel lblNewLabel_1 = new JLabel("        ");
		menuBar.add(lblNewLabel_1);
		
		lblFileStatus = new JLabel("Log File");
		lblFileStatus.setToolTipText("red: old file\ngreen: EOF, waiting\nyellow: parsing new");
		lblFileStatus.setForeground(new Color(0, 125, 250));
		lblFileStatus.setHorizontalAlignment(SwingConstants.TRAILING);
		menuBar.add(lblFileStatus);
		
		JLabel label = new JLabel("       ");
		menuBar.add(label);
		
		
		lblX = new JTextField("X");
		menuBar.add(lblX);
		
		JLabel label_2 = new JLabel("       ");
		menuBar.add(label_2);
		
		lblY = new JTextField("Z");
		menuBar.add(lblY);
		
		goXY = new JButton("Go to X,Z");
        menuBar.add(goXY);
        goXY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//	MapPanel_1.CenterViewOnCoordinates(lblX.getText(), lblY.getText());
				MapPanel_1.MarkLocation( Integer.parseInt(lblX.getText()), Integer.parseInt(lblY.getText()) );
				};				
			});
		
		// Set sections up
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(200, Short.MAX_VALUE));
		tabbedPane.setPreferredSize(new Dimension(250, Short.MAX_VALUE));
		tabbedPane.setMaximumSize(new Dimension(250, Short.MAX_VALUE));
		contentPane.add(tabbedPane);
		
		itemTree tree = new itemTree("item");
		JScrollPane treeScrollPane = new JScrollPane(tree);
		tabbedPane.addTab("Items by Location", null, treeScrollPane, null);
		
		JTextArea textArea = new JTextArea();
		tabbedPane.addTab("New tab", null, textArea, null);
		
		// Map panel
		MapPanel = null;
		BufferedImage Image = null;
		// Load the image
		try {
			Image = ImageIO.read( getClass().getResourceAsStream("/map.jpg") );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Image doesn't exist fucker");
			e1.printStackTrace();
		}
		MapPanel_1 = new MapPanel( Image, this );
		MapPanel_1.setBackground(new Color(0, 255, 255));
		MapPanel_1.setMinimumSize(new Dimension(400, 400));
		MapPanel_1.setPreferredSize(new Dimension(400, 400));
		MapPanel_1.setMaximumSize(new Dimension(500, 500));	
		//MapPanel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane v= new JScrollPane(MapPanel_1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
         add(v);
         v.getVerticalScrollBar().setUnitIncrement(20);

         v.getHorizontalScrollBar().setUnitIncrement(20); //doesn't work.

		//contentPane.add(MapPanel_1);
		
		MapTooltip = new MapTooltip();
		MapTooltip.setMinimumSize(new Dimension(50, 50));
		MapTooltip.setPreferredSize(new Dimension(100, 100));
		MapTooltip.setMaximumSize(new Dimension(100, 100));	
		MapTooltip.setBounds(0, 0, 50, 50);
		
		contentPane.add(MapTooltip);
		MapTooltip.setVisible(false);
		
		sextant.GUILoaded=true;
		System.out.println("Gui Loaded: window");
		
	}
	
	public void ClickedOnPort(Port inPort, GridPoint ClickLocation)
	{
		// Called from MapPanel when user clicked on a known port on the map
		
		System.out.println("User clicked on " + inPort.name);
	}

	public void MouseOverPort(Port inPort, GridPoint inPoint)
	{
		// Called when a port is moused over in MapPanel
		
		System.out.println("Mouse over " + inPort.name);
		MapTooltip.setVisible(true);
		MapTooltip.setMinimumSize(new Dimension(50, 50));
		MapTooltip.setPreferredSize(new Dimension(100, 100));
		MapTooltip.setMaximumSize(new Dimension(100, 100));	
		MapTooltip.setBounds(0, 0, 50, 50);
		
		MapTooltip.setLocation(inPoint.X, inPoint.Y);
		MapTooltip.SetPort(inPort);		
		MapTooltip.setSize(MapTooltip.getPreferredSize());
	}
	
	public void EndMouseOverPort()
	{
		// Called when a user stops mousing over a port
		
		MapTooltip.setVisible(false);
	}
	
	public void setLogStatus (int num)
	{
		//System.out.println("setting log status "+num);
		if(num==0)
		{
			lblFileStatus.setForeground(Color.red);
		}
		else if(num==1)
		{
			lblFileStatus.setForeground(Color.yellow);
		}
		else if (num==2)
		{
			lblFileStatus.setForeground(Color.green);
		}		
		else if (num==-1)
		{
			lblFileStatus.setForeground(Color.orange);
		}
		else
		{
			System.out.println("Someone's trying to pass "+num+" to SetLogStatus, and 0, 1, or 2 is expected.");
		}
		sextant.logStatus=num;
		
		//Window.
		
	}
	
	public void setLoc(int x, int z)
	{
		lblX.setText(Integer.toString(x * -1));
		lblY.setText(Integer.toString(z * -1));
		
		MapPanel_1.SetPlayerLocation(x * -1, z * -1);
		
		System.out.println("Setting player loc in Window");
	}

}

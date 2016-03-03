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
import javax.swing.text.DefaultCaret;

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
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Button;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;

public class Window extends JFrame {

	private JPanel contentPane;
	//private MapPanel MapPanel;
	private MapPanel MapPanel_1;
	
	private itemTree tree;
	public itemTree production;
	public itemTree warehouse;

	private MapTooltip MapTooltip;

	public JTextArea textArea;
	public JTextArea shotText;

	//this is where everything that needs to get updated goes.
	public JLabel lblPlayerName;
	public JLabel lblFileStatus;
	public JTextField lblX;
	public JTextField lblY;
	public JButton goXY;
	public JButton goXYShip;
	/**
	 * Create the frame.
	 */
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Sextant");
		setSize(1600,900);
		//setSize(1440,900);
		//setSize(800,600);
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



		JMenu mnAdmin = new JMenu("Admin");
		mnNewMenu.add(mnAdmin);

		JMenuItem mntmFilldb = new JMenuItem("FillDB");
		mntmFilldb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sextant.mySql.fillDB();
			}
		});
		mnAdmin.add(mntmFilldb);


		JMenuItem mntmVersion = new JMenuItem(sextant.version);
		mntmFilldb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//sextant.mySql.version();
			}
		});
		mnAdmin.add(mntmVersion);
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

		lblX.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						lblX.selectAll(); //select all text when the box gains focus
					}
				});
			}
		});

		lblY.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						lblY.selectAll();
					}
				});
			}
		});

		Action action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MapPanel_1.SetPlayerLocation(lblX.getText(),lblY.getText() );
			}
		};

		lblX.addActionListener(action);
		lblY.addActionListener(action);



		goXY = new JButton("Set Marker");
		menuBar.add(goXY);
		goXY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//	MapPanel_1.CenterViewOnCoordinates(lblX.getText(), lblY.getText());
				MapPanel_1.MarkLocation( lblX.getText(),lblY.getText() );
			};				
		});

		goXYShip = new JButton("Set Ship");
		menuBar.add(goXYShip);
		goXYShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//	MapPanel_1.CenterViewOnCoordinates(lblX.getText(), lblY.getText());
				MapPanel_1.SetPlayerLocation(lblX.getText(),lblY.getText() );
			};				
		});



		// Set sections up
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setMinimumSize(new Dimension(375, Short.MAX_VALUE));
		tabbedPane.setPreferredSize(new Dimension(375, Short.MAX_VALUE));
		tabbedPane.setMaximumSize(new Dimension(375, Short.MAX_VALUE));
		contentPane.add(tabbedPane);

		tree = new itemTree("item");
		JScrollPane treeScrollPane = new JScrollPane(tree);
		tabbedPane.addTab("Items", null, treeScrollPane, null);
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				processTreeClick(me, "item");
			}
		});

		warehouse = new itemTree("warehouse");
		JScrollPane warehouseScrollPane = new JScrollPane(warehouse);
		tabbedPane.addTab("Warehouse", null, warehouseScrollPane, null);

		production = new itemTree("production");
		JScrollPane productionScrollPane = new JScrollPane(production);
		tabbedPane.addTab("production", null, productionScrollPane, null);
		production.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				processTreeClick(me, "production");
			}
		});

		textArea = new JTextArea();
		JScrollPane textAreaScrollPane = new JScrollPane(textArea); //I think JTextArea handles scrolling internally
		tabbedPane.addTab("debug info", null, textAreaScrollPane, null);
		//tabbedPane.addTab("debug info", null, textArea, null);
		// Map panel
		//MapPanel = null;
		BufferedImage Image = null;
		// Load the image
		try {
			Image = ImageIO.read( getClass().getResourceAsStream("/map.jpg") );
		} catch (IOException e1) {
			// 
			sextant.println("Image doesn't exist fucker");
			e1.printStackTrace();
		}
		MapPanel_1 = new MapPanel( Image, this );
		MapPanel_1.setBackground(new Color(0, 255, 255));
		MapPanel_1.setMinimumSize(new Dimension(400, 400));
		MapPanel_1.setPreferredSize(new Dimension(400, 400));
		MapPanel_1.setMaximumSize(new Dimension(500, 500));	

		JTabbedPane mapTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mapTabbedPane.setMinimumSize(new Dimension(this.getWidth()-388, Short.MAX_VALUE));
		mapTabbedPane.setPreferredSize(new Dimension(this.getWidth()-388, Short.MAX_VALUE));
		mapTabbedPane.setMaximumSize(new Dimension(this.getWidth()-388, Short.MAX_VALUE));
		add(mapTabbedPane);
		//MapPanel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane mapPane= new JScrollPane(MapPanel_1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		mapTabbedPane.addTab("Map", null, mapPane, null);
		mapPane.getVerticalScrollBar().setUnitIncrement(20);
		mapPane.getHorizontalScrollBar().setUnitIncrement(20); //doesn't work.


		shotText = new JTextArea();
		JScrollPane shotScrollPane = new JScrollPane(shotText); //I think JTextArea handles scrolling internally
		mapTabbedPane.addTab("Shot Logger", null, shotScrollPane, null);
		DefaultCaret caret = (DefaultCaret)shotText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		//contentPane.add(MapPanel_1);

		MapTooltip = new MapTooltip();
		MapTooltip.setMinimumSize(new Dimension(50, 50));
		MapTooltip.setPreferredSize(new Dimension(100, 100));
		MapTooltip.setMaximumSize(new Dimension(100, 100));	
		MapTooltip.setBounds(0, 0, 50, 50);

		mapPane.add(MapTooltip);
		MapTooltip.setVisible(false);

		sextant.GUILoaded=true;
		sextant.println("Gui Loaded: window");

	}

	void processTreeClick(MouseEvent me, String type) {
		TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
		DefaultMutableTreeNode node;
		
		MapPanel_1.toShow=type;
		
		if (tp != null)
		{
			node = (DefaultMutableTreeNode) tp.getLastPathComponent();
			if(node.getParent()==tree.root)
			{
				//it's an item
				int ItemID=sextant.items.find((String)node.getUserObject()); 
				println("item? "+ItemID + node.getUserObject().toString());

				MapPanel_1.displayItem(ItemID); 
				//TODO MapPanel. draw item prices
			}else{//it's a sale
				sale thisSale = (sale) node.getUserObject();
				MapPanel_1.centerViewOnPort(thisSale.port.x, thisSale.port.y);
			}
		}	
		else
			println("Tree clicked, but treePath null");
	}


	public void ClickedOnPort(Port inPort, GridPoint ClickLocation)
	{
		// Called from MapPanel when user clicked on a known port on the map

		sextant.println("User clicked on " + inPort.name);
		MapPanel_1.MarkLocation(-inPort.x, -inPort.y);
	}

	public void MouseOverPort(Port inPort, GridPoint inPoint)
	{
		// Called when a port is moused over in MapPanel

		/*sextant.println("Mouse over " + inPort.name);
		MapTooltip popup = new MapTooltip();
		popup.setVisible(false);
		popup.setMinimumSize(new Dimension(50, 50));
		popup.setPreferredSize(new Dimension(100, 100));
		popup.setMaximumSize(new Dimension(100, 100));	
		popup.setBounds(0, 0, 50, 50);

		popup.setLocation(inPoint.X, inPoint.Y);
		popup.SetPort(inPort);		
		popup.setSize(MapTooltip.getPreferredSize());
		popup.setVisible(true);
		//MapPanel_1.add(popup);
*/		
	}

	public void EndMouseOverPort()
	{
		// Called when a user stops mousing over a port

		//MapTooltip.setVisible(false);
	}

	public void setLogStatus (int num)
	{
		//sextant.println("setting log status "+num);
		//0 is reading initially loaded file. at 1, we've hit EOF. at 2, we've read new info, confirming that we have a live file.
		if(num==0)
		{
			lblFileStatus.setForeground(Color.red);
			lblFileStatus.setText("Log: no file");
		}
		else if(num==1)
		{
			lblFileStatus.setForeground(Color.yellow);

			lblFileStatus.setText("Log: reading");
		}
		else if (num==2)
		{
			lblFileStatus.setForeground(Color.green);
			lblFileStatus.setText("Log: live file");
		}		
		else if (num==-1)
		{
			lblFileStatus.setForeground(Color.orange);
			lblFileStatus.setText("Log: reloading");			
		}
		else
		{
			sextant.println("Someone's trying to pass "+num+" to SetLogStatus, and 0, 1, or 2 is expected.");
		}
		sextant.logStatus=num;

		//Window.

	}

	public void setLoc(int x, int z)
	{
		lblX.setText(Integer.toString(x));
		lblY.setText(Integer.toString(z));

		MapPanel_1.SetPlayerLocation(x * -1, z * -1);

		sextant.println("Setting player loc in Window");
	}

	public void println(Object toPrint)
	{
		//textArea.append(toPrint+"\n"); //or insert
		System.out.println(toPrint);
	}

}

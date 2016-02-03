package com.shenko.sextant;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JToolBar;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.Insets;
import javax.swing.JTree;
import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
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

public class Window extends JFrame {

	private JPanel contentPane;
	
	//this is where everything that needs to get updated goes.
	public JLabel lblPlayerName;
	public JLabel lblFileStatus;
	public JLabel lblX;
	public JLabel lblY;
	/**
	 * Create the frame.
	 */
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("New menu");
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
		
		lblX = new JLabel("X");
		menuBar.add(lblX);
		
		JLabel label_2 = new JLabel("       ");
		menuBar.add(label_2);
		
		lblY = new JLabel("y");
		menuBar.add(lblY);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		
		JTree tree = new JTree();
		tabbedPane.addTab("Items by Location", null, tree, null);
		
		JTextArea textArea = new JTextArea();
		tabbedPane.addTab("New tab", null, textArea, null);
		
		
		sextant.GUILoaded=true;
		System.out.println("Gui Loaded: window");
		
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
	
public void setLoc(int x, int y)
{
	lblX.setText(Integer.toString(x));
	lblY.setText(Integer.toString(y));
	
}

}
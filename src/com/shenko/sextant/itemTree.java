package com.shenko.sextant;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public class itemTree extends JTree{
	
	private DefaultTreeModel model = (DefaultTreeModel) getModel();
	DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
	
	

	//private static final long serialVersionUID = 1L;
	
	public itemTree(String type) //"item" or "port"
	{
		sextant.println("Tree building started.");
		setRootVisible(false);
		root.removeAllChildren();
		for(int i=0;i<sextant.items.count();i++)
		{
			//sextant.println(i);
			model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(i))), root, model.getChildCount(root));
			//sextant.println(sextant.items.get(sextant.items.index(i)));
		}
		
		fillTree(type);
		
		
		
		/*
		
		setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("JTree") {
					{
						DefaultMutableTreeNode node_1;
						node_1 = new DefaultMutableTreeNode("default");
							node_1.add(new DefaultMutableTreeNode("bullshit"));
							node_1.add(new DefaultMutableTreeNode("violet"));
							node_1.add(new DefaultMutableTreeNode("red"));
							node_1.add(new DefaultMutableTreeNode("yellow"));
						add(node_1);
						node_1 = new DefaultMutableTreeNode("sports");
							node_1.add(new DefaultMutableTreeNode("basketball"));
							node_1.add(new DefaultMutableTreeNode("soccer"));
							node_1.add(new DefaultMutableTreeNode("football"));
							node_1.add(new DefaultMutableTreeNode("hockey"));
						add(node_1);
						node_1 = new DefaultMutableTreeNode("food");
							node_1.add(new DefaultMutableTreeNode("hot dogs"));
							node_1.add(new DefaultMutableTreeNode("pizza"));
							node_1.add(new DefaultMutableTreeNode("ravioli"));
							node_1.add(new DefaultMutableTreeNode("bananas"));
						add(node_1);
					}
				}
			));
			
			*/
		
		this.setVisible(true);
		sextant.println("Tree building finished.");
	}

	
	public void fillTree(String type) //"item" or "port"
	{
		
		if(type=="item")
		{
			Iterator<Integer> portIterator = sextant.portsHash.keySet().iterator();
			while(portIterator.hasNext()) //this goes through all the ports
			{
				Integer key = portIterator.next();
				Port myPort = sextant.portsHash.get(key);
				sextant.println(myPort+ " has "+myPort.salesArray.size()+" sales.");
				for(int i=0;i<root.getChildCount();i++) //this one goes through tree nodes
				{
					DefaultMutableTreeNode tempNode =(DefaultMutableTreeNode) root.getChildAt(i);
					String itemName=tempNode.toString();					
					int nodeItemID=sextant.items.find(itemName); //numerical ItemID corresponding to the node name, which is an item name from the itemtable.
					//sextant.println(nodeItemID+" "+itemName +"i");
					for(int j=0;j<myPort.salesArray.size();j++) //this goes through sales
					{
						//sextant.println(myPort.salesArray.get(j));
						//sextant.println(myPort.salesArray.get(j).ItemID+" "+nodeItemID+" itemid");
						if(myPort.salesArray.get(j).ItemID==nodeItemID)
						{
							String nodeText;
							sale mySale=myPort.salesArray.get(j);
							//sextant.println("Inserting "+itemName+" "+myPort+" "+mySale);
							nodeText = mySale.quantity+" b"+ mySale.buyPrice+" s"+mySale.sellPrice+" "+myPort.name+ " "+mySale.modified;
							DefaultMutableTreeNode toInsert = new DefaultMutableTreeNode(nodeText);
							tempNode.add(toInsert);
						}
					}
				}
				
			}

		}
		model.reload(root);
	}
	/*private TreePath find(DefaultMutableTreeNode root, String s) {
	    @SuppressWarnings("unchecked")
	    Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = e.nextElement();
	        if (node.toString().equalsIgnoreCase(s)) {
	            return new TreePath(node.getPath());
	        }
	    }
	    return null;
	}*/

}

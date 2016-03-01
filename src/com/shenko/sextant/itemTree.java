package com.shenko.sextant;

import java.util.ArrayList;
import java.util.Enumeration;
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

	@SuppressWarnings("unused") //it's smart to think that tempChild==null would exclude code.
	public itemTree(String type) //"item" or "port" or "warehouse" or "production"
	{
		sextant.println(type+" tree building started.");
		setRootVisible(false);
		root.removeAllChildren();

		//insert first item
		if(type=="item")
		{
			if(sextant.items.count()>0)
			{
				model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(0))), root, 0);
			}
			for(int i=0;i<sextant.items.count();i++)
			{
				//sextant.println(i);
				int nodeCount=0;
				DefaultMutableTreeNode tempChild=(DefaultMutableTreeNode) root.getFirstChild(); 
				for(tempChild=(DefaultMutableTreeNode) root.getFirstChild(); tempChild==null || 
						sextant.items.get(sextant.items.index(i)).compareTo(tempChild.toString())>0;tempChild=(DefaultMutableTreeNode) root.getChildAfter(tempChild))
				{
					//loop until either we find a node where the text is greater than it, or we reach the end
				}
				if(tempChild == null)
				{
					//then the item to insert is greater than everything, put it at the end
					model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(i))), root, model.getChildCount(root));

				} else {
					//tempChild is the first child greater than the string, so
					model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(i))), root, model.getIndexOfChild(root, tempChild));

				}
			}
		}else if(type=="port"){

		}else if(type=="warehouse"){
		}else if(type=="production"){
			if(sextant.items.count()>0)
			{
				model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(0))), root, 0);
			}
			for(int i=0;i<sextant.items.count();i++)
			{
				//sextant.println(i);
				int nodeCount=0;
				DefaultMutableTreeNode tempChild=(DefaultMutableTreeNode) root.getFirstChild(); 
				for(tempChild=(DefaultMutableTreeNode) root.getFirstChild(); tempChild==null || 
						sextant.items.get(sextant.items.index(i)).compareTo(tempChild.toString())>0;tempChild=(DefaultMutableTreeNode) root.getChildAfter(tempChild))
				{
					//loop until either we find a node where the text is greater than it, or we reach the end
				}
				if(tempChild == null)
				{
					//then the item to insert is greater than everything, put it at the end
					model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(i))), root, model.getChildCount(root));

				} else {
					//tempChild is the first child greater than the string, so
					model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(sextant.items.index(i))), root, model.getIndexOfChild(root, tempChild));

				}
			}
			
			

		}






		fillTree(type);


		this.setVisible(true);
		sextant.println("Tree building finished.");
	}


	public void fillTree(String type) //"item" or "port" or "production"
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
							//nodeText = mySale.quantity+" b"+ mySale.buyPrice+" s"+mySale.sellPrice+" "+myPort.name+ " "+mySale.modified;
							DefaultMutableTreeNode toInsert = new DefaultMutableTreeNode(mySale);
							tempNode.add(toInsert);
						}
					}
				}

			}

		}
		if(type=="production")
		{
			Iterator<Integer> portIterator = sextant.portsHash.keySet().iterator();
			while(portIterator.hasNext()) //this goes through all the ports
			{
				Integer key = portIterator.next();
				Port myPort = sextant.portsHash.get(key);
				sextant.println(myPort+ " has "+myPort.productionArray.size()+" sales.");
				for(int i=0;i<root.getChildCount();i++) //this one goes through tree nodes
				{
					DefaultMutableTreeNode tempNode =(DefaultMutableTreeNode) root.getChildAt(i);
					String itemName=tempNode.toString();					
					int nodeItemID=sextant.items.find(itemName); //numerical ItemID corresponding to the node name, which is an item name from the itemtable.
					for(int j=0;j<myPort.productionArray.size();j++) //this goes through productions
					{
							if(myPort.productionArray.get(j).ItemID==nodeItemID)
						{
							String nodeText;
							production myProduction=myPort.productionArray.get(j);
							//nodeText = myProduction.quantity+" "+myPort.name+ " "+myProduction.modified;
							DefaultMutableTreeNode toInsert = new DefaultMutableTreeNode(myProduction);
							tempNode.add(toInsert);
						}
					}
				}

			}

		}

		model.reload(root);
	}

	public void insertItems(Port port, int ItemID, int quantity) //insert warehouse items
	{
		int nodeCount=0;

		if(sextant.items.get(ItemID)!=null){

			DefaultMutableTreeNode subroot=null;
			Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
			while (e.hasMoreElements()) {
				DefaultMutableTreeNode node = e.nextElement();
				if (node.getUserObject()==port) {
					subroot=node;
				}
			}

			if(subroot==null)
			{
				subroot = new DefaultMutableTreeNode(port.name);
				subroot.setUserObject(port);
				root.add(subroot);
				sextant.println("inserting node?");
			}

			model.insertNodeInto(new DefaultMutableTreeNode(sextant.items.get(ItemID)+": "+quantity), subroot, 0); //TODO i was around here when the internet went out.
			model.reload(root);
		}
		sortTree();
	}

	
	public void clearPort(Port port) //TODO when to run this?
	{
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if (node.getUserObject()==port) {
				node.removeAllChildren();
			}
		}

	}

	public void sortTree() {
		model.reload(sort(root));
	}

	
    public static DefaultMutableTreeNode sort(DefaultMutableTreeNode root) {
        for (int i = 0; i < root.getChildCount(); i++) {
             DefaultMutableTreeNode node = (DefaultMutableTreeNode) root
                       .getChildAt(i);
             String nt = node.getUserObject().toString();
             for (int j=0; j<i; j++) {
                  DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) root
                  .getChildAt(j);
                  String np = prevNode.getUserObject().toString();
                  if (nt.compareToIgnoreCase(np)<0) {
                       root.insert(node, j);
                       root.insert(prevNode, i);
                  }
             }
             if (node.getChildCount() > 0) {
                  node = sort(node);
             }
        }
        return root;
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

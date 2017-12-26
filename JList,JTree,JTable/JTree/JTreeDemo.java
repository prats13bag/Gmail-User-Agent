/**
 * @(#)JTreeDemo.java
 *
 *
 * @author
 * @version 1.00 2008/6/12
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.net.URL;

public class JTreeDemo extends JPanel implements TreeSelectionListener {

	private JTree tree;

    /**
     * Creates a new instance of <code>JTreeDemo</code>.
     */
    public JTreeDemo() {

    	super(new BorderLayout());
    			DefaultMutableTreeNode box = new DefaultMutableTreeNode("Box");

    			DefaultMutableTreeNode inbox = new DefaultMutableTreeNode("Inbox");
    			DefaultMutableTreeNode outbox = new DefaultMutableTreeNode("outbox");
    			DefaultMutableTreeNode draft = new DefaultMutableTreeNode("Draft");
    			DefaultMutableTreeNode deleted = new DefaultMutableTreeNode("Deleted");

    			box.add(inbox);
    			box.add(outbox);
				box.add(draft);
				box.add(deleted);




    	tree = new JTree(box);
    	tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	tree.addTreeSelectionListener(this);

    	DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
    	render.setLeafIcon(createImageIcon("images/leafIcon.GIF"));
    	render.setClosedIcon(createImageIcon("images/closedIcon.GIF"));
    	render.setOpenIcon(createImageIcon("images/openIcon.GIF"));
    	tree.setCellRenderer(render);

    	JScrollPane treeView = new JScrollPane(tree);
    	treeView.setPreferredSize(new Dimension(180, 140));
    	add(treeView);
    }

    public void valueChanged(TreeSelectionEvent e) {

    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    	System.out.println(node.getUserObject());
    }

    private ImageIcon createImageIcon(String path) {

    	URL imageURL = this.getClass().getResource(path);
    	if(imageURL != null)
    		return new ImageIcon(imageURL);
    	else
    		return null;
    }

   private static void createAndShowGUI()
   	{

    	JFrame f = new JFrame("JTree Demo");
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	f.add(new JTreeDemo());
    	f.pack();
    	f.setLocation(250, 250);
    	f.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    	 {
        // TODO code application logic here

        SwingUtilities.invokeLater(new Runnable() {

        	public void run() {

        		createAndShowGUI();

        	}
        });
    }
}

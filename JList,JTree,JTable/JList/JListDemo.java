/**
 * @(#)JListDemo.java
 *
 *
 * @author 
 * @version 1.00 2008/6/11
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class JListDemo implements ActionListener, ListSelectionListener {
	
	JList list;
	DefaultListModel listModel;        
        
    /**
     * Creates a new instance of <code>JListDemo</code>.
     */
    public JListDemo() {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(100, 150);
        listModel = new DefaultListModel(); 
        listModel.addElement("one");
        listModel.addElement("two");
        listModel.addElement("three");
        listModel.addElement("four");
        list = new JList(listModel);
        listModel.addElement("five");
        listModel.addElement("six");
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.addMouseListener(new MouseAdapter() {
        	
        	public void mouseClicked(MouseEvent e) {
        		
        		if(e.getClickCount() == 2) {
        			
        			System.out.println("Selected index " + list.getSelectedIndex());
        			System.out.println("Selected value " + list.getSelectedValue());
        		}
        	}
        });
        
        JButton btn = new JButton("Remove");
        btn.addActionListener(this);
        
        f.add(list);
        f.add(btn, "South");
        f.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new JListDemo();
    }
    
    public void actionPerformed(ActionEvent e) {
    	
    	listModel.remove(list.getSelectedIndex());
    	//listModel.removeAllElements();
    }
    
    public void valueChanged(ListSelectionEvent e) {

    	if(e.getValueIsAdjusting() == false) {
    		
    		System.out.println("hi");
    	}
    }
}

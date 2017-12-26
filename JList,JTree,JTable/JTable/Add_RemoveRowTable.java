import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Add_RemoveRowTable implements ListSelectionListener
{
	DefaultTableModel model;
	JTable table;

	/* To make the table cell non editable we create our own class,
	   by default cells of the table are editable */
	class MyDefaultTableModel extends DefaultTableModel {

	    MyDefaultTableModel(Object[][] data, Object[] columnNames) {

	    	super(data, columnNames);
	    }

	    public boolean isCellEditable(int row, int column) {

	        return false;
	    }
	}

	Add_RemoveRowTable()
	{
		JFrame frame = new JFrame("Sorting JTable");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Object rows[][] = {
							{"AMZN", "Amazon", 41.28},
							{"EBAY", "eBay", 41.57},
							{"GOOG", "Google", 388.33},
							{"MSFT", "Microsoft", 26.56},
							{"NOK", "Nokia Corp", 17.13},
							{"ORCL", "Oracle Corp.", 12.52},
							{"SUNW", "Sun Microsystems", 3.86},
							{"TWX",  "Time Warner", 17.66},
							{"VOD",  "Vodafone Group", 26.02},
							{"YHOO", "Yahoo!", 37.69}

						  };

		String columns[] = {"Symbol", "Name", "Price"};
		// after creating model, later on if we need to change the model data
		// we can call setDataVector() on model object
		model = new MyDefaultTableModel(rows, columns);
		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener(this);

		/* to change the color of the header */
		/* JTableHeader header = table.getTableHeader();
    		   header.setBackground(Color.yellow); */

		/* to stop reordering of columns in jtable */
		//table.getTableHeader().setReorderingAllowed(false);

		/* prevent resizing of columns */
		// table.getTableHeader().setResizingAllowed(false);

		JScrollPane pane = new JScrollPane(table);
		frame.add(pane, BorderLayout.CENTER);
		JPanel p1=new JPanel();
		JButton addBtn=new JButton("Add");
		addBtn.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					model.addRow(new String[]{"AMZN", "Amazon", "41.28"});
				}
			});
		p1.add(addBtn);
		JButton removeBtn=new JButton("Remove");
		removeBtn.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					int []rowNum=table.getSelectedRows();
					System.out.println(rowNum.length);
					for(int i=rowNum.length;i>0;i--)
					{
						model.removeRow(rowNum[i-1]);
					}
				}
			});
		p1.add(removeBtn);
		JButton changeHeading = new JButton("Change Heading");
		changeHeading.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				TableColumn column = table.getColumn("Price");
				column.setHeaderValue("Rupees");
				table.getTableHeader().repaint();
			}
		});
		p1.add(changeHeading);

		/*	to remove the column from the jtable view, but the column
		 *	remains in the model, so we can get its value in
		 *	valueChanged() method
		 */
		/*table.getColumn("Symbol").setWidth(0);
		table.getColumn("Symbol").setMinWidth(0);
		table.getColumn("Symbol").setMaxWidth(0);*/

		frame.add(p1,"South");
		frame.setBounds(200,100,350, 350);
		frame.setVisible(true);
	}

	public void valueChanged(ListSelectionEvent e) {
		System.out.println("Value Changed" + e.getValueIsAdjusting());
		if(e.getValueIsAdjusting() == false) {
			int row = table.getSelectedRow();
			if (row > -1) {
				// source of event
				// ListSelectionModel lsm = (ListSelectionModel)e.getSource();

				// clears the selection
				// lsm.clearSelection();

				// check if the specified index is selected
				// System.out.println(lsm.isSelectedIndex(0));

				// returns true if no indices are selected
				// System.out.println(lsm.isSelectionEmpty());

				// returns the value for the cell at specified row and column
				Object val = model.getValueAt(row, 0);
				System.out.println(val);
			}
		}
    }

	public final static void main(String args[])
	{
		new Add_RemoveRowTable();
	}
}

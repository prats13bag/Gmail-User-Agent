package src.MUA;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.tree.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;

public class MailServer extends emailidreader implements MouseListener,ActionListener,TreeSelectionListener,ListSelectionListener {
	JFrame mailserverframe = new JFrame("Gmail User Agent");
	JSplitPane splitpanehorizontal, splitpanevertical;
	JPanel treepanel, bodypanel, inboxbuttonpanel, contentpanel, sentitembuttonpanel, deletedbuttonpanel;
	JButton inboxreply, inboxforward, inboxdelete, inboxdeleteall;
	JButton sentitemsendbt, sentitemsendallbt, sentitemforwardbt, sentitemdeletebt, sentitemdeleteallbt;
	JButton deletedrestorebt, deletedrestoreallbt, deleteddeletebt, deleteddeleteallbt;
	JButton recieveallbt, composebt;
	private JTree tree;
	JDesktopPane desktoppane;
	JInternalFrame internalcomposeframe;
	JDialog dialog;
	JButton composesendbt, composesavebt, composecancelbt;
	JTextField composetotf, composesubtf;
	JLabel composetolabel, composesublabel, composemsglabel;
	JEditorPane composemsgcontent = new JEditorPane();
	JFrame composeframe;
	JPanel panel;
	DefaultTableModel model;
	JTable table;
	JPanel outboxbuttonpanel;
	JButton outboxsendbt, outboxsendallbt, outboxforwardbt, outboxdeletebt, outboxdeleteallbt;
	JButton messagedetailssendbt, messagedetailssavebt, messagedetailscancelbt;
	JDialog messagedetailsdialog;
	JTextField messagedetailstotf, messagedetailssubtf;
	JLabel messagedetailspreview, messagedetailstolabel, messagedetailssublabel, messagedetailsmsglabel;
	JEditorPane messagedetailsmsgcontent = new JEditorPane();
	JFrame messagedetailsframe;
	Connection con;
	PreparedStatement psmt;
	ResultSet res;
	DefaultMutableTreeNode inbox, sentitem, outbox, deleted, Draft;
	JPanel tablepanel;
	JPanel sentitemtablepanel;
	DefaultTableModel sentitemmodel;
	JTable sentitemtable;
	JTable outboxtable;
	DefaultTableModel outboxmodel;
	JPanel outboxtablepanel;
	JTable deletedtable;
	DefaultTableModel deletedmodel;
	JPanel deletedtablepanel;
	Object val;//selected line in table
	String selectedtree = "Inbox";
	JPanel draftbuttonpanel = new JPanel();
	JButton draftsendbt, draftsendallbt, draftforwardbt, draftdeletebt, draftdeleteallbt;
	JPanel drafttablepanel;
	DefaultTableModel draftmodel;
	JTable drafttable;

	/*--------------------------------------------------constructor--------------------------------------------------*/
	public MailServer() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		desktoppane = new JDesktopPane();
		desktoppane.setBackground(Color.white);
		treepanel = new JPanel();
		bodypanel = new JPanel();
		treepanel.setBackground(Color.white);
		bodypanel.setBackground(Color.white);
		bodypanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcbodypanel = new GridBagConstraints();
		treepanel.setMinimumSize(new Dimension(150, 800));
		splitpanehorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treepanel, bodypanel);
		splitpanehorizontal.setDividerSize(1);
		splitpanehorizontal.setContinuousLayout(true);
		mailserverframe.add(splitpanehorizontal);

		/*---------------------------------------------------inbox---------------------------------------------------*/
		inboxbuttonpanel = new JPanel();
		inboxreply = new JButton("Reply");
		inboxforward = new JButton("Forward");
		inboxdelete = new JButton("Delete");
		inboxdeleteall = new JButton("DeleteAll");
		inboxreply.getBaseline(0, 0);
		inboxbuttonpanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		inboxbuttonpanel.add(inboxreply, gbc);
		gbc.gridx = 1;
		inboxbuttonpanel.add(inboxforward, gbc);
		gbc.gridx = 2;
		inboxbuttonpanel.add(inboxdelete, gbc);
		gbc.gridx = 3;
		inboxbuttonpanel.add(inboxdeleteall, gbc);
		inboxbuttonpanel.setVisible(true);
		gbcbodypanel.gridx = 0;
		gbcbodypanel.gridy = 0;
		gbcbodypanel.gridwidth = 1;
		gbcbodypanel.gridheight = 1;
		gbcbodypanel.weightx = 1;
		gbcbodypanel.weighty = 1;
		bodypanel.add(inboxbuttonpanel, gbcbodypanel);
		inboxreply.addActionListener(this);
		inboxforward.addActionListener(this);
		inboxdelete.addActionListener(this);
		inboxdeleteall.addActionListener(this);

		/*-------------------------------------------------sent items-------------------------------------------------*/
		sentitembuttonpanel = new JPanel();
		sentitemsendbt = new JButton("Send");
		sentitemsendallbt = new JButton("Send All");
		sentitemforwardbt = new JButton("Forward");
		sentitemdeletebt = new JButton("Delete");
		sentitemdeleteallbt = new JButton("DeleteAll");
		sentitembuttonpanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		sentitembuttonpanel.add(sentitemsendbt, gbc);
		gbc.gridx = 1;
		sentitembuttonpanel.add(sentitemsendallbt, gbc);
		gbc.gridx = 2;
		sentitembuttonpanel.add(sentitemforwardbt, gbc);
		gbc.gridx = 3;
		sentitembuttonpanel.add(sentitemdeletebt, gbc);
		gbc.gridx = 4;
		sentitembuttonpanel.add(sentitemdeleteallbt, gbc);
		bodypanel.add(sentitembuttonpanel, gbcbodypanel);
		sentitembuttonpanel.setVisible(false);
		sentitemsendbt.addActionListener(this);
		sentitemsendallbt.addActionListener(this);
		sentitemforwardbt.addActionListener(this);
		sentitemdeletebt.addActionListener(this);
		sentitemdeleteallbt.addActionListener(this);

		/*---------------------------------------------------outbox---------------------------------------------------*/
		outboxbuttonpanel = new JPanel();
		outboxsendbt = new JButton("Send");
		outboxsendallbt = new JButton("Send All");
		outboxforwardbt = new JButton("Forward");
		outboxdeletebt = new JButton("Delete");
		outboxdeleteallbt = new JButton("DeleteAll");
		outboxbuttonpanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		outboxbuttonpanel.add(outboxsendbt, gbc);
		gbc.gridx = 1;
		outboxbuttonpanel.add(outboxsendallbt, gbc);
		gbc.gridx = 2;
		outboxbuttonpanel.add(outboxforwardbt, gbc);
		gbc.gridx = 3;
		outboxbuttonpanel.add(outboxdeletebt, gbc);
		gbc.gridx = 4;
		outboxbuttonpanel.add(outboxdeleteallbt, gbc);
		bodypanel.add(outboxbuttonpanel, gbcbodypanel);
		outboxbuttonpanel.setVisible(false);
		outboxsendbt.addActionListener(this);
		outboxsendallbt.addActionListener(this);
		outboxforwardbt.addActionListener(this);
		outboxdeletebt.addActionListener(this);
		outboxdeleteallbt.addActionListener(this);

		/*---------------------------------------------------drafts---------------------------------------------------*/
		draftbuttonpanel = new JPanel();
		draftsendbt = new JButton("Send");
		draftsendallbt = new JButton("Send All");
		draftforwardbt = new JButton("Forward");
		draftdeletebt = new JButton("Delete");
		draftdeleteallbt = new JButton("DeleteAll");
		draftbuttonpanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		draftbuttonpanel.add(draftsendbt, gbc);
		gbc.gridx = 1;
		draftbuttonpanel.add(draftsendallbt, gbc);
		gbc.gridx = 2;
		draftbuttonpanel.add(draftforwardbt, gbc);
		gbc.gridx = 3;
		draftbuttonpanel.add(draftdeletebt, gbc);
		gbc.gridx = 4;
		draftbuttonpanel.add(draftdeleteallbt, gbc);
		bodypanel.add(draftbuttonpanel, gbcbodypanel);
		draftbuttonpanel.setVisible(false);
		draftsendbt.addActionListener(this);
		draftsendallbt.addActionListener(this);
		draftforwardbt.addActionListener(this);
		draftdeletebt.addActionListener(this);
		draftdeleteallbt.addActionListener(this);

		/*-----------------------------------------------deleted mails-----------------------------------------------*/
		deletedbuttonpanel = new JPanel();
		deletedrestorebt = new JButton("Restore");
		deletedrestoreallbt = new JButton("Restore All");
		deleteddeletebt = new JButton("Delete");
		deleteddeleteallbt = new JButton("DeleteAll");
		deletedbuttonpanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		deletedbuttonpanel.add(deletedrestorebt, gbc);
		gbc.gridx = 1;
		deletedbuttonpanel.add(deletedrestoreallbt, gbc);
		gbc.gridx = 2;
		deletedbuttonpanel.add(deleteddeletebt, gbc);
		gbc.gridx = 3;
		deletedbuttonpanel.add(deleteddeleteallbt, gbc);
		deletedbuttonpanel.setVisible(false);
		bodypanel.add(deletedbuttonpanel, gbcbodypanel);
		deletedrestorebt.addActionListener(this);
		deleteddeleteallbt.addActionListener(this);
		deletedrestoreallbt.addActionListener(this);
		deleteddeletebt.addActionListener(this);

		/*----------------------------------------------left panel tree----------------------------------------------*/
		treepanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		DefaultMutableTreeNode box = new DefaultMutableTreeNode("Folders");
		inbox = new DefaultMutableTreeNode("Inbox");
		Draft = new DefaultMutableTreeNode("Drafts");
		outbox = new DefaultMutableTreeNode("Outbox");
		sentitem = new DefaultMutableTreeNode("Sent Mails");
		deleted = new DefaultMutableTreeNode("Deleted Mails");

		box.add(inbox);
		box.add(Draft);
		box.add(outbox);
		box.add(sentitem);
		box.add(deleted);

		tree = new JTree(box);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);

		DefaultTreeCellRenderer render = new DefaultTreeCellRenderer();
		render.setLeafIcon(createImageIcon("images/feed.PNG"));
		render.setClosedIcon(createImageIcon("images/closedfolder.PNG"));
		render.setOpenIcon(createImageIcon("images/openfolder.PNG"));
		tree.setCellRenderer(render);
		JScrollPane treeView = new JScrollPane(tree);
		treeView.setPreferredSize(new Dimension(125, 125));
		treeView.setMinimumSize(new Dimension(125, 125));
		treepanel.add(treeView, gbc);
		gbc.gridy = 1;
		composebt = new JButton("Compose Mail");
		treepanel.add(composebt, gbc);
		composebt.addActionListener(this);
		gbc.gridy = 2;
		recieveallbt = new JButton("Recieve All");
		treepanel.add(recieveallbt, gbc);
		recieveallbt.addActionListener(this);

 		/*---------------------------------------compose mail frame and dialog---------------------------------------*/
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		composetolabel = new JLabel("To");
		composesublabel = new JLabel("Subject");
		composemsglabel = new JLabel("Message");
		composemsgcontent.setContentType("text/html/rtf");
		composemsgcontent.setSize(270, 100);
		composetotf = new JTextField(20);
		composesubtf = new JTextField(20);
		composesendbt = new JButton("Send");
		composesavebt = new JButton("Save");
		composecancelbt = new JButton("Cancel");
		JScrollPane composescrollmsg = new JScrollPane(composemsgcontent);

		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(composetolabel, gbc);
		gbc.gridx = 1;
		panel.add(composetotf, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		panel.add(composesublabel, gbc);
		gbc.gridx = 1;
		panel.add(composesubtf, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(composemsglabel, gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.ipady = 50;
		panel.add(composescrollmsg, gbc);
		gbc.ipadx = 30;
		gbc.ipady = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(composesendbt, gbc);
		gbc.gridx = 1;
		panel.add(composesavebt, gbc);
		gbc.gridx = 2;
		panel.add(composecancelbt, gbc);
		composeframe = new JFrame();
		composeframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		composeframe.pack();
		composeframe.pack();

		dialog = new JDialog(composeframe, "New Mail", true);
		dialog.setSize(400, 300);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - dialog.getWidth()) / 2;
		final int y = (screenSize.height - dialog.getHeight()) / 2;

		dialog.setLocation(x, y);
		dialog.add(panel);
		dialog.setResizable(false);
		dialog.setVisible(false);

		composeframe.dispose();
		composesendbt.addActionListener(this);
		composesavebt.addActionListener(this);
		composecancelbt.addActionListener(this);

		/*-----------------------------------------------Table frame-----------------------------------------------*/
		tablepanel = new JPanel();
		tablepanel.setBackground(Color.white);
		class MyDefaultTableModel extends DefaultTableModel {
			MyDefaultTableModel(Object[][] data, Object[] columnNames) {
				super(data, columnNames);
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		Object rows[][] = {};
		String columns[] = {"From", "Subject", "Date"};
		model = new MyDefaultTableModel(rows, columns);
		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener(this);
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.BLACK);
		JScrollPane pane = new JScrollPane(table);
		tablepanel.add(pane, BorderLayout.CENTER);
		tablepanel.setVisible(true);
		gbcbodypanel.gridy = 1;
		gbcbodypanel.gridwidth = 1;
		gbcbodypanel.gridheight = 1;
		gbcbodypanel.ipadx = 450;
		gbcbodypanel.ipady = 400;
		bodypanel.add(tablepanel, gbcbodypanel);

		/*-------------------------------------------Sent Item Table Panel-------------------------------------------*/
		sentitemtablepanel = new JPanel();
		class MyDefaultsentitemTableModel extends DefaultTableModel {
			MyDefaultsentitemTableModel(Object[][] data, Object[] columnNames) {
				super(data, columnNames);
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		Object sentitemrows[][] = {};
		String sentitemcolumns[] = {"From", "Subject", "Date"};
		sentitemmodel = new MyDefaultsentitemTableModel(sentitemrows, sentitemcolumns);
		sentitemtable = new JTable(sentitemmodel);
		sentitemtable.getSelectionModel().addListSelectionListener(this);
		JScrollPane sentitempane = new JScrollPane(sentitemtable);
		sentitemtablepanel.add(sentitempane, BorderLayout.CENTER);
		sentitemtablepanel.setVisible(false);
		gbcbodypanel.gridy = 1;
		gbcbodypanel.gridx = 0;
		bodypanel.add(sentitemtablepanel, gbcbodypanel);

		/*--------------------------------------------Outbox Table Panel--------------------------------------------*/
		outboxtablepanel = new JPanel();
		class MyDefaultoutboxTableModel extends DefaultTableModel {
			MyDefaultoutboxTableModel(Object[][] data, Object[] columnNames) {
				super(data, columnNames);
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		Object outboxrows[][] = {};
		String outboxcolumns[] = {"From", "Subject", "Date"};
		outboxmodel = new MyDefaultoutboxTableModel(outboxrows, outboxcolumns);
		outboxtable = new JTable(outboxmodel);
		outboxtable.getSelectionModel().addListSelectionListener(this);
		JScrollPane outboxpane = new JScrollPane(outboxtable);
		outboxtablepanel.add(outboxpane, BorderLayout.CENTER);
		outboxtablepanel.setVisible(false);
		gbcbodypanel.gridy = 1;
		gbcbodypanel.gridx = 0;
		bodypanel.add(outboxtablepanel, gbcbodypanel);


		/*---------------------------------------------Draft Table Panel---------------------------------------------*/

		drafttablepanel = new JPanel();
		class MyDefaultdraftTableModel extends DefaultTableModel {
			MyDefaultdraftTableModel(Object[][] data, Object[] columnNames) {
				super(data, columnNames);
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		Object draftrows[][] = {};
		String draftcolumns[] = {"From", "Subject", "Date"};
		draftmodel = new MyDefaultdraftTableModel(draftrows, draftcolumns);
		drafttable = new JTable(draftmodel);
		drafttable.getSelectionModel().addListSelectionListener(this);
		JScrollPane draftpane = new JScrollPane(drafttable);
		drafttablepanel.add(draftpane, BorderLayout.CENTER);
		drafttablepanel.setVisible(false);
		gbcbodypanel.gridy = 1;
		gbcbodypanel.gridx = 0;
		bodypanel.add(drafttablepanel, gbcbodypanel);

		/*-----------------------------------------Deleted Mails Table Panel-----------------------------------------*/

		deletedtablepanel = new JPanel();
		class MyDefaultdeletedTableModel extends DefaultTableModel {
			MyDefaultdeletedTableModel(Object[][] data, Object[] columnNames) {
				super(data, columnNames);
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		Object deletedrows[][] = {};
		String deletedcolumns[] = {"From", "Subject", "Date"};
		deletedmodel = new MyDefaultdeletedTableModel(deletedrows, deletedcolumns);
		deletedtable = new JTable(deletedmodel);
		deletedtable.getSelectionModel().addListSelectionListener(this);
		JScrollPane deletedpane = new JScrollPane(deletedtable);
		deletedtablepanel.add(deletedpane, BorderLayout.CENTER);
		deletedtablepanel.setVisible(false);
		gbcbodypanel.gridy = 1;
		gbcbodypanel.gridx = 0;
		bodypanel.add(deletedtablepanel, gbcbodypanel);

		/*----------------------------------------Message Details Table Panel----------------------------------------*/

		JPanel messagedetailspanel = new JPanel();
		messagedetailspanel.setBackground(Color.white);
		messagedetailspanel.setLayout(new GridBagLayout());
		String text = "<html><b>E-Mail Preview</b></html>";
		messagedetailspreview = new JLabel(text);
		messagedetailstolabel = new JLabel("From/ To");
		messagedetailssublabel = new JLabel("Subject");
		messagedetailsmsglabel = new JLabel("Message");
		messagedetailsmsgcontent.setContentType("text/html/rtf");
		messagedetailsmsgcontent.setEditable(false);
		messagedetailsmsgcontent.setSize(225, 500);
		messagedetailstotf = new JTextField(15);
		messagedetailstotf.setEditable(false);
		messagedetailstotf.setBackground(Color.white);
		messagedetailssubtf = new JTextField(15);
		messagedetailssubtf.setEditable(false);
		messagedetailssubtf.setBackground(Color.white);
		messagedetailssendbt = new JButton("Send");
		messagedetailssavebt = new JButton("Save");
		messagedetailscancelbt = new JButton("Cancel");
		JScrollPane messagedetailsscrollmsg = new JScrollPane(messagedetailsmsgcontent);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		messagedetailspanel.add(messagedetailspreview, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		messagedetailspanel.add(messagedetailstolabel, gbc);
		gbc.gridx = 1;
		messagedetailspanel.add(messagedetailstotf, gbc);
		gbc.gridy = 2;
		gbc.gridx = 0;
		messagedetailspanel.add(messagedetailssublabel, gbc);
		gbc.gridx = 1;
		messagedetailspanel.add(messagedetailssubtf, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		messagedetailspanel.add(messagedetailsmsglabel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.ipady = 100;
		messagedetailspanel.add(messagedetailsscrollmsg, gbc);
		gbc.ipadx = 50;
		gbc.ipady = 5;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbcbodypanel.gridx = 1;
		gbcbodypanel.gridy = 1;
		gbcbodypanel.gridheight = 1;
		gbcbodypanel.gridwidth = 1;
		gbcbodypanel.ipadx = 200;
		gbcbodypanel.ipady = 300;
		bodypanel.add(messagedetailspanel, gbcbodypanel);

		/*------------------------------------------Connection to Database------------------------------------------*/
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			String url = "jdbc:mysql://localhost:3306/";
			String dbusername = "root";
			String dbpassword = "root";
			con = DriverManager.getConnection(url, dbusername, dbpassword);
			psmt = con.prepareStatement("CREATE DATABASE MAILS");
			psmt.executeUpdate();
			psmt = con.prepareStatement("USE MAILS");
			psmt.executeUpdate();
			psmt = con.prepareStatement("CREATE TABLE messagedetails " + "(From_Email VARCHAR(100), " + "Subject VARCHAR(100), " + "Date VARCHAR(100), " + "Message VARCHAR(1000))");
			psmt.executeUpdate();
			psmt = con.prepareStatement("CREATE TABLE sentitemdbtable " + "(To_Email VARCHAR(100), " + "Subject VARCHAR(100), " + "Date VARCHAR(100), " + "Message VARCHAR(1000))");
			psmt.executeUpdate();
			psmt = con.prepareStatement("CREATE TABLE outboxdbtable " + "(To_Email VARCHAR(100), " + "Subject VARCHAR(100), " + "Date VARCHAR(100), " + "Message VARCHAR(1000))");
			psmt.executeUpdate();
			psmt = con.prepareStatement("CREATE TABLE draftdbtable " + "(To_Email VARCHAR(100), " + "Subject VARCHAR(100), " + "Date VARCHAR(100), " + "Message VARCHAR(1000))");
			psmt.executeUpdate();
			psmt = con.prepareStatement("CREATE TABLE deleteddbtable " + "(Email VARCHAR(100), " + "Subject VARCHAR(100), " + "Date VARCHAR(100), " + "Message VARCHAR(1000))");
			psmt.executeUpdate();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			System.out.println("Couldn't connect to database at line number 571 in MailServer.java file");
			try
			{
				psmt = con.prepareStatement("DROP DATABASE MAILS");
				psmt.executeUpdate();
				con.close();
			}
			catch (Exception ex) {}
		}

		/*---------------------------------------------------Inbox---------------------------------------------------*/
		try {
			psmt = con.prepareStatement("SELECT * FROM MESSAGEDETAILS");
			res = psmt.executeQuery();
			while (res.next()) {
				model.addRow(new String[]{res.getString("From_Email"), res.getString("Subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 589 in MailServer.java file");
		}
		if (table.getRowCount() != 0)
			table.setRowSelectionInterval(0, 0);

		/*-------------------------------------------------Sent Item-------------------------------------------------*/
		try {
			psmt = con.prepareStatement("SELECT * FROM sentitemdbtable");
			res = psmt.executeQuery();
			while (res.next()) {
				sentitemmodel.addRow(new String[]{res.getString("To_Email"), res.getString("Subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 602 in MailServer.java file");
		}

		/*---------------------------------------------------Outbox---------------------------------------------------*/
		try {
			psmt = con.prepareStatement("SELECT * FROM outboxdbtable");
			res = psmt.executeQuery();
			while (res.next()) {
				outboxmodel.addRow(new String[]{res.getString("To_Email"), res.getString("Subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 613 in MailServer.java file");

		}

		/*---------------------------------------------------Draft---------------------------------------------------*/
		try {
			psmt = con.prepareStatement("SELECT * FROM draftdbtable");
			res = psmt.executeQuery();
			while (res.next()) {
				draftmodel.addRow(new String[]{res.getString("To_Email"), res.getString("Subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 625 in MailServer.java file");
		}

		/*--------------------------------------------------Deleted--------------------------------------------------*/
		try {
			psmt = con.prepareStatement("SELECT * FROM deleteddbtable");
			res = psmt.executeQuery();
			while (res.next()) {
				deletedmodel.addRow(new String[]{res.getString("Email"), res.getString("Subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 636 in MailServer.java file");
		}

		/*-----------------------------Code ends for establishing connection to database-----------------------------*/

		mailserverframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mailserverframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				try {
					DriverManager.registerDriver(new com.mysql.jdbc.Driver());
					DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root").prepareStatement("DROP DATABASE MAILS").executeUpdate();
					DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root").close();
				} catch (Exception ex) {
					System.out.println("Failed to drop database schema and close the connection");
					try {
						DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root").close();
					} catch (Exception except) {
						System.out.println("Failed to close database connection");
						ex.printStackTrace();
					}
				}
				System.exit(0);
			}
		} );
		mailserverframe.setSize(900, 600);
		mailserverframe.setVisible(true);

	}
	/*---------------------------------------------Constructor ends here---------------------------------------------*/


	/*-------------------------------------------Starts here for all Events-------------------------------------------*/
	public void mousePressed(MouseEvent mp) {
		// do nothing
	}

	public void mouseReleased(MouseEvent mr) {
		// do nothing
	}

	public void mouseExited(MouseEvent mex) {
		inboxreply = (JButton) mex.getSource();
		inboxreply.setBackground(Color.white);

	}

	public void mouseClicked(MouseEvent mc) {
		// do nothing
	}

	public void mouseEntered(MouseEvent me) {
		inboxreply = (JButton) me.getSource();
		inboxreply.setBackground(new Color(100, 2, 25));
	}

	public void actionPerformed(ActionEvent ae) {
		JButton actionbt = (JButton) ae.getSource();
		if (actionbt == inboxreply) {
			inboxreplyHandler();
		}
		if (actionbt == inboxforward) {
			inboxforwardHandler();
		}
		if (actionbt == inboxdelete) {
			inboxdeleteHandler();
		}
		if (actionbt == inboxdeleteall) {
			inboxdeleteallHandler();
		}
		if (actionbt == sentitemsendbt) {
			sentitemsendbtHandler();
		}
		if (actionbt == sentitemsendallbt) {
			sentitemsendallbtHandler();
		}
		if (actionbt == sentitemforwardbt) {
			sentitemforwardbtHandler();
		}
		if (actionbt == sentitemdeletebt) {
			sentitemdeletebtHAndler();
		}
		if (actionbt == sentitemdeleteallbt) {
			sentitemdeleteallbtHandler();
		}
		if (actionbt == outboxsendbt) {
			System.out.println("Outbox Sent Event");
			outboxsendbtHandler();
		}
		if (actionbt == outboxsendallbt) {
			outboxsendallbtHandler();
		}
		if (actionbt == outboxforwardbt) {
			outboxforwardbtHandler();
		}
		if (actionbt == outboxdeletebt) {
			outboxdeletebtHandler();
		}
		if (actionbt == outboxdeleteallbt) {
			outboxdeleteallbtHandler();
		}
		if (actionbt == draftsendbt) {
			System.out.println("Draft Sent Event");
			draftsendbtHandler();
		}
		if (actionbt == draftsendallbt) {
			draftsendallbtHandler();
		}
		if (actionbt == draftforwardbt) {
			draftforwardbtHandler();
		}
		if (actionbt == draftdeletebt) {
			draftdeletebtHandler();
		}
		if (actionbt == draftdeleteallbt) {
			draftdeleteallbtHandler();
		}
		if (actionbt == deletedrestorebt) {
			deletedrestorebtHandler();
		}
		if (actionbt == deletedrestoreallbt) {
			deletedrestoreallbtHandler();
		}
		if (actionbt == deleteddeletebt) {
			deleteddeletebtHandler();
		}
		if (actionbt == deleteddeleteallbt) {
			deleteddeleteallbtHandler();
		}
		if (actionbt == recieveallbt) {
			System.out.println("Receiving all e-mails");
			Thread t = new Thread(new recieveallbtHandler(psmt, con, model));
			t.start();
		}
		if (actionbt == composebt) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					composebtHandler();
				}
			});
		}
		if (actionbt == composesendbt) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					composesendbtHandler();
				}
			});
		}
		if (actionbt == composesavebt) {
			composesavebtHandler();
		}
		if (actionbt == composecancelbt) {
			composecancelbtHandler();
		}
	}

	public void valueChanged(TreeSelectionEvent e) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		selectedtree = (String) node.getUserObject();
		if (selectedtree.equals("Inbox")) {
			sentitemtable.clearSelection();
			outboxtable.clearSelection();
			drafttable.clearSelection();
			deletedtable.clearSelection();
			if (table.getRowCount() != 0)
				table.setRowSelectionInterval(0, 0);
			else {
				messagedetailstotf.setText("");
				messagedetailssubtf.setText("");
				messagedetailsmsgcontent.setText("");
			}
			inboxHandler();
		}
		if (selectedtree.equals("sentitem")) {
			table.clearSelection();
			outboxtable.clearSelection();
			drafttable.clearSelection();
			deletedtable.clearSelection();
			if (sentitemtable.getRowCount() != 0)
				sentitemtable.setRowSelectionInterval(0, 0);
			else {
				messagedetailstotf.setText("");
				messagedetailssubtf.setText("");
				messagedetailsmsgcontent.setText("");
			}
			sentitemHandler();
		}
		if (selectedtree.equals("outbox")) {
			table.clearSelection();
			sentitemtable.clearSelection();
			drafttable.clearSelection();
			deletedtable.clearSelection();
			if (outboxtable.getRowCount() != 0)
				outboxtable.setRowSelectionInterval(0, 0);
			else {
				messagedetailstotf.setText("");
				messagedetailssubtf.setText("");
				messagedetailsmsgcontent.setText("");
			}
			outboxHandler();
		}

		if (selectedtree.equals("Draft")) {
			table.clearSelection();
			outboxtable.clearSelection();
			sentitemtable.clearSelection();
			deletedtable.clearSelection();
			if (drafttable.getRowCount() != 0)
				drafttable.setRowSelectionInterval(0, 0);
			else {
				messagedetailstotf.setText("");
				messagedetailssubtf.setText("");
				messagedetailsmsgcontent.setText("");
			}
			draftHandler();
		}
		if (selectedtree.equals("Deleted")) {
			table.clearSelection();
			outboxtable.clearSelection();
			drafttable.clearSelection();
			sentitemtable.clearSelection();
			if (deletedtable.getRowCount() != 0)
				deletedtable.setRowSelectionInterval(0, 0);
			else {
				messagedetailstotf.setText("");
				messagedetailssubtf.setText("");
				messagedetailsmsgcontent.setText("");
			}
			deletedHandler();
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		System.out.println("Value Changed" + e.getValueIsAdjusting());
		if (e.getValueIsAdjusting() == false) {
			int row = table.getSelectedRow();
			if (row > -1) {
				val = model.getValueAt(row, 2);
				System.out.println(val);
			}
			int sentitemrow = sentitemtable.getSelectedRow();
			if (sentitemrow > -1) {
				val = sentitemmodel.getValueAt(sentitemrow, 2);
				sentitemmessagedetailsHandler();
			}
			int outboxrow = outboxtable.getSelectedRow();
			if (outboxrow > -1) {
				val = outboxmodel.getValueAt(outboxrow, 2);
				outboxmessagedetailsHandler();
			}

			int draftrow = drafttable.getSelectedRow();
			if (draftrow > -1) {
				val = draftmodel.getValueAt(draftrow, 2);
				draftmessagedetailsHandler();
			}

			int deletedrow = deletedtable.getSelectedRow();
			if (deletedrow > -1) {
				val = deletedmodel.getValueAt(deletedrow, 2);
				deletedmessagedetailsHandler();
			}
		}
	}

    /*--------------------------------------------Ends here for all Events--------------------------------------------*/

	private ImageIcon createImageIcon(String path) {

		URL imageURL = this.getClass().getResource(path);
		if (imageURL != null)
			return new ImageIcon(imageURL);
		else
			return null;
	}

	public void inboxreplyHandler() {
		composesubtf.setText("");
		composemsgcontent.setText("");
		composetotf.setText("");

		try {
			psmt = con.prepareStatement("SELECT * FROM messagedetails where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				composetotf.setText(res.getString("From"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 927 in MailServer.java file");
		}
		dialog.setVisible(true);
	}

	public void inboxforwardHandler() {
		composesubtf.setText("");
		composemsgcontent.setText("");
		composetotf.setText("");
		try {
			psmt = con.prepareStatement("SELECT * FROM messagedetails where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				composesubtf.setText(res.getString("Subject"));
				composemsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 945 in MailServer.java file");
		}
		dialog.setVisible(true);
	}

	public void inboxdeleteHandler() {
		int ideli = 0;
		int idel = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO deleteddbtable SELECT *FROM messagedetails where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("delete FROM messagedetails  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
				} catch (Exception m) {
				}
				try {
					psmt = con.prepareStatement("INSERT INTO deletedinfo values(?,?)");
					psmt.setString(1, (String) val);
					psmt.setString(2, selectedtree);
					ideli = psmt.executeUpdate();
				} catch (Exception m) {
				}
				if (idel != 0 && ideli != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM deleteddbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next())
							deletedmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
					} catch (Exception n) {
					}
					int[] rowNum = table.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						model.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (Exception e) {
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 987");
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void inboxdeleteallHandler() {
		int rowcount = table.getRowCount();
		while (rowcount != 0) {
			table.setRowSelectionInterval(0, 0);
			inboxdeleteHandler();
			rowcount--;
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void sentitemsendbtHandler() {
		sentitemsentbthandler o = new sentitemsentbthandler(val, sentitemtable, sentitemmodel, outboxmodel);
		Thread t = new Thread(o);
		t.start();
	}

	public void sentitemsendallbtHandler() {
		int rowcount = sentitemtable.getRowCount();
		while (rowcount != 0) {
			sentitemtable.setRowSelectionInterval(0, 0);
			sentitemsendbtHandler();
			rowcount--;
		}
	}

	public void sentitemforwardbtHandler() {
		composesubtf.setText("");
		composemsgcontent.setText("");
		composetotf.setText("");
		try {
			psmt = con.prepareStatement("SELECT * FROM sentitemdbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				composesubtf.setText(res.getString("Subject"));
				composemsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1034 in MailServer.java file");
		}
		dialog.setVisible(true);
	}


	public void sentitemdeletebtHAndler() {
		int idel = 0;
		int ideli = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO deleteddbtable SELECT *FROM sentitemdbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("INSERT INTO deletedinfo values(?,?)");
					psmt.setString(1, (String) val);
					psmt.setString(2, selectedtree);
					ideli = psmt.executeUpdate();
				} catch (Exception m) {
				}
				try {
					psmt = con.prepareStatement("delete FROM sentitemdbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
					if (idel != 0) {
						System.out.printf("Deleted");
					}
				} catch (Exception m) {
				}
				if (ideli != 0 && idel != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM deleteddbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next())
							deletedmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
					} catch (Exception e) {
					}
					int[] rowNum = sentitemtable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						sentitemmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (Exception e) {
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1080");
			e.printStackTrace();
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void sentitemdeleteallbtHandler() {
		int rowcount = sentitemtable.getRowCount();
		while (rowcount != 0) {
			sentitemtable.setRowSelectionInterval(0, 0);
			sentitemdeletebtHAndler();
			rowcount--;
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void outboxsendbtHandler() {
		outboxsendbtHandler o = new outboxsendbtHandler(val, outboxtable, sentitemmodel, outboxmodel);
		Thread t = new Thread(o);
		t.start();
	}

	public void outboxsendallbtHandler() {
		int rowcount = outboxtable.getRowCount();
		while (rowcount != 0) {
			outboxtable.setRowSelectionInterval(0, 0);
			outboxsendbtHandler();
			rowcount--;
		}
	}

	public void outboxforwardbtHandler() {
		try {
			composetotf.setText("");
			psmt = con.prepareStatement("SELECT * FROM outboxdbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				composesubtf.setText(res.getString("Subject"));
				composemsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1126 in MailServer.java file");
		}
		dialog.setVisible(true);
	}

	public void outboxdeletebtHandler() {
		int idel = 0, ideli = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO deleteddbtable SELECT *FROM outboxdbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("INSERT INTO deletedinfo values(?,?)");
					psmt.setString(1, (String) val);
					psmt.setString(2, selectedtree);
					ideli = psmt.executeUpdate();
				} catch (Exception m) {
				}
				try {
					psmt = con.prepareStatement("delete FROM outboxdbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
				} catch (Exception n) {
				}
				if (idel != 0 && ideli != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM deleteddbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next()) {
							deletedmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
						}
					} catch (Exception e) {
					}
					int[] rowNum = outboxtable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						outboxmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (Exception e) {
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1168");
			e.printStackTrace();
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void outboxdeleteallbtHandler() {
		int rowcount = outboxtable.getRowCount();
		while (rowcount != 0) {
			outboxtable.setRowSelectionInterval(0, 0);
			outboxdeletebtHandler();
			rowcount--;
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void draftsendbtHandler() {
		draftsendbtHandler o = new draftsendbtHandler(val, drafttable, sentitemmodel, outboxmodel, draftmodel);
		Thread t = new Thread(o);
		t.start();
	}

	public void draftsendallbtHandler() {
		int rowcount = drafttable.getRowCount();
		while (rowcount != 0) {
			drafttable.setRowSelectionInterval(0, 0);
			draftsendbtHandler();
			rowcount--;
		}
	}

	public void draftforwardbtHandler() {
		try {
			psmt = con.prepareStatement("SELECT * FROM draftdbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				composesubtf.setText(res.getString("Subject"));
				composemsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1213 in MailServer.java file");
		}
		dialog.setVisible(true);
	}


	public void draftdeletebtHandler() {
		System.out.println("Draft Deleted Handler");
		int idel = 0, ideli = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO deleteddbtable SELECT *FROM draftdbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("INSERT INTO deletedinfo values(?,?)");
					psmt.setString(1, (String) val);
					psmt.setString(2, selectedtree);
					ideli = psmt.executeUpdate();
				} catch (Exception m) {
				}
				try {
					psmt = con.prepareStatement("delete FROM draftdbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
				} catch (Exception n) {
				}
				if (idel != 0 && ideli != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM deleteddbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next()) {
							deletedmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
						}
					} catch (Exception e) {
					}
					int[] rowNum = drafttable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						draftmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (Exception e) {
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1257");
			e.printStackTrace();
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void draftdeleteallbtHandler() {
		int rowcount = drafttable.getRowCount();
		while (rowcount != 0) {
			drafttable.setRowSelectionInterval(0, 0);
			draftdeletebtHandler();
			rowcount--;
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void deletedrestorebtHandler() {
		System.out.println("Deleted Restore Handler");
		try {
			psmt = con.prepareStatement("SELECT *FROM deletedinfo where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				String restore = res.getString("Where");
				if (restore.equals("outbox")) {
					outboxdeletedrestore();
				} else if (restore.equals("sentitem")) {
					sentitemdeletedrestore();
				} else if (restore.equals("Inbox")) {
					inboxdeletedrestore();
				} else if (restore.equals("Draft")) {
					draftdeletedrestore();
				}
			}
		} catch (Exception ew) {
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void deletedrestoreallbtHandler() {
		int rowcount = deletedtable.getRowCount();
		while (rowcount != 0) {
			deletedtable.setRowSelectionInterval(0, 0);
			deletedrestorebtHandler();
			rowcount--;
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void deleteddeletebtHandler() {
		System.out.println("Deleted handler");
		try {
			psmt = con.prepareStatement("delete FROM deleteddbtable  where date= ?");
			psmt.setString(1, (String) val);
			int idel = psmt.executeUpdate();
			try {
				psmt = con.prepareStatement("delete FROM deletedinfo  where date= ?");
				psmt.setString(1, (String) val);
				int idei = psmt.executeUpdate();
			} catch (SQLException m) {
				try {
				} catch (Exception b) {
				}
			}
			int[] rowNum = deletedtable.getSelectedRows();
			for (int i = rowNum.length; i > 0; i--) {
				deletedmodel.removeRow(rowNum[i - 1]);
			}
		} catch (Exception e) {
			try {
			} catch (Exception b) {
			}
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");

	}

	public void deleteddeleteallbtHandler() {
		int rowcount = deletedtable.getRowCount();
		while (rowcount != 0) {
			deletedtable.setRowSelectionInterval(0, 0);
			deleteddeletebtHandler();
			rowcount--;
		}
		messagedetailstotf.setText("");
		messagedetailssubtf.setText("");
		messagedetailsmsgcontent.setText("");
	}

	public void recieveallbtHandler() {
		try {
			Properties prop = new Properties();
			prop.put("mail.imap.host", "imap.gmail.com");
			prop.put("mail.imap.port", "993");
			prop.put("mail.imap.auth", "true");
			prop.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			Session session = Session.getInstance(prop, null);
			Store store = session.getStore("imap");
			store.connect(emailid, emailpswrd);
			Folder inboxfolder = store.getFolder("inbox");
			inboxfolder.open(Folder.READ_ONLY);
			Message arr[] = inboxfolder.getMessages();
			for (int i = 0; i < arr.length; i++) {
				System.out.println("--------------------------------Message" + (i + 1) + "--------------------------------");
				Address[] from = arr[i].getFrom();
				System.out.println("From:  " + from[0]);
				System.out.println("Subject  " + arr[i].getSubject());
				System.out.println("Date   " + arr[i].getSentDate());
				System.out.println("Message   " + arr[i].getContent());

				try {
					psmt = con.prepareStatement("INSERT INTO MESSAGEDETAILS values(?,?,?,?)");
					psmt.setString(1, (String) from[0].toString());
					psmt.setString(2, arr[i].getSentDate().toString());
					psmt.setString(3, (String) arr[i].getContent());
					psmt.setString(4, (String) arr[i].getSubject());
					boolean b = psmt.execute();
				} catch (Exception e) {
					System.out.println("Couldn't insert data into database. Exception at line number 1385 in MailServer.java file");
				}


			}
			inboxfolder.close(false);
			store.close();
			System.out.println("Successfully received");
		} catch (Exception e) {
			System.out.println("Error! Could not connect to server" + "\n" + "Please check connection");
		}

	}

	public void composebtHandler() {
		composesubtf.setText("");
		composemsgcontent.setText("");
		composetotf.setText("");
		dialog.setVisible(true);

	}

	public void composesendbtHandler() {
		System.out.println("Sending Mail");
		try {
			Properties prop = new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
			prop.put("mail.smtp.port", "465");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			Session session = Session.getInstance(prop, new SimpleMailAuthenticator(emailid, emailpswrd));
			Message msg = new MimeMessage(session);
			if (composetotf.getText().equals("")) {
				JOptionPane.showMessageDialog(mailserverframe, "Please Enter Valid Mail Address ");
				composetotf.requestFocusInWindow();
			} else {
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(composetotf.getText()));
				msg.setSubject(composesubtf.getText());
				msg.setText(composemsgcontent.getText());
				Transport.send(msg);
				try {
					psmt = con.prepareStatement("INSERT INTO sentitemdbtable values(?,?,?,?)");
					psmt.setString(1, composetotf.getText());
					psmt.setString(2, new java.util.Date().toString());
					psmt.setString(3, composemsgcontent.getText());
					psmt.setString(4, composesubtf.getText());
					boolean b = psmt.execute();
					dialog.setVisible(false);
					dialog.dispose();
					try {
						psmt = con.prepareStatement("SELECT *FROM sentitemdbtable where date=?");
						psmt.setString(1, new java.util.Date().toString());
						res = psmt.executeQuery();
						while (res.next()) {
							System.out.println(res.getString("to"));
							sentitemmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
						}
					} catch (Exception e1) {
						try {
							dialog.setVisible(false);
							composeframe.dispose();
						} catch (Exception b1) {
						}
					}
				} catch (Exception e) {
					dialog.setVisible(false);
					composeframe.dispose();
					System.out.println("Couldn't insert data into database. Exception at line number 14352 in MailServer.java file");
				}
				System.out.println("Successfully sent");
			}
		} catch (Exception e) {
			System.out.println("Error! Could not connect to server" + "\n" + "Please check connection");
			try {
				psmt = con.prepareStatement("INSERT INTO outboxdbtable values(?,?,?,?)");
				psmt.setString(1, composetotf.getText());
				psmt.setString(2, new java.util.Date().toString());
				psmt.setString(3, composemsgcontent.getText());
				psmt.setString(4, composesubtf.getText());
				boolean b = psmt.execute();
				dialog.setVisible(false);
				dialog.dispose();
				try {
					psmt = con.prepareStatement("SELECT *FROM outboxdbtable where date=?");
					psmt.setString(1, new java.util.Date().toString());
					res = psmt.executeQuery();
					while (res.next()) {
						System.out.println(res.getString("to"));
						outboxmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
					}
				} catch (Exception e1) {

					try {
					} catch (Exception b1) {
					}
				}
			} catch (Exception e1) {
				System.out.println("Couldn't insert data into database. Exception at line number 1482 in MailServer.java file");
			}
		}
	}

	public void composesavebtHandler() {
		if (composetotf.getText().equals("")) {
			JOptionPane.showMessageDialog(mailserverframe, "Please Enter Valid Mail Address ");
			composetotf.requestFocus();
		} else {
			try {
				psmt = con.prepareStatement("INSERT INTO draftdbtable values(?,?,?,?)");
				psmt.setString(1, composetotf.getText());
				psmt.setString(2, new java.util.Date().toString());
				psmt.setString(3, composemsgcontent.getText());
				psmt.setString(4, composesubtf.getText());
				boolean b = psmt.execute();
				dialog.setVisible(false);
				dialog.dispose();
				try {
					psmt = con.prepareStatement("SELECT *FROM draftdbtable where date=?");
					psmt.setString(1, new java.util.Date().toString());
					res = psmt.executeQuery();
					while (res.next()) {
						System.out.println(res.getString("to"));
						draftmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
					}
				} catch (Exception ex) {
				}
			} catch (Exception e) {
				dialog.setVisible(false);
				composeframe.dispose();
				System.out.println("Couldn't insert data into database. Exception at line number 1514 in MailServer.java file");
			}

		}
	}

	public void composecancelbtHandler() {
		dialog.setVisible(false);
		composeframe.dispose();
	}

	public void inboxHandler() {
		try {
			while (res.next()) {
				model.addRow(new String[]{res.getString("from"), res.getString("subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1531 in MailServer.java file");

		}
		sentitembuttonpanel.setVisible(false);
		outboxbuttonpanel.setVisible(false);
		draftbuttonpanel.setVisible(false);
		deletedbuttonpanel.setVisible(false);
		inboxbuttonpanel.setVisible(true);
		sentitemtablepanel.setVisible(false);
		outboxtablepanel.setVisible(false);
		drafttablepanel.setVisible(false);
		deletedtablepanel.setVisible(false);
		tablepanel.setVisible(true);

	}

	public void sentitemHandler() {
		try {
			while (res.next()) {
				sentitemmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1553 in MailServer.java file");

		}
		inboxbuttonpanel.setVisible(false);
		outboxbuttonpanel.setVisible(false);
		deletedbuttonpanel.setVisible(false);
		draftbuttonpanel.setVisible(false);
		drafttablepanel.setVisible(false);
		sentitembuttonpanel.setVisible(true);
		tablepanel.setVisible(false);
		outboxtablepanel.setVisible(false);
		deletedtablepanel.setVisible(false);
		sentitemtablepanel.setVisible(true);
	}

	public void outboxHandler() {
		try {
			while (res.next()) {
				outboxmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1574 in MailServer.java file");

		}

		inboxbuttonpanel.setVisible(false);
		deletedbuttonpanel.setVisible(false);
		sentitembuttonpanel.setVisible(false);
		outboxbuttonpanel.setVisible(true);
		draftbuttonpanel.setVisible(false);
		drafttablepanel.setVisible(false);
		tablepanel.setVisible(false);
		sentitemtablepanel.setVisible(false);
		deletedtablepanel.setVisible(false);
		outboxtablepanel.setVisible(true);
	}

	public void draftHandler() {
		try {
			while (res.next()) {
				draftmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1596 in MailServer.java file");
		}

		inboxbuttonpanel.setVisible(false);
		deletedbuttonpanel.setVisible(false);
		sentitembuttonpanel.setVisible(false);
		outboxbuttonpanel.setVisible(false);
		tablepanel.setVisible(false);
		sentitemtablepanel.setVisible(false);
		deletedtablepanel.setVisible(false);
		outboxtablepanel.setVisible(false);
		draftbuttonpanel.setVisible(true);
		drafttablepanel.setVisible(true);
	}

	public void deletedHandler() {
		try {
			while (res.next()) {
				deletedmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("Date")});
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1617 in MailServer.java file");
		}
		inboxbuttonpanel.setVisible(false);
		outboxbuttonpanel.setVisible(false);
		sentitembuttonpanel.setVisible(false);
		deletedbuttonpanel.setVisible(true);
		draftbuttonpanel.setVisible(false);
		drafttablepanel.setVisible(false);
		tablepanel.setVisible(false);
		sentitemtablepanel.setVisible(false);
		outboxtablepanel.setVisible(false);
		deletedtablepanel.setVisible(true);
	}

	public void inboxmessagedetailsHandler() {
		try {
			psmt = con.prepareStatement("SELECT * FROM messagedetails where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				messagedetailstotf.setText(res.getString("From"));
				messagedetailssubtf.setText(res.getString("subject"));
				messagedetailsmsgcontent.setText(res.getString("message"));

			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1643 in MailServer.java file");
		}
	}

	public void sentitemmessagedetailsHandler() {

		try {
			psmt = con.prepareStatement("SELECT * FROM sentitemdbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				messagedetailstotf.setText(res.getString("To"));
				messagedetailssubtf.setText(res.getString("subject"));
				messagedetailsmsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1659 in MailServer.java file");
		}
	}

	public void outboxmessagedetailsHandler() {

		try {
			psmt = con.prepareStatement("SELECT * FROM outboxdbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				messagedetailstotf.setText(res.getString("To"));
				messagedetailssubtf.setText(res.getString("subject"));
				messagedetailsmsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1675 in MailServer.java file");
			sqle.printStackTrace();

		}
	}

	public void draftmessagedetailsHandler() {
		try {
			psmt = con.prepareStatement("SELECT * FROM draftdbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				messagedetailstotf.setText(res.getString("To"));
				messagedetailssubtf.setText(res.getString("subject"));
				messagedetailsmsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1692 in MailServer.java file");
			sqle.printStackTrace();

		}
	}

	public void deletedmessagedetailsHandler() {
		try {
			psmt = con.prepareStatement("SELECT * FROM deleteddbtable where date=?");
			psmt.setString(1, (String) val);
			res = psmt.executeQuery();
			while (res.next()) {
				messagedetailstotf.setText(res.getString("To"));
				messagedetailssubtf.setText(res.getString("subject"));
				messagedetailsmsgcontent.setText(res.getString("message"));
			}
		} catch (SQLException sqle) {
			System.out.println("Couldn't connect to database at line number 1709 in MailServer.java file");
			sqle.printStackTrace();
		}
	}

	public void outboxdeletedrestore() {
		System.out.println("Deleted outbox items restoring");
		int idel = 0, ideli = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO outboxdbtable SELECT *FROM deleteddbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("delete FROM deleteddbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
				} catch (Exception m) {
					try {
						con.rollback();
					} catch (Exception b) {
					}
				}
				try {
					psmt = con.prepareStatement("delete FROM deletedinfo where date= ?");
					psmt.setString(1, (String) val);
					ideli = psmt.executeUpdate();
				}
				catch (Exception n) {
					try {
						con.rollback();
					}
					catch (Exception b) {
					}
				}
				if (idel != 0 && ideli != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM outboxdbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next()) {
							System.out.println(res.getString("to"));
							outboxmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
						}
					} catch (Exception e) {
						try {
						} catch (Exception b) {
						}
					}
					int[] rowNum = deletedtable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						deletedmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (SQLException e) {
			try {
			} catch (Exception b) {
				b.printStackTrace();
			}
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1769");
			e.printStackTrace();
		}
	}

	public void draftdeletedrestore() {
		System.out.println("Deleted draft items restoring");
		int idel = 0, ideli = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO draftdbtable SELECT *FROM deleteddbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("delete FROM deleteddbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
				} catch (Exception m) {
					try {
						con.rollback();
					} catch (Exception b) {
					}
				}
				try {
					psmt = con.prepareStatement("delete FROM deletedinfo where date= ?");
					psmt.setString(1, (String) val);
					ideli = psmt.executeUpdate();
				} catch (Exception n) {
					try {
						con.rollback();
					} catch (Exception b) {
					}
				}
				if (idel != 0 && ideli != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM draftdbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next()) {
							System.out.println(res.getString("to"));
							draftmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
						}
					} catch (Exception e) {
						try {
						} catch (Exception b) {
						}
					}
					int[] rowNum = deletedtable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						deletedmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (SQLException e) {
			try {
			} catch (Exception b) {
				b.printStackTrace();
			}
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1827");
			e.printStackTrace();
		}
	}

	public void inboxdeletedrestore() {
		System.out.println("Deleted inbox items restoring");
		int idel = 0;
		int ideli = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO messagedetails SELECT *FROM deleteddbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("delete FROM deleteddbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
					if (idel != 0) {
						System.out.printf("Deleted");

					}
				} catch (Exception m) {
				}
				try {
					psmt = con.prepareStatement("delete FROM deletedinfo where date=?");
					psmt.setString(1, (String) val);
					ideli = psmt.executeUpdate();
				} catch (Exception n) {
				}
				if (idel != 0 && ideli != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM messagedetails where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next())
							model.addRow(new String[]{res.getString("From"), res.getString("subject"), res.getString("date")});
					} catch (Exception e) {
					}
					int[] rowNum = deletedtable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						deletedmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (Exception e) {
			try {
			} catch (Exception b) {
			}
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1876");
		}
	}

	public void sentitemdeletedrestore() {
		System.out.println("Deleted sent items restoring");
		int ideli = 0;
		int idel = 0;
		try {
			psmt = con.prepareStatement("INSERT INTO sentitemdbtable SELECT *FROM deleteddbtable where date=?");
			psmt.setString(1, (String) val);
			int ins = psmt.executeUpdate();
			if (ins != 0) {
				try {
					psmt = con.prepareStatement("delete FROM deleteddbtable  where date= ?");
					psmt.setString(1, (String) val);
					idel = psmt.executeUpdate();
					if (idel != 0) {
						System.out.printf("Deleted");
					}
				} catch (Exception m) {
				}
				try {
					psmt = con.prepareStatement("delete FROM deletedinfo where date=?");
					psmt.setString(1, (String) val);
					ideli = psmt.executeUpdate();
				} catch (Exception n) {
				}
				if (ideli != 0 && idel != 0) {
					try {
						psmt = con.prepareStatement("SELECT *FROM sentitemdbtable where date=?");
						psmt.setString(1, (String) val);
						res = psmt.executeQuery();
						while (res.next())
							sentitemmodel.addRow(new String[]{res.getString("To"), res.getString("subject"), res.getString("date")});
					} catch (Exception e) {
					}
					int[] rowNum = deletedtable.getSelectedRows();
					for (int i = rowNum.length; i > 0; i--) {
						deletedmodel.removeRow(rowNum[i - 1]);
					}
				}
			}
		} catch (Exception e) {
			try {
			} catch (Exception b) {
			}
			System.out.printf("Error! Could not delete e-mails \n Error Location: File Name - MailServer, Line Number - 1923");
			e.printStackTrace();
		}
	}

	public void setEditableTrue() {
		inboxreply.setEnabled(true);
		inboxforward.setEnabled(true);
		inboxdelete.setEnabled(true);
		inboxdeleteall.setEnabled(true);
	}

	public void setEditableFalse() {
		inboxreply.setEnabled(false);
		inboxforward.setEnabled(false);
		inboxdelete.setEnabled(false);
		inboxdeleteall.setEnabled(false);
	}

	public static void main(String args[]) {

		new emailidreader();
		new MailServer();
	}
}
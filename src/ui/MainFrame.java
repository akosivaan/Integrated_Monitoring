package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.Server;
import models.ServerTableModel;
import services.MainService;
import utilities.DatabaseUtil;

import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JSplitPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextArea txtrFsdfsdfsd;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setResizable(false);
		setTitle("Main Window");
		DatabaseUtil.setupDatabases();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 566, 662);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 530, 319);
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);
		
		table = new JTable(new ServerTableModel());
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me){
				 JTable table =(JTable) me.getSource();
			     Point p = me.getPoint();
			     int row = table.rowAtPoint(p);
			     if (me.getClickCount() == 2 && row != -1 && SwingUtilities.isLeftMouseButton(me)) {
			    	 	int id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
			            
			            Server svr = DatabaseUtil.getServer(id);
						ServerFrameEdit sfe = new ServerFrameEdit(svr);
			            sfe.setVisible(true);
			    }
			     else if (me.getClickCount() == 2 && row != -1 && SwingUtilities.isRightMouseButton(me) && table.getModel().getValueAt(row, 3).toString().contains("FILESYSTEM")){
			    	int id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
			    	Server svr = DatabaseUtil.getServer(id);
					ServerFrameEdit sfe = new ServerFrameEdit(svr);
			    	FSFrame fframe = new FSFrame(svr);
			    	fframe.setVisible(true);
			     }
			}
		});
		scrollPane_1.setViewportView(table);
		
		
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Add Server");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerFrame sf = new ServerFrame();
				sf.setVisible(true);
			}
		});
		
		JButton btnRefreshUi = new JButton("Refresh table");
		btnRefreshUi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setServerModel();
			}
		});
		panel_2.add(btnRefreshUi);
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Execute Monitoring");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainService ms = new MainService();
				JOptionPane.showMessageDialog(null, "DONE!");
			}
		});
		panel_2.add(btnNewButton_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 341, 530, 262);
		panel.add(scrollPane);
		
		txtrFsdfsdfsd = new JTextArea();
		txtrFsdfsdfsd.setText("*Note:\r\n-Double Right-click the server to show the filesystem under it, if it is a filesystem type server.\r\n-Double Left-click the server to update/delete the server.\r\n-After adding server, click refresh table to reset the table UI.\r\n\r\n******************************************************************************************\r\nStarting here...");
		txtrFsdfsdfsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrFsdfsdfsd.setBackground(Color.BLACK);
		txtrFsdfsdfsd.setForeground(Color.WHITE);
		txtrFsdfsdfsd.setEditable(false);
		scrollPane.setViewportView(txtrFsdfsdfsd);
		setServerModel();
	}
	
	public void setServerModel(){
		ArrayList<Server> svr_l = DatabaseUtil.getServers();
		table.setModel(new ServerTableModel(svr_l));
	}
	
	public JTextArea getTextArea(){
		return txtrFsdfsdfsd;
	}
}

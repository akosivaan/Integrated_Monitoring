package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.Server;
import entity.FileSystem;
import models.FilesystemTableModel;
import utilities.DatabaseUtil;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JSeparator;

public class FSFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private Server serv;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	/**
	 * Launch the application.
	 * Ignore this.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FSFrame frame = new FSFrame(new Server());
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
	public FSFrame(Server svr) {
		this.serv = svr;
		
		setTitle("Filesystem Window");
		setBounds(100, 100, 486, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "name_167998911077771");
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.SOUTH);
		
		JButton btnAddFilesystem = new JButton("Add FileSystem");
		btnAddFilesystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.next(contentPane);
				textField.setText(serv.getId()+"");
				textField_1.setText(serv.getName());
				textField_2.setText("");
			}
		});
		panel.add(btnAddFilesystem);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		panel.add(btnExit);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable(new FilesystemTableModel());
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me){
				 JTable table =(JTable) me.getSource();
			     Point p = me.getPoint();
			     int row = table.rowAtPoint(p);
			     if (me.getClickCount() == 2 && row != -1 && SwingUtilities.isLeftMouseButton(me)) {
			    	 	int id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
			    	 	FileSystem f = DatabaseUtil.getFileSystem(id);
			    	 	textField_3.setText(f.getId()+"");
			    	 	textField_5.setText(f.getServerName());
			    	 	textField_4.setText(f.getServerId()+"");
			    	 	textField_6.setText(f.getFileSystem());
			    	 	CardLayout c1 = (CardLayout) contentPane.getLayout();
						c1.last(contentPane);
			    }
			}
		});
		scrollPane.setViewportView(table);
	
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, "name_168044016551394");
		panel_2.setLayout(null);
		
		JLabel lblFilesystemConfiguration = new JLabel("Filesystem Configuration");
		lblFilesystemConfiguration.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFilesystemConfiguration.setBounds(10, 11, 186, 16);
		panel_2.add(lblFilesystemConfiguration);
		
		JLabel label = new JLabel("");
		label.setBounds(20, 38, 146, 14);
		panel_2.add(label);
		
		JLabel lblServerId = new JLabel("Server ID");
		lblServerId.setBounds(20, 38, 46, 14);
		panel_2.add(lblServerId);
		
		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(20, 63, 146, 14);
		panel_2.add(lblServerName);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(162, 38, 86, 20);
		panel_2.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(162, 63, 288, 20);
		panel_2.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Filesystem");
		lblNewLabel.setBounds(20, 102, 128, 14);
		panel_2.add(lblNewLabel);
		
		textField_2 = new JTextField();
		textField_2.setBounds(162, 99, 288, 20);
		panel_2.add(textField_2);
		textField_2.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(22, 140, 428, 2);
		panel_2.add(separator);
		
		JButton btnNewButton = new JButton("Add FileSystem");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField_2.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "All fields are required.");
				}
				else{
					insertData();
					JOptionPane.showMessageDialog(null, "Successful insertion of filesystem");
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.first(contentPane);
					setTableModel();
				}
			}
		});
		btnNewButton.setBounds(200, 153, 120, 36);
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.first(contentPane);
			}
		});
		btnNewButton_1.setBounds(330, 153, 120, 36);
		panel_2.add(btnNewButton_1);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, "name_168046539335997");
		panel_3.setLayout(null);
		
		JLabel lblFilesystemConfiguration_1 = new JLabel("Filesystem Configuration");
		lblFilesystemConfiguration_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFilesystemConfiguration_1.setBounds(10, 11, 229, 14);
		panel_3.add(lblFilesystemConfiguration_1);
		
		JLabel lblFilesystemId = new JLabel("Filesystem ID");
		lblFilesystemId.setBounds(10, 50, 98, 14);
		panel_3.add(lblFilesystemId);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(153, 47, 86, 20);
		panel_3.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblServerId_1 = new JLabel("Server ID");
		lblServerId_1.setBounds(10, 75, 98, 14);
		panel_3.add(lblServerId_1);
		
		JLabel lblServerName_1 = new JLabel("Server Name");
		lblServerName_1.setBounds(10, 100, 98, 14);
		panel_3.add(lblServerName_1);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(153, 72, 86, 20);
		panel_3.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(153, 97, 297, 20);
		panel_3.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblFilesystem = new JLabel("FileSystem");
		lblFilesystem.setBounds(10, 125, 98, 14);
		panel_3.add(lblFilesystem);
		
		textField_6 = new JTextField();
		textField_6.setBounds(153, 128, 297, 20);
		panel_3.add(textField_6);
		textField_6.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 167, 440, 2);
		panel_3.add(separator_1);
		
		JButton btnNewButton_2 = new JButton("Delete");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this filesystem?");
				if(opt == JOptionPane.YES_OPTION){
					int fs_id = Integer.parseInt(textField_3.getText());
				    int s_id = Integer.parseInt(textField_4.getText());
				    String s_name = textField_5.getText();
				    String fs = textField_6.getText();
				    FileSystem f = new FileSystem(fs_id,s_id,s_name,fs);
				    f.deleteFS();
					JOptionPane.showMessageDialog(null, "You have successfull deleted this filesystem");
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.first(contentPane);
					setTableModel();
				}
			}
		});
		btnNewButton_2.setBounds(170, 180, 89, 37);
		panel_3.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JOptionPane.showConfirmDialog(null, "Are you sure you want to save this changes?");
				if(opt == JOptionPane.YES_OPTION){
				    int fs_id = Integer.parseInt(textField_3.getText());
				    int s_id = Integer.parseInt(textField_4.getText());
				    String s_name = textField_5.getText();
				    String fs = textField_6.getText();
				    FileSystem f = new FileSystem(fs_id,s_id,s_name,fs);
				    f.updateFS();
					JOptionPane.showMessageDialog(null, "You have successfull updated this filesystem");
					CardLayout c1 = (CardLayout) contentPane.getLayout();
					c1.first(contentPane);
					setTableModel();
				}
			}
		});
		btnNewButton_3.setBounds(267, 180, 89, 37);
		panel_3.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout c1 = (CardLayout) contentPane.getLayout();
				c1.first(contentPane);
			}
		});
		btnNewButton_4.setBounds(366, 180, 89, 37);
		panel_3.add(btnNewButton_4);
		setTableModel();
	}

	public void setTableModel(){
		ArrayList<FileSystem> fs_l = DatabaseUtil.getFileSystem(serv);
		table.setModel(new FilesystemTableModel(fs_l));
	}
	
	public void insertData(){
		int id = this.serv.getId();
		String name = this.serv.getName();
		String fs = textField_2.getText();
		
		FileSystem f = new FileSystem(id,name,fs);
		f.insertFS();
	}

}

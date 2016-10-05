package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.Server;
import entity.User;
import utilities.DatabaseUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class ServerFrameEdit extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JPasswordField passwordField;
	private JComboBox comboBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrameEdit frame = new ServerFrameEdit(new Server());
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
	public ServerFrameEdit(Server svr) {
		setResizable(false);
		setBounds(100, 100, 456, 416);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("Server Configuration");
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setBounds(10, 11, 132, 22);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Server Name");
		label_1.setBounds(10, 70, 122, 14);
		contentPane.add(label_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(138, 70, 272, 20);
		contentPane.add(textField);
		
		JLabel label_2 = new JLabel("Server Address");
		label_2.setBounds(10, 106, 122, 14);
		contentPane.add(label_2);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(138, 103, 272, 20);
		contentPane.add(textField_1);
		
		JLabel label_3 = new JLabel("Type");
		label_3.setBounds(10, 148, 46, 14);
		contentPane.add(label_3);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"FILESYSTEM - FULFILLMENT AND IMAGING", "FILESYSTEM - CLAIMS AND ADJUDICATION", "PRINTER", "OTHERS"}));
		comboBox.setBounds(138, 145, 272, 20);
		contentPane.add(comboBox);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(10, 45, 46, 14);
		contentPane.add(lblId);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(138, 42, 108, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 176, 420, 2);
		contentPane.add(separator);
		
		JLabel label_4 = new JLabel("User Configuration");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_4.setBounds(10, 189, 132, 22);
		contentPane.add(label_4);
		
		JLabel label_5 = new JLabel("Username");
		label_5.setBounds(20, 222, 122, 14);
		contentPane.add(label_5);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(138, 219, 272, 20);
		contentPane.add(textField_3);
		
		JLabel label_6 = new JLabel("Password");
		label_6.setBounds(20, 268, 122, 14);
		contentPane.add(label_6);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(138, 265, 272, 20);
		contentPane.add(passwordField);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 304, 410, 1);
		contentPane.add(separator_1);
		
		JButton btnNewButton = new JButton("Delete");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = JOptionPane.showConfirmDialog(null,"Do you really wish to delete this server?");
				if(ans == JOptionPane.YES_OPTION){
					String name = textField.getText();
					String address = textField_1.getText();
					String username = textField_3.getText();
					String password = passwordField.getText();
					String type = (String) comboBox.getSelectedItem();
					int id = Integer.parseInt(textField_2.getText());
					
					Server svr = new Server(id,name,address,type, new User(username,password));
					svr.deleteServerRecord();
					JOptionPane.showMessageDialog(null, "You have successfully deleted a server record");
					dispose();
				}
			}
		});
		btnNewButton.setBounds(138, 316, 89, 51);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = JOptionPane.showConfirmDialog(null,"Do you really wish to update this server?");
				if(ans == JOptionPane.YES_OPTION){
					String name = textField.getText();
					String address = textField_1.getText();
					String username = textField_3.getText();
					String password = passwordField.getText();
					String type = (String) comboBox.getSelectedItem();
					int id = Integer.parseInt(textField_2.getText());
					
					Server svr = new Server(id,name,address,type, new User(username,password));
					svr.updateServerRecord();
					JOptionPane.showMessageDialog(null, "You have successfully updated a server record");
					dispose();
				}
				
			}
		});
		btnNewButton_1.setBounds(237, 316, 89, 51);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Cancel");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton_2.setBounds(336, 316, 89, 51);
		contentPane.add(btnNewButton_2);
		
		setData(svr);
		if(svr.getType().equals("FILESYSTEM - FULFILLMENT AND IMAGING")){
			comboBox.setSelectedIndex(0);
		}
		else if(svr.getType().equals("FILESYSTEM - CLAIMS AND ADJUDICATION")){
			comboBox.setSelectedIndex(1);
		}
		else if(svr.getType().equals("PRINTER")){
			comboBox.setSelectedIndex(2);
		}
		else{
			comboBox.setSelectedIndex(3);
		}
	}
	
	public void setData(Server svr){
		textField.setText(svr.getName());
		textField_1.setText(svr.getAddress());
		textField_2.setText(svr.getId()+"");
		textField_3.setText(svr.getUser().getUsername());
		passwordField.setText(svr.getUser().getPassword());
	}
}

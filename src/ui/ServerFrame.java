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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
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
	public ServerFrame() {
		setResizable(false);
		setTitle("Server Window");
		setBounds(100, 100, 446, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblServerConfiguration = new JLabel("Server Configuration");
		lblServerConfiguration.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblServerConfiguration.setBounds(10, 11, 132, 22);
		contentPane.add(lblServerConfiguration);
		
		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(20, 44, 122, 14);
		contentPane.add(lblServerName);
		
		textField = new JTextField();
		lblServerName.setLabelFor(textField);
		textField.setBounds(148, 44, 272, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Server Address");
		lblNewLabel.setBounds(20, 80, 122, 14);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		lblNewLabel.setLabelFor(textField_1);
		textField_1.setBounds(148, 77, 272, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(20, 122, 46, 14);
		contentPane.add(lblType);
		
		final JComboBox comboBox = new JComboBox();
		lblType.setLabelFor(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"FILESYSTEM - FULFILLMENT AND IMAGING", "FILESYSTEM - CLAIMS AND ADJUDICATION", "PRINTER", "OTHERS"}));
		comboBox.setBounds(148, 119, 272, 20);
		contentPane.add(comboBox);
		
		JLabel lblUserConfiguration = new JLabel("User Configuration");
		lblUserConfiguration.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblUserConfiguration.setBounds(10, 170, 132, 22);
		contentPane.add(lblUserConfiguration);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(20, 203, 122, 14);
		contentPane.add(lblNewLabel_1);
		
		textField_2 = new JTextField();
		lblNewLabel_1.setLabelFor(textField_2);
		textField_2.setBounds(148, 200, 272, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(20, 249, 122, 14);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		lblPassword.setLabelFor(passwordField);
		passwordField.setBounds(148, 246, 272, 20);
		contentPane.add(passwordField);
		
		JButton btnAddServer = new JButton("Add Server");
		btnAddServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().isEmpty() || textField_1.getText().isEmpty() || textField_2.getText().isEmpty() || passwordField.getText().isEmpty()){
					JOptionPane.showMessageDialog(null,"All fields are required to be filled up");
				}
				else{
					String name = textField.getText();
					String address = textField_1.getText();
					String type = (String) comboBox.getSelectedItem();
					String username = textField_2.getText();
					String password = passwordField.getText();
					System.out.println(type);
					Server svr = new Server(name,address,type,new User(username,password));
					DatabaseUtil.addServer(svr);
					//svr.insertServerRecord();
					JOptionPane.showMessageDialog(null, "Successfully added a server record in the database");
					dispose();
				}
			}
		});
		btnAddServer.setBounds(173, 297, 122, 51);
		contentPane.add(btnAddServer);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(298, 297, 122, 51);
		contentPane.add(btnCancel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 159, 410, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 285, 410, 1);
		contentPane.add(separator_1);
	}
}

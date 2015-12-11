import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AdminFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_8;

	/**
	 * Create the frame.
	 */
	public AdminFrame() {
		setTitle("Admin Panel");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 200, 540, 500);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel form = new JPanel();
		form.setBackground(Color.GRAY);
		form.setBounds(0, 0, 542, 132);
		contentPane.add(form);
		form.setLayout(null);

		JLabel lblWelcomeAdmin = new JLabel("Welcome Admin!");
		lblWelcomeAdmin.setBounds(211, 6, 137, 20);
		lblWelcomeAdmin.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		form.add(lblWelcomeAdmin);

		JButton btnLog = new JButton("Log Off");
		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							dispose();
							LoginFrame frame = new LoginFrame();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}
		});
		btnLog.setBounds(102, 97, 117, 29);
		form.add(btnLog);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(291, 97, 117, 29);
		form.add(btnExit);

		JPanel cardPanel = new JPanel();
		cardPanel.setBounds(0, 130, 542, 359);
		contentPane.add(cardPanel);
		cardPanel.setLayout(new CardLayout(0, 0));

		JPanel NewAccount = new JPanel();
		NewAccount.setBackground(Color.WHITE);
		cardPanel.add(NewAccount, "name_202325691618108");
		NewAccount.setLayout(null);

		JLabel lblAccountNumber = new JLabel("Account Number :");
		lblAccountNumber.setBounds(39, 58, 131, 16);
		NewAccount.add(lblAccountNumber);

		JLabel lblNewAccountDetails = new JLabel("New Account Details");
		lblNewAccountDetails.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewAccountDetails.setBounds(195, 6, 161, 16);
		NewAccount.add(lblNewAccountDetails);

		JLabel lblFullName = new JLabel("Full Name :");
		lblFullName.setBounds(39, 98, 131, 16);
		NewAccount.add(lblFullName);

		JLabel lblCheckingBalance = new JLabel("Checking Balance :");
		lblCheckingBalance.setBounds(39, 139, 131, 16);
		NewAccount.add(lblCheckingBalance);

		JLabel lblSavingBalance = new JLabel("Saving Balance :");
		lblSavingBalance.setBounds(39, 186, 131, 16);
		NewAccount.add(lblSavingBalance);

		JLabel lblLoginPassword = new JLabel("Login Password :");
		lblLoginPassword.setBounds(39, 234, 131, 16);
		NewAccount.add(lblLoginPassword);

		textField = new JTextField();
		textField.setBounds(229, 52, 241, 28);
		NewAccount.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(229, 92, 241, 28);
		NewAccount.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(229, 133, 241, 28);
		NewAccount.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(229, 180, 241, 28);
		NewAccount.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(229, 228, 241, 28);
		NewAccount.add(textField_4);

		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(75, 297, 117, 29);
		NewAccount.add(btnCreate);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					String data = "3::" + "New Account::" + textField.getText()
							+ "::" + textField_1.getText() + "::"
							+ textField_2.getText() + "::"
							+ textField_3.getText() + "::"
							+ textField_4.getText();
					if (textField.getText().equals("")
							|| textField_1.getText().equals("")
							|| textField_2.getText().equals("")
							|| textField_3.getText().equals("")
							|| textField_4.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"All feilds are compulsary!", "Success",
								JOptionPane.PLAIN_MESSAGE);
					} else {

						DataObject myObject = new DataObject();

						myObject.setMessage(data);

						System.out.println("Message sent : "
								+ myObject.getMessage());

						Socket socketToServer = new Socket(
								"afsaccess1.njit.edu", 3090);
						ObjectOutputStream myOutputStream = new ObjectOutputStream(
								socketToServer.getOutputStream());

						ObjectInputStream myInputStream = new ObjectInputStream(
								socketToServer.getInputStream());

						myOutputStream.writeObject(myObject);

						myObject = (DataObject) myInputStream.readObject();

						String msg = myObject.getMessage();

						String userData[] = msg.split("::");

						System.out
								.println("Messaged received : " + userData[0]);

						if (userData[0].equals("User Created"))

						{
							JOptionPane.showMessageDialog(null,
									"New User Created Successfully", "Success",
									JOptionPane.PLAIN_MESSAGE);

						} else if (userData[0].equals("User already Exists")) {
							JOptionPane.showMessageDialog(null,
									"User already Exists, change the Name",
									"Success", JOptionPane.PLAIN_MESSAGE);
						}

						myOutputStream.close();

						myInputStream.close();

						socketToServer.close();

					}

				} catch (NumberFormatException e11) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
			}
		});

		JButton btnReferesh = new JButton("Refresh");
		btnReferesh.setBounds(296, 297, 117, 29);
		NewAccount.add(btnReferesh);
		btnReferesh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");

			}
		});

		JPanel ViewAll = new JPanel();
		cardPanel.add(ViewAll, "name_202495069022514");
		ViewAll.setLayout(null);

		JLabel lblNewLabel = new JLabel("View Accounts");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewLabel.setBounds(220, 24, 120, 23);
		ViewAll.add(lblNewLabel);

		JLabel UserName1 = new JLabel("Account Number :");
		UserName1.setBounds(69, 100, 131, 16);
		ViewAll.add(UserName1);

		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(229, 95, 241, 28);
		ViewAll.add(textField_8);

		JButton btnReferesh2 = new JButton("Refresh");
		btnReferesh2.setBounds(296, 230, 117, 29);
		ViewAll.add(btnReferesh2);
		btnReferesh2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_8.setText("");

			}
		});

		JButton btnCreate2 = new JButton("View");
		btnCreate2.setBounds(75, 230, 117, 29);
		ViewAll.add(btnCreate2);
		btnCreate2.addActionListener(new ActionListener() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void actionPerformed(ActionEvent e) {

				try {

					int view = Integer.parseInt(textField_8.getText());

					String data = "3::" + "View Account::" + view;
					if (textField_8.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please insert User Name!", "Success",
								JOptionPane.PLAIN_MESSAGE);
					} else {

						DataObject myObject = new DataObject();

						myObject.setMessage(data);

						System.out.println("Message sent : "
								+ myObject.getMessage());

						Socket socketToServer = new Socket(
								"afsaccess1.njit.edu", 3090);
						ObjectOutputStream myOutputStream = new ObjectOutputStream(
								socketToServer.getOutputStream());

						ObjectInputStream myInputStream = new ObjectInputStream(
								socketToServer.getInputStream());

						myOutputStream.writeObject(myObject);

						myObject = (DataObject) myInputStream.readObject();

						String msg = myObject.getMessage();

						String userData[] = msg.split("::");

						System.out
								.println("Messaged received : " + userData[0]);

						if (userData[0].equals("User Not found")) {
							JOptionPane.showMessageDialog(null,
									"User not found", "Success",
									JOptionPane.PLAIN_MESSAGE);

						} else if (userData[0].equals("View Successful")) {

							String item = "User Name: " + userData[1] + "::"
									+ "Checking Balance: " + userData[2] + "::"
									+ "Saving Balance: " + userData[3];
							String[] items = item.split("::");

							JList list = new JList(items);
							JPanel panel = new JPanel();
							panel.add(list);
							JOptionPane.showMessageDialog(null, panel,
									"Success", JOptionPane.PLAIN_MESSAGE);

						}

						myOutputStream.close();

						myInputStream.close();

						socketToServer.close();

					}

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}

				textField_8.setText("");
			}
		});

		JPanel FreezeAccount = new JPanel();
		FreezeAccount.setBackground(Color.WHITE);
		cardPanel.add(FreezeAccount, "name_202500424560148");
		FreezeAccount.setLayout(null);

		JLabel lblFreezeAccount = new JLabel("Freeze Account");
		lblFreezeAccount.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblFreezeAccount.setBounds(209, 17, 128, 29);
		FreezeAccount.add(lblFreezeAccount);

		JLabel UserName = new JLabel("Account Number :");
		UserName.setBounds(69, 120, 131, 16);
		FreezeAccount.add(UserName);

		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(229, 115, 241, 28);
		FreezeAccount.add(textField_5);

		JButton btnReferesh1 = new JButton("Refresh");
		btnReferesh1.setBounds(296, 260, 117, 29);
		FreezeAccount.add(btnReferesh1);
		btnReferesh1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_5.setText("");
			}
		});

		JButton btnCreate1 = new JButton("Freeze");
		btnCreate1.setBounds(75, 260, 117, 29);
		FreezeAccount.add(btnCreate1);
		btnCreate1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					int freeze = Integer.parseInt(textField_5.getText());

					String data = "3::" + "Freeze Account::" + freeze;
					if (textField_5.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please insert Account Number!", "Success",
								JOptionPane.PLAIN_MESSAGE);
					} else {

						DataObject myObject = new DataObject();

						myObject.setMessage(data);

						System.out.println("Message sent : "
								+ myObject.getMessage());

						Socket socketToServer = new Socket(
								"afsaccess1.njit.edu", 3090);
						ObjectOutputStream myOutputStream = new ObjectOutputStream(
								socketToServer.getOutputStream());

						ObjectInputStream myInputStream = new ObjectInputStream(
								socketToServer.getInputStream());

						myOutputStream.writeObject(myObject);

						myObject = (DataObject) myInputStream.readObject();

						String msg = myObject.getMessage();

						String userData[] = msg.split("::");

						System.out
								.println("Messaged received : " + userData[0]);

						if (userData[0].equals("User Not found")) {
							JOptionPane.showMessageDialog(null,
									"User not found", "Success",
									JOptionPane.PLAIN_MESSAGE);

						} else if (userData[0].equals("Freeze Successful")) {
							JOptionPane.showMessageDialog(null,
									"User Freezed Successfully!", "Success",
									JOptionPane.PLAIN_MESSAGE);
						}

						myOutputStream.close();

						myInputStream.close();

						socketToServer.close();
						textField_5.setText("");

					}

				} catch (NumberFormatException e11) {
					JOptionPane.showMessageDialog(null, "Invalid input");
					textField_5.setText("");

				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}

			}
		});

		JButton btnCreate3 = new JButton("UN Freeze");
		btnCreate3.setBounds(75, 290, 117, 29);
		FreezeAccount.add(btnCreate3);
		btnCreate3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int unFreeze = Integer.parseInt(textField_5.getText());

					String data = "3::" + "UN Freeze Account::" + unFreeze;
					if (textField_5.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please insert Account Number!", "Success",
								JOptionPane.PLAIN_MESSAGE);
					} else {

						DataObject myObject = new DataObject();

						myObject.setMessage(data);

						System.out.println("Message sent : "
								+ myObject.getMessage());

						Socket socketToServer = new Socket(
								"afsaccess1.njit.edu", 3090);
						ObjectOutputStream myOutputStream = new ObjectOutputStream(
								socketToServer.getOutputStream());

						ObjectInputStream myInputStream = new ObjectInputStream(
								socketToServer.getInputStream());

						myOutputStream.writeObject(myObject);

						myObject = (DataObject) myInputStream.readObject();

						String msg = myObject.getMessage();

						String userData[] = msg.split("::");

						System.out
								.println("Messaged received : " + userData[0]);

						if (userData[0].equals("User Not found")) {
							JOptionPane.showMessageDialog(null,
									"User not found", "Success",
									JOptionPane.PLAIN_MESSAGE);

						} else if (userData[0].equals("UN Freeze Successful")) {
							JOptionPane.showMessageDialog(null,
									"User Un Freezed Successfully!", "Success",
									JOptionPane.PLAIN_MESSAGE);
						}

						myOutputStream.close();

						myInputStream.close();

						socketToServer.close();
						textField_5.setText("");

					}

				} catch (NumberFormatException e11) {
					JOptionPane.showMessageDialog(null, "Invalid input");
					textField_5.setText("");

				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}

			}
		});

		JButton btnCreateNewAccount = new JButton("Create New Account");
		btnCreateNewAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(NewAccount);
				cardPanel.repaint();
				cardPanel.revalidate();
			}
		});
		btnCreateNewAccount.setBounds(24, 38, 152, 29);
		form.add(btnCreateNewAccount);

		JButton btnViewAllAccounts = new JButton("View Account");
		btnViewAllAccounts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cardPanel.removeAll();
				cardPanel.add(ViewAll);
				cardPanel.repaint();
				cardPanel.revalidate();

			}
		});
		btnViewAllAccounts.setBounds(206, 38, 152, 29);
		form.add(btnViewAllAccounts);

		JButton btnShow = new JButton("Freeze Account");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardPanel.removeAll();
				cardPanel.add(FreezeAccount);
				cardPanel.repaint();
				cardPanel.revalidate();

			}
		});
		btnShow.setBounds(375, 38, 137, 29);
		form.add(btnShow);

	}
}

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
import javax.swing.border.EmptyBorder;

public class UserFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Socket socket;

	/**
	 * Create the frame.
	 * 
	 * @param userName
	 * @param savingBal
	 * @param checkingBal
	 */
	public UserFrame(String userName, String checkingBal, String savingBal) {
		setTitle(userName + "'s Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 551, 506);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel WelcomeUser = new JLabel("Welcome  " + userName + "!");
		WelcomeUser.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		WelcomeUser.setLabelFor(WelcomeUser);
		WelcomeUser.setBounds(40, 18, 337, 60);
		contentPane.add(WelcomeUser);

		JLabel lblChecking = new JLabel("Checking Account ==>\n");
		lblChecking.setBounds(40, 107, 173, 53);
		contentPane.add(lblChecking);

		JLabel lblNewLabel = new JLabel("Saving Account ==>");
		lblNewLabel.setBounds(40, 245, 173, 39);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel(checkingBal + " $");
		lblNewLabel_1.setBounds(289, 125, 94, 16);
		contentPane.add(lblNewLabel_1);

		JLabel label = new JLabel(savingBal + " $");
		label.setBounds(289, 256, 94, 16);
		contentPane.add(label);

		JButton btnNewButton0 = new JButton("Account History");
		btnNewButton0.addActionListener(new ActionListener() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void actionPerformed(ActionEvent e) {
				try {

					String data = "2::" + "Account History" + "::" + userName;

					DataObject myObject = new DataObject();

					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);

					if (userData[0].equals("History"))

					{
						String item = userData[1];
						String[] items = item.split(",");

						JList list = new JList(items);
						JPanel panel = new JPanel();
						panel.add(list);
						JOptionPane.showMessageDialog(null, panel, "Success",
								JOptionPane.PLAIN_MESSAGE);
					}
					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{
					System.out.println(e1.getMessage());
					System.exit(0);
				}

			}
		});
		btnNewButton0.setBounds(400, 18, 130, 60);
		contentPane.add(btnNewButton0);

		JButton btnNewButton = new JButton("Deposit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String DepositAmount = JOptionPane.showInputDialog(
						"Please Enter the amount to Deposit :", "0");

				try {

					int Deposit = Integer.parseInt(DepositAmount);

					String data = "2::" + "Checking_Deposit::" + Deposit + "::"
							+ userName + "::" + checkingBal;

					DataObject myObject = new DataObject();

					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);

					if (userData[0].equals("Transaction Successful"))

					{
						JOptionPane.showMessageDialog(null,
								"Transaction Successful, New Balance = "
										+ userData[1], "Success",
								JOptionPane.PLAIN_MESSAGE);
						dispose();
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									UserFrame frame = new UserFrame(userName,
											userData[1], savingBal);

									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");

				} catch (Exception e1)

				{
					System.out.println(e1.getMessage());
					System.exit(0);
				}

			}
		});
		btnNewButton.setBounds(40, 172, 117, 29);
		contentPane.add(btnNewButton);

		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String WithdrawAmount = JOptionPane.showInputDialog(
						"Please Enter the amount to Withdraw :", "0");
				try {
					int Withdraw = Integer.parseInt(WithdrawAmount);

					String data = "2::" + "Checking_Withdraw::" + Withdraw
							+ "::" + userName + "::" + checkingBal;

					DataObject myObject = new DataObject();

					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);

					if (userData[0].equals("Transaction Successful"))

					{
						JOptionPane.showMessageDialog(null,
								"Transaction Successful, New Balance = "
										+ userData[1], "Success",
								JOptionPane.PLAIN_MESSAGE);

						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									UserFrame frame = new UserFrame(userName,
											userData[1], savingBal);

									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

						dispose();
					}

					else if (userData[0].equals("Balance Low")) {
						JOptionPane.showMessageDialog(null,
								"Transaction fail, Balance low ", "Success",
								JOptionPane.PLAIN_MESSAGE);
					}

					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				}

				catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}

			}
		});
		btnWithdraw.setBounds(189, 172, 117, 29);
		contentPane.add(btnWithdraw);

		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String TransferAmount = JOptionPane.showInputDialog(
						"Please Enter the amount to transfer :", "0");
				try {

					int Transfer = Integer.parseInt(TransferAmount);

					String data = "2::" + "Checking_Transfer::" + Transfer
							+ "::" + userName + "::" + checkingBal + "::"
							+ savingBal;

					DataObject myObject = new DataObject();
					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);

					if (userData[0].equals("Transaction Successful"))

					{
						JOptionPane.showMessageDialog(null,
								"Transaction Successful, New Checking Balance = "
										+ userData[1], "Success",
								JOptionPane.PLAIN_MESSAGE);

						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									UserFrame frame = new UserFrame(userName,
											userData[1], userData[2]);
									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

						dispose();
					} else if (userData[0].equals("Balance Low")) {
						JOptionPane.showMessageDialog(null,
								"Transaction fail, Balance low ", "Success",
								JOptionPane.PLAIN_MESSAGE);
					}

					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}
			}
		});
		btnTransfer.setBounds(351, 172, 117, 29);
		contentPane.add(btnTransfer);

		JButton button = new JButton("Withdraw");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String WithdrawAmount = JOptionPane.showInputDialog(
						"Please Enter the amount to Withdraw :", "0");
				try {
					int Withdraw = Integer.parseInt(WithdrawAmount);

					String data = "2::" + "Saving_Withdraw::" + Withdraw + "::"
							+ userName + "::" + savingBal;

					DataObject myObject = new DataObject();

					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);

					if (userData[0].equals("Transaction Successful"))

					{
						JOptionPane.showMessageDialog(null,
								"Transaction Successful, New Balance = "
										+ userData[1], "Success",
								JOptionPane.PLAIN_MESSAGE);

						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									UserFrame frame = new UserFrame(userName,
											checkingBal, userData[1]);

									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

						dispose();
					}

					else if (userData[0].equals("Balance Low")) {
						JOptionPane.showMessageDialog(null,
								"Transaction fail, Balance low ", "Success",
								JOptionPane.PLAIN_MESSAGE);
					}

					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}

			}
		});
		button.setBounds(189, 326, 117, 29);
		contentPane.add(button);

		JButton button_1 = new JButton("Transfer");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String TransferAmount = JOptionPane.showInputDialog(
						"Please Enter the amount to Transfer :", "0");

				try {

					int Transfer = Integer.parseInt(TransferAmount);

					String data = "2::" + "Saving_Transfer::" + Transfer + "::"
							+ userName + "::" + checkingBal + "::" + savingBal;

					DataObject myObject = new DataObject();
					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);

					if (userData[0].equals("Transaction Successful"))

					{
						JOptionPane.showMessageDialog(null,
								"Transaction Successful, New Saving Balance = "
										+ userData[2], "Success",
								JOptionPane.PLAIN_MESSAGE);

						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									UserFrame frame = new UserFrame(userName,
											userData[1], userData[2]);
									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

						dispose();
					} else if (userData[0].equals("Balance Low")) {
						JOptionPane.showMessageDialog(null,
								"Transaction fail, Balance low ", "Success",
								JOptionPane.PLAIN_MESSAGE);
					}

					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{

					System.out.println(e1.getMessage());
					System.exit(0);

				}
			}
		});
		button_1.setBounds(351, 326, 117, 29);
		contentPane.add(button_1);

		JButton button_2 = new JButton("Deposit");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String DepositAmount = JOptionPane.showInputDialog(
						"Please Enter the amount to Deposit :", "0");
				try {
					int Deposit = Integer.parseInt(DepositAmount);

					String data = "2::" + "Saving_Deposit::" + Deposit + "::"
							+ userName + "::" + savingBal;

					DataObject myObject = new DataObject();

					myObject.setMessage(data);

					System.out.println("Message sent : "
							+ myObject.getMessage());

					Socket socketToServer = new Socket("afsaccess1.njit.edu",
							3090);
					ObjectOutputStream myOutputStream = new ObjectOutputStream(
							socketToServer.getOutputStream());

					ObjectInputStream myInputStream = new ObjectInputStream(
							socketToServer.getInputStream());

					myOutputStream.writeObject(myObject);

					myObject = (DataObject) myInputStream.readObject();

					String msg = myObject.getMessage();

					String userData[] = msg.split("::");

					System.out.println("Messaged received : " + userData[0]);
					if (userData[0].equals("Transaction Successful"))

					{
						JOptionPane.showMessageDialog(null,
								"Transaction Successful, New Balance = "
										+ userData[1], "Success",
								JOptionPane.PLAIN_MESSAGE);
						dispose();
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									UserFrame frame = new UserFrame(userName,
											checkingBal, userData[1]);
									frame.setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
					myOutputStream.close();

					myInputStream.close();

					socketToServer.close();

				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				} catch (Exception e1)

				{
					System.out.println(e1.getMessage());
					System.exit(0);
				}
			}
		});
		button_2.setBounds(40, 326, 117, 29);
		contentPane.add(button_2);

		JButton btnLogOff = new JButton("Log Off");
		btnLogOff.addActionListener(new ActionListener() {
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
		btnLogOff.setBounds(109, 406, 117, 29);
		contentPane.add(btnLogOff);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(333, 406, 117, 29);
		contentPane.add(btnExit);
	}
}

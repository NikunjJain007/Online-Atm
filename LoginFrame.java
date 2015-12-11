import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField UserName;
	private JPasswordField Password;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Socket socket;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setTitle("Login Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 250, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel UserNameLabel = new JLabel("User Name*");
		UserNameLabel.setBackground(Color.DARK_GRAY);
		UserNameLabel.setVerticalAlignment(SwingConstants.TOP);
		UserNameLabel.setBounds(79, 96, 95, 34);
		contentPane.add(UserNameLabel);

		JLabel LoginPageLabel = new JLabel("Please enter your credentials");
		LoginPageLabel.setBounds(150, 40, 200, 16);
		contentPane.add(LoginPageLabel);

		JLabel WelcomeLabel = new JLabel("Welcome to the Online ATM ");
		WelcomeLabel.setBounds(150, 10, 200, 16);
		contentPane.add(WelcomeLabel);

		UserName = new JTextField();
		UserName.setBounds(202, 90, 134, 28);
		contentPane.add(UserName);
		UserName.setColumns(10);

		JLabel PasswordLabel = new JLabel("Password*");
		PasswordLabel.setBounds(78, 159, 79, 34);
		contentPane.add(PasswordLabel);

		Password = new JPasswordField();
		Password.setBounds(202, 162, 134, 28);
		contentPane.add(Password);
		Password.setColumns(10);

		JButton OKButton = new JButton("OK");
		OKButton.setBounds(57, 229, 117, 29);
		contentPane.add(OKButton);

		JButton CancelButton = new JButton("Cancel");
		CancelButton.setBounds(224, 229, 117, 29);
		contentPane.add(CancelButton);

		OKButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				String userName = UserName.getText().trim();
				@SuppressWarnings("deprecation")
				String text = Password.getText();
				String password = text.trim();

				if (userName.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Both User Name and Password are required ",
							"Try again", JOptionPane.PLAIN_MESSAGE);
				} else {

					try {
						DataObject myObject = new DataObject();

						@SuppressWarnings("deprecation")
						String data = "1::" + UserName.getText() + "::"
								+ Password.getText();

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

						if (userName.equals("Admin")
								&& password.equals("Password")) {

							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										JOptionPane.showMessageDialog(null,
												"Welcome Admin! ", "Success",
												JOptionPane.PLAIN_MESSAGE);

										dispose();
										AdminFrame frame = new AdminFrame();
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}

						else if (userData[0].equals("Login Successful")) {
							JOptionPane.showMessageDialog(null,
									"Login Successful! ", "Success",
									JOptionPane.PLAIN_MESSAGE);

							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										UserFrame frame = new UserFrame(
												userName, userData[1],
												userData[2]);

										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

							dispose();

						} else if (userData[0].equals("User Not found")) {
							JOptionPane.showMessageDialog(null,
									"User Not found!... Please try again",
									"Error", 1);

						} else if (userData[0].equals("User Feezed")) {
							JOptionPane.showMessageDialog(null,
									"User Freezed, Please contact the Admin",
									"Error", 1);

						}

						else if (userData[0].equals("Invalid Password")) {
							JOptionPane.showMessageDialog(null,
									"Invalid Password!... Please try again",
									"Error", 1);

						}

						else

						{

							// show message if login password doesn't match
							// with
							// the user Id

							JOptionPane.showMessageDialog(null,
									"Login Unsuccessful!... Please try again",
									"Error", 1);

						}

						myOutputStream.close();

						myInputStream.close();

						socketToServer.close();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"Server Down, please try again later", "Error",
								1);
						System.out.println(e1.getMessage());
					}

				}

			}

		});

		CancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				System.exit(0);

			}

		});
	}
}

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ThreadedDataObjectServer {
	public static void main(String[] args) {

		try {
			@SuppressWarnings("resource")
			ServerSocket myServerSocket = new ServerSocket(3090);

			for (;;) {
				Socket incoming = myServerSocket.accept();
				new ThreadedDataObjectHandler(incoming).start();

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

class ThreadedDataObjectHandler extends Thread {
	String connectionURL = "jdbc:mysql://sql1.njit.edu:3306/nj86";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ThreadedDataObjectHandler(Socket i) {
		incoming = i;
	}

	public void run() {
		try {

			ObjectOutputStream myOutputStream = new ObjectOutputStream(
					incoming.getOutputStream());
			ObjectInputStream myInputStream = new ObjectInputStream(
					incoming.getInputStream());

			myObject = (DataObject) myInputStream.readObject();

			String msg = myObject.getMessage();

			String userData[] = msg.split("::");
			if (userData[0].equals("1")) {
				System.out.println("Message received : " + userData[1]
						+ "Password -> " + userData[2]);

				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection(connectionURL, "nj86",
						"Qg0dLUtv");
				statement = connection.createStatement();
				rs = statement
						.executeQuery("SELECT * FROM User_Info where Full_Name= '"
								+ userData[1] + "'");

				if (!rs.next()) {
					myObject.setMessage("User Not found::" + "0::" + "0");
				} else if (rs.getString("Password").equals(userData[2])) {
					if (rs.getString("Freeze").equals("1")) {
						myObject.setMessage("User Feezed::" + "0::" + "0");
					} else {
						String Check_Bal = rs.getString("Checking_Balance");
						String Saving_Bal = rs.getString("Saving_Balance");
						String data = "Login Successful::" + Check_Bal + "::"
								+ Saving_Bal;
						myObject.setMessage(data);
					}
				} else {
					myObject.setMessage("Invalid Password::" + "0::" + "0");
				}

				rs.close();
				String msg1 = myObject.getMessage();
				String userData1[] = msg1.split("::");
				System.out.println("Message sent : " + userData1[0]);

				myOutputStream.writeObject(myObject);

				myOutputStream.close();
				myInputStream.close();
				incoming.close();
			} else if (userData[0].equals("2")) {
				System.out.println("Message received : " + userData[1] + " "
						+ "Amount -> " + userData[2]);

				if (userData[1].equals("Account History")) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					statement = connection.createStatement();
					rs = statement
							.executeQuery("SELECT * FROM Transaction where User_Name= '"
									+ userData[2] + "'");
					List<String> a = new ArrayList<String>();
					while (rs.next()) {
						a.add(rs.getString("Info"));

					}
					String data = "History::" + a;
					myObject.setMessage(data);
					rs.close();
					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);

					myOutputStream.writeObject(myObject);

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("Checking_Deposit")) {
					int Deposit_Amnt = Integer.parseInt(userData[2]);
					int new_checking_balance1 = Integer.parseInt(userData[4]);

					int new_checking_balance2 = (Deposit_Amnt + new_checking_balance1);

					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");

					String query = "update User_Info set Checking_Balance = ? where Full_Name = ?";
					PreparedStatement preparedStmt = connection
							.prepareStatement(query);
					preparedStmt.setInt(1, new_checking_balance2);
					preparedStmt.setString(2, userData[3]);
					preparedStmt.executeUpdate();
					String data = "Transaction Successful::"
							+ new_checking_balance2 + "::" + "AD";
					myObject.setMessage(data);
					preparedStmt.close();

					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");

					String query1 = "INSERT INTO Transaction (Info, User_Name) VALUES (?,?)";

					PreparedStatement preparedStmt1 = connection
							.prepareStatement(query1);
					String x = Integer.toString(Deposit_Amnt);

					String data2 = "Deposit " + x + "$ to " + "Saving";

					preparedStmt1.setString(1, data2);
					preparedStmt1.setString(2, userData[3]);
					preparedStmt1.executeUpdate();
					preparedStmt1.close();
					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);

					myOutputStream.writeObject(myObject);

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("Checking_Withdraw")) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					int withdrawAmt = Integer.parseInt(userData[2]);
					int checking_bal = Integer.parseInt(userData[4]);
					if (withdrawAmt <= checking_bal) {
						String query = "update User_Info set Checking_Balance = ? where Full_Name = ?";
						PreparedStatement preparedStmt = connection
								.prepareStatement(query);
						int new_checking_balance1 = (checking_bal - withdrawAmt);
						preparedStmt.setInt(1, new_checking_balance1);
						preparedStmt.setString(2, userData[3]);
						preparedStmt.executeUpdate();
						preparedStmt.close();
						String data = "Transaction Successful::"
								+ new_checking_balance1 + "::" + "CD";
						myObject.setMessage(data);
						Class.forName("com.mysql.jdbc.Driver").newInstance();
						connection = DriverManager.getConnection(connectionURL,
								"nj86", "Qg0dLUtv");

						String query1 = "INSERT INTO Transaction (Info, User_Name) VALUES (?,?)";

						PreparedStatement preparedStmt1 = connection
								.prepareStatement(query1);
						String x = Integer.toString(withdrawAmt);

						String data2 = "Withdraw " + x + "$ from " + "Checking";

						preparedStmt1.setString(1, data2);
						preparedStmt1.setString(2, userData[3]);
						preparedStmt1.executeUpdate();
						preparedStmt1.close();

						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);

						myOutputStream.writeObject(myObject);
					} else {
						String data = "Balance Low::" + checking_bal + "::"
								+ "CD";
						myObject.setMessage(data);
						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);

						myOutputStream.writeObject(myObject);

					}

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("Checking_Transfer")) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					int TransferAmt = Integer.parseInt(userData[2]);
					int checking_bal = Integer.parseInt(userData[4]);
					int saving_bal = Integer.parseInt(userData[5]);
					if (TransferAmt <= checking_bal) {
						String query = "update User_Info set Checking_Balance = ?, Saving_Balance = ? where Full_Name = ?";
						PreparedStatement preparedStmt = connection
								.prepareStatement(query);
						int new_checking_balance1 = (checking_bal - TransferAmt);
						int new_saving_bal = saving_bal + TransferAmt;
						preparedStmt.setInt(1, new_checking_balance1);
						preparedStmt.setInt(2, new_saving_bal);
						preparedStmt.setString(3, userData[3]);
						preparedStmt.executeUpdate();
						preparedStmt.close();

						String data = "Transaction Successful::"
								+ new_checking_balance1 + "::" + new_saving_bal
								+ "::" + "CT";
						myObject.setMessage(data);

						Class.forName("com.mysql.jdbc.Driver").newInstance();
						connection = DriverManager.getConnection(connectionURL,
								"nj86", "Qg0dLUtv");

						String query1 = "INSERT INTO Transaction (Info, User_Name) VALUES (?,?)";

						PreparedStatement preparedStmt1 = connection
								.prepareStatement(query1);
						String x = Integer.toString(TransferAmt);

						String data2 = "Transfer " + x + "$ to " + "Saving";

						preparedStmt1.setString(1, data2);
						preparedStmt1.setString(2, userData[3]);
						preparedStmt1.executeUpdate();
						preparedStmt1.close();

						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);
						myOutputStream.writeObject(myObject);
					} else {
						String data = "Balance Low::" + checking_bal + "::"
								+ saving_bal + "::" + "CT";
						myObject.setMessage(data);
						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);

						myOutputStream.writeObject(myObject);

					}

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("Saving_Deposit")) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					int Deposit_Amnt = Integer.parseInt(userData[2]);
					int new_checking_balance1 = Integer.parseInt(userData[4]);
					int new_checking_balance2 = (Deposit_Amnt + new_checking_balance1);
					String query = "update User_Info set Saving_Balance = ? where Full_Name = ?";
					PreparedStatement preparedStmt = connection
							.prepareStatement(query);
					preparedStmt.setInt(1, new_checking_balance2);
					preparedStmt.setString(2, userData[3]);
					preparedStmt.executeUpdate();
					preparedStmt.close();

					String data = "Transaction Successful::"
							+ new_checking_balance2 + "::" + "AS";
					myObject.setMessage(data);
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");

					String query1 = "INSERT INTO Transaction (Info, User_Name) VALUES (?,?)";

					PreparedStatement preparedStmt1 = connection
							.prepareStatement(query1);
					String x = Integer.toString(Deposit_Amnt);

					String data2 = "Deposit " + x + "$ to " + "Saving";

					preparedStmt1.setString(1, data2);
					preparedStmt1.setString(2, userData[3]);
					preparedStmt1.executeUpdate();
					preparedStmt1.close();

					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);

					myOutputStream.writeObject(myObject);
					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("Saving_Withdraw")) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					int withdrawAmt = Integer.parseInt(userData[2]);
					int checking_bal = Integer.parseInt(userData[4]);
					if (withdrawAmt <= checking_bal) {
						String query = "update User_Info set Saving_Balance = ? where Full_Name = ?";
						PreparedStatement preparedStmt = connection
								.prepareStatement(query);
						int new_checking_balance1 = (checking_bal - withdrawAmt);
						preparedStmt.setInt(1, new_checking_balance1);
						preparedStmt.setString(2, userData[3]);
						preparedStmt.executeUpdate();
						preparedStmt.close();

						String data = "Transaction Successful::"
								+ new_checking_balance1 + "::" + "CD";
						myObject.setMessage(data);

						Class.forName("com.mysql.jdbc.Driver").newInstance();
						connection = DriverManager.getConnection(connectionURL,
								"nj86", "Qg0dLUtv");

						String query1 = "INSERT INTO Transaction (Info, User_Name) VALUES (?,?)";

						PreparedStatement preparedStmt1 = connection
								.prepareStatement(query1);
						String x = Integer.toString(withdrawAmt);

						String data2 = "Withdraw " + x + "$ from " + "Saving";

						preparedStmt1.setString(1, data2);
						preparedStmt1.setString(2, userData[3]);
						preparedStmt1.executeUpdate();
						preparedStmt1.close();

						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);
						myOutputStream.writeObject(myObject);
					} else {
						String data = "Balance Low::" + checking_bal + "::"
								+ "CD";
						myObject.setMessage(data);
						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);

						myOutputStream.writeObject(myObject);

					}

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("Saving_Transfer")) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					int TransferAmt = Integer.parseInt(userData[2]);
					int checking_bal = Integer.parseInt(userData[4]);
					int saving_bal = Integer.parseInt(userData[5]);
					if (TransferAmt <= saving_bal) {
						String query = "update User_Info set Checking_Balance = ?, Saving_Balance = ? where Full_Name = ?";
						PreparedStatement preparedStmt = connection
								.prepareStatement(query);
						int new_checking_balance1 = (checking_bal + TransferAmt);
						int new_saving_bal = saving_bal - TransferAmt;
						preparedStmt.setInt(1, new_checking_balance1);
						preparedStmt.setInt(2, new_saving_bal);
						preparedStmt.setString(3, userData[3]);
						preparedStmt.executeUpdate();
						preparedStmt.close();

						String data = "Transaction Successful::"
								+ new_checking_balance1 + "::" + new_saving_bal
								+ "::" + "CT";
						myObject.setMessage(data);

						Class.forName("com.mysql.jdbc.Driver").newInstance();
						connection = DriverManager.getConnection(connectionURL,
								"nj86", "Qg0dLUtv");

						String query1 = "INSERT INTO Transaction (Info, User_Name) VALUES (?,?)";

						PreparedStatement preparedStmt1 = connection
								.prepareStatement(query1);
						String x = Integer.toString(TransferAmt);

						String data2 = "Transfer " + x + "$ to " + "Checking";

						preparedStmt1.setString(1, data2);
						preparedStmt1.setString(2, userData[3]);
						preparedStmt1.executeUpdate();
						preparedStmt1.close();

						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);
						myOutputStream.writeObject(myObject);
					} else {
						String data = "Balance Low::" + checking_bal + "::"
								+ saving_bal + "::" + "CT";
						myObject.setMessage(data);
						String msg1 = myObject.getMessage();
						String userData1[] = msg1.split("::");
						System.out.println("Message sent : " + userData1[0]);

						myOutputStream.writeObject(myObject);

					}

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

			}

			else if (userData[0].equals("3")) {

				System.out.println("Message received : " + userData[1] + " "
						+ "Account No -> " + userData[2]);
				if (userData[1].equals("New Account")) {
					int Acc_No = Integer.parseInt(userData[2]);
					int checking_bal = Integer.parseInt(userData[4]);
					int saving_bal = Integer.parseInt(userData[5]);
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					String query = "INSERT INTO User_Info (Account_Number, Full_Name,"
							+ "Checking_Balance, Saving_Balance, Password) VALUES (?,?,?,?,?)";
					PreparedStatement preparedStmt = connection
							.prepareStatement(query);

					preparedStmt.setInt(1, Acc_No);
					preparedStmt.setString(2, userData[3]);
					preparedStmt.setInt(3, checking_bal);
					preparedStmt.setInt(4, saving_bal);
					preparedStmt.setString(5, userData[6]);
					preparedStmt.executeUpdate();
					String data = "User Created::" + "::" + "CT";
					myObject.setMessage(data);
					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);
					preparedStmt.close();
					myOutputStream.writeObject(myObject);

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}

				if (userData[1].equals("View Account")) {
					int acc_no = Integer.parseInt(userData[2]);
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					statement = connection.createStatement();
					rs = statement
							.executeQuery("SELECT * FROM User_Info where Account_Number= '"
									+ acc_no + "'");

					if (!rs.next()) {
						myObject.setMessage("User Not found::" + "0::" + "0::"
								+ "0");
					} else {
						String User_Name = rs.getString("Full_Name");
						String Check_Bal = rs.getString("Checking_Balance");
						String Saving_Bal = rs.getString("Saving_Balance");
						String data = "View Successful::" + User_Name + "::"
								+ Check_Bal + "::" + Saving_Bal;
						myObject.setMessage(data);
					}
					rs.close();
					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);

					myOutputStream.writeObject(myObject);

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}
				if (userData[1].equals("Freeze Account")) {
					int acc_no = Integer.parseInt(userData[2]);
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					String query = "update User_Info set Freeze = ? where Account_Number = ?";
					PreparedStatement preparedStmt = connection
							.prepareStatement(query);

					preparedStmt.setInt(1, 1);
					preparedStmt.setInt(2, acc_no);
					preparedStmt.executeUpdate();
					String data = "Freeze Successful";
					myObject.setMessage(data);
					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);

					myOutputStream.writeObject(myObject);

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}
				if (userData[1].equals("UN Freeze Account")) {
					int acc_no = Integer.parseInt(userData[2]);
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(connectionURL,
							"nj86", "Qg0dLUtv");
					String query = "update User_Info set Freeze = ? where Account_Number = ?";
					PreparedStatement preparedStmt = connection
							.prepareStatement(query);

					preparedStmt.setInt(1, 0);
					preparedStmt.setInt(2, acc_no);
					preparedStmt.executeUpdate();
					String data = "UN Freeze Successful";
					myObject.setMessage(data);
					String msg1 = myObject.getMessage();
					String userData1[] = msg1.split("::");
					System.out.println("Message sent : " + userData1[0]);

					myOutputStream.writeObject(myObject);

					myOutputStream.close();
					myInputStream.close();
					incoming.close();
				}
			}

		} catch (Exception e) {

			System.out.println(e);
		}
	}

	DataObject myObject = null;
	private Socket incoming;

}
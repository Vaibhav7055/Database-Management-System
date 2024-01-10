package BankingProjectJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	private Connection con;

	private Scanner sc;

	public AccountManager(Connection con, Scanner sc) {
		this.con = con;
		this.sc = sc;
	}

	public void credit_money(long account_number) throws SQLException {
		sc.nextLine();
		System.out.println("Enter amount:- ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter security pin");
		String security_pin = sc.next();

		try {
			con.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement ps = con
						.prepareStatement("Select * from Accounts where account_number = ? and security_pin = ?");
				ps.setLong(1, account_number);
				ps.setString(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					String credit_query = "Update Accounts set balance = balance + ? where account_number = ?";
					PreparedStatement ps1 = con.prepareStatement(credit_query);
					ps1.setDouble(1, amount);
					ps1.setLong(2, account_number);
					int rowsAffected = ps1.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Rs." + amount + " credit successfully");
						con.commit();
						con.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction failed..!");
						con.rollback();
						con.setAutoCommit(true);
					}
				} else {
					System.out.println("Invalid securiy pin..!");
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		con.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException {
		sc.nextLine();
		System.out.println("Enter amount:- ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter security pin");
		String security_pin = sc.next();
		sc.nextLine();

		try {
			con.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement ps = con
						.prepareStatement("Select * from Accounts where account_number = ? and security_pin = ?");
				ps.setLong(1, account_number);
				ps.setString(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					double current_balance = rs.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "UPDATE Accounts set balance = balance - ? WHERE account_number = ?";
						PreparedStatement ps1 = con.prepareStatement(debit_query);
						ps1.setDouble(1, amount);
						ps1.setLong(2, account_number);
						int rowsAffected = ps1.executeUpdate();
						if (rowsAffected > 0) {
							System.out.println("Rs." + amount + " debited Successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!");
							con.rollback();
							con.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance!");
					}
				} else {
					System.out.println("Invalid Pin!");
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		con.setAutoCommit(true);
	}

	public void transfer_money(long sender_account_number) throws SQLException {
		sc.nextLine();
		System.out.println("Enter reciever account number:- ");
		long reciever_account_number = sc.nextLong();
		System.out.println("Enter amount:- ");
		double amount = sc.nextDouble();
		sc.nextLine();
		System.out.println("Enter security pin:- ");
		String security_pin = sc.next();

		try {
			con.setAutoCommit(false);
			if (sender_account_number != 0 && reciever_account_number != 0) {
				PreparedStatement ps = con
						.prepareStatement("Select * from Accounts where account_number = ? and security_pin = ?");
				ps.setLong(1, sender_account_number);
				ps.setString(2, security_pin);
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					double current_balance = rs.getDouble("balance");
					if (amount <= current_balance) {

						String debit_query = "Update Accounts set balance = balance - ? where account_number = ?";
						String credit_query = "Update Accounts set balance = balance + ? where account_number = ?";

						PreparedStatement debitps = con.prepareStatement(debit_query);
						debitps.setDouble(1, amount);
						debitps.setLong(2, sender_account_number);
						int rowsAffected1 = debitps.executeUpdate();

						PreparedStatement creditps = con.prepareStatement(credit_query);
						creditps.setDouble(1, amount);
						creditps.setLong(2, reciever_account_number);
						int rowsAffected2 = creditps.executeUpdate();

						if (rowsAffected1 > 0 && rowsAffected2 > 0) {
							System.out.println("Transaction Successfull...!");
							System.out.println("Rs." + amount + " Transferred Successfully...!");
							con.commit();
							con.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed...!");
							con.rollback();
							con.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance...!");
					}
				} else {
					System.out.println("Invalid security pin..");
				}

			} else {
				System.out.println("Invalid account number...");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		con.setAutoCommit(true);
	}

	public void getBalance(long account_number) {
		sc.nextLine();
		System.out.println("Enter security pin:- ");
		String security_pin = sc.next();

		try {
			PreparedStatement ps = con
					.prepareStatement("Select balance from Accounts where account_number = ? and security_pin = ?");
			ps.setLong(1, account_number);
			ps.setString(2, security_pin);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				double balance = rs.getDouble("balance");
				System.out.println("Balance:- " + balance);
			} else {
				System.out.println("Invalid pin...");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
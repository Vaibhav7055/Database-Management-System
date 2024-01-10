package BankingProjectJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class BankingApp {
	public static void main(String args[]) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Javaaugust", "root",
					"vaibh@7055");
			Scanner sc = new Scanner(System.in);

			User user = new User(con, sc);

			Accounts accounts = new Accounts(con, sc);

			AccountManager accountManager = new AccountManager(con, sc);

			String email;
			long account_number;

			while (true) {
//				char ans = 'y';
//				while (ans == 'y' || ans == 'Y') {
					System.out.println("Welcome to Banking System...");
					System.out.println();
					System.out.println("1. Register");
					System.out.println("2. Login");
					System.out.println("3. Exit ");
					System.out.println("Enter your choice");

					int choice1 = sc.nextInt();
					switch (choice1) {
					case 1:
						user.Register();
						break;

					case 2:
						email = user.login();
						if (email != null) {
							System.out.println();
							System.out.println("User logged in...");
							if (!accounts.account_exist(email)) {
								System.out.println();
								System.out.println("1. Open a New Bank Account");
								System.out.println("2. Exit");
								if (sc.nextInt() == 1) {
									account_number = accounts.open_account(email);
									System.out.println("Account Created Successfully...!");
									System.out.println("Your Account Number is " + account_number);
								} else {
									break;
								}

							}

							account_number = accounts.getAccount_number(email);
							int choice2 = 0;
							while (choice2 != 5) {
								System.out.println();
								System.out.println("1. Debit Money");
								System.out.println("2. Credit Money");
								System.out.println("3. Transfer Money");
								System.out.println("4. Check Balance");
								System.out.println("5. Log Out");
								System.out.println("Enter your choice:- ");
								choice2 = sc.nextInt();
								switch (choice2) {
								case 1:
									accountManager.debit_money(account_number);
									break;
								case 2:
									accountManager.credit_money(account_number);
									break;
								case 3:
									accountManager.transfer_money(account_number);
									break;
								case 4:
									accountManager.getBalance(account_number);
									break;
								case 5:
//									System.out.println("Would you like to move back to banking system");
//									System.out.println("Press y or n");
//									ans = sc.next().charAt(0);
//									if (ans == 'n' || ans == 'N') {
//										System.out.println("Exiting Banking System...");
//									}
									System.out.println("Logging out...");
									break;
								default:
									System.out.println("Enter valid choice...");
									break;
								}
							}

						} else {
							System.out.println("Incorrect email or password...!");
						}
					case 3:
						System.out.println("Thank u for using Banking System...!");
						System.out.println("Exiting System");
						return;
					default:
						System.out.println("Enter valid choice..");
						break;
					}
				}
//			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

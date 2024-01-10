package BankingProjectJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.PreparableStatement;

public class User {
private Connection con;
	
	private Scanner sc;
	
	public User(Connection con, Scanner sc) {
		this.con=con;
		this.sc=sc;
	}
	
	public void Register(){
		sc.nextLine();
		System.out.println("Full name:- ");
		String full_name = sc.nextLine();
		System.out.println("Email:- ");
		String email = sc.nextLine();
		System.out.println("Password:- ");
		String password = sc.nextLine();
		
		if(user_exist(email)){
			System.out.println("User Already Exists for this Email Address");
			return;
		}
		String reqister_query = "Insert into User(full_name, email, password) values (?,?,?)";
		try{
			PreparedStatement ps = con.prepareStatement(reqister_query);
			ps.setString(1, full_name);
			ps.setString(2, email);
			ps.setString(3, password);
			
			int affectedRows =ps.executeUpdate();
			if(affectedRows > 0){
				System.out.println("Registration Successfull...!");
			}
			else{
				System.out.println("Registration Failed...!");
			}
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	
	public String login(){
		sc.nextLine();
		System.out.println("Email:- ");
		String email = sc.nextLine();
		System.out.println("Password:- ");
		String password = sc.nextLine();
		String login_query = "Select * from user where email = ? and password = ?";
		try{
			PreparedStatement ps = con.prepareStatement(login_query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				return email;
			}
			else{
				return null;
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public boolean user_exist(String email){
		String query = "Select * from User where email = ?";
		try{
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				return true;
			}
			else{
				return false;
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}

package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
	
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	
	public StudentDAO(){
		
	}
	
	
	/**
	 * @param namePrefix
	 * @param minGpa
	 * @param sorting
	 * @return
	 * @throws Exception
	 */
	public List<StudentBean> retrieve(String namePrefix, String minGpa, String sorting) throws Exception{
		List<StudentBean> list = new ArrayList<>();
		String query = "SELECT SURNAME, GIVENNAME, MAJOR, COURSES, GPA FROM SIS WHERE SURNAME LIKE '" 
				+ namePrefix 
				+ "%' AND GPA >="
				+ minGpa
				+ " ";
		if(!sorting.equals("NONE")) {
			query += "ORDER BY " + sorting;
		}
		System.out.println("Query is " + query);
		
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		
		ResultSet r = s.executeQuery(query);
		while (r.next()) {
			//System.out.println(r.toString());
			StudentBean student = new StudentBean();
			student.setName(r.getString("SURNAME") + ", " + r.getString("GIVENNAME"));
			student.setMajor(r.getString("MAJOR"));
			student.setCourses(r.getInt("COURSES"));
			student.setGpa(r.getDouble("GPA"));
			list.add(student);
		}
		
		r.close(); s.close(); con.close();
		return list;
	}
	

}

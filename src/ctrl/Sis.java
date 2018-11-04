package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;
import model.StudentBean;

/**
 * Servlet implementation class Sis
 */
@WebServlet("/Sis.do")
public class Sis extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			System.out.println("execute Sis.do");
			String prefix = (String) request.getParameter("prefix");
			String minGpa = (String) request.getParameter("minGpa");
			String sortBy = (String) request.getParameter("sortBy");
			
			Writer out = response.getWriter();
			response.setContentType("text/json");
			
			Engine engine = Engine.getInstance();
			try {
				List<StudentBean> list = engine.doSis(prefix, minGpa, sortBy);
				//request.setAttribute("message", "Sorted by " + sortBy);
				//request.setAttribute("list", list);
				String json = "";
				json += "{\"status\":1, \"message\" : \"Sorted by " + sortBy + "\", \"list\" : [";
				for(StudentBean studentBean : list) {
					json += "{";
					json += "\"name\":" + "\"" + studentBean.getName() + "\",";
					json += "\"major\":" + "\"" + studentBean.getMajor() + "\",";
					json += "\"courses\":" + "\"" + studentBean.getCourses()+ "\",";
					json += "\"gpa\":" + "\"" + studentBean.getGpa() + "\"";
					json += "},";
				}
				json = json.substring(0, json.length() - 1);
				json += "]}";
				//System.out.println(json);
				out.write(json);
			} catch (Exception e) {
				out.write("{\"status\":0, \"error\":\"" + e.getMessage() + "\"}");
				e.printStackTrace();
			} 
			
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

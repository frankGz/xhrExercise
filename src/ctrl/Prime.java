package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Engine;

/**
 * Servlet implementation class Prime
 */
@WebServlet("/Prime.do")
public class Prime extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Engine engine = Engine.getInstance();
		Writer out = response.getWriter();
		response.setContentType("text/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		
			System.out.println("execute Prime.do calc");
			int min = Integer.parseInt(request.getParameter("min"));
			if (min >= 0) {
				int max = Integer.parseInt(request.getParameter("max"));
				//request.setAttribute("min", min);
				//request.setAttribute("max", max);
				try {
					int prime = engine.doPrime(min, max);
					if (prime != -1) {
						// we have a result
						//request.setAttribute("result", prime);
						//request.setAttribute("found", 1);
						out.write("{\"status\":1, \"result\":" + prime + "}");
					} else {
						//no result in range
						//request.setAttribute("found", 2);
						out.write("{\"status\":0, \"error\":" +"\"" + "No more primes in range."+ "\"" + "}");
					}
				} catch (Exception e) {
					// exception found, put in error
					//request.setAttribute("found", 4);
					//request.setAttribute("error", e.getMessage());
					out.write("{\"status\":0, \"error\":'" + "\"" + e.getMessage() + "\"" + "'}");
					e.printStackTrace();
				} 
			}else {
				// cannot start with negative
				int max = Integer.parseInt(request.getParameter("max"));
				//request.setAttribute("min", min);
				//request.setAttribute("max", max);
				//request.setAttribute("found", 3);
				out.write("{\"status\":0, \"error\":" + "\"" + "start &lt; 0: " + min + "\"" + "}");
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

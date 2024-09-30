package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletEstaciones extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletEstaciones() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Establecemos el tipo MIME de la respuesta
		response.setContentType("text/html");
		// Escribimos la respuesta
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		// Establecemos el título de la página HTML
		out.println("<title>" + "Procesamiento Datos Cliente" + "</title>");
		out.println("</head>");
		// Cuerpo de la página
		out.println("<body>");
		out.println("<B><P> Prueba Servlet Estaciones </P></B>");
		out.println("</body>");
		out.println("</html>");
	}

}

package eu.engjoy.openshift.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -2345771005431135205L;

        @Override
         protected void doGet(HttpServletRequest request, HttpServletResponse response)
                 throws ServletException, IOException {

             PrintWriter out = response.getWriter();

             out.println("<html>");
             out.println(" <head>");
             out.println("  <title>Hello World - Servlet Example</title>");
             out.println(" </head>");
             out.println(" <body>");
             out.println("  <h1>Hello, World!</h1>");
             out.println(" </body>");
             out.println("</html>");
        }
}

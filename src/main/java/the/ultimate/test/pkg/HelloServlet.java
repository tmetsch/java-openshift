package the.ultimate.test.pkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple REST Hello World Service.
 *
 * @author tmetsch
 *
 */
public class HelloServlet extends HttpServlet {

    /**
     * A serial UID.
     */
    private static final long serialVersionUID = -2345771005431135205L;

    /**
     * List of names.
     */
    private static List<String> users = new LinkedList<String>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();

        PrintWriter out = resp.getWriter();
        if (users.size() == 0) {
            out.write("No user resource found.");
        } else if (path == null || path.equals("/")) {
            out.write("I know the following users:");
            for (String name : users) {
                out.write(name);
            }
        } else if (users.contains(path.substring(1))) {
            out.write("Hello " + path.substring(1));
        } else {
            resp.sendError(404, "Path not found: " + path);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        // extract data
        if (req.getContentType().equals("text/plain")) {
            String data = readBody(req).toLowerCase();
            users.add(data);
            out.write("Done: " + data);
        } else {
            resp.sendError(406,
                    "Content-Type not defined or unknown - needs to be"
                            + " text/plain...");
        }
    }

    /**
     * Reads the HTTP body from the request.
     *
     * @param request
     *            The HTTPServlet request.
     * @return A String.
     * @throws IOException
     *             On error.
     */
    private final String readBody(final HttpServletRequest request)
            throws IOException {
        InputStream in = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(in));

        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        bufferedReader.close();
        return stringBuilder.toString().replace("\n", "");
    }
}

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
            out.write(createHtml("<h1>Currently no Users are known</h1>"));
        } else if (path == null || path.equals("/")) {
            String tmp = "<h1>Know users</h1>";
            for (String name : users) {
                tmp += "<a href=/\"" + name + "\">" + name + "</a><br />";
            }
            out.write(createHtml(tmp));
        } else if (users.contains(path.substring(1))) {
            out.write(createHtml("<h1>Hello " + path.substring(1) + "</h1>"));
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
            if (users.contains(data)) {
                resp.sendError(400,
                        "Not supporting updates - user does exist.");
            }
            users.add(data);
            out.write(createHtml(data));
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

    /**
     * Create a HTML string.
     *
     * @param body
     *            The body part...
     * @return A String.
     */
    private final String createHtml(String body) {
        String tmp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        tmp += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        tmp += "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
        tmp += "  <head>";
        tmp += "<title>Simple App</title>";
        tmp += "<meta name=\"description\" content=\"An OpenShift Test App\" />";
        tmp += "<meta name=\"Content-Language\" content=\"en\" />";
        tmp += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />";
        tmp += "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\" media=\"screen\" />";
        tmp += "</head>";
        tmp += "<body>";
        tmp += body;
        tmp += "</body>";
        tmp += "</html>";

        return tmp;
    }
}

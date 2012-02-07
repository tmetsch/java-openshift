package the.ultimate.test.pkg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

/**
 * Unittest for the HelloServlet.
 *
 * @author tmetsch
 *
 */
public class HelloServletTest extends TestCase{

    /**
     * Mock context.
     */
    private Mockery context = new Mockery();

    /**
     * req mock.
     */
    private HttpServletRequest req = context.mock(HttpServletRequest.class);

    /**
     * resp mock.
     */
    private HttpServletResponse resp = context.mock(HttpServletResponse.class);

    /**
     * test instance.
     */
    private HelloServlet testClass = new HelloServlet();


    /*
     * Test for success
     */

    /**
     * Test method for doGet.
     */
    public void testDoGetForSuccess() {
        try {
            context.checking(new Expectations() {
                {
                    oneOf(req).getPathInfo();
                    will(returnValue("/"));

                    oneOf(resp).getWriter();
                    will(returnValue(new PrintWriter(System.out, true)));
                }
            });
            testClass.doGet(req, resp);
            context.assertIsSatisfied();
        } catch (ServletException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for doPost.
     */
    public void testDoPostForSuccess() {
        try {
            context.checking(new Expectations() {
                {
                    oneOf(req).getContentType();
                    will(returnValue("text/plain"));

                    oneOf(req).getInputStream();
                    will(returnValue(new DummyInputStream("Bob")));

                    oneOf(resp).getWriter();
                    will(returnValue(new PrintWriter(System.out, true)));
                }
            });
            testClass.doPost(req, resp);
            context.assertIsSatisfied();
        } catch (ServletException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /*
     * Test for failure
     */

    /**
     * Test method for doGet.
     */
    public void testDoGetForFailure() {
        try {
            context.checking(new Expectations() {
                {
                    allowing(req).getPathInfo();
                    will(returnValue("/foobar"));

                    oneOf(resp).getWriter();
                    will(returnValue(new PrintWriter(System.out, true)));

                    oneOf(resp).sendError(with(any(Integer.class)),
                            with(any(String.class)));
                }
            });
            testClass.doGet(req, resp);
            context.assertIsSatisfied();
        } catch (ServletException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for doPost.
     */
    public void testDoPostForFailure() {
        try {
            context.checking(new Expectations() {
                {
                    oneOf(req).getContentType();
                    will(returnValue("application/json"));

                    oneOf(resp).getWriter();
                    will(returnValue(new PrintWriter(System.out, true)));

                    oneOf(resp).sendError(with(any(Integer.class)),
                            with(any(String.class)));
                }
            });
            testClass.doPost(req, resp);
            context.assertIsSatisfied();
        } catch (ServletException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /*
     * Test for sanity
     */

    /**
     * Test method for doPost.
     */
    public void testDoPostForSanity() {
        try {
            context.checking(new Expectations() {
                {
                    oneOf(req).getContentType();
                    will(returnValue("text/plain"));

                    oneOf(req).getInputStream();
                    will(returnValue(new DummyInputStream("Bob2")));

                    oneOf(resp).getWriter();
                    will(returnValue(new PrintWriter(System.out, true)));
                }
            });
            testClass.doPost(req, resp);
            context.assertIsSatisfied();

            context.checking(new Expectations() {
                {
                    oneOf(req).getPathInfo();
                    will(returnValue("/bob"));

                    oneOf(resp).getWriter();
                    will(returnValue(new PrintWriter(System.out, true)));
                }
            });
            testClass.doGet(req, resp);
            context.assertIsSatisfied();
        } catch (ServletException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Innter class for testing purposes.
     *
     * @author tmetsch
     *
     */
    protected static class DummyInputStream extends ServletInputStream {

        /**
         * A counter.
         */
        private InputStream reader;

        /**
         * Constructor.
         *
         * @param text
         *            text to use.
         */
        public DummyInputStream(final String text) {
            reader = new ByteArrayInputStream(text.getBytes());
        }

        @Override
        public final int read() throws IOException {
            return reader.read();
        }
    }
}

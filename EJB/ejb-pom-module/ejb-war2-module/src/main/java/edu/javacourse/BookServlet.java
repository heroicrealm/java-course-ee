package edu.javacourse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Artem Pronchakov | email/xmpp: artem.pronchakov@calisto.email
 */

@WebServlet(name = "BookServlet2", urlPatterns = {"/bookServlet2"})
public class BookServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BookServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.trace("Servlet BookServlet doGet begin");

        BookEJBRemote bookEJBRemote;

        try {
            Context context = new InitialContext();
            bookEJBRemote = (BookEJBRemote) context.lookup("java:global/ejb-ear-module/ejb-war-module-1.0-SNAPSHOT/BookEJB!edu.javacourse.BookEJBRemote");
        } catch (NamingException e) {
            log.error("Error while creating JNDI context: {}", e.getMessage());
            throw new ServletException("Error while creating JNDI context");
        }

        log.debug("BookEJBLocal class: {}", bookEJBRemote == null ? "EJB not initialized" : bookEJBRemote.getClass().getCanonicalName());

        List<Book> books = bookEJBRemote.getBooks();

        log.debug("Books returned by EJB: {}", books);

        request.setAttribute("books", books);

        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        log.trace("Servlet BookServlet doGet end");
    }

}

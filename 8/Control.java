
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author milosz
 */
@WebServlet(urlPatterns = {"/Control/*"})
public class Control extends HttpServlet {

    private static final long serialVersionUID = 99999L;
    private IControlRemote controlEJB;
    private static final String BEAN_ID = "controlEJB";

    /**
     * Gets access to ControlImpl EJB. If ControlImpl EJB is already in session
     * then retrieves EJB from it, otherwise tries to find EJB using JNDI and
     * lookup
     *
     * @param session HttpSession
     */
    private void accessEJB(HttpSession session) {
        controlEJB = (IControlRemote) session.getAttribute(BEAN_ID);
        if (controlEJB == null) {
            try {
                controlEJB = connectToEJB();
                session.setAttribute(BEAN_ID, controlEJB);
            } catch (NamingException e) {
            }
        }
    }

    /**
     * Finds and fetches ControlImpl EJB using JNDI lookup
     *
     * @return ControlImpl EJB
     * @throws NamingException thrown when ControlImpl not found
     */
    private IControlRemote connectToEJB() throws NamingException {
        InitialContext ctx = new InitialContext();
        String name = "java:global/114014/ControlImpl!IControlRemote";
        return (IControlRemote) ctx.lookup(name);
    }

    /**
     * Processes request with state parameter in URL
     *
     * @param stateVal value of state parameter in URL
     */
    private void handleStateReq(String stateVal) {
        if (stateVal.equals("")) {
            controlEJB.increment(1);
        } else {
            try {
                controlEJB.increment(Integer.parseInt(stateVal));
            } catch (NumberFormatException e) {
            }
        }
    }

    /**
     * Registers request and processes it
     * 
     * @param request HttpServletRequest
     * @return String that is response for request
     */
    private String register(HttpServletRequest request) {
        String retVal = "";

        if (request.getParameter("state") != null) {
            handleStateReq(request.getParameter("state"));
        } else if (request.getParameter("login") != null) {
            controlEJB.start();
        } else if (request.getParameter("logout") != null) {
            controlEJB.stop();
        } else if (request.getParameter("result") != null) {
            retVal = "" + (controlEJB.counter() - controlEJB.errors());
        }
        return retVal;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        accessEJB(request.getSession());
        if (controlEJB == null) {
            return;
        }

        String resp = register(request);

        try (PrintWriter out = response.getWriter()) {
            out.println(resp);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

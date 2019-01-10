/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author milosz
 */
public class Calc extends HttpServlet {

    private static final long serialVersionUID = 99999L;

    @EJB
    private IBeanRemote iBean;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request request
     * @param response response
     * @throws ServletException if error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        int n;
        double a, b;

        try {
            n = Integer.parseInt(request.getParameter("n"));
            a = Double.parseDouble(request.getParameter("a"));
            b = Double.parseDouble(request.getParameter("b"));
        } catch (NumberFormatException ex) {
            return;
        }

        double result;
        result = iBean.solve(a, b, n);

        try (PrintWriter out = response.getWriter()) {
            out.println(result);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request request
     * @param response response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request request
     * @param response response
     * @throws ServletException if error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a description of the servlet.
     *
     * @return a String containing description
     */
    @Override
    public String getServletInfo() {
        return "Description";
    }
}

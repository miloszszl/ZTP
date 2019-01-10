/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

/**
 *
 * @author milosz
 */
@WebServlet(urlPatterns = {"/Gauss"})
public class Gauss extends HttpServlet {

    private static final long serialVersionUID = 9999L;

    /**
     * Removes session attribute that contains list of Double values
     *
     * @param session HttpSession, collected data will be removed from it
     */
    private void clearSessionGauss(HttpSession session) {
        session.removeAttribute("dataGauss");
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nUrlArgument = request.getParameter("n");
        double n;
        try {
            nUrlArgument = nUrlArgument.replace(",", ".");
            n = Double.parseDouble(nUrlArgument);
        } catch (NullPointerException | NumberFormatException e) {
            return;
        }

        HttpSession session = request.getSession();
        List<Double> dataGauss
                = (session.getAttribute("dataGauss") instanceof LinkedList)
                ? (LinkedList<Double>) session.getAttribute("dataGauss") : null;

        if (dataGauss == null) {
            dataGauss = new LinkedList<>();
        }

        dataGauss.add(n);
        session.setAttribute("dataGauss", dataGauss);
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
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        List<Double> dataGauss
                = (session.getAttribute("dataGauss") instanceof LinkedList)
                ? (LinkedList<Double>) session.getAttribute("dataGauss") : null;

        if (dataGauss != null) {

            double[] dataGaussArray = convertDoubleListToDoubleArray(dataGauss);

            if (dataGaussArray.length >= 2) {
                NormalDistribution normalDistribution
                        = createNormalDistribution(dataGaussArray);

                double significanceLevel = findSignificanceLevel(dataGaussArray,
                        normalDistribution);

                try (PrintWriter out = response.getWriter()) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.FLOOR);
                    out.println(df.format(significanceLevel).replace(",", "."));
                }

                clearSessionGauss(session);
            }
        }
    }

    /**
     * Creates normal distribution for given data
     *
     * @param data array of double that will be used for normal distribution
     * creation
     * @return normal distribution for given array of double
     */
    private NormalDistribution createNormalDistribution(double[] data) {
        double mean = new Mean().evaluate(data);
        double sd = new StandardDeviation().evaluate(data, mean);

        return new NormalDistribution(mean, sd);
    }

    /**
     * Calculates significance level for given normal distribution and array of
     * double values. Method uses KolomogrovSmirnovTest
     *
     * @param data array of double values used for significance level
     * calculation
     * @param normalDistribution normal distribution used for significance level
     * calculation
     * @return significance level for given method parameters
     */
    private double findSignificanceLevel(double[] data,
            NormalDistribution normalDistribution) {
        KolmogorovSmirnovTest test = new KolmogorovSmirnovTest();
        double epsilon = 0.000000001;
        double pValue = test
                .kolmogorovSmirnovTest(normalDistribution, data);
        double alpha = pValue - epsilon;
        if (alpha < 0.0) {
            alpha = 0.0;
        }
        return alpha;
    }

    /**
     * Converts list of Double to array of double
     *
     * @param doubleList list of Double
     * @return array of double
     */
    private double[] convertDoubleListToDoubleArray(List<Double> doubleList) {
        double[] arr = new double[doubleList.size()];

        for (int i = 0; i < doubleList.size(); i++) {
            arr[i] = doubleList.get(i);
        }
        return arr;
    }

    /**
     * Gives servlet description
     *
     * @return servlet description (String)
     */
    @Override
    public String getServletInfo() {
        return "Description";
    }// </editor-fold
}



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import pl.jrj.mdb.IMdbManager;

/**
 *
 * @author milosz
 */
@Path("/exchangeRate")
public class ExchRates {

    private IMdbManager mdbManager = null;
    private final String nbpURL = "http://www.nbp.pl/kursy/xml/LastA.xml";

    /**
     * Registers remote session bean using JNDI
     */
    private void registerMdbManager() {
        if (mdbManager == null) {
            try {
                Context context = new InitialContext();
                mdbManager = (IMdbManager) context
                        .lookup("java:global/mdb-project/MdbManager"
                                + "!pl.jrj.mdb.IMdbManager");
            } catch (NamingException ex) {
            }
        }
    }

    /**
     * Endpoint that returns exchange ratio between currency given in URL and
     * currency retrieved from remote session bean
     *
     * @param currencyCode currency code that will be exchanged
     * @return exchange rate between currency given in URL and currency
     * retrieved from remote session bean
     */
    @GET
    @Path("/{code}")
    @Produces("text/plain")
    public String calculateRatio(@PathParam("code") String currencyCode) {
        registerMdbManager();
        String referencedCurrencyCode = mdbManager.currencyId();
        Document doc = getNBPDecument();
        double result = CurrencyEvaluator.calculateRatioFromDocument(doc,
                currencyCode, referencedCurrencyCode);
        return roundDouble(result, 4) + "";
    }

    /**
     * Retrieves XML with exchange rates from NBP website and converts it to
     * Document object
     *
     * @return xml document
     */
    private Document getNBPDecument() {
        Document doc = null;
        try {
            URL url = new URL(nbpURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            InputStream xml = con.getInputStream();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(xml);
            con.disconnect();
        } catch (MalformedURLException ex) {
        } catch (IOException | ParserConfigurationException | SAXException ex) {
        }

        return doc;
    }

    /**
     * Rounds double to certain amount of decimal places
     *
     * @param value double value to be rounded
     * @param decimalPlaces number of decimal places to round
     * @return rounded double value
     */
    private double roundDouble(double value, int decimalPlaces) {
        double shift = Math.pow(10, decimalPlaces);
        return Math.round(value * shift) / shift;
    }
}

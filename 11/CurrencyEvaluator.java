

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author milosz
 */
public class CurrencyEvaluator {

    /**
     * Parses document, extracts data from it and calculate exchange ratio using
     * data from parsed document
     *
     * @param doc document to be parsed
     * @param currencyCode currency code (currency to be exchanged)
     * @param referencedCurrencyCode referenced currency code
     * @return exchange ration between currencies identified by curryncyCode and
     * referencedCurrencyCode
     */
    public static double calculateRatioFromDocument(Document doc,
            String currencyCode, String referencedCurrencyCode) {

        Double exRate;
        Double quantity;
        Double exRateRef;
        Double quantityRef;

        exRate = quantity = exRateRef = quantityRef = null;
        doc.getDocumentElement().normalize();

        if ("PLN".equals(referencedCurrencyCode)) {
            exRateRef = quantityRef = 1.0;
        }

        NodeList nList = doc.getElementsByTagName("pozycja");
        try {
            for (int i = 0; i < nList.getLength(); i++) {

                Element element = (Element) nList.item(i);
                String code = getCodeFromXMLElement(element);

                if (currencyCode.equals(code)) {
                    exRate = getExchangeRateFromXMLElement(element);
                    quantity = getQuantityFromXMLElement(element);
                }

                if (referencedCurrencyCode.equals(code)) {
                    exRateRef = getExchangeRateFromXMLElement(element);
                    quantityRef = getQuantityFromXMLElement(element);
                }
            }

            if (exRate != null && quantity != null && exRateRef != null
                    && quantityRef != null) {
                return (exRate / quantity) / (exRateRef / quantityRef);
            }

        } catch (NumberFormatException ex) {
        }

        return 0.0;
    }

    /**
     * Extracts currency code from xml document element
     *
     * @param element xml document element
     * @return currency code
     */
    private static String getCodeFromXMLElement(Element element) {
        return element.getElementsByTagName("kod_waluty")
                .item(0).getTextContent();
    }

    /**
     * Extracts exchange rate from xml document element
     *
     * @param element xml document element
     * @return exchange rate for currency
     */
    private static Double getExchangeRateFromXMLElement(Element element) {
        return stringToDouble(element.getElementsByTagName("kurs_sredni")
                .item(0).getTextContent());
    }

    /**
     * Extracts quantity of currency from xml document element
     *
     * @param element xml document element
     * @return quantity of currency
     */
    private static Double getQuantityFromXMLElement(Element element) {
        return stringToDouble(element.getElementsByTagName("przelicznik")
                .item(0).getTextContent());
    }

    /**
     * In given String replaces colons with dots and converts result to Double
     *
     * @param val value to be converted
     * @return double value of given String
     */
    private static Double stringToDouble(String val) {
        return Double.parseDouble(val.replace(",", "."));
    }
}

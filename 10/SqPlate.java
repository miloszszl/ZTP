

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author milosz
 */
@Path("/plate")
public class SqPlate {

    /**
     * Endpoint that returns minimal cost of cutting plate into pieces
     *
     * @param datasource defines data source (Used in JNDI)
     * @param tableName table name in database to fetch data from
     * @return String that contains minimal cost of cutting plate into pieces
     */
    @GET
    @Path("/{datasource}/{table}")
    public String minimumTotalCost(@PathParam("datasource") String datasource,
            @PathParam("table") String tableName) {

        List<Float> xCostList = new LinkedList<>();
        List<Float> yCostList = new LinkedList<>();

        try {
            Context context = new InitialContext();
            String JNDIName = "jdbc/" + datasource;
            DataSource dataBaseSource = (DataSource) context.lookup(JNDIName);
            String sqlQuery = "SELECT x, y FROM " + tableName;

            Connection connection = dataBaseSource.getConnection();
            Statement sm = connection.createStatement();
            ResultSet rs = sm.executeQuery(sqlQuery);

            float x, y;
            while (rs.next()) {
                x = rs.getFloat("x");
                y = rs.getFloat("y");

                if (x > 0.0 && y > 0.0) {
                    xCostList.add(x);
                    yCostList.add(y);
                }
            }
        } catch (NamingException | SQLException ex) {
            return "Koszt ciecia : " + 0.0;
        }

        double cost = MyPlate.calculateMinCost(xCostList, yCostList);
        return String.format("Koszt ciecia : %.5f", cost);
    }
}

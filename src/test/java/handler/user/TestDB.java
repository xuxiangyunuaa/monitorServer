package handler.user;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import org.nit.monitorserver.Config;
import org.nit.monitorserver.VertxInstance;
import org.nit.monitorserver.database.MysqlConnection;
import org.nit.monitorserver.util.StringUtil;

import java.util.List;

public class TestDB {

    public static void main(String[] args) {
        Config.setLocalDatabaseConfFile();
        String databaseConfFile = Config.getDatabaseConfFile();
        //将配置转换为json

        SQLClient sqlClient;
        Vertx vertx = Vertx.vertx();
        final JsonObject config = new JsonObject(StringUtil.readFileToString(databaseConfFile));

        sqlClient = JDBCClient.createShared(vertx, config);
        System.out.println("success connect");

        String query = "SELECT * FROM target ; " ;

        sqlClient.query(query, ar -> {
            ResultSet resultSet = ar.result();
            //List<JsonArray> results = resultSet.getRows();
        });



    }

}

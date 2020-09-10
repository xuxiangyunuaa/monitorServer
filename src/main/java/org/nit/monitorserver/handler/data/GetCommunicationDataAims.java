package org.nit.monitorserver.handler.data;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MysqlConnection;

import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;

import java.util.List;

import static org.nit.monitorserver.constant.ResponseError.DATA_REQUIRED_ERROR;
/**
 * 功能描述: <把相关的类功能全部整合至SearchData>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 15:22
 */
public class GetCommunicationDataAims extends AbstractRequestHandler {

    protected static final Logger logger = Logger.getLogger(GetCommunicationDataAims.class);
    private final SQLClient mySQLClient = new MysqlConnection().getMySQLClient();

    @Override
    public void handle(final RoutingContext routingContext, final Request request) {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String sql = "SELECT DISTINCT TM_Name FROM PM_CommunicationRecord";

        mySQLClient.query(sql, ar -> {
            if (ar.failed()) {
                response.error(DATA_REQUIRED_ERROR.getCode(), DATA_REQUIRED_ERROR.getMsg());
                return;
            }
            logger.info("");
            JsonArray resultData = new JsonArray();
            List<JsonArray> dataList = ar.result().getResults();
            for (int i = 0; i < dataList.size(); i++) {
                JsonArray jsonArray = dataList.get(i);
                //System.out.println(jsonArray);
                resultData.add(jsonArray.getValue(0));
            }
            JsonObject result = new JsonObject();
            result.put("aimList", resultData);
            response.success(result);

        });


    }

}

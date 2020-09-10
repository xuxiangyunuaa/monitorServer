package org.nit.monitorserver.handler.data;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.nit.monitorserver.constant.ResponseError.DATAID;
import static org.nit.monitorserver.constant.ResponseError.*;

/**
 * @ClassName ExportData
 * @Description TODO导出数据
 * @Author 20643
 * @Date 2020-9-1 14:46
 * @Version 1.0
 **/
public class ExportData extends AbstractRequestHandler {
    protected static final Logger logger = Logger.getLogger(ExportData.class);
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();

    @Override
    public void handle(final RoutingContext routingContext,final Request request) throws IOException {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);

        String id = request.getParams().getString("id");
        if(id.equals("") || id == null){
            logger.error(String.format("delete exception: %s", "采集数据id为必填参数"));
            response.error(DATAID.getCode(), DATAID.getMsg());
            return;
        }
        JsonObject searchObject = new JsonObject().put("id",id);
        String format = request.getParams().getString("format");
        if(format.equals("") || format == null){
            logger.error(String.format("delete exception: %s", "采集数据id为必填参数"));
            response.error(DATAID.getCode(), DATAID.getMsg());
            return;
        }

        mongoClient.find("collectionData",searchObject,r->{
            if(r.failed()){
                logger.error(String.format("search collectionData: %s 查找失败", Tools.getTrace(r.cause())));
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                return;
            }else if(r.result().size() == 0){
                logger.error(String.format("search collectionData: %s 记录不存在",id));
                response.error(RECORD_NOT_EXISTED.getCode(), RECORD_NOT_EXISTED.getMsg());
                return;
            }
            String exportUrl = null;
            JsonObject result = new JsonObject();
            switch (format){
                case "xml":
                    byte[] document = Tools.jsonToXML(r.result().get(0)).getBytes();
                    exportUrl = exportUrl+"\\"+"collectionData.xml";
                    if (document != null){
                        try {
                            File file = new File(exportUrl);
                            if(!file.exists()) {
                                File dir = new File(file.getParent());
                                dir.mkdirs();
                                file.createNewFile();
                            }
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(document);
                            fileOutputStream.close();
                            result.put("url",exportUrl);

                            response.success(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "excel":
                    HSSFWorkbook documentExcel = Tools.jsonToExcel(r.result().get(0));
                    exportUrl = exportUrl+"\\"+"collectionData.xls";
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(exportUrl);
                        documentExcel.write(fileOutputStream);
                        fileOutputStream.close();
                        result.put("url",exportUrl);
                        response.success(new JsonObject().put("resutlt",exportUrl));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "二进制":



            }
        });






    }
}

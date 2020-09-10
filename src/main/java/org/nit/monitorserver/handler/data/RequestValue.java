package org.nit.monitorserver.handler.data;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;
import org.nit.monitorserver.constant.HttpHeaderContentType;
import org.nit.monitorserver.database.MongoConnection;
import org.nit.monitorserver.message.AbstractRequestHandler;
import org.nit.monitorserver.message.Request;
import org.nit.monitorserver.message.ResponseFactory;
import org.nit.monitorserver.util.FormValidator;
import org.nit.monitorserver.util.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nit.monitorserver.constant.ResponseError.FORMAT_ERROR;
import static org.nit.monitorserver.constant.ResponseError.QUERY_FAILURE;

/**
 * 功能描述: <整合至exportdata>
 * 〈〉
 * @Author: 20643
 * @Date: 2020-9-1 15:25
 */
public class RequestValue extends AbstractRequestHandler {
    private final MongoClient mongoClient = new MongoConnection().getMongoClient();
    protected static final Logger logger = Logger.getLogger(RequestValue.class);

    @Override
    public void handle(RoutingContext routingContext, Request request) throws IOException, Exception {
        routingContext.response().putHeader(HttpHeaderContentType.CONTENT_TYPE_STR, HttpHeaderContentType.JSON);
        ResponseFactory response = new ResponseFactory(routingContext, request);//根据路由上下文以及请求产生响应

        Object EDT_DATA_ANALYSIS_RTN_Object = request.getParamWithKey("EDT_DATA_ANALYSIS_RTN");
        if(!FormValidator.isJsonObject(EDT_DATA_ANALYSIS_RTN_Object)){
            logger.info(String.format("exception:%s", "存在格式非法的参数"));
            response.error(FORMAT_ERROR.getCode(), FORMAT_ERROR.getMsg());
            return;
        }
        JsonObject condition = new JsonObject();
        JsonObject edt_data_analysis_rtn = (JsonObject) EDT_DATA_ANALYSIS_RTN_Object;
        mongoClient.find("analysisRtn",condition,r->{
            if(r.failed()){
                response.error(QUERY_FAILURE.getCode(), QUERY_FAILURE.getMsg());
                logger.error(String.format("mongodb find analysisRtn data error: %s", Tools.getTrace(r.cause())));
                return;
            }
            List<JsonObject> elementObject = new ArrayList<>();
            JsonObject latestData = r.result().get(0);//取出最新一则数据
            JsonObject EDT_DATA_ANALYSIS_RTN = latestData.getJsonObject("EDT_DATA_ANALYSIS_RTN");
            if(edt_data_analysis_rtn.containsKey("evt_mem_effect_rtn")){
                JsonObject EVT_MEM_EFFECT_RTN = EDT_DATA_ANALYSIS_RTN.getJsonObject("evt_mem_effect_rtn");
                JsonObject evt_mem_effect_rtn = new JsonObject();
                String evt_mem_effect_rtn_string = edt_data_analysis_rtn.getString("evt_mem_effect_rtn");
                String[] evt_mem_effect_rtn_key = evt_mem_effect_rtn_string.split(",");
                for(String elementKey : evt_mem_effect_rtn_key){
                    evt_mem_effect_rtn.put(elementKey,EVT_MEM_EFFECT_RTN.getValue(elementKey));
                }
                elementObject.add(evt_mem_effect_rtn);

            }
            if(edt_data_analysis_rtn.containsKey("tsk_mem_effect_rtn")){
                JsonObject TSK_MEM_EFFECT_RTN = EDT_DATA_ANALYSIS_RTN.getJsonObject("tsk_mem_effect_rtn");
                JsonObject tsk_mem_effect_rtn = new JsonObject();
                String tsk_mem_effect_rtn_string = edt_data_analysis_rtn.getString("tsk_mem_effect_rtn");
                String[] tsk_mem_effect_rtn_key = tsk_mem_effect_rtn_string.split(",");
                for(String elementKey : tsk_mem_effect_rtn_key){
                    tsk_mem_effect_rtn.put(elementKey,TSK_MEM_EFFECT_RTN.getValue(elementKey));
                }
                elementObject.add(tsk_mem_effect_rtn);

            }
            if(edt_data_analysis_rtn.containsKey("func_timeuse_static_rtn")){
                JsonObject FUNC_TIMEUSE_STATIC_RTN = EDT_DATA_ANALYSIS_RTN.getJsonObject("func_timeuse_static_rtn");
                JsonObject func_timeuse_static_rtn = new JsonObject();
                String func_timeuse_static_rtn_string = edt_data_analysis_rtn.getString("func_timeuse_static_rtn");
                String[] func_timeuse_static_rtn_key = func_timeuse_static_rtn_string.split(",");
                for(String elementKey : func_timeuse_static_rtn_key){
                    func_timeuse_static_rtn.put(elementKey,FUNC_TIMEUSE_STATIC_RTN.getValue(elementKey));
                }
                elementObject.add(func_timeuse_static_rtn);

            }
            if(edt_data_analysis_rtn.containsKey("part_runtime_analysis_result")){
                JsonObject PART_RUNTIME_ANALYSIS_RESULT = EDT_DATA_ANALYSIS_RTN.getJsonObject("part_runtime_analysis_result");
                JsonObject part_runtime_analysis_result = new JsonObject();
                String part_runtime_analysis_result_string = edt_data_analysis_rtn.getString("part_runtime_analysis_result");
                String[] part_runtime_analysis_result_key = part_runtime_analysis_result_string.split(",");
                for(String elementKey : part_runtime_analysis_result_key){
                    part_runtime_analysis_result.put(elementKey,PART_RUNTIME_ANALYSIS_RESULT.getValue(elementKey));
                }
                elementObject.add(part_runtime_analysis_result);

            }
            if(edt_data_analysis_rtn.containsKey("part_section_size_result")){
                JsonObject PART_SECTION_SIZE_RESULT = EDT_DATA_ANALYSIS_RTN.getJsonObject("part_section_size_result");
                JsonObject part_section_size_result = new JsonObject();
                String part_section_size_result_string = edt_data_analysis_rtn.getString("part_section_size_result");
                String[] part_section_size_result_key = part_section_size_result_string.split(",");
                for(String elementKey : part_section_size_result_key){
                    part_section_size_result.put(elementKey,PART_SECTION_SIZE_RESULT.getValue(elementKey));
                }
                elementObject.add(part_section_size_result);

            }
            if(edt_data_analysis_rtn.containsKey("task_runtime_analysis_result")){
                JsonObject TASK_RUNTIME_ANALYSIS_RESULT = EDT_DATA_ANALYSIS_RTN.getJsonObject("task_runtime_analysis_result");
                JsonObject task_runtime_analysis_result = new JsonObject();
                String task_runtime_analysis_result_string = edt_data_analysis_rtn.getString("task_runtime_analysis_result");
                String[] task_runtime_analysis_result_key = task_runtime_analysis_result_string.split(",");
                for(String elementKey : task_runtime_analysis_result_key){
                    task_runtime_analysis_result.put(elementKey,TASK_RUNTIME_ANALYSIS_RESULT.getValue(elementKey));
                }
                elementObject.add(task_runtime_analysis_result);

            }
            JsonObject result = new JsonObject().put("EDT_DATA_ANALYSIS_RTN",elementObject);
            response.success(result);

        });


    }
}

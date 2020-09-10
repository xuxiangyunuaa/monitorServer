package handler.user;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import io.vertx.core.json.JsonObject;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class testICDParse {


    public static JSONObject xml2json(String xml){
//        this.xml=xml;
        JSONObject xmlToJson=XML.toJSONObject(xml);
        return xmlToJson;
    }

    //验证json文件是否符合jasonICD
    public static boolean validateXmlWithICD(JSONObject valIcd, JSONObject resJson){
        JSONObject resultObject = new JSONObject();
        if (null == resJson || null==valIcd) {
//            resultObject.put(VALIDATE_RESULT, VALIDATE_FAILED);
            resultObject.put("VALIDATE_MESSAGE", "The inputted json object is null, please check!");
            return false;
        }

//        JSONObject schemaObject = new JSONObject(new JSONTokener(schema));
        Schema jsonSchema = SchemaLoader.load(valIcd);

        try {
            jsonSchema.validate(resJson);
            return true;
//            resultObject.put(VALIDATE_RESULT, VALIDATE_SUCCESS);
//            resultObject.put("VALIDATE_MESSAGE", "Validate is success!");
        } catch (ValidationException e) {
//            resultObject = e.toJSON();
//            resultObject.put("VALIDATE_RESULT", "VALIDATE_FAILED");
            System.out.println(e.getAllMessages());
            return false;
        }
    }

    public static void main(final String[] args) throws FileNotFoundException {
        File testFile=new File("D:\\研究生项目\\宿主机端应用性能监控与分析评估工具\\MonitorServer\\src\\test\\java\\handler\\user\\testjson.json");
        File icdFile=new File("D:\\研究生项目\\宿主机端应用性能监控与分析评估工具\\MonitorServer\\src\\test\\java\\handler\\user\\testicd.json");
////        File testxml=new File("D:\\研究生项目\\宿主机端应用性能监控与分析评估工具\\MonitorServer\\src\\test\\java\\handler\\user\\testxml.xml");
//        FileInputStream inputStream = new FileInputStream(testFile);
        FileInputStream inputStream1 = new FileInputStream(icdFile);
        FileInputStream inputStream2 = new FileInputStream(testFile);
//        System.out.println(inputStream);
//        InputStream inputStream1 = testICDParse.class.getResourceAsStream("testicd.json");
        String testxml="<project>\n" +
                "<BID/>\n" +
                "<ID>56e4260d-ac5e-4ed6-9a1a-92b72f9f55b1</ID>\n" +
                "<Description/>\n" +
                "<Version>0.1</Version>\n" +
                "<TheTimeStamp>0</TheTimeStamp>\n" +
                "<Creator>a44ff47a-211e-4829-9fe7-496a464726e0@wangh</Creator>\n" +
                "<CreationTime>2017-09-05T17:02:21</CreationTime>\n" +
                "<Modifier>a44ff47a-211e-4829-9fe7-496a464726e0@wangh</Modifier\n" +
                ">\n" +
                "<ModifyTime>2017-09-05T17:02:21</ModifyTime>\n" +
                "<Remark/>\n" +
                "<Name>Bug0905</Name>\n" +
                "<ShortName>Bug0905</ShortName>\n" +
                "<Index>0</Index>\n" +
                "</project>";
//        InputStream inputStream = testICDParse.class.getResourceAsStream("testIcd.json");
        JSONObject testXml=xml2json(testxml);
        JSONObject testXml2=testXml.getJSONObject("project");
        Object testXml3=testXml.get("project");
        if(testXml3.equals(testXml2)){
            System.out.println(testXml.length());
        }
        Iterator<String> jsonKey=testXml2.keys();
        System.out.println(testXml.length());
        System.out.println(testXml2.keySet());
        while (jsonKey.hasNext()){
            String Keys=jsonKey.next();
            System.out.println(Keys);
        }


        System.out.println(testXml);
        Object object=Configuration.defaultConfiguration().jsonProvider().parse(testXml.toString());
        System.out.println(object);
        Object test1=JsonPath.read(object,"$.project.*");
        System.out.println(test1);

        JSONObject Schema = new JSONObject(new JSONTokener(inputStream1));
        JSONArray testjson =new JSONArray(new JSONTokener(inputStream2));

//        JSONObject data = new JSONObject("{\"foo\" : 1234}");
        Schema schema = SchemaLoader.load(Schema);
        try {
            schema.validate(testjson);

            System.out.println("true!");
        } catch (ValidationException e) {
            System.out.println(e.getAllMessages());
        }
//        JSONObject icdJson=new JSONObject(inputStream1);
//        JSONObject testJson=new JSONObject(inputStream);
////        System.out.println(icdJson);
////        System.out.println(testJson);
////        JSONObject data = new JSONObject("{\"foo\" : 1234}");
////        System.out.println(testXml);
//        boolean result=validateXmlWithICD(icdJson,testJson);
//        System.out.println(result);
//        icdJson=
    }


}

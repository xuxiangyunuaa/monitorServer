//package handler.user;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import io.vertx.core.json.JsonObject;
//import org.nit.monitorserver.proto.TestAnalysisCfg;
//
////import
//public class testProtoImport {
//    public static void main(String[] args) throws InvalidProtocolBufferException {
//        TestAnalysisCfg.EVT_MEM_EFFECT_CFG.Builder evt_mem=TestAnalysisCfg.EVT_MEM_EFFECT_CFG.newBuilder();
//        evt_mem.setEndTime(151313).setEvtId(15).setOptId(4).setStartTime(145366);
//        proto.TestAnalysisRtn.EVT_MEM_EFFECT_RTN.Builder evt_mem_rtn=proto.TestAnalysisRtn.EVT_MEM_EFFECT_RTN.newBuilder();
//        evt_mem_rtn.setEvtId(15).setMemEffect(456).setOptId(45);
//        evt_mem.setEvtMemEffectRtn(evt_mem_rtn);
//        byte[] test=evt_mem.build().toByteArray();
//        System.out.println(test);
//        JsonObject res=new JsonObject(TestAnalysisCfg.EVT_MEM_EFFECT_CFG.parseFrom(test).toString());
//        System.out.println(res);
//    }
//}

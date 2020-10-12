package com.jTranProc.Parser;

import com.jTranProc.Common.BiDirectionalTTPSvc;
import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.ThreadedTpSvc;
import com.jTranProc.Common.UtilityClass.JLogger;

import javax.json.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.*;

public class JSONTransactionParser extends BiDirectionalTTPSvc {

    public JSONTransactionParser(ParserConfig config){
        this.Config = config;
    }

    @Override
    protected void ProcessRequestMessage(Object msg) {
        TranMessage tmsg = (TranMessage)msg;
        String sJson = this.CreateJsonPacket(tmsg);
        JLogger.Get().WriteTrace("Outgoing-request json generated [" + sJson + "]");
        this.MsgBroker.RouteMsgToService(ServiceType.SVC_REST_CLIENT, sJson);

    }

    private String CreateJsonPacket(TranMessage tmsg) {
        JsonObjectBuilder jOBr = Json.createObjectBuilder();
        JLogger.Get().WriteTrace("Creating JSON object");
        List<String> alkeys = Collections.list(tmsg.GetKeys());
        for(String key : alkeys){
            String val = tmsg.GetField(key);
            jOBr.add(key, val);
        }
        JsonObject jobj = jOBr.build();
        return jobj.toString();
    }

    @Override
    protected void ProcessResponseMessage(Object msg) {
        String sMsg = (String)msg;
        TranMessage tmsg = this.ParseJsonPacket(sMsg);
        JLogger.Get().WriteTrace("Created Response Tran Message from JSON:- ");
        tmsg.LogMessage();
        this.MsgBroker.RouteMsgToService(ServiceType.SVC_TRAN_HANDLER_RESPONSE, tmsg);
    }

    private TranMessage ParseJsonPacket(String sMsg) {
        TranMessage tranMsg = new TranMessage();
        InputStream strm = new ByteArrayInputStream(sMsg.getBytes());
        JsonReader rdr = Json.createReader(strm);
        JsonObject jsonRoot = rdr.readObject();
        for(Map.Entry<String, JsonValue> itr : jsonRoot.entrySet()){
            String sName = itr.getKey();
            String sValue = itr.getValue().toString();
        }
        return tranMsg;
    }

    @Override
    protected ServiceType GetResponseServiceType() { return ServiceType.SVC_JSON_PARSER_RESPONSE; }

    @Override
    protected ServiceType GetRequestServiceType() { return ServiceType.SVC_JSON_PARSER; }

    private ParserConfig Config;
}

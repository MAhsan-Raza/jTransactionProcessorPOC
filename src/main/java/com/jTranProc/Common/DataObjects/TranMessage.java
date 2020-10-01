package com.jTranProc.Common.DataObjects;

import com.jTranProc.Common.Enums.LogLevel;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TranMessage {

    public String GetField(String Tag) {

        return Msg.get(Tag);
    }

    public void SetField(String Tag, String Value){
        Msg.put(Tag, Value);
    }

    public void LogMessage(){

        StringBuilder sLog = new StringBuilder();
        sLog.append("------------Logging Tran Msg---------------------");
        sLog.append(System.lineSeparator());

        for(Map.Entry itr : this.Msg.entrySet())
        {
            sLog.append(String.format("%s:\t\t%s", itr.getKey(), itr.getValue()));
            sLog.append(System.lineSeparator());
        }

        sLog.append("-------------------------------------------------");
        sLog.append(System.lineSeparator());

        JLogger.Get().Write(LogLevel.TRACE, sLog);
    }

    public String GetStringMsg() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> Set : this.Msg.entrySet()){
            sb.append(Set.getKey() + "=" + Set.getValue() + ",");
        }
            return sb.toString();
    }

    private final ConcurrentHashMap<String, String> Msg = new ConcurrentHashMap<>();
}

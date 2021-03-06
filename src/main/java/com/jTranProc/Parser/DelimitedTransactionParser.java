package com.jTranProc.Parser;

import com.jTranProc.Common.BiDirectionalTTPSvc;
import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.ThreadedTpSvc;
import com.jTranProc.Common.UtilityClass.JLogger;

public class DelimitedTransactionParser extends BiDirectionalTTPSvc {

    public DelimitedTransactionParser(ParserConfig config){
        this.Config = config;
    }

    @Override
    protected void ProcessRequestMessage(Object msg) {
        JLogger.Get().WriteTrace("DelimitedTransactionParser got message: " + (String) msg);
        TranMessage tranMessage = this.CreateTranMessage((String) msg);
        JLogger.Get().WriteTrace("Created Tran Message: " + msg);
        tranMessage.LogMessage();
        this.MsgBroker.RouteMsgToService(ServiceType.SVC_TRAN_HANDLER, tranMessage);
    }

    @Override
    protected void ProcessResponseMessage(Object msg) {

    }

    @Override
    protected ServiceType GetResponseServiceType() { return ServiceType.SVC_DELIMITED_PARSER; }

    @Override
    protected ServiceType GetRequestServiceType() { return ServiceType.SVC_DELIMITED_PARSER_RESPONSE; }

    private TranMessage CreateTranMessage(String msg) {
        TranMessage rezMessage = new TranMessage();
        String[] Fields = msg.split(this.Config.FieldDelimiter);
        JLogger.Get().WriteTrace("DelimitedTransactionParser ["
                + Fields.length + "] Tags found in incoming message");

        for(String sTagVal : Fields){
            String[] arrTV = sTagVal.split(this.Config.ValueDelimiter);
            if(arrTV.length == 2){
                rezMessage.SetField(arrTV[0], arrTV[1]);
            }
            else
                JLogger.Get().WriteTrace("Invalid field received: [" + sTagVal + "]");
        }

        return rezMessage;
    }

    private ParserConfig Config;
}

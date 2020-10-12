package com.jTranProc.Processor;

import com.jTranProc.Common.BiDirectionalTTPSvc;
import com.jTranProc.Common.DataObjects.ProcessorConfig;
import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.ThreadedTpSvc;
import com.jTranProc.Common.UtilityClass.JLogger;
import com.jTranProc.DataAccessLayer.OracleTransactionLogger;

public class TransactionHandler extends BiDirectionalTTPSvc {

    public TransactionHandler(ProcessorConfig config){
        this.Config = config;
        }

    @Override
    protected ServiceType GetResponseServiceType() { return ServiceType.SVC_TRAN_HANDLER_RESPONSE; }

    @Override
    protected ServiceType GetRequestServiceType() { return ServiceType.SVC_TRAN_HANDLER; }

    @Override
    protected void ProcessRequestMessage(Object msg) {
        this.HandleTransactionRequest((TranMessage) msg);
        this.MsgBroker.RouteMsgToService(ServiceType.SVC_JSON_PARSER, msg);
    }

    private Boolean HandleTransactionRequest(TranMessage tmsg) {
        try {
            OracleTransactionLogger otl = new OracleTransactionLogger();
            if (otl.Initialize(this.Config.ConStr,
                    this.Config.UID, this.Config.PWD) == false) {
                JLogger.Get().WriteTrace("Failed to initialize OracleTransactionLogger");
                return false;
            }
            if (otl.AddToTranLog(tmsg) == false) {
                JLogger.Get().WriteTrace("AddToTranLog Failed");
                return false;
            }
            return true;
        }
        catch (Exception ex){
            JLogger.Get().WriteTrace("HandleTransaction crashed, error: " + ex.getMessage());
        }
        return false;
    }

    @Override
    protected void ProcessResponseMessage(Object msg) {
        this.HandleTransactionResponse((TranMessage) msg);
        this.MsgBroker.RouteMsgToService(ServiceType.SVC_DELIMITED_PARSER_RESPONSE, msg);
    }

    protected void HandleTransactionResponse(TranMessage tmsg){
        try {
            OracleTransactionLogger otl = new OracleTransactionLogger();
            if (otl.Initialize(this.Config.ConStr,
                    this.Config.UID, this.Config.PWD) == false) {
                JLogger.Get().WriteTrace("Failed to initialize OracleTransactionLogger");
                return ;
            }
            if (otl.AddToResponseLog(tmsg) == false) {
                JLogger.Get().WriteTrace("AddToResponseLog Failed");
                return ;
            }
            return ;
        }
        catch (Exception ex){
            JLogger.Get().WriteTrace("HandleTransaction crashed while handling response, error: " + ex.getMessage());
        }
        return ;
    }

    private ProcessorConfig Config;
}

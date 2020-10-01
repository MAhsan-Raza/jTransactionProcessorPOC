package com.jTranProc.Processor;

import com.jTranProc.Common.DataObjects.ProcessorConfig;
import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.ITransactionHandler;
import com.jTranProc.Common.UtilityClass.JLogger;
import com.jTranProc.DataAccessLayer.OracleTransactionLogger;

import java.util.ArrayList;

public class TransactionHandler implements ITransactionHandler {

    public TransactionHandler(ProcessorConfig config){
        this.IsRunning = true;
        this.Config = config;
        this.ProcessorThreadGroup = new ArrayList<>();
        for(Integer i=0; i<this.Config.Threads;i++){
            ProcessorThreadGroup.add(new Thread(this::ThreadedProcessorMethod));
        }}

    @Override
    public void SetMsgBroker(IMsgBroker imb) {
        this.MsgBroker = imb;
    }

    @Override
    public void Start() {
        JLogger.Get().WriteTrace("Starting Transaction Processor threads");
        for(Thread t : this.ProcessorThreadGroup)
            t.start();
    }

    @Override
    public void Stop() {

    }

    protected void ThreadedProcessorMethod() {
        while (this.IsRunning) {

            try {
                TranMessage tmsg = (TranMessage) this.MsgBroker.TryGetMessage(ServiceType.SVC_TRAN_HANDLER);
                JLogger.Get().WriteTrace("Transaction Processor got message: ");
                this.HandleTransaction(tmsg);
                this.MsgBroker.RouteMsgToService(ServiceType.SVC_JSON_PARSER, tmsg);
            } catch (InterruptedException ex) {
                JLogger.Get().WriteTrace("Transaction Processor thread Interrupted, error: " + ex.getMessage());
                return;
            }
        }
    }

    private Boolean HandleTransaction(TranMessage tmsg) {
        try {
            OracleTransactionLogger otl = new OracleTransactionLogger();
            if (otl.Initialize("jdbc:oracle:thin:@192.168.0.138:1521:ipath",
                    "impact_psopsp1", "Ipath_2020") == false) {
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

    private volatile Boolean IsRunning;
    private ProcessorConfig Config;
    private ArrayList<Thread> ProcessorThreadGroup;
    private IMsgBroker MsgBroker;
}

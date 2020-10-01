package com.jTranProc.Parser;

import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.IParser;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.util.ArrayList;

public class DelimitedTransactionParser implements IParser {

    public DelimitedTransactionParser(ParserConfig config){
        this.IsRunning = true;
        this.Config = config;
        this.ParserThreadGroup = new ArrayList<>();
        for(Integer i=0; i<this.Config.ParserThreads;i++){
            ParserThreadGroup.add(new Thread(this::ThreadedParserMethod));
        }
    }

    @Override
    public void SetMsgBroker(IMsgBroker imb) {
        this.MsgBroker = imb;
    }

    @Override
    public void Start() {
        JLogger.Get().WriteTrace("Starting DelimitedTransactionParser threads");
        for(Thread t : this.ParserThreadGroup)
            t.start();
    }

    @Override
    public void Stop() {

    }

    protected void ThreadedParserMethod(){
        while(this.IsRunning){

            try {
                String msg = (String) this.MsgBroker.TryGetMessage(ServiceType.SVC_DELIMITED_PARSER);
                JLogger.Get().WriteTrace("DelimitedTransactionParser got message: " + msg);
                TranMessage tranMessage = this.CreateTranMessage(msg);
                JLogger.Get().WriteTrace("Created Tran Message: " + msg);
                tranMessage.LogMessage();
                this.MsgBroker.RouteMsgToService(ServiceType.SVC_TRAN_HANDLER, tranMessage);
            }
            catch (InterruptedException ex)
            {
                JLogger.Get().WriteTrace("DelimitedTransactionParser thread Interrupted, error: " + ex.getMessage());
                return;
            }
        }

    }

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

    private volatile Boolean IsRunning;

    private IMsgBroker MsgBroker;
    private ParserConfig Config;
    private ArrayList<Thread> ParserThreadGroup;
}

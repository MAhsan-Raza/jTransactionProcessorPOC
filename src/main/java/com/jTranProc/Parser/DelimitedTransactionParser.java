package com.jTranProc.Parser;

import com.jTranProc.Common.DataObjects.ParserConfig;
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
        for(Integer i=0; i<this.ParserThreads;i++){
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
            }
            catch (InterruptedException ex)
            {
                JLogger.Get().WriteTrace("DelimitedTransactionParser thread Interrupted, error: " + ex.getMessage());
                return;
            }
        }

    }

    private volatile Boolean IsRunning;

    private IMsgBroker MsgBroker;
    private ParserConfig Config;
    private final Integer ParserThreads = 5;
    private ArrayList<Thread> ParserThreadGroup;
}

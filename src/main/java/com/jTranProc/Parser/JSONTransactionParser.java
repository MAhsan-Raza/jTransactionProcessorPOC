package com.jTranProc.Parser;

import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.IParser;

public class JSONTransactionParser implements IParser {

    public JSONTransactionParser(ParserConfig config){
        this.Config = config;
    }

    @Override
    public void SetMsgBroker(IMsgBroker imb) {
        this.MsgBroker = imb;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Stop() {

    }

    private ParserConfig Config;
    private IMsgBroker MsgBroker;
}

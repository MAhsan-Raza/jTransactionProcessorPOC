package com.jTranProc.Common.Factories;

import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.Enums.EnParserType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.ITPSvc;
import com.jTranProc.Parser.DelimitedTransactionParser;
import com.jTranProc.Parser.JSONTransactionParser;

public class ParserFactory {

    public static ITPSvc CreateParser(EnParserType pt, IMsgBroker broker, ParserConfig config){

        ITPSvc parser = null;
        switch (pt)
        {
            case DELIMITED_PARSER:
                parser = new DelimitedTransactionParser(config);
                break;
            case JSON_PARSER:
                parser = new JSONTransactionParser(config);
                break;
            default:
                throw new RuntimeException("Parser not implemented: " + pt.toString());
        }

        if(parser != null)
            parser.Initialize(broker, config.ParserThreads);

        return parser;
    }
}

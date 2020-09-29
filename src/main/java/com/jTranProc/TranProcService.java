package com.jTranProc;

import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.Enums.EnAdaptorType;
import com.jTranProc.Common.Enums.EnParserType;
import com.jTranProc.Common.Enums.LogLevel;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Factories.AdaptorFactory;
import com.jTranProc.Common.Factories.ParserFactory;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.IParser;
import com.jTranProc.Common.Interfaces.IAdaptor;
import com.jTranProc.Common.Interfaces.ITransactionHandler;
import com.jTranProc.Common.UtilityClass.JLogger;
import com.jTranProc.Common.UtilityClass.MessageBroker;
import com.jTranProc.DataAccessLayer.ConfigurationStore;
import com.jTranProc.Processor.TransactionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TranProcService {

    public void start(){

        JLogger.Get().Write(LogLevel.TRACE, "Creating Member Objects");

        try {
            ConfigurationStore cfgLdr = this.LoadConfig();
            this.CreateMembers(cfgLdr);
            this.InitiateMessagebroker();

            this.StartProcessorServices();
        }
        catch (FileNotFoundException ex)
        {
            JLogger.Get().Write(LogLevel.TRACE, "Config file not found. Error: "
                    + ex.getMessage() + System.lineSeparator() + ex.getStackTrace());
        }
        catch (Exception ex)
        {
            JLogger.Get().Write(LogLevel.TRACE, "Error: "
                    + ex.getMessage() + System.lineSeparator() + ex.getStackTrace());
        }
    }

    private ConfigurationStore LoadConfig() throws FileNotFoundException {

        ConfigurationStore cfgLdr = new ConfigurationStore();
        cfgLdr.init(ConfigPath);
        return cfgLdr;
    }

    private void StartProcessorServices() {
    }

    private void InitiateMessagebroker() {
        this.MessageBroker.InitiateMsgQueue(ServiceType.SVC_TCP_SERVER);
        this.MessageBroker.InitiateMsgQueue(ServiceType.SVC_DELIMITED_PARSER);
        this.MessageBroker.InitiateMsgQueue(ServiceType.SVC_TRAN_HANDLER);
        this.MessageBroker.InitiateMsgQueue(ServiceType.SVC_JSON_PARSER);
        this.MessageBroker.InitiateMsgQueue(ServiceType.SVC_REST_CLIENT);
    }

    private void CreateMembers(ConfigurationStore cfgLdr) throws Exception {

        AdaptorConfig TcpSrvrCfg = cfgLdr.ReadTcpServerAdaptorConfig();
        AdaptorConfig RestClntCfg = cfgLdr.ReadRestClientConfig();
        ParserConfig DelimCfg = cfgLdr.ReadDelimitedParserConfig();
        ParserConfig JsonCfg = cfgLdr.ReadJsonParserConfig();

        this.MessageBroker = new MessageBroker();
        this.TCPServer = AdaptorFactory.CreateAdaptor(EnAdaptorType.TCP_SERVER, this.MessageBroker, TcpSrvrCfg);
        this.RestClient = AdaptorFactory.CreateAdaptor(EnAdaptorType.REST_CLIENT, this.MessageBroker, RestClntCfg);
        this.DelimitedParser = ParserFactory.CreateParser(EnParserType.DELIMITED_PARSER, this.MessageBroker, DelimCfg);
        this.JsonParser = ParserFactory.CreateParser(EnParserType.JSON_PARSER, this.MessageBroker, JsonCfg);

        this.TransactionHandler = new TransactionHandler();
    }

    private static String ConfigPath =
            "C:\\Users\\ahsan.razaa\\IdeaProjects\\jTransactionProcessorPOC\\config.json";

    private IAdaptor TCPServer;
    private IAdaptor RestClient;
    private ITransactionHandler TransactionHandler;
    private IParser DelimitedParser;
    private IParser JsonParser;
    private IMsgBroker MessageBroker;
}

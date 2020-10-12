package com.jTranProc.Common.Factories;

import com.jTranProc.Adaptor.RESTClient;
import com.jTranProc.Adaptor.TCPServer;
import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.Enums.EnAdaptorType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.ITPSvc;

public class AdaptorFactory {

    public static ITPSvc CreateAdaptor(EnAdaptorType at, IMsgBroker broker, AdaptorConfig config) throws Exception {

        ITPSvc adaptor = null;
        switch (at)
        {
            case TCP_SERVER:
                adaptor = new TCPServer(config);
                break;
            case REST_CLIENT:
                adaptor = new RESTClient(config.URI);
                break;
            default:
                throw new RuntimeException("Adaptor not implemented: " + at.toString());
        }

        if(adaptor != null)
            adaptor.Initialize(broker, 1);

        return adaptor;
    }

}

package com.jTranProc.Common.Factories;

import com.jTranProc.Adaptor.RESTClient;
import com.jTranProc.Adaptor.TCPServer;
import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.Enums.EnAdaptorType;
import com.jTranProc.Common.Interfaces.IAdaptor;
import com.jTranProc.Common.Interfaces.IMsgBroker;

public class AdaptorFactory {

    public static IAdaptor CreateAdaptor(EnAdaptorType at, IMsgBroker broker, AdaptorConfig config) throws Exception {

        IAdaptor adaptor = null;
        switch (at)
        {
            case TCP_SERVER:
                adaptor = new TCPServer(config);
                break;
            case REST_CLIENT:
                adaptor = new RESTClient();
                break;
            default:
                throw new RuntimeException("Adaptor not implemented: " + at.toString());
        }

        if(adaptor != null)
            adaptor.SetMsgBroker(broker);

        return adaptor;
    }

}

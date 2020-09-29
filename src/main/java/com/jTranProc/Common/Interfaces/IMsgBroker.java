package com.jTranProc.Common.Interfaces;

import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;

public interface IMsgBroker {

    public void RouteMsgToService(ServiceType svcTp, TranMessage msg);
    public TranMessage TryGetMessage(ServiceType svcTp);
    public void InitiateMsgQueue(ServiceType svcTp);
    public void PurgeMsgQueue(ServiceType svcTp);
    public void PurgeAllQueues();
}

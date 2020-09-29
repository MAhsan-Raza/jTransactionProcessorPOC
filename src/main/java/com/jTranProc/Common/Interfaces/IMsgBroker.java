package com.jTranProc.Common.Interfaces;

import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;

public interface IMsgBroker {

    public void RouteMsgToService(ServiceType svcTp, Object msg);
    public Object TryGetMessage(ServiceType svcTp) throws InterruptedException;
    public void InitiateMsgQueue(ServiceType svcTp);
    public void PurgeMsgQueue(ServiceType svcTp);
    public void PurgeAllQueues();
}

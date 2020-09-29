package com.jTranProc.Common.UtilityClass;

import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageBroker implements IMsgBroker {

    public MessageBroker()
    {
        this.Queues = new ConcurrentHashMap<ServiceType, ConcurrentLinkedQueue<TranMessage>>();
    }

    @Override
    public void RouteMsgToService(ServiceType svcTp, TranMessage msg) {
        if(Queues.keySet().contains(svcTp) == false)
            throw new IllegalArgumentException(
                    "MessageBroker.RouteMsgToService Service type not found: [" + svcTp.toString() + "]");

        ConcurrentLinkedQueue<TranMessage> q = this.Queues.get(svcTp);
        q.add(msg);
    }

    @Override
    public TranMessage TryGetMessage(ServiceType svcTp) {
        if(Queues.keySet().contains(svcTp) == false)
            throw new IllegalArgumentException(
                    "MessageBroker.RouteMsgToService Service type not found: [" + svcTp.toString() + "]");

        ConcurrentLinkedQueue<TranMessage> q = this.Queues.get(svcTp);

        if(q.isEmpty())
            return null;

        return q.remove();
    }

    @Override
    public void InitiateMsgQueue(ServiceType svcTp) {
        this.Queues.put(svcTp, new ConcurrentLinkedQueue<TranMessage>());
    }

    @Override
    public void PurgeMsgQueue(ServiceType svcTp) {

    }

    @Override
    public void PurgeAllQueues() {

    }

    private ConcurrentHashMap<ServiceType, ConcurrentLinkedQueue<TranMessage>> Queues;
}

package com.jTranProc.Common.UtilityClass;

import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageBroker implements IMsgBroker {

    public MessageBroker()
    {
        this.Queues = new ConcurrentHashMap<ServiceType, BlockingQueue<Object>>();
    }

    @Override
    public void RouteMsgToService(ServiceType svcTp, Object msg) {
        if(Queues.keySet().contains(svcTp) == false)
            throw new IllegalArgumentException(
                    "MessageBroker.RouteMsgToService Service type not found: [" + svcTp.toString() + "]");

        BlockingQueue<Object> q = this.Queues.get(svcTp);
        q.add(msg);
        JLogger.Get().WriteTrace("Enqueued message for service: " + svcTp.toString());
    }

    @Override
    public Object TryGetMessage(ServiceType svcTp) throws InterruptedException {
        if(Queues.keySet().contains(svcTp) == false)
            throw new IllegalArgumentException(
                    "MessageBroker.RouteMsgToService Service type not found: [" + svcTp.toString() + "]");

        BlockingQueue<Object> q = this.Queues.get(svcTp);

        return q.take();
    }

    @Override
    public void InitiateMsgQueue(ServiceType svcTp) {
        this.Queues.put(svcTp, new LinkedBlockingQueue<Object>());
    }

    @Override
    public void PurgeMsgQueue(ServiceType svcTp) {

    }

    @Override
    public void PurgeAllQueues() {

    }

    private ConcurrentHashMap<ServiceType, BlockingQueue<Object>> Queues;
}

package com.jTranProc.Common;

import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.util.ArrayList;

public abstract class BiDirectionalTTPSvc extends ThreadedTpSvc {

    @Override
    public void Initialize(IMsgBroker imb, int nThreads) {
        super.Initialize(imb, nThreads);

        this.ResponseThreadGroups = new ArrayList<>();
        for(Integer i=0; i<nThreads; i++){
            ResponseThreadGroups.add(new Thread(this::ResponseProcThreadMethod));
        }
    }

    @Override
    public void Start() {
        super.Start();

        JLogger.Get().WriteTrace("Starting ResponseThreadGroups threads");
        for(Thread t : this.ResponseThreadGroups)
            t.start();
    }

    @Override
    public void Stop() {
        super.Stop();

        JLogger.Get().WriteTrace("Stopping ResponseThreadGroups threads");
        for(Thread t : this.ResponseThreadGroups)
            t.start();
    }

    @Override final
    public void ProcessMessage(Object msg) {
        this.ProcessRequestMessage(msg);
    }

    @Override
    public ServiceType GetServiceType() { return this.GetRequestServiceType(); }

    private void ResponseProcThreadMethod(){
        while(super.IsRunning) {

            try {
                Object msg = this.MsgBroker.TryGetMessage(this.GetResponseServiceType());
                this.ProcessResponseMessage(msg);
            } catch (InterruptedException ex) {
                JLogger.Get().WriteTrace("ResponseProcThreadMethod thread Interrupted for service ["
                        + this.GetResponseServiceType().toString() + "], error: " + ex.getMessage());
                return;
            }
        }
    }

    protected abstract void ProcessRequestMessage(Object msg);
    protected abstract void ProcessResponseMessage(Object msg);
    protected abstract ServiceType GetResponseServiceType();
    protected abstract ServiceType GetRequestServiceType();

    private ArrayList<Thread> ResponseThreadGroups;
}

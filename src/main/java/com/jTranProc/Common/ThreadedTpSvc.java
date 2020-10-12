package com.jTranProc.Common;

import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.Interfaces.ITPSvc;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.util.ArrayList;

public abstract class ThreadedTpSvc implements ITPSvc {

    @Override
    public void Initialize(IMsgBroker imb, int nThreads) {
        this.NumThreads = nThreads;
        this.MsgBroker = imb;
        this.IsRunning = true;
        this.SvcThreadGroups = new ArrayList<>();
        for(Integer i=0; i<this.NumThreads; i++){
            SvcThreadGroups.add(new Thread(this::ProcThreadMethod));
        }
    }


    @Override
    public void Start() {
        JLogger.Get().WriteTrace("Starting SvcThreadGroups threads");
        for(Thread t : this.SvcThreadGroups)
            t.start();
    }

    @Override
    public void Stop() {
        try {
            JLogger.Get().WriteTrace("Stopping SvcThreadGroups threads, svc type: "
                    + this.GetServiceType().toString());
            this.IsRunning = false;
            for (Thread t : this.SvcThreadGroups)
                t.join();
        }
        catch (Exception ex){
            JLogger.Get().WriteTrace(
                    "Failed stopping SvcThreadGroups threads, svc type: "
                            + this.GetServiceType().toString());
        }
    }

    protected void ProcThreadMethod(){
        while(this.IsRunning) {

            try {
                Object msg = this.MsgBroker.TryGetMessage(this.GetServiceType());
                this.ProcessMessage(msg);
            } catch (InterruptedException ex) {
                JLogger.Get().WriteTrace("ProcThreadMethod thread Interrupted for service ["
                        + this.GetServiceType().toString() + "], error: " + ex.getMessage());
                return;
            }
        }
    }

    public abstract void ProcessMessage(Object msg);
    public abstract ServiceType GetServiceType();

    protected IMsgBroker MsgBroker;

    private int NumThreads;
    protected volatile Boolean IsRunning;
    private ArrayList<Thread> SvcThreadGroups;
}

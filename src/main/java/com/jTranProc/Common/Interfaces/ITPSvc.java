package com.jTranProc.Common.Interfaces;

public interface ITPSvc {

    public void Initialize(IMsgBroker imb, int nThreads);

    public void Start();
    public void Stop();
}

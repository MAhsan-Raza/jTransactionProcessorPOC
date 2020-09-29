package com.jTranProc.Adaptor;

import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.Interfaces.IAdaptor;
import com.jTranProc.Common.Interfaces.IMsgBroker;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements IAdaptor {
    @Override
    public void SetMsgBroker(IMsgBroker imb) {
        this.IMsgBroker = imb;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Stop() {

    }

    public TCPServer(AdaptorConfig cfg) throws Exception {

        this.IsRunning = true;
        this.Config = cfg;
        this.TcpServer = new ServerSocket(this.Config.ListenPort);

        this.ServerThread = new Thread(this::ServerThreadMethod);
    }

    protected void ServerThreadMethod() {

        while(IsRunning) {
            try {
                Socket TcpClient = this.TcpServer.accept();

            } catch (Exception ex) {
            }
        }
    }

    private ServerSocket TcpServer;
    private Runnable ServerThread;
    private Runnable OutgoingQReader;
    private IMsgBroker IMsgBroker;
    private AdaptorConfig Config;
    private volatile Boolean IsRunning;
}

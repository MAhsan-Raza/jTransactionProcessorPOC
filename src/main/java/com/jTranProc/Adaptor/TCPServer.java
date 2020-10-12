package com.jTranProc.Adaptor;

import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.ThreadedTpSvc;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends ThreadedTpSvc {

    @Override
    public void Start() {
        super.Start();
        this.ServerThread.start();

        try {
            this.ServerThread.join();
        }
        catch (InterruptedException e)
        {
            JLogger.Get().WriteTrace("Accepting tcp clients on port: " + this.Config.ListenPort);
        }
    }

    @Override
    public void ProcessMessage(Object msg) {
        //Get related ClientConnection from map
        //Return Outgoing message
    }

    @Override
    public ServiceType GetServiceType() {
        return ServiceType.SVC_TCP_SERVER;
    }

    public TCPServer(AdaptorConfig cfg) throws Exception {

        this.Config = cfg;
        this.IsServerRunning = true;
        this.TcpServer = new ServerSocket(this.Config.ListenPort);
        this.ServerThread = new Thread(this::ServerThreadMethod);
    }

    protected void ServerThreadMethod() {

        while(IsServerRunning) {
            try {
                JLogger.Get().WriteTrace("Accepting tcp clients on port: " + this.Config.ListenPort);
                Socket TcpClient = this.TcpServer.accept();
                JLogger.Get().WriteTrace("Got client connection starting reader thread");
                new Thread(new TCPClientReader(
                        TcpClient, this.Config.MetaLength, this.MsgBroker)).start();

            } catch (Exception ex) {
                JLogger.Get().WriteTrace("ServerThreadMethod crashed, Error: " + ex.getMessage());
            }
        }
    }

    private ServerSocket TcpServer;
    private Thread ServerThread;
    private AdaptorConfig Config;
    private volatile Boolean IsServerRunning;
}

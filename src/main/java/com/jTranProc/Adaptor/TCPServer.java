package com.jTranProc.Adaptor;

import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.Interfaces.IAdaptor;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements IAdaptor {
    @Override
    public void SetMsgBroker(IMsgBroker imb) {
        this.MsgBroker = imb;
    }

    @Override
    public void Start() {
        this.ServerThread.start();

        try {
            this.ServerThread.join();
        }
        catch (InterruptedException e)
        {}
    }

    @Override
    public void Stop() {

    }

    @Override
    public AdaptorConfig GetAdaptorConfig() {
        return null;
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
    private Runnable OutgoingQReader;
    private IMsgBroker MsgBroker;
    private AdaptorConfig Config;
    private volatile Boolean IsRunning;
}

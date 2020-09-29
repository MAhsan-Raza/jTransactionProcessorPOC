package com.jTranProc.Adaptor;


import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.Interfaces.IMsgBroker;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClientReader implements Runnable {

    public TCPClientReader(Socket client, Integer metalen, IMsgBroker messageBroker)
    {
        this.ClientConnection = client;
        this.MetaLength = metalen;
        this.MessageBroker = messageBroker;
    }

    @Override
    public void run() {
        try {
            JLogger.Get().WriteTrace("[TCPClientReader] Reading meta length of: " + this.MetaLength.toString());

            InputStreamReader istream = new InputStreamReader(this.ClientConnection.getInputStream());
            String MetaLenBuffer = this.readByLength(istream, this.MetaLength);

            Integer mtl = Integer.parseInt(MetaLenBuffer);
            JLogger.Get().WriteTrace("[TCPClientReader] Reading packet of size: " + mtl.toString());
            String sPacket = this.readByLength(istream, mtl);
            JLogger.Get().WriteTrace("[TCPClientReader] Got packet: " + sPacket);
            JLogger.Get().WriteTrace("Routing inbound message to parser service");
            this.MessageBroker.RouteMsgToService(ServiceType.SVC_DELIMITED_PARSER, sPacket);
        }
        catch (Exception ex)
        {
            JLogger.Get().WriteTrace("[TCPClientReader] crashed, Error: " + ex.getMessage());
        }
    }

    private String readByLength(InputStreamReader istream, Integer reqLength) throws IOException {
        char[] charBuffer = new char[reqLength];
        int received = 0;
        while(received != reqLength)
        {
            received = istream.read(charBuffer, received, reqLength);
        }

        return new String(charBuffer, 0, reqLength);
    }

    private Integer MetaLength;
    private Socket ClientConnection;
    IMsgBroker MessageBroker;
}

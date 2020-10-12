package com.jTranProc.Adaptor;

import com.jTranProc.Common.Enums.ServiceType;
import com.jTranProc.Common.ThreadedTpSvc;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RESTClient extends ThreadedTpSvc {

    public RESTClient(String uri){
        this.URI = uri;
    }

    @Override
    public void ProcessMessage(Object msg) {

        try {
            URL url = new URL(this.URI);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.getOutputStream().write(((String)msg).getBytes());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            StringBuilder sbOutPkt = new StringBuilder();
            JLogger.Get().WriteTrace("Got response from Server .... ");
            while ((output = br.readLine()) != null) {
                sbOutPkt.append(output);
            }
            String sOutPkt = sbOutPkt.toString();
            JLogger.Get().WriteTrace(sOutPkt);
            JLogger.Get().WriteTrace("..................................");
            this.MsgBroker.RouteMsgToService(ServiceType.SVC_JSON_PARSER_RESPONSE, sOutPkt);
        }
        catch (Exception ex){
            JLogger.Get().WriteTrace("RESTClient postMethod crashed, Error: " + ex.getMessage());
        }
    }

    @Override
    public ServiceType GetServiceType() {
        return ServiceType.SVC_REST_CLIENT;
    }

    private String URI;
}

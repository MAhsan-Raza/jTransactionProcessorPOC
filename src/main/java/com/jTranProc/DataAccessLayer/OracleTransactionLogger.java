package com.jTranProc.DataAccessLayer;

import com.jTranProc.Common.DataObjects.TranMessage;
import com.jTranProc.Common.UtilityClass.JLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class OracleTransactionLogger {

    public OracleTransactionLogger(){
    }

    public Boolean Initialize(String constr, String uid, String pwd){
        this.ConStr = constr;
        this.UID = uid;
        this.PWD = pwd;
        return true;
    }

    public Boolean AddToTranLog(TranMessage msg){

        String TranKey = "";
        String TranKeyValue = msg.GetField("STAN");
        if(TranKeyValue.isEmpty()){
            JLogger.Get().WriteTrace("Error: STAN not found in message, rejecting transaction");
            return false;
        }
        String TranBuffer = msg.GetStringMsg();

        return this.AddToTranLogInternal(TranKeyValue, TranBuffer);
    }

    public Boolean AddToTranLogInternal(String tranKey, String tranMsg){

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(this.ConStr, this.UID, this.PWD);
            String qry = "INSERT INTO TEST_TRAN_LOG (TRAN_KEY, TRAN_BUFFER) VALUES ("
                    + tranKey + ", " + tranMsg + ");";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(qry);
        }
        catch (Exception ex){
            JLogger.Get().WriteTrace("Failed adding transaction to DB, error: " + ex.getMessage());
            return false;
        }

        return true;
    }

    private String ConStr;
    private String UID;
    private String PWD;

    public boolean AddToResponseLog(TranMessage tmsg) {
        return false;
    }
}

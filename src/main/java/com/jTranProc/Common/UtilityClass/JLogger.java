package com.jTranProc.Common.UtilityClass;

import com.jTranProc.Common.Enums.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JLogger {

    private JLogger()
    {
        PrintToConsole = true;
        DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.Write(LogLevel.ERROR, "-----------------------------------");
    }

    private Boolean PrintToConsole;
    private DateTimeFormatter DTF;
    private static JLogger Instance;
    public static JLogger Get(){

        if(Instance == null)
            Instance = new JLogger();
        return Instance;
    }

    public void Write(LogLevel logLevel, StringBuilder logLine)
    {
        this.Write(logLevel, logLine.toString());
    }

    public void Write(LogLevel logLevel, String logLine)
    {
        if(PrintToConsole)
            System.out.println(DTF.format(LocalDateTime.now()) + "\t|\t" + logLine);
    }
}

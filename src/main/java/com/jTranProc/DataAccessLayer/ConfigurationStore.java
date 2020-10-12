package com.jTranProc.DataAccessLayer;

import com.jTranProc.Common.DataObjects.AdaptorConfig;
import com.jTranProc.Common.DataObjects.ParserConfig;
import com.jTranProc.Common.DataObjects.ProcessorConfig;
import com.jTranProc.Common.UtilityClass.JLogger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.io.*;

public class ConfigurationStore {

    public void init(String path) throws FileNotFoundException {
        this.LoadJsonConfig(path);
    }

    private void LoadJsonConfig(String configPath) throws FileNotFoundException {

        InputStream strm = new FileInputStream(configPath);
        JsonReader rdr = Json.createReader(strm);
        this.jsonRoot = rdr.readObject();
    }

    public AdaptorConfig ReadTcpServerAdaptorConfig(){

        AdaptorConfig cfg = new AdaptorConfig();
        JsonObject jsTcpSrvr = this.jsonRoot.getJsonObject("TcpServer");
        cfg.ListenPort = jsTcpSrvr.getInt("ListenPort");
        cfg.MetaLength = jsTcpSrvr.getInt("MetaLength");
        return cfg;
    }

    public AdaptorConfig ReadRestClientConfig(){
        AdaptorConfig cfg = new AdaptorConfig();
        JsonObject jsTcpSrvr = this.jsonRoot.getJsonObject("RestClient");
        cfg.URI = jsTcpSrvr.getString("Address");
        return cfg;
    }
    public ParserConfig ReadJsonParserConfig(){
        ParserConfig cfg = new ParserConfig();
        JsonObject js = this.jsonRoot.getJsonObject("DelimitedParser");
        cfg.ParserThreads = js.getInt("Threads");
        return cfg;
    }
    public ParserConfig ReadDelimitedParserConfig(){
        ParserConfig cfg = new ParserConfig();
        JsonObject js = this.jsonRoot.getJsonObject("DelimitedParser");
        cfg.ParserThreads = js.getInt("Threads");
        cfg.FieldDelimiter = js.getString("FieldDelimiter");
        cfg.ValueDelimiter = js.getString("ValueDelimiter");
        JLogger.Get().WriteTrace("Loaded Delimited Parser Config: Threads[" + cfg.ParserThreads
            + "], FieldDelimiter[" + cfg.FieldDelimiter + "], ValueDelimiter[" + cfg.ValueDelimiter + "]");
        return cfg;
    }

    public ProcessorConfig ReadTranProcessorConfig() {
        ProcessorConfig cfg = new ProcessorConfig();
        JsonObject js = this.jsonRoot.getJsonObject("Processor");
        cfg.Threads = js.getInt("Threads");
        return cfg;
    }

    private JsonObject jsonRoot;
}

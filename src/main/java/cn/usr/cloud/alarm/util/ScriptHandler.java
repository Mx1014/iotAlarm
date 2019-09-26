package cn.usr.cloud.alarm.util;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by pi on 2019-02-22.
 */
public class ScriptHandler {
    private static ScriptEngine engine;

    static{
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("luaj");
    }

    public static String process(String rawValue, String script) {
        if(script.indexOf("%s")<0){
            return rawValue;
        }

        engine.put("x",rawValue);
        try {
            engine.eval("y="+script.replace("%s","x"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return engine.get("y").toString();
    }
}

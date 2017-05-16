package xyz.nkb.rhino.script.types;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSGetter;

import xyz.nkb.rhino.script.annotation.JavaScriptFunction;
import xyz.nkb.rhino.script.annotation.JavaScriptScope;
import xyz.nkb.rhino.script.core.ScriptBaseObject;

@JavaScriptScope(name="system", version = "0.5.1")
public class ScriptSystem extends ScriptBaseObject {
	
	private static final long serialVersionUID = 2252406393248637714L;

	public ScriptSystem(Scriptable scope) {
		super(scope);
	}

	@Override
	public String getClassName() {
		return "System";
	}
	
	@JSGetter("config")
	private Object getConfig(Scriptable scope) {
		File file = new File(System.getProperty("oa_application.config"), "resources.properties");
		Properties prop = new Properties();
		try {
			FileInputStream fin = new FileInputStream(file);
			try {
				prop.load(fin);
			} finally {
				fin.close();
			}
		} catch ( Exception ex ) {
			logger.error("Failed to read resources.properties", ex);
		}
		return prop;
	}

	@JSGetter("env")
	public Object getEnv(Scriptable scope) {
		return System.getenv();
	}
	
	@JSGetter("prop")
	public Object getProp(Scriptable scope) {
		return System.getProperties();
	}
}

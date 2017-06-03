
package xyz.nkb.rhino.script.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import xyz.nkb.rhino.script.test.ScriptFile;

/**
 *
 * @author yunhao
 */
public class NewGlobal implements Scriptable {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	private final Map<String, Object> prop = new HashMap<String, Object>();
	private final Scriptable scope;
	
	public NewGlobal(Scriptable scope) {
		this.scope = scope;
		
		ScriptFile file = new ScriptFile(this);
		prop.put("file", file);
	}

	@Override
	public String getClassName() {
		return "NewGlobal";
	}

	@Override
	public Object get(String string, Scriptable s) {
		logger.info("get: string=" + string + ", s=" + s);
		return prop.containsKey(string) ? prop.get(string) : NOT_FOUND;
	}

	@Override
	public Object get(int i, Scriptable s) {
		logger.info("get: i=" + i + ", s=" + s);
		return NOT_FOUND;
	}

	@Override
	public boolean has(String string, Scriptable s) {
		logger.info("has: string=" + string + ", s=" + s);
		return prop.containsKey(string);
	}

	@Override
	public boolean has(int i, Scriptable s) {
		logger.info("has: i=" + i + ", s=" + s);
		return false;
	}

	@Override
	public void put(String string, Scriptable s, Object o) {
		logger.info("put: string=" + string + ", s=" + s + ", o=" + o);
		prop.put(string, o);
	}

	@Override
	public void put(int i, Scriptable s, Object o) {
		logger.info("put: i=" + i + ", s=" + s + ", o=" + o);
	}

	@Override
	public void delete(String string) {
		logger.info("delete: string=" + string);
		prop.remove(string);
	}

	@Override
	public void delete(int i) {
		logger.info("delete: i=" + i);
	}

	@Override
	public Scriptable getPrototype() {
		logger.info("getPrototype");
		return null;
	}

	@Override
	public void setPrototype(Scriptable s) {
		logger.info("setPrototype: s=" + s);
	}

	@Override
	public Scriptable getParentScope() {
		logger.info("getParentScope");
		return scope == null ? null : scope.getParentScope();
	}

	@Override
	public void setParentScope(Scriptable s) {
		logger.info("setParentScope: s=" + s);
		if ( scope != null ) {
			scope.setParentScope(s);
		}
	}

	@Override
	public Object[] getIds() {
		logger.info("getIds");
		ArrayList<String> list = new ArrayList<String>(prop.keySet());
		return list.toArray(new String[list.size()]);
	}

	@Override
	public Object getDefaultValue(Class<?> type) {
		logger.info("getDefaultValue: type=" + type);
		return Context.getUndefinedValue();
	}

	@Override
	public boolean hasInstance(Scriptable s) {
		logger.info("hasInstance: s=" + s);
		return false;
	}
	
}

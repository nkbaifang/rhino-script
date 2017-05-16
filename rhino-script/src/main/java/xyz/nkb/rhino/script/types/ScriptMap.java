package xyz.nkb.rhino.script.types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ScriptMap implements Scriptable {
	
	private final Map<String, Object> map = new LinkedHashMap<String, Object>();
	private Scriptable parent;
	
	public ScriptMap(Scriptable parent) {
		this.parent = parent;
	}
	
	public ScriptMap(Scriptable parent, Map<String, Object> map) {
		this(parent);
		if ( map != null ) {
			this.map.putAll(map);
		}
	}

	public ScriptMap(Scriptable parent, Properties prop) {
		this(parent);
		if ( prop != null ) {
			for ( Map.Entry<Object, Object> entry : prop.entrySet() ) {
				map.put(entry.getKey().toString(), entry.getValue());
			}
		}
	}

	@Override
	public void delete(String key) {
		map.remove(key);
	}

	@Override
	public void delete(int key) {
		throw new RuntimeException("Unsupported operation.");
	}

	@Override
	public Object get(String key, Scriptable start) {
		return map.containsKey(key) ? map.get(key) : Context.getUndefinedValue();
	}

	@Override
	public Object get(int key, Scriptable start) {
		throw new RuntimeException("Unsupported operation.");
	}

	@Override
	public String getClassName() {
		return "Object";
	}

	@Override
	public Object getDefaultValue(Class<?> arg0) {
		return Context.getUndefinedValue();
	}

	@Override
	public Object[] getIds() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.addAll(map.keySet());
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Scriptable getParentScope() {
		return parent;
	}

	@Override
	public Scriptable getPrototype() {
		return this;
	}

	@Override
	public boolean has(String key, Scriptable start) {
		return map.containsKey(key);
	}

	@Override
	public boolean has(int key, Scriptable start) {
		return false;
	}

	@Override
	public boolean hasInstance(Scriptable start) {
		return false;
	}

	@Override
	public void put(String key, Scriptable start, Object value) {
		map.put(key, value);
	}

	@Override
	public void put(int key, Scriptable start, Object value) {
		throw new RuntimeException("Unsupported operation.");
	}

	@Override
	public void setParentScope(Scriptable parent) {
		this.parent = parent;
	}

	@Override
	public void setPrototype(Scriptable proto) {
	}

}

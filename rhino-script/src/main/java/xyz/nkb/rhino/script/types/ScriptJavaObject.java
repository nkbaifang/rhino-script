package xyz.nkb.rhino.script.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

public class ScriptJavaObject extends NativeJavaObject {

	private static final long serialVersionUID = 5524138414819375033L;

	private static final List<String> HIDDEN_PROPERTY_NAMES = Arrays.asList("getClass", "class");

	public ScriptJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
		super(scope, javaObject, staticType, true);
	}

	@Override
	public Object get(String name, Scriptable start) {
		if ( HIDDEN_PROPERTY_NAMES.contains(name) ) {
			return Scriptable.NOT_FOUND;
		}
		return super.get(name, start);
	}
	
	@Override
	public Object[] getIds() {
		Object[] ids = super.getIds();
		if ( ids == null || ids.length == 1 ) {
			return ids;
		}
		
		ArrayList<Object> list = new ArrayList<Object>();
		for ( Object id : ids ) {
			if ( !HIDDEN_PROPERTY_NAMES.contains(id) ) {
				list.add(id);
			}
		}
		return list.toArray(new Object[list.size()]);
		
	}
}

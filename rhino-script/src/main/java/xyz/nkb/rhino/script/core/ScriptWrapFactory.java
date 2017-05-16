package xyz.nkb.rhino.script.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.WrapFactory;

import xyz.nkb.rhino.script.types.ScriptJavaObject;

class ScriptWrapFactory extends WrapFactory {
	
	ScriptWrapFactory() {
		super();
		setJavaPrimitiveWrap(false);
	}

	@Override
	public Object wrap(Context ctx, Scriptable scope, Object obj, Class<?> staticType) {
		if ( obj == null || obj == Undefined.instance || obj instanceof Scriptable ) {
			return obj;
		}
		
		if ( staticType != null && staticType.isPrimitive() ) {
			if ( staticType == Void.TYPE ) {
				return Undefined.instance;
			}
			if ( staticType == Character.TYPE ) {
				return Integer.valueOf(((Character) obj).charValue());
			}
			return obj;
		}
		
		if ( obj instanceof String || obj instanceof Number || obj instanceof Boolean ) {
			return obj;
		}
		
		if ( obj instanceof Character ) {
			return String.valueOf(((Character) obj).charValue());
		}

		Object result;
		Class<?> cls = obj.getClass();
		if ( cls.isArray() ) {
			result = wrapJavaArray(ctx, scope, obj);
		} else {
			result = wrapAsJavaObject(ctx, scope, obj, staticType);
		}
		return result;
	}
	
	private Scriptable wrapJavaArray(Context ctx, Scriptable scope, Object array) {
		int length = Array.getLength(array);
		Object[] a = new Object[length];
		for ( int i = 0; i < length; i++ ) {
			Object o = Array.get(array, i);
			a[i] = wrap(ctx, scope, o, o.getClass());
		}
		
		return new NativeArray(a);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Scriptable wrapAsJavaObject(Context ctx, Scriptable scope, Object javaObject, Class<?> staticType) {
		Scriptable result;
		if ( javaObject instanceof Collection ) {
			Collection<? extends Object> c = (Collection<? extends Object>) javaObject;
			Object[] a = new Object[c.size()];
			int i = 0;
			for ( Object o :c ) {
				a[i++] = wrap(ctx, scope, o, o.getClass());
			}
			result = new NativeArray(a);
		} else if ( javaObject instanceof Map ) {
			result = ctx.newObject(scope);
			for ( Map.Entry<?, ?> entry : ((Map<?, ?>)javaObject).entrySet() ) {
				String name = entry.getKey().toString();
				Object value = entry.getValue();
				result.put(name, result, value);
			}
		} else {
			result = new ScriptJavaObject(scope, javaObject, javaObject.getClass());
		}
		return result;
	}

	@Override
	public Scriptable wrapJavaClass(Context cx, Scriptable scope, Class<?> javaClass) {
		// TODO Auto-generated method stub
		return super.wrapJavaClass(cx, scope, javaClass);
	}

	@Override
	public Scriptable wrapNewObject(Context cx, Scriptable scope, Object obj) {
		// TODO Auto-generated method stub
		return super.wrapNewObject(cx, scope, obj);
	}
}

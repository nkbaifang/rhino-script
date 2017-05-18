package xyz.nkb.rhino.script.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionCall;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

import xyz.nkb.rhino.script.annotation.JavaScriptFunction;
import xyz.nkb.rhino.script.annotation.JavaScriptScope;

public abstract class ScriptBaseObject extends ScriptableObject implements IdFunctionCall {
	
	private static final long serialVersionUID = -6232289249029439246L;

	static final int BUILTIN_PROPERTY = ScriptableObject.READONLY | ScriptableObject.PERMANENT;

	protected final Logger logger = LogManager.getLogger(this.getClass());
	private final Map<Integer, Method> jsFuncMap = new HashMap<Integer, Method>();
	private final Map<String, PropMethodPair> propFuncMap = new HashMap<String, PropMethodPair>();
	
	private final Scriptable scope;
	
	public ScriptBaseObject(Scriptable scope) {
		this.scope = scope;
		initGlobalVariants();
	}
	
	protected final Scriptable getScope() {
		return this.scope;
	}

	private void initGlobalVariants() {
		jsFuncMap.clear();
		String tag = this.getClass().getAnnotation(JavaScriptScope.class).name();
		
		for ( Method method : this.getClass().getDeclaredMethods() ) {
			if ( method.isAnnotationPresent(JavaScriptFunction.class) ) {
				JavaScriptFunction a = method.getAnnotation(JavaScriptFunction.class);
				
				IdFunctionObject func = new IdFunctionObject(this, tag, a.id(), a.name(), a.arity(), scope);
				ScriptableObject.defineProperty(scope, a.name(), func, BUILTIN_PROPERTY);
				
				jsFuncMap.put(Integer.valueOf(a.id()), method);
			}
			
			if ( method.isAnnotationPresent(JSGetter.class) ) {
				JSGetter a = method.getAnnotation(JSGetter.class);
				PropMethodPair pair = findPropMethodPair(a.value());
				pair.getter = method;
			}
			if ( method.isAnnotationPresent(JSSetter.class) ) {
				JSSetter a = method.getAnnotation(JSSetter.class);
				PropMethodPair pair = findPropMethodPair(a.value());
				pair.setter = method;
			}
		}
		
		initProperties();
	}
	
	private void initProperties() {
		
		ScriptableObject obj = (ScriptableObject) scope;
		
		for ( Map.Entry<String, PropMethodPair> entry : propFuncMap.entrySet() ) {
			PropMethodPair pair = entry.getValue();
			if ( pair.ok() ) {
				obj.defineProperty(pair.name, this, pair.getter, pair.setter, ScriptableObject.PERMANENT);
			}
		}
	}

	@Override
	public Object execIdCall(IdFunctionObject func, Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if ( func.getArity() > 0 && args.length < func.getArity() ) {
			throw ScriptRuntime.throwError(ctx, scope, "Missing arguments: " + func.getFunctionName());
		}
		
		Integer id = Integer.valueOf(func.methodId());
		
		Method method = jsFuncMap.get(id);
		if ( method == null ) {
			throw ScriptRuntime.notFoundError(scope, func.getFunctionName());
		}
		
		try {
			Object[] params = { ctx, scope, args };
			method.setAccessible(true);
			return method.invoke(this, params);
		} catch ( InvocationTargetException ex ) {
			logger.error("Failed to execute function: " + func.getFunctionName(), ex);
			Throwable error = ex.getCause();
			throw ScriptRuntime.throwError(ctx, scope, error.getMessage());
		} catch ( Throwable error ) {
			logger.error("Failed to execute function: " + func.getFunctionName(), error);
			throw ScriptRuntime.throwError(ctx, scope, error.getMessage());
		}
	}
	
	private PropMethodPair findPropMethodPair(String name) {
		PropMethodPair pair = this.propFuncMap.get(name);
		if ( pair == null ) {
			pair = new PropMethodPair(name);
			this.propFuncMap.put(name, pair);
		}
		return pair;
	}
	
	private static class PropMethodPair {
		private final String name;
		private Method getter;
		private Method setter;
		
		PropMethodPair(String name) {
			this.name = name;
		}
		
		boolean ok() {
			return getter != null || setter != null;
		}
		
		@Override
		public boolean equals(Object other) {
			if ( other == null ) {
				return false;
			}
			if ( this == other ) {
				return true;
			}
			if ( this.getClass() != other.getClass() ) {
				return false;
			}
			PropMethodPair o = (PropMethodPair) other;
			if ( name == null ) {
				return o.name == null;
			} else {
				return name.equals(o.name);
			}
		}
		
		@Override
		public int hashCode() {
			int hash = this.getClass().hashCode();
			if ( name != null ) {
				hash = hash * 107 + name.hashCode();
			}
			return hash;
		}
	}
}

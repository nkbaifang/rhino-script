package xyz.nkb.rhino.script.core;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSGetter;

import xyz.nkb.rhino.script.annotation.JavaScriptFunction;
import xyz.nkb.rhino.script.annotation.JavaScriptScope;
import xyz.nkb.rhino.script.types.JSResponse;
import xyz.nkb.rhino.script.types.ScriptRequest;
import xyz.nkb.rhino.script.types.ScriptResponse;
import xyz.nkb.rhino.script.types.ScriptSystem;
//import cn.firstsoft.firstframe.web.FirstFrameBeanFactory;

@JavaScriptScope(name="Global", version = "0.9.4")
public class GlobalProvider extends ScriptBaseObject {

	private static final long serialVersionUID = 3155922971711855231L;
	private final ScriptSystem system;

	public GlobalProvider(Context ctx) {
		super(ctx.initSafeStandardObjects());
		
		Scriptable scope = this.getScope();
		
		initGlobalClasses();
		
		system = new ScriptSystem(scope);
	}
	
	private void initGlobalClasses() {
		Scriptable scope = this.getScope();
		
	/*	Class<? extends Scriptable>[] classes = new Class[]{
			ScriptRequest.class,
			ScriptResponse.class,
			JSResponse.class
		}; */

		try {
		/*	for ( Class<? extends Scriptable> c : classes ) {
				ScriptableObject.defineClass(scope, c, true);
			} */
			ScriptableObject.defineClass(scope, ScriptRequest.class, true);
			ScriptableObject.defineClass(scope, ScriptResponse.class, true);
			ScriptableObject.defineClass(scope, JSResponse.class, true);
		} catch ( Exception ex ) {
			ex.printStackTrace(System.err);
			logger.error("Failed to define global classes.", ex);
			throw ScriptRuntime.constructError("Error", "Global initialization error.");
		}
		
	}

	@JavaScriptFunction( id = 2, name = "bean", arity = 1 )
	private Object _bean(Context ctx, Scriptable scope, Object[] args) {
	/*	String name = (String)args[0];
		Object result = null;
		try {
			result = FirstFrameBeanFactory.getBean((String)args[0]);
		} catch ( Exception ex ) {
			logger.error("Cannot find bean: " + name, ex);
		}
		
		if ( result == null ) {
			return Undefined.instance;
		} else {
			result = Context.javaToJS(result, scope);
		}
		return result; */
		return Context.getUndefinedValue();
	}

	@JavaScriptFunction( id = 3, name = "print", arity = 1 )
	private Object _print(Context ctx, Scriptable scope, Object[] args) {
		String str = String.valueOf(args[0]);
		System.out.println("S:>" + str);
		return Undefined.instance;
	}

	@JavaScriptFunction( id = 4, name = "log", arity = 1 )
	private Object _log(Context ctx, Scriptable scope, Object[] args) {
		String str = String.valueOf(args[0]);
		logger.info(str);
		return Undefined.instance;
	}

	@JavaScriptFunction( id = 5, name = "version", arity = 0 )
	private Object _version(Context ctx, Scriptable scope, Object[] args) {
		JavaScriptScope a = this.getClass().getAnnotation(JavaScriptScope.class);
		return a.version();
	}
	
	@JSGetter("version")
	private Object _version(ScriptableObject obj) {
		JavaScriptScope a = this.getClass().getAnnotation(JavaScriptScope.class);
		return a.version();
	}
	
	@JSGetter("system")
	public Object _system(ScriptableObject obj) {
		return new java.util.HashMap();
	}

	@Override
	public String getClassName() {
		return "Global";
	}
}

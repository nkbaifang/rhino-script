package xyz.nkb.rhino.script.core;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

public class ModuleEnvironment {

	static {
		ContextFactory.initGlobal(new ScriptContextFactory());
	}

	private final Logger logger = LogManager.getLogger(this.getClass());

	private final URI base;
	private final GlobalProvider global;
	private RequireBuilder builder;

	public ModuleEnvironment(URI base) {
		this.base = base;

		Context ctx = Context.enter();
		try {
			global = new GlobalProvider(ctx);
			initScriptSource();
		} finally {
			Context.exit();
		}
	}
	
	private synchronized void initScriptSource() {
		List<URI> paths = Arrays.asList(base);
		ModuleSourceProvider sourceProvider = new UrlModuleSourceProvider(paths, null);
		ModuleScriptProvider scriptProvider = new SoftCachingModuleScriptProvider(sourceProvider);
		builder = new RequireBuilder().setModuleScriptProvider(scriptProvider).setSandboxed(true);
	}
	
	private synchronized Require createRequireFunction(Context ctx) {
		Require require = builder.createRequire(ctx, global.getScope());
		return require;
	}
	
	public void reload() {
		initScriptSource();
	}
	
	private Scriptable getModuleObject(Context ctx, String name) throws Exception {
		Scriptable scope = global.getScope();
		
		Require require = createRequireFunction(ctx);

		Scriptable result = null;
		Object[] args = { name };
		Object o = require.call(ctx, scope, scope, args);
		if ( o instanceof Scriptable ) {
			result = (Scriptable) o;
		} else {
			logger.warn("Unrecognized module: " + o);
		}
		
		return result;
	}
	
	public Object invokeModule(String name, String func, Object[] args) throws Exception {
		Context ctx = Context.enter();
		try {
			Scriptable scope = global.getScope();
			Scriptable obj = this.getModuleObject(ctx, name);
			if ( obj == null && obj == Undefined.instance ) {
				throw ScriptRuntime.notFoundError(scope, name);
			}
			
			Object o = obj.get(func, obj);
			if ( !(o instanceof BaseFunction) ) {
				throw ScriptRuntime.notFunctionError(func);
			}
			
			return ((BaseFunction)o).call(ctx, scope, obj, args);
		} finally {
			Context.exit();
		}
	}
}

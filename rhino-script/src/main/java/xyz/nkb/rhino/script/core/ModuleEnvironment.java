package xyz.nkb.rhino.script.core;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

	private final Logger logger = Logger.getLogger(this.getClass());

	private final URL base;
	private final Scriptable scope;
	private final Require require;
	private final ModuleScriptProvider scriptProvider;
	private final GlobalProvider global;
	
	private final Map<String, Scriptable> moduleMap = new HashMap<String, Scriptable>();

	public ModuleEnvironment(URL moduleBaseURL) {
		this.base = moduleBaseURL;

		Context ctx = Context.enter();
		try {
			global = new GlobalProvider(ctx);
			scope = global.getScope();
			List<URI> paths = Arrays.asList(base.toURI());
			ModuleSourceProvider sourceProvider = new UrlModuleSourceProvider(paths, null);
			scriptProvider = new SoftCachingModuleScriptProvider(sourceProvider);
		
			require = new RequireBuilder().setModuleScriptProvider(scriptProvider)
					.setSandboxed(true)
					.createRequire(ctx, scope);
			require.install(scope);
			
		} catch ( Exception ex ) {
			throw new RuntimeException(ex);
		} finally {
			Context.exit();
		}
	}
	
	private synchronized Scriptable getModuleObject(Context ctx, String name) throws Exception {
		Scriptable obj = moduleMap.get(name);
	//	if ( obj == null ) {
			Object[] args = { name };
			Object o = require.call(ctx, scope, scope, args);
			if ( o instanceof Scriptable ) {
				obj = (Scriptable) o;
			} else {
				logger.warn("Unrecognized module: " + o);
			}
			moduleMap.put(name, obj);
	//	}
		return obj;
	}
	
	public Object invokeModule(String name, String func, Object[] args) throws Exception {
		Context ctx = Context.enter();
		ctx.putThreadLocal("module", name);
		try {
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

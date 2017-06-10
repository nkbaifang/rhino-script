
package xyz.nkb.rhino.script.test;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import xyz.nkb.rhino.script.annotation.JavaScriptFunction;
import xyz.nkb.rhino.script.annotation.JavaScriptScope;
import xyz.nkb.rhino.script.core.ScriptBaseObject;

/**
 *
 * @author yunhao
 */
@JavaScriptScope(name = "file", version = "0.2.9")
public class ScriptFile extends ScriptBaseObject {

	public ScriptFile(Scriptable scope) {
		super(scope);
	}

	@Override
	public String getClassName() {
		return "File";
	}

	@JavaScriptFunction(id = 3, name = "list", arity = 1)
	private Object _list(Context ctx, Scriptable scope, Object[] args) {
		if ( !(args[0] instanceof String) ) {
			throw ScriptRuntime.throwError(ctx, scope, "Illegal argument type: " + args[0]);
		}
		
		Object result;
		File dir = new File((String)args[0]);
		if ( dir.isDirectory() ) {
			List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
			
			File[] files = dir.listFiles();
			for ( File file : files ) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("name", file.getName());
				if ( file.isFile() ) {
					map.put("type", "F");
					map.put("size", Long.valueOf(file.length()));
				} else {
					map.put("type", "D");
				}
				list.add(map);
			}
			
			
			
			result = list;
		} else {
			result = Context.getUndefinedValue();
		}
		return result;
	}
}

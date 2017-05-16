package xyz.nkb.rhino.script.types;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

public class ScriptResponse extends ScriptableObject {
	
	private static final long serialVersionUID = 877561821772426518L;
	
	private int status;
	private String contentType;
	private Object data;
	
	public ScriptResponse() {
		this.status = 200;
		this.contentType = "application/json";
	}

	@Override
	public String getClassName() {
		return "Response";
	}

	@JSConstructor
	public static Scriptable jsConstructor(Context ctx, Object[] args, Function func, boolean isNewExpr) {
		if ( !isNewExpr ) {
			throw ScriptRuntime.constructError("JavaScript Error", "Call constructor using the \"new\" keyword.");
		}
		
		ScriptResponse result = new ScriptResponse();
		if ( args.length == 1 ) {
			if ( args[0] == Context.getUndefinedValue() ) {
			} else if ( args[0] instanceof Number ) {
				result.status = ((Number)args[0]).intValue();
			} else if ( args[0] instanceof String ) {
				result.contentType = (String)args[0];
			} else {
				throw ScriptRuntime.constructError("Type Error", "Unsupported argument type: " + args[0]);
			}
		} else if ( args.length >= 2 ) {
			result.status = ((Number)args[0]).intValue();
			result.contentType = (String)args[1];
		}
		return result;
	}
	
	@JSSetter("data")
	public void setData(Object data) {
		this.data = data;
	}
	
	@JSGetter("data")
	public Object getData() {
		return data;
	}

	@JSGetter("status")
	public int getStatus() {
		return status;
	}

	@JSGetter("contentType")
	public String getContentType() {
		return contentType;
	}
}

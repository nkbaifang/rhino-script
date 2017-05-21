
package xyz.nkb.rhino.script.types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

public class ScriptRequest extends IdScriptableObject {
	
	private static final long serialVersionUID = 5856909222711845556L;
	
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	
	private static final Logger logger = LogManager.getLogger(ScriptRequest.class);
	private static final String REQ_TAG = "Request";
	
	private static final int
		ID_constructor = 1,
		ID_method = 2,
		ID_contentType = 3,
		ID_data = 4,
		MAX_ID = 4;

	private final String method;
	private final String contentType;
	private final Object data;

	public static void init(Context ctx, Scriptable scope, boolean sealed) {
		logger.debug("init: scope=" + scope + ", sealed=" + sealed);
		
		ScriptRequest obj = new ScriptRequest();
		obj.exportAsJSClass(MAX_ID, scope, sealed);
	}
	
	public ScriptRequest() {
		this.method = null;
		this.contentType = null;
		this.data = null;
	}

	public ScriptRequest(String method, String contentType, Object data) {
		this.method = method;
		this.contentType = contentType;
		this.data = data;
	}

	@Override
	protected void fillConstructorProperties(IdFunctionObject ctor) {
		super.fillConstructorProperties(ctor);
		
		ctor.defineProperty("METHOD_GET", METHOD_GET, CONST);
		ctor.defineProperty("METHOD_POST", METHOD_POST, CONST);
		ctor.defineProperty("CONTENT_TYPE_JSON", CONTENT_TYPE_JSON, CONST);
		ctor.defineProperty("CONTENT_TYPE_FORM", CONTENT_TYPE_FORM, CONST);
	}
	
	@Override
	public String getClassName() {
		return "Request";
	}
	
	@Override
	protected void initPrototypeId(int id) {
		logger.debug("initPrototypeId: id=" + id);
		
		if ( id == ID_constructor ) {
			this.initPrototypeMethod(REQ_TAG, id, "constructor", 0);
		} else {
			String name;
			Object value;
			
			switch ( id ) {
			case ID_method:
				name = "method";
				value = this.method;
				break;
			case ID_contentType:
				name = "contentType";
				value = this.contentType;
				break;
			case ID_data:
				name = "data";
				value = this.data;
				break;
			default:
				throw new IllegalArgumentException("id=" + id);
			}
			
			if ( value == null ) {
				value = Context.getUndefinedValue();
			}
			this.initPrototypeValue(id, name, value, EMPTY);
		}
	}

	@Override
	protected int findInstanceIdInfo(String name) {
		int id = 0;
		if ( "constructor".equals(name) ) {
			id = ID_constructor;
		} else if ( "method".equals(name) ) {
			id = ID_method;
		} else if ( "contentType".equals(name) ) {
			id = ID_contentType;
		} else if ( "data".equals(name) ) {
			id = ID_data;
		}
		logger.debug("findInstanceIdInfo: name=" + name + ", id=" + id);
		return id;
	}
	
	@Override
	protected int findPrototypeId(String name) {
		logger.debug("findPrototypeId: name=" + name);
		int id = 0;
		if ( "constructor".equals(name) ) {
			id = ID_constructor;
		}
		return id;
	}
	
	@Override
	public Object[] getIds() {
		logger.debug("getIds");
		return new String[] { "method", "contentType", "data" };
	}

	@Override
	protected String getInstanceIdName(int id) {
		String result = null;
		switch ( id ) {
			case ID_constructor:
				result = "constructor";
				break;
			default:
				break;
		}
		return result;
	}
	
	@Override
	protected Object getInstanceIdValue(int id) {
		logger.debug("getInstanceIdValue: id=" + id);
		Object result;
		switch ( id ) {
			case ID_method:
				result = this.method;
				break;
			case ID_contentType:
				result = this.contentType;
				break;
			case ID_data:
				result = Context.javaToJS(this.data, this);
				break;
			default:
				result = NOT_FOUND;
				break;
		}
		return result == null ? NOT_FOUND : result;
	}

	@Override
	protected void setInstanceIdValue(int id, Object value) {
		logger.debug("setInstanceIdValue: id=" + id + ", value=" + value);
	}

	@Override
	protected void setInstanceIdAttributes(int id, int attr) {
		logger.debug("setInstanceIdAttributes: id=" + id + ", attr=" + attr);
	}
	

	@Override
	public Object execIdCall(IdFunctionObject func, Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
		int id = func.methodId();
		logger.debug("execIdCall: id=" + id + ", this=" + thisObj);
		
		if ( !func.hasTag(REQ_TAG) ) {
			return super.execIdCall(func, ctx, scope, thisObj, args);
		}
		
		if ( id == ID_constructor ) {
			throw ScriptRuntime.constructError("Constructor Error", "Cannot construct a request object");
		} else {
			throw func.unknown();
		}
	}
	
	public String getMethod() {
		return method;
	}

	public String getContentType() {
		return contentType;
	}

	public Object getData() {
		return data;
	}
}

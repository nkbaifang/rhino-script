package xyz.nkb.rhino.script.types;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

public class ScriptResponse extends IdScriptableObject {
	
	public static final int STATUS_OK = 200;
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	
	private static final long serialVersionUID = 877561821772426518L;
	
	private static final Logger logger = LogManager.getLogger(ScriptResponse.class);
	private static final String RSP_TAG = "Response";
	
	private static final int
		ID_constructor = 1,
		ID_status = 2,
		ID_contentType = 3,
		ID_data = 4,
		MAX_ID = 4;
	
	private static final AtomicInteger idg = new AtomicInteger(0);

	private final int _id;
	private int status = STATUS_OK;
	private String contentType = CONTENT_TYPE_JSON;
	private Object data;
	
	public static void init(Context ctx, Scriptable scope, boolean sealed) {
		logger.info("init: scope=" + scope + ", sealed=" + sealed);

		ScriptResponse obj = new ScriptResponse();
		obj.exportAsJSClass(MAX_ID, scope, sealed);
	}
	
	private ScriptResponse() {
		this._id = idg.getAndIncrement();
	}

	@Override
	protected void fillConstructorProperties(IdFunctionObject ctor) {
		super.fillConstructorProperties(ctor);
		
		ctor.defineProperty("STATUS_OK", ScriptRuntime.wrapInt(STATUS_OK), CONST);
		ctor.defineProperty("CONTENT_TYPE_JSON", CONTENT_TYPE_JSON, CONST);
		ctor.defineProperty("CONTENT_TYPE_FORM", CONTENT_TYPE_FORM, CONST);
	}

	@Override
	public String getClassName() {
		return "Response";
	}
	
	@Override
	protected void initPrototypeId(int id) {
		logger.info("initPrototypeId: id=" + id);
		
		if ( id == ID_constructor ) {
			this.initPrototypeMethod(RSP_TAG, id, "constructor", 0);
		} else {
			String name;
			Object value;
			
			switch ( id ) {
			case ID_status:
				name = "status";
				value = Integer.valueOf(this.status);
				break;
			case ID_contentType:
				name = "contentType";
				value = this.contentType != null ? this.contentType : Context.getUndefinedValue();
				break;
			case ID_data:
				name = "data";
				value = this.data != null ? this.data : Context.getUndefinedValue();
				break;
			default:
				throw new IllegalArgumentException("id=" + id);
			}
			
			this.initPrototypeValue(id, name, value, EMPTY);
		}
	}

	@Override
	protected int findInstanceIdInfo(String name) {
		int id = 0;
		if ( "constructor".equals(name) ) {
			id = ID_constructor;
		} else if ( "status".equals(name) ) {
			id = ID_status;
		} else if ( "contentType".equals(name) ) {
			id = ID_contentType;
		} else if ( "data".equals(name) ) {
			id = ID_data;
		}
		logger.info("findInstanceIdInfo: name=" + name + ", id=" + id);
		return id;
	}
	
	@Override
	protected int findPrototypeId(String name) {
		logger.info("findPrototypeId: name=" + name);
		int id = 0;
		if ( "constructor".equals(name) ) {
			id = ID_constructor;
		} /*else if ( "status".equals(name) ) {
			id = ID_status;
		} else if ( "contentType".equals(name) ) {
			id = ID_contentType;
		} else if ( "data".equals(name) ) {
			id = ID_data;
		} */
		return id;
	}
	
	@Override
	public Object[] getIds() {
		logger.info("getIds");
		return new String[] { "status", "contentType", "data" };
	}
	
	@Override
	protected Object getInstanceIdValue(int id) {
		logger.info("getInstanceIdValue: id=" + id);
		Object result;
		switch ( id ) {
			case ID_status:
				result = Integer.valueOf(this.status);
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
		logger.info("setInstanceIdValue: id=" + id + ", value=" + value);
		
		if ( value == NOT_FOUND ) {
			logger.info("delete property: id=" + id);
			return;
		}
		switch ( id ) {
			case ID_status:
				this.status = ((Integer)value).intValue();
				break;
			case ID_contentType:
				this.contentType = value.toString();
				break;
			case ID_data:
				this.data = Context.jsToJava(value, Object.class);
				break;
			default:
				break;
		}
	}

	@Override
	public Object execIdCall(IdFunctionObject func, Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
		int id = func.methodId();
		logger.info("execIdCall: id=" + id + ", this=" + thisObj);
		
		if ( !func.hasTag(RSP_TAG) ) {
			return super.execIdCall(func, ctx, scope, thisObj, args);
		}
		
		Object result = Context.getUndefinedValue();
		if ( id == ID_constructor ) {
			if ( thisObj != null ) {
				result = func.construct(ctx, scope, args);
			} else {
				ScriptResponse rsp = new ScriptResponse();
				rsp.activatePrototypeMap(MAX_ID);
				
				if ( args.length == 1 ) {
					if ( args[0] == Context.getUndefinedValue() ) {
					} else if ( args[0] instanceof Number ) {
						rsp.status = ((Number)args[0]).intValue();
					} else if ( args[0] instanceof String ) {
						rsp.contentType = (String)args[0];
					} else {
						throw ScriptRuntime.constructError("Type Error", "Unsupported argument type: " + args[0]);
					}
				} else if ( args.length >= 2 ) {
					rsp.status = ((Number)args[0]).intValue();
					rsp.contentType = (String)args[1];
				}
				result = rsp;
			}
		} else {
			throw func.unknown();
		}
		return result;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getContentType() {
		return contentType;
	}

	public Object getData() {
		return data;
	}
	
}

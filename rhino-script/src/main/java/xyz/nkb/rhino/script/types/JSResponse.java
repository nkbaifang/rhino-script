package xyz.nkb.rhino.script.types;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdFunctionObject;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

public class JSResponse extends IdScriptableObject {
	
	private static final Logger logger = Logger.getLogger(JSResponse.class);

	private static final long serialVersionUID = 7286799850333274071L;

	private static final String RSP_TAG = "Response";
	
	private static final int
		ID_STATUS_OK = -2,
		ID_constructor = 1,
		ID_status = 2,
		ID_contentType = 3,
		ID_data = 4,
		MAX_ID = 4;
	
	private static final AtomicInteger idg = new AtomicInteger(0);

	private final int _id;
	private int status = 200;
	private String contentType;
	private Object data;
	
	public static void init(Context ctx, Scriptable scope, boolean sealed) {
		logger.info("init: scope=" + scope + ", sealed=" + sealed);

		JSResponse obj = new JSResponse();
		obj.exportAsJSClass(MAX_ID, scope, sealed);
	//	obj.activatePrototypeMap(MAX_ID);
	//	obj.setPrototype(getObjectPrototype(scope));
	/*	obj.setParentScope(scope);
		if ( sealed ) {
			obj.sealObject();
		} */
	}
	
	private JSResponse() {
		this._id = idg.getAndIncrement();
	}
/*
	@Override
	protected int getMaxInstanceId() {
		return ID_MAX;
	} */
	
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
				value = Integer.valueOf(200);
				break;
			case ID_contentType:
				name = "contentType";
				value = Context.getUndefinedValue();
				break;
			case ID_data:
				name = "data";
				value = Context.getUndefinedValue();
				break;
			default:
				throw new IllegalArgumentException("id=" + id);
			}
			
			this.initPrototypeValue(id, name, value, EMPTY);
		}
	}
/*
	@Override
	protected int findInstanceIdInfo(String name) {
		int id = 0;
		for ( Map.Entry<Integer, String> entry : instPropNames.entrySet() ) {
			if ( entry.getValue().equals(name) ) {
				id = entry.getKey().intValue();
				break;
			}
		}
		logger.info("findInstanceIdInfo: name=" + name + ", id=" + id);
		
		return id;
	} */
	
	@Override
	protected int findPrototypeId(String name) {
		logger.info("findPrototypeId: name=" + name);
	/*	if ( !"constructor".equals(name) ) {
			throw new NullPointerException();
		} */
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
	/*	if ( name.equals("status") ) {
			id = ID_status;
		} else if ( name.equals("contentType") ) {
			id = ID_contentType;
		} else if ( name.equals("data") ) {
			id = ID_data;
		} */
		return id;
	}
	
	@Override
	protected void fillConstructorProperties(IdFunctionObject ctor) {
		logger.info("fillConstructorProperties: ctor=" + ctor);
		super.fillConstructorProperties(ctor);
	//	this.constructor = ctor;
	/*	ScriptableObject.putConstProperty(ctor, "CONTENT_TYPE_FORM", "application/x-www-form-urlencoded");
		ScriptableObject.putConstProperty(ctor, "CONTENT_TYPE_JSON", "application/json");
		ScriptableObject.putConstProperty(ctor, "STATUS_OK", Integer.valueOf(200)); */
		
	/*	Scriptable scope = ScriptableObject.getTopLevelScope(ctor);
		ScriptableObject.defineProperty(scope, "CONTENT_TYPE_FORM", "application/x-www-form-urlencoded", CONST);
		ScriptableObject.defineProperty(scope, "CONTENT_TYPE_JSON", "application/json", CONST);
		ScriptableObject.defineProperty(scope, "STATUS_OK", Integer.valueOf(200), READONLY); */
	}
	
	@Override
	public Object[] getIds() {
		logger.info("getIds");
		return new String[] { "status", "contentType", "data" };
	}
/*
	@Override
	protected String getInstanceIdName(int id) {
		logger.info("getInstanceIdName: id=" + id);
		String result;
		if ( id == ID_status ) {
			result = "status";
		} else if ( id == ID_contentType ) {
			result = "contentType";
		} else if ( id == ID_data ) {
			result = "data";
		} else {
			result = null;
		}
		return result;
	}
*/
	@Override
	protected Object getInstanceIdValue(int id) {
		logger.info("getInstanceIdValue: id=" + id);
		Object result = null;
		if ( id == ID_status ) {
			result = Integer.valueOf(this.status);
		} else if ( id == ID_contentType ) {
			result = this.contentType;
		} else if ( id == ID_data ) {
			result = Context.javaToJS(this.data, this);
		} else {
			result = NOT_FOUND;
		}
		return result == null ? NOT_FOUND : result;
	}
/*
	@Override
	protected void setInstanceIdAttributes(int id, int attr) {
		logger.info("setInstanceIdAttributes: id=" + id + ", attr=" + attr);
	}
*/
	@Override
	protected void setInstanceIdValue(int id, Object value) {
		logger.info("setInstanceIdValue: id=" + id + ", value=" + value);
		if ( value == NOT_FOUND ) {
			logger.info("delete property: id=" + id);
			return;
		}
		if ( id == ID_status ) {
			this.status = ((Integer)value).intValue();
		} else if ( id == ID_contentType ) {
			this.contentType = value.toString();
		} else if ( id == ID_data ) {
			this.data = Context.jsToJava(value, Object.class);
		} else {
		//	throw new IllegalArgumentException("Unknown property id: " +id);
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
				JSResponse rsp = new JSResponse();
				rsp.activatePrototypeMap(MAX_ID);
				
				if ( args.length == 1 ) {
					if ( args[0] == Context.getUndefinedValue() ) {
					} else if ( args[0] instanceof Number ) {
					//	rsp.status = ((Number)args[0]).intValue();
						rsp.initPrototypeValue(ID_status, "status", args[0], 0);
					} else if ( args[0] instanceof String ) {
					//	rsp.contentType = (String)args[0];
						rsp.initPrototypeValue(ID_contentType, "contentType", args[0], 0);
					} else {
						throw ScriptRuntime.constructError("Type Error", "Unsupported argument type: " + args[0]);
					}
				} else if ( args.length >= 2 ) {
				//	rsp.status = ((Number)args[0]).intValue();
				//	rsp.contentType = (String)args[1];
					rsp.initPrototypeValue(ID_status, "status", args[0], 0);
					rsp.initPrototypeValue(ID_contentType, "contentType", args[1], 0);
				}
				result = rsp;
			}
		} else {
			throw func.unknown();
		}
		return result;
	}

	@Override
	public String getClassName() {
		return "JSResponse";
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

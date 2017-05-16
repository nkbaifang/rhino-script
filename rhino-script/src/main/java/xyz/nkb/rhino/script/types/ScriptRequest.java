
package xyz.nkb.rhino.script.types;

import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSGetter;

public class ScriptRequest extends IdScriptableObject {
	
	private static final long serialVersionUID = 5856909222711845556L;
	
	public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_JSON = "application/json";

	private final String method;
	private final String contentType;
	private final Object data;
	
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
	
	public String getClassName() {
		return "Request";
	}

	@JSGetter("method")
	public String getMethod() {
		return method;
	}

	@JSGetter("contentType")
	public String getContentType() {
		return contentType;
	}

	@JSGetter("data")
	public Object getData() {
		return data;
	}
}

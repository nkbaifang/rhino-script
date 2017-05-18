package xyz.nkb.rhino.script;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import org.mozilla.javascript.Context;

import xyz.nkb.rhino.script.core.ModuleEnvironment;
import xyz.nkb.rhino.script.types.JSResponse;
import xyz.nkb.rhino.script.types.ScriptRequest;

public class Main {

	public static void main(String[] args) throws Exception {
	
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		
		File dir = new File(".\\src\\test\\scripts");
		System.out.println(dir.getAbsolutePath());
		ModuleEnvironment env = new ModuleEnvironment(dir.toURI().toURL());
		
		Object data = new String[] { "13", "14" };
		ScriptRequest sr = new ScriptRequest("POST", "application/x-www-form-urlencoded", data);
	//	System.out.println("ScriptRequest: " + mapper.writeValueAsString(sr));

		Context ctx = Context.enter();
		try {
			Object o = sr;//Context.javaToJS(sr, ctx.initSafeStandardObjects());
			
			JSResponse sp = (JSResponse) env.invokeModule("test", "show", new Object[]{ o });
			String json = mapper.writeValueAsString(sp.getData());
			System.out.println(sp.getStatus() + ": " + sp.getContentType());
			System.out.println(json);
		} finally {
			Context.exit();
		}
		
	}

}

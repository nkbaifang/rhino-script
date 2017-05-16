package xyz.nkb.rhino.script.core;

import java.util.Arrays;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

class ScriptContextFactory extends ContextFactory {
	
	private static final List<Integer> ENABLED_FEATURES = Arrays.asList(
			Integer.valueOf(Context.FEATURE_DYNAMIC_SCOPE), 
			Integer.valueOf(Context.FEATURE_STRICT_MODE),
			Integer.valueOf(Context.FEATURE_STRICT_EVAL),
			Integer.valueOf(Context.FEATURE_NON_ECMA_GET_YEAR),
			Integer.valueOf(Context.FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME),
			Integer.valueOf(Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER)
	);
	
	private static final List<Integer> DISABLED_FEATURES = Arrays.asList(
			Integer.valueOf(Context.FEATURE_PARENT_PROTO_PROPERTIES)
	);
	
	ScriptContextFactory() {
	}
	
	@Override
	protected boolean hasFeature(Context ctx, int featureIndex) {
		Integer index = Integer.valueOf(featureIndex);
		if ( ENABLED_FEATURES.contains(index) ) {
			return true;
		}
		
		if ( DISABLED_FEATURES.contains(index) ) {
			return false;
		}
		
		return super.hasFeature(ctx, featureIndex);
	}
	
	@Override
	protected Context makeContext() {
		Context ctx = super.makeContext();
		ctx.setLanguageVersion(Context.VERSION_1_8);
		ctx.setClassShutter(new ScriptClassShutter());
		ctx.setWrapFactory(new ScriptWrapFactory());
		return ctx;
	}
}

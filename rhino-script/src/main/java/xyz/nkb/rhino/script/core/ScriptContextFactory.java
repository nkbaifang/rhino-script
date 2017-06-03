package xyz.nkb.rhino.script.core;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

class ScriptContextFactory extends ContextFactory {
	
	private static final Logger logger = LogManager.getLogger(ScriptContextFactory.class);
	
	private static final List<Integer> ENABLED_FEATURES = Arrays.asList(
			Integer.valueOf(Context.FEATURE_DYNAMIC_SCOPE), 
			Integer.valueOf(Context.FEATURE_STRICT_MODE),
			Integer.valueOf(Context.FEATURE_STRICT_EVAL),
			Integer.valueOf(Context.FEATURE_NON_ECMA_GET_YEAR),
			Integer.valueOf(Context.FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME)
	);
	
	private static final List<Integer> DISABLED_FEATURES = Arrays.asList(
		//	Integer.valueOf(Context.FEATURE_PARENT_PROTO_PROPERTIES)
	);
	
	ScriptContextFactory() {
	}
	
	@Override
	protected boolean hasFeature(Context ctx, int featureIndex) {
		boolean result;
		Integer index = Integer.valueOf(featureIndex);
		if ( ENABLED_FEATURES.contains(index) ) {
			result = true;
		} else if ( DISABLED_FEATURES.contains(index) ) {
			result = false;
		} else {
			result = super.hasFeature(ctx, featureIndex);
		}
		logger.info("hasFeature: index=" + featureIndex + ", result=" + result);
		return result;
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

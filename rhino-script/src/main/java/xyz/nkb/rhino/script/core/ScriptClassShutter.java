package xyz.nkb.rhino.script.core;

import org.mozilla.javascript.ClassShutter;

class ScriptClassShutter implements ClassShutter {

	@Override
	public boolean visibleToScripts(String className) {
		// TODO: 
		return true;
	}

}


package xyz.nkb.rhino.script.annotation;

/**
 *
 * @author yunhao
 */
public @interface JavaScriptGetter {
	
	String value();
	
	boolean delegate() default false;
}

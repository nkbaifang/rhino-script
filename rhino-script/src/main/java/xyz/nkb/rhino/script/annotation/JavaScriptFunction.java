
package xyz.nkb.rhino.script.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JavaScriptFunction {
	
	/**
	 * Function identifier
	 * 
	 * @return
	 */
	int id();

	/**
	 * Function name for JavaScript
	 * 
	 * @return
	 */
	String name();

	/**
	 * Arguments count
	 * 
	 * @return
	 */
	int arity();
}

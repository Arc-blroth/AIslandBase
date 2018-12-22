package base.swing;

import java.lang.annotation.Documented;

@Documented
public @interface Unfinished {
	
	String description() default "";
	String value() default "";
	
}

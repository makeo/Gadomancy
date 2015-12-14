package makeo.gadomancy.common.data.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 15.11.2015 22:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={FIELD})
public @interface Sync {
}

package njtech.nanjing.com.core.utils;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;

/**
 * Created by Heyha on 2017/7/11.
 */

public class RippleHelper {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static final void apply(Drawable d, @ColorInt int color) {
        if (d instanceof RippleDrawable) {
            ((RippleDrawable) d).setColor(ColorStateList.valueOf(color));
        }
    }
}

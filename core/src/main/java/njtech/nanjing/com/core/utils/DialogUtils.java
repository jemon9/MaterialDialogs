package njtech.nanjing.com.core.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;

/**
 * Created by 张志付 on 2017/6/25.
 */

public class DialogUtils {

    @ColorInt
    public static int resolveColor(Context context, @AttrRes int attr) {
        return resolveColor(context, attr, 0);
    }

    /**
     * 根据attr的值获取颜色的int值
     * @param context
     * @param attr
     * @param defValue
     * @return
     */
    @ColorInt
    private static int resolveColor(Context context, @AttrRes int attr, int defValue) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0,defValue);
        } finally {
            a.recycle();
        }

    }
}

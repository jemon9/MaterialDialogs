package njtech.nanjing.com.core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import njtech.nanjing.com.core.GravityEnum;

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
     *
     * @param context
     * @param attr
     * @param defValue
     * @return
     */
    @ColorInt
    public static int resolveColor(Context context, @AttrRes int attr, int defValue) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, defValue);
        } finally {
            a.recycle();
        }

    }

    public static int getColor(Context context, @ColorRes int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    public static ColorStateList getActionTextStateList(Context context, int newPrimaryColor) {

        return null;
    }

    /**
     * get GravityEnum value from attr
     *
     * @param context
     * @param attr
     * @param defValue
     * @return
     */
    public static GravityEnum resolveGravityEnum(Context context, @AttrRes int attr, GravityEnum
            defValue) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attr});
        try {
            switch (a.getInt(0, gravityEnumToAttrInt(defValue))) {
                case 1:
                    return GravityEnum.CENTER;
                case 2:
                    return GravityEnum.END;
                default:
                    return GravityEnum.START;
            }
        } finally {
            a.recycle();
        }
    }

    private static int gravityEnumToAttrInt(GravityEnum defValue) {
        switch (defValue) {
            case CENTER:
                return 1;
            case END:
                return 2;
            default:
                return 0;
        }
    }

    public static Drawable resolveDrawable(Context context, @AttrRes int iconAttr) {
        return resolveDrawable(context, iconAttr, null);
    }

    private static Drawable resolveDrawable(Context context, @AttrRes int iconAttr, Drawable
            fallback) {
        TypedArray a = context.obtainStyledAttributes(new int[]{iconAttr});
        try {
            Drawable d = a.getDrawable(0);
            if (d == null && fallback != null) {
                d = fallback;
            }
            return d;
        } finally {
            a.recycle();
        }
    }
}

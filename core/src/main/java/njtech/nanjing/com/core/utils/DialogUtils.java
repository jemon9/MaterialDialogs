package njtech.nanjing.com.core.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

import njtech.nanjing.com.core.GravityEnum;
import njtech.nanjing.com.core.R;
import njtech.nanjing.com.core.internal.MDButton;

/**
 * Created by 张志付 on 2017/6/25.
 */

/**
 * 该类主要用于获取自定义属性的值
 * getXXX():根据给定的ColorResId等内容获取相关值
 * resolveXXX():根据给定的AttrResId等内容获取相关值
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

    public static int resolveDimension(Context context, @AttrRes int attrId) {
        return resolveDimension(context, attrId, -1);
    }

    public static int resolveDimension(Context context, @AttrRes int attrId, int fallback) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrId});
        try {
            return a.getDimensionPixelSize(0, fallback);
        } finally {
            a.recycle();
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

    /**
     * 根据ColorRes获取button的颜色
     *
     * @param context
     * @param colorId
     * @return
     */
    public static ColorStateList getActionTextColorStateList(Context context, @ColorRes int colorId) {
        TypedValue value = new TypedValue();
        context.getResources().getValue(colorId, value, true);
        if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return getActionTextStateList(context, value.data);
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                return context.getResources().getColorStateList(colorId);
            } else {
                return context.getColorStateList(colorId);
            }
        }
    }

    //used in getActionTextColorStateList()
    public static ColorStateList getActionTextStateList(Context context, int newPrimaryColor) {
        int fallbackColor = DialogUtils.resolveColor(context, android.R.attr.textColorPrimary);
        if (newPrimaryColor == 0) {
            newPrimaryColor = fallbackColor;
        }
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_enabled}, //disable
                new int[]{}  //enable
        };
        int[] colors = new int[]{DialogUtils.adjustAlpha(newPrimaryColor, 0.4f), newPrimaryColor};
        return new ColorStateList(states, colors);
    }

    //used in getActionTextStateList()
    @ColorInt
    public static int adjustAlpha(@ColorInt int color, @SuppressWarnings("SameParameterValue") float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 根据ColorAttr获取按钮颜色
     *
     * @param context
     * @param colorAttr
     * @param fallback
     * @return
     */
    public static ColorStateList resolveActionTextColorStateList(Context context, @AttrRes int colorAttr, ColorStateList fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{colorAttr});
        try {
            final TypedValue typedValue = a.peekValue(0);
            if (typedValue == null) {
                return fallback;
            }
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                return getActionTextStateList(context, typedValue.data);
            } else {
                final ColorStateList colorStateList = a.getColorStateList(0);
                if (colorStateList != null) {
                    return colorStateList;
                } else {
                    return fallback;
                }
            }
        } finally {
            a.recycle();
        }
    }

    public static boolean resolveBoolean(Context context, @AttrRes int boolAttr) {
        return resolveBoolean(context, boolAttr, false);
    }

    public static boolean resolveBoolean(Context context, @AttrRes int boolAttr, boolean defValue) {
        TypedArray a = context.obtainStyledAttributes(new int[]{boolAttr});
        try {
            return a.getBoolean(0, defValue);
        } finally {
            a.recycle();
        }
    }

    /**
     * 设置view的背景,区别不同api版本
     * @param view
     * @param drawable
     */
    public static void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}

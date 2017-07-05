package njtech.nanjing.com.core;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.CheckBox;
import android.widget.RadioButton;

import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by Heyha on 2017/7/4.
 */

@SuppressWarnings("PrivateResources")
public class MDTintHelper {
    public static void setTint(@NonNull RadioButton radioButton, @NonNull ColorStateList colors) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            radioButton.setButtonTintList(colors);
        } else {
            Drawable drawable = ContextCompat.getDrawable(radioButton.getContext(), R.drawable.abc_btn_radio_material);
            Drawable d = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(d, colors);
            radioButton.setButtonDrawable(d);
        }
    }

    public static void setTint(@NonNull RadioButton radioButton, @ColorInt int color) {
        final int disabledColor = DialogUtils.getDisabledColor(radioButton.getContext());
        ColorStateList sl =
                new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled, android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}
                        },
                        new int[]{
                                DialogUtils.resolveColor(radioButton.getContext(), R.attr.colorControlNormal),
                                color,
                                disabledColor,
                                disabledColor
                        });
        setTint(radioButton, sl);
    }

    public static void setTint(@NonNull CheckBox checkBox, @NonNull ColorStateList colors) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            checkBox.setButtonTintList(colors);
        } else {
            Drawable drawable = ContextCompat.getDrawable(checkBox.getContext(), R.drawable.abc_btn_check_material);
            Drawable d = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(d, colors);
            checkBox.setButtonDrawable(d);
        }
    }

    public static void setTint(@NonNull CheckBox checkBox, @ColorInt int color) {
        final int disabledColor = DialogUtils.getDisabledColor(checkBox.getContext());
        ColorStateList sl =
                new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled, android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, -android.R.attr.state_checked},
                                new int[]{-android.R.attr.state_enabled, android.R.attr.state_checked}
                        },
                        new int[]{
                                DialogUtils.resolveColor(checkBox.getContext(), R.attr.colorControlNormal),
                                color,
                                disabledColor,
                                disabledColor
                        });
        setTint(checkBox, sl);
    }
}
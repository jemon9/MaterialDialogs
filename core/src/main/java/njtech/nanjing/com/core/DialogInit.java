package njtech.nanjing.com.core;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import njtech.nanjing.com.core.internal.MDButton;
import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class DialogInit {

    /**
     * 根据builder选择主题
     *
     * @param builder
     * @return
     */
    @StyleRes
    static int getTheme(@NonNull MaterialDialog.Builder builder) {
        return 0;
    }

    /**
     * 根据builder选择合适的布局文件
     *
     * @param builder
     * @return
     */
    @LayoutRes
    static int getInflateLayout(@NonNull MaterialDialog.Builder builder) {
        if (builder.customView != null) {
            return R.layout.md_dialog_custom;
        } else {
            return R.layout.md_dialog_basic;
        }
    }

    /**
     * 初始化dialog
     *
     * @param dialog
     */
    static void init(final MaterialDialog dialog) {
        final MaterialDialog.Builder builder = dialog.builder;

        dialog.setCancelable(builder.cancelable);
        dialog.setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
        if (builder.backgroundColor == 0) {
            builder.backgroundColor = DialogUtils.resolveColor(builder.context, R.attr.md_background_color, DialogUtils.resolveColor(dialog.getContext(), R
                    .attr.colorBackgroundFloating));
        }
        if (builder.backgroundColor != 0) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(builder.backgroundColor);
            drawable.setCornerRadius(builder.context.getResources().getDimension(R.dimen.md_bg_corner_radius));
            dialog.getWindow().setBackgroundDrawable(drawable);
        }

        //retrieve color theme attributes
        if (!builder.positiveColorSet) {
            builder.positiveColor = DialogUtils.resolveActionTextColorStateList(builder.context, R.attr.md_positive_color, builder.positiveColor);
        }
        if (!builder.neutralColorSet) {
            builder.neutralColor = DialogUtils.resolveActionTextColorStateList(builder.context, R.attr.md_neutral_color, builder.neutralColor);
        }
        if (!builder.negativeColorSet) {
            builder.negativeColor = DialogUtils.resolveActionTextColorStateList(builder.context, R.attr.md_negative_color, builder.negativeColor);
        }
        if (!builder.widgetColorSet) {
            builder.widgetColor = DialogUtils.resolveColor(builder.context, R.attr.md_widget_color, builder.widgetColor);
        }

        //retrieve default title/content color
        if (!builder.titleColorSet) {
            final int titleColorFallback = DialogUtils.resolveColor(dialog.getContext(), android.R.attr.textColorPrimary);
            builder.titleColor = DialogUtils.resolveColor(builder.context, R.attr.md_title_color, titleColorFallback);
        }
        if (!builder.contentColorSet) {
            final int contentColorFallback = DialogUtils.resolveColor(dialog.getContext(), android.R.attr.textColorSecondary);
            builder.contentColor = DialogUtils.resolveColor(builder.context, R.attr.md_content_color, contentColorFallback);
        }

        //retrieve references to views
        dialog.title = (TextView) dialog.findViewById(R.id.md_title);
        dialog.titleFrame = dialog.findViewById(R.id.md_titleFrame);
        dialog.icon = (ImageView) dialog.findViewById(R.id.md_icon);
        dialog.content = (TextView) dialog.findViewById(R.id.md_content);

        //retrieve dialog's buttons
        dialog.positiveButton = (MDButton) dialog.findViewById(R.id.md_buttonDefaultPositive);
        dialog.neutralButton = (MDButton) dialog.findViewById(R.id.md_buttonDefaultNeutral);
        dialog.negativeButton = (MDButton) dialog.findViewById(R.id.md_buttonDefaultNegative);

        //setup the visibility of dialog's buttons
        dialog.positiveButton.setVisibility(builder.positiveText != null ? View.VISIBLE : View.GONE);
        dialog.neutralButton.setVisibility(builder.neutralText != null ? View.VISIBLE : View.GONE);
        dialog.negativeButton.setVisibility(builder.negativeText != null ? View.VISIBLE : View.GONE);

        //setup the focus of dialog's buttons
        dialog.positiveButton.setFocusable(true);
        dialog.neutralButton.setFocusable(true);
        dialog.negativeButton.setFocusable(true);

        if (builder.positiveFocus) {
            dialog.positiveButton.requestFocus();
        }
        if (builder.neutralFocus) {
            dialog.neutralButton.requestFocus();
        }
        if (builder.negativeFocus) {
            dialog.negativeButton.requestFocus();
        }

        //setup icon
        if (builder.icon != null) {
            dialog.icon.setImageDrawable(builder.icon);
            dialog.icon.setVisibility(View.VISIBLE);
        } else {
            Drawable icon = DialogUtils.resolveDrawable(builder.context, R.attr.md_icon);
            if (icon != null) {
                dialog.icon.setVisibility(View.VISIBLE);
                dialog.icon.setImageDrawable(icon);
            } else {
                dialog.icon.setVisibility(View.GONE);
            }
        }

        //setup icon size limiting
        int maxIconSize = builder.maxIconSize;
        if (maxIconSize == -1) {
            maxIconSize = DialogUtils.resolveDimension(builder.context, R.attr.md_icon_max_size);
        }
        if (builder.limitIconToDefaultSize || DialogUtils.resolveBoolean(builder.context, R.attr.md_icon_limit_icon_to_default_size)) {
            maxIconSize = builder.context.getResources().getDimensionPixelSize(R.dimen.md_icon_max_size);
        }
        if (maxIconSize > -1) {
            dialog.icon.setAdjustViewBounds(true);
            dialog.icon.setMaxHeight(maxIconSize);
            dialog.icon.setMaxWidth(maxIconSize);
            dialog.icon.requestLayout();
        }

        //setup divider color in case content scrolls
        if (!builder.dividerColorSet) {
            final int dividerColorFallback = DialogUtils.resolveColor(dialog.getContext(), R.attr.md_divider);
            builder.dividerColor = DialogUtils.resolveColor(builder.context, R.attr.md_divider_color, dividerColorFallback);
        }
        dialog.view.setDividerColor(builder.dividerColor);
    }
}

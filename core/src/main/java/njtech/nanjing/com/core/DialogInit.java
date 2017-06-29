package njtech.nanjing.com.core;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.annotation.UiThread;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import njtech.nanjing.com.core.internal.MDButton;
import njtech.nanjing.com.core.internal.MDRootLayout;
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
        boolean darkTheme = DialogUtils.resolveBoolean(builder.context,R.attr.md_dark_theme,builder.theme == Theme.DARK);
        builder.theme = darkTheme ? Theme.DARK : Theme.LIGHT;
        return darkTheme ? R.style.MD_Dark : R.style.MD_Light;
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
    @SuppressWarnings("ConstantConditions")
    @UiThread
    public static void init(final MaterialDialog dialog) {
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

        //setup title and titleFrame
        if (dialog.title != null) {
            dialog.title.setTextColor(builder.titleColor);
            dialog.title.setGravity(builder.titleGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialog.title.setTextAlignment(builder.titleGravity.getTextAlignmentInt());
            }
            if (builder.title == null) {
                dialog.titleFrame.setVisibility(View.GONE);
            } else {
                dialog.title.setText(builder.title);
                dialog.titleFrame.setVisibility(View.VISIBLE);
            }
        }

        //setup content
        if (dialog.content != null) {
            //设置超链接
            dialog.content.setMovementMethod(new LinkMovementMethod());
            dialog.content.setLineSpacing(0f, builder.contentLineSpaceMultiplier);
            dialog.content.setTextColor(builder.contentColor);
            dialog.content.setGravity(builder.contentGravity.getGravityInt());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                dialog.content.setTextAlignment(builder.contentGravity.getTextAlignmentInt());
            }
            if (builder.content != null) {
                dialog.content.setText(builder.content);
                dialog.content.setVisibility(View.VISIBLE);
            } else {
                dialog.content.setVisibility(View.GONE);
            }
        }

        //setup buttons
        dialog.view.setButtonGravity(builder.buttonsGravity);
        dialog.view.setButtonsStackedGravity(builder.btnStackedGravity);
        dialog.view.setStackBehavior(builder.stackBehavior);
        boolean textAllCaps;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            textAllCaps = DialogUtils.resolveBoolean(builder.context, android.R.attr.textAllCaps, true);
            if (textAllCaps) {
                textAllCaps = DialogUtils.resolveBoolean(builder.context, R.attr.textAllCaps, true);
            }
        } else {
            textAllCaps = DialogUtils.resolveBoolean(builder.context, R.attr.textAllCaps, true);
        }
        MDButton positiveTextView = dialog.positiveButton;
        positiveTextView.setAllCapsCompat(textAllCaps);   //该设置不能由builder构建，只能从系统中获取选择并构建
        positiveTextView.setText(builder.positiveText);
        positiveTextView.setTextColor(builder.positiveColor);
        dialog.positiveButton.setStackedSelector(dialog.getButtonSelector(DialogAction.POSITIVE, true));
        dialog.positiveButton.setDefaultSelector(dialog.getButtonSelector(DialogAction.POSITIVE, false));
        dialog.positiveButton.setTag(DialogAction.POSITIVE);
        dialog.positiveButton.setOnClickListener(dialog);
        dialog.positiveButton.setVisibility(View.VISIBLE);

        MDButton neutralTextView = dialog.neutralButton;
        neutralTextView.setAllCapsCompat(textAllCaps);
        neutralTextView.setText(builder.neutralText);
        neutralTextView.setTextColor(builder.neutralColor);
        dialog.neutralButton.setStackedSelector(dialog.getButtonSelector(DialogAction.NEUTRAL, true));
        dialog.neutralButton.setDefaultSelector(dialog.getButtonSelector(DialogAction.NEUTRAL, false));
        dialog.neutralButton.setTag(DialogAction.NEUTRAL);
        dialog.neutralButton.setOnClickListener(dialog);
        dialog.neutralButton.setVisibility(View.VISIBLE);

        MDButton negativeTextView = dialog.negativeButton;
        negativeTextView.setAllCapsCompat(textAllCaps);
        negativeTextView.setText(builder.negativeText);
        negativeTextView.setTextColor(builder.negativeColor);
        negativeTextView.setStackedSelector(dialog.getButtonSelector(DialogAction.NEGATIVE, true));
        negativeTextView.setDefaultSelector(dialog.getButtonSelector(DialogAction.NEGATIVE, false));
        negativeTextView.setTag(DialogAction.NEGATIVE);
        negativeTextView.setOnClickListener(dialog);
        negativeTextView.setVisibility(View.VISIBLE);

        //setup custom view
        if (builder.customView != null) {
            ((MDRootLayout) dialog.findViewById(R.id.root)).setNoTitleNoPadding();
            FrameLayout frameLayout = (FrameLayout) dialog.findViewById(R.id.md_customViewFrame);
            dialog.customViewFrame = frameLayout;
            View innerView = builder.customView;
            if (innerView.getParent() != null) {
                ((ViewGroup) innerView.getParent()).removeView(innerView);
            }
            if (builder.wrapCustomViewInScroll) {
                final Resources r = dialog.getContext().getResources();
                final int framePadding = r.getDimensionPixelSize(R.dimen.md_dialog_frame_margin);
                final ScrollView sv = new ScrollView(dialog.getContext());
                int paddingTop = r.getDimensionPixelSize(R.dimen.md_content_padding_top);
                int paddingBottom = r.getDimensionPixelSize(R.dimen.md_content_padding_bottom);
                sv.setClipToPadding(false);
                if (innerView instanceof EditText) {
                    sv.setPadding(framePadding, paddingTop, framePadding, paddingBottom);
                } else {
                    sv.setPadding(0, paddingTop, 0, paddingBottom);
                    innerView.setPadding(framePadding, 0, framePadding, 0);
                }
                sv.addView(innerView, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                innerView = sv;
            }
            frameLayout.addView(innerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //setup user listener
        if (builder.onShowListener != null) {
            dialog.setOnShowListener(builder.onShowListener);
        }
        if (builder.onCancelListener != null) {
            dialog.setOnCancelListener(builder.onCancelListener);
        }
        if (builder.onDismissListener != null) {
            dialog.setOnDismissListener(builder.onDismissListener);
        }
        if (builder.onKeyListener != null) {
            dialog.setOnKeyListener(builder.onKeyListener);
        }

        //setup internal show listener
        dialog.setOnShowListenerInternal();

        //setup other internal init
        dialog.setViewInternal(dialog.view);

        //setup min height and max width calculations
        WindowManager wm = dialog.getWindow().getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int windowWidth = size.x;
        final int windowHeight = size.y;
        final int windowVerticalPadding = builder.context.getResources().getDimensionPixelSize(R.dimen.md_dialog_vertical_margin);
        final int windowHorizontalPadding = builder.context.getResources().getDimensionPixelSize(R.dimen.md_dialog_horizontal_margin);
        final int maxWidth = builder.context.getResources().getDimensionPixelSize(R.dimen.md_dialog_max_width);
        final int calculatedWidth = windowWidth - 2 * windowHorizontalPadding;
        dialog.view.setMaxHeight(windowHeight - 2 * windowVerticalPadding);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = Math.min(maxWidth, calculatedWidth);
        dialog.getWindow().setAttributes(lp);
    }
}

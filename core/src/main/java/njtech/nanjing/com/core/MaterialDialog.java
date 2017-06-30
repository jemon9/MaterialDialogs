package njtech.nanjing.com.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;

import njtech.nanjing.com.core.internal.MDButton;
import njtech.nanjing.com.core.internal.MDRootLayout;
import njtech.nanjing.com.core.internal.ThemeSingleton;
import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class MaterialDialog extends DialogBase implements View.OnClickListener {

    protected final Builder builder;
    private Handler mHandler;
    protected ImageView icon;
    protected TextView title;
    protected View titleFrame;
    protected TextView content;
    MDButton positiveButton;
    MDButton neutralButton;
    MDButton negativeButton;
    ProgressBar progressBar;
    TextView progressLable;
    TextView progressMinMax;

    FrameLayout customViewFrame;

    protected MaterialDialog(Builder builder) {
        super(builder.context, DialogInit.getTheme(builder));
        mHandler = new Handler();
        this.builder = builder;
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        view = (MDRootLayout) inflater.inflate(DialogInit.getInflateLayout(builder), null);
        DialogInit.init(this);
    }

    public final View getView() {
        return view;
    }

    public final TextView getTitleView() {
        return title;
    }

    public final ImageView getIconView() {
        return icon;
    }

    public final TextView getContentView() {
        return content;
    }

    public final View getCustomView() {
        return builder.customView;
    }

    @UiThread
    @Override
    public final void setTitle(@Nullable CharSequence title) {
        this.title.setText(title);
    }

    @UiThread
    @Override
    public final void setTitle(@StringRes int titleId) {
        setTitle(builder.context.getString(titleId));
    }

    @UiThread
    public final void setTitle(@StringRes int titleId, Object... formatArgs) {
        setTitle(builder.context.getString(titleId, formatArgs));
    }

    @UiThread
    public final void setIcon(final Drawable drawable) {
        this.icon.setImageDrawable(drawable);
        this.icon.setVisibility(drawable != null ? View.VISIBLE : View.GONE);
    }

    @UiThread
    public final void setIcon(@DrawableRes int drawableId) {
        this.icon.setImageResource(drawableId);
        this.icon.setVisibility(drawableId != 0 ? View.VISIBLE : View.GONE);
    }

    @UiThread
    public final void setIconAttribute(@AttrRes int iconAttribute) {
        Drawable drawable = DialogUtils.resolveDrawable(builder.context, iconAttribute);
        setIcon(drawable);
    }

    @UiThread
    public final void setContent(CharSequence content) {
        this.content.setText(content);
        this.content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
    }

    @UiThread
    public final void setContent(@StringRes int contentId) {
        setContent(builder.context.getString(contentId));
    }

    @UiThread
    public final void setContent(@StringRes int contentId, @Nullable Object... formatArgs) {
        setContent(builder.context.getString(contentId, formatArgs));
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public final int getCurrentProgress() {
        if (progressBar != null) {
            return progressBar.getProgress();
        }
        return -1;
    }

    public final int getMaxProgress() {
        if (progressBar != null) {
            return progressBar.getMax();
        }
        return -1;
    }

    public final void incrementProgress(final int by) {
        setProgress(getCurrentProgress() + by);
    }

    public final void setProgress(final int progress) {
        if (builder.progress <= -2) {
            Log.w("Material", "Using setProgress() has no effect on a indeterminate dialog");
            return;
        }
        progressBar.setProgress(progress);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (progressLable != null) {
                    progressLable.setText(builder.progressPercentFormat.format((float) getCurrentProgress() / (float) builder.progressMax));
                }
                if (progressMinMax != null) {
                    progressMinMax.setText(String.format(builder.progressNumberFormat, getCurrentProgress(), builder.progressMax));
                }
            }
        });
    }

    @SuppressWarnings("unused")
    public final void setProgressPercentFormat(NumberFormat numberFormat) {
        builder.progressPercentFormat = numberFormat;
        setProgress(getCurrentProgress());
    }

    @SuppressWarnings("unused")
    public final void setProgressNumberFormat(String numberFormat) {
        builder.progressNumberFormat = numberFormat;
        setProgress(getCurrentProgress());
    }


    @Override
    public void onClick(View v) {
        DialogAction tag = (DialogAction) v.getTag();
        switch (tag) {
            case POSITIVE:
                if (builder.onPositiveCallback != null) {
                    builder.onPositiveCallback.onClick(this, tag);
                }
                if (builder.autoDismiss) {
                    dismiss();
                }
                break;
            case NEUTRAL:
                if (builder.onNeutralCallback != null) {
                    builder.onNeutralCallback.onClick(this, tag);
                }
                if (builder.autoDismiss) {
                    dismiss();
                }
                break;
            case NEGATIVE:
                if (builder.onNegativeCallback != null) {
                    builder.onNegativeCallback.onClick(this, tag);
                }
                if (builder.autoDismiss) {
                    cancel();
                }
                break;
        }
    }

    /**
     * 获取按钮的背景图片
     *
     * @param which     POSITIVE  NEUTRAL  NEGATIVE
     * @param isStacked 是否层叠
     * @return
     */
    Drawable getButtonSelector(DialogAction which, boolean isStacked) {
        if (isStacked) {
            if (builder.btnSelectorStacked != 0) {
                return ResourcesCompat.getDrawable(builder.context.getResources(), builder.btnSelectorStacked, null);
            }
            final Drawable d = DialogUtils.resolveDrawable(builder.context, R.attr.md_btn_stacked_selector);
            if (d != null) {
                return d;
            }
            return DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_stacked_selector);
        } else {
            switch (which) {
                default: {
                    if (builder.btnSelectorPositive != 0) {
                        return ResourcesCompat.getDrawable(builder.context.getResources(), builder.btnSelectorPositive, null);
                    }
                    Drawable d = DialogUtils.resolveDrawable(builder.context, R.attr.md_btn_positive_selector);
                    if (d != null) {
                        return d;
                    }
                    d = DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_positive_selector);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //设置水波效果
                        // TODO: 2017/6/28 设置drawable的水波效果
                    }
                    return d;
                }
                case NEUTRAL: {
                    if (builder.btnSelectorNeutral != 0) {
                        return ResourcesCompat.getDrawable(builder.context.getResources(), builder.btnSelectorNeutral, null);
                    }
                    Drawable d = DialogUtils.resolveDrawable(builder.context, R.attr.md_btn_neutral_selector);
                    if (d != null) {
                        return d;
                    }
                    d = DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_neutral_selector);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //设置水波效果
                        // TODO: 2017/6/28 设置drawable的水波效果
                    }
                    return d;
                }
                case NEGATIVE: {
                    if (builder.btnSelectorNegative != 0) {
                        return ResourcesCompat.getDrawable(builder.context.getResources(), builder.btnSelectorNegative, null);
                    }
                    Drawable d = DialogUtils.resolveDrawable(builder.context, R.attr.md_btn_negative_selector);
                    if (d != null) {
                        return d;
                    }
                    d = DialogUtils.resolveDrawable(getContext(), R.attr.md_btn_negative_selector);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //设置水波效果
                        // TODO: 2017/6/28 设置drawable的水波效果
                    }
                    return d;
                }

            }
        }
    }

    public final boolean isCanceled() {
        return !isShowing();
    }


    public interface SingleButtonCallback {
        void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which);
    }

    /**
     * 创建MaterialDialog
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class Builder {
        protected final Context context;
        protected int widgetColor;
        protected int buttonRippleColor = 0;
        protected int titleColor = -1;
        protected int contentColor = -1;
        protected boolean titleColorSet = false;
        protected boolean contentColorSet = false;
        protected float contentLineSpaceMultiplier = 1.2f;
        protected Drawable icon;
        protected View customView;
        protected CharSequence content;
        protected StackBehavior stackBehavior;
        protected GravityEnum titleGravity = GravityEnum.START;
        protected GravityEnum contentGravity = GravityEnum.START;
        protected GravityEnum btnStackedGravity = GravityEnum.END;
        protected GravityEnum itemsGravity = GravityEnum.START;
        protected GravityEnum buttonsGravity = GravityEnum.START;
        protected CharSequence title;
        protected CharSequence positiveText;
        protected CharSequence neutralText;
        protected CharSequence negativeText;
        protected boolean positiveFocus;
        protected boolean neutralFocus;
        protected boolean negativeFocus;
        protected ColorStateList positiveColor;
        protected ColorStateList negativeColor;
        protected ColorStateList neutralColor;
        protected ColorStateList choiceWidgetColor;
        protected boolean positiveColorSet = false;
        protected boolean neutralColorSet = false;
        protected boolean negativeColorSet = false;
        protected boolean widgetColorSet = false;
        protected boolean wrapCustomViewInScroll;
        protected boolean dividerColorSet = false;
        protected boolean cancelable = true;
        protected boolean canceledOnTouchOutside = true;
        protected boolean limitIconToDefaultSize;
        protected int backgroundColor;
        protected int maxIconSize = -1;
        protected int dividerColor;
        protected Theme theme = Theme.LIGHT;
        protected OnShowListener onShowListener;
        protected OnCancelListener onCancelListener;
        protected OnDismissListener onDismissListener;
        protected OnKeyListener onKeyListener;
        protected boolean autoDismiss = true;
        protected SingleButtonCallback onPositiveCallback;
        protected SingleButtonCallback onNeutralCallback;
        protected SingleButtonCallback onNegativeCallback;
        protected SingleButtonCallback onAnyCallback;
        protected int progress = -2;
        protected int progressMax = 0;
        protected boolean indeterminateProgress;
        protected boolean indeterminateIsHorizontalProgress;
        protected boolean showMinMax;
        protected NumberFormat progressPercentFormat;
        protected String progressNumberFormat;

        @DrawableRes
        protected int btnSelectorStacked;

        @DrawableRes
        protected int btnSelectorPositive;

        @DrawableRes
        protected int btnSelectorNeutral;

        @DrawableRes
        protected int btnSelectorNegative;


        public Builder(Context context) {
            this.context = context;
            final int materialBlue = DialogUtils.getColor(context, R.color.md_material_blue_600);
            this.widgetColor = DialogUtils.resolveColor(context, R.attr.colorAccent, materialBlue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.widgetColor = DialogUtils.resolveColor(context, android.R.attr.colorAccent,
                        this.widgetColor);
            }
            positiveColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            negativeColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            neutralColor = DialogUtils.getActionTextStateList(context, this.widgetColor);
            int fallback = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fallback = DialogUtils.resolveColor(context, android.R.attr.colorControlHighlight);
            }
            this.buttonRippleColor = DialogUtils.resolveColor(context, R.attr
                    .md_btn_ripple_color, DialogUtils.resolveColor(context, R.attr
                    .colorControlHighlight, fallback));

            //progressbar percent format
            this.progressPercentFormat = NumberFormat.getPercentInstance();
            this.progressNumberFormat = "%1d/%2d";

            //setup the default theme based on Activity theme's primary color darkness(more white or more black)
            final int primaryTextColor = DialogUtils.resolveColor(context, android.R.attr.textColorPrimary);
            this.theme = DialogUtils.isColorDark(primaryTextColor) ? Theme.LIGHT : Theme.DARK;

            checkSingleton();

            this.titleGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_title_gravity,
                    this.titleGravity);
            this.contentGravity = DialogUtils.resolveGravityEnum(context, R.attr
                    .md_content_gravity, this.contentGravity);
            this.btnStackedGravity = DialogUtils.resolveGravityEnum(context, R.attr
                    .md_btnstacked_gravity, this.btnStackedGravity);
            this.itemsGravity = DialogUtils.resolveGravityEnum(context, R.attr.md_items_gravity,
                    this.itemsGravity);
            this.buttonsGravity = DialogUtils.resolveGravityEnum(context, R.attr
                    .md_buttons_gravity, this.buttonsGravity);
        }

        private void checkSingleton() {
            if (ThemeSingleton.get(false) == null) {
                return;
            }
            ThemeSingleton ts = ThemeSingleton.get();
            if (ts.darkTheme) {
                this.theme = Theme.DARK;
            }
            if (ts.titleColor != 0) {
                this.titleColor = ts.titleColor;
            }
            if (ts.contentColor != 0) {
                this.contentColor = ts.contentColor;
            }
            if (ts.positiveColor != null) {
                this.positiveColor = ts.positiveColor;
            }
            if (ts.neutralColor != null) {
                this.neutralColor = ts.neutralColor;
            }
            if (ts.negativeColor != null) {
                this.negativeColor = ts.negativeColor;
            }
            if (ts.icon != null) {
                this.icon = ts.icon;
            }
            if (ts.backgroundColor != 0) {
                this.backgroundColor = ts.backgroundColor;
            }
            if (ts.dividerColor != 0) {
                this.dividerColor = ts.dividerColor;
            }
            if (ts.btnSelectorStacked != 0) {
                this.btnSelectorStacked = ts.btnSelectorStacked;
            }
            if (ts.btnSelectorPositive != 0) {
                this.btnSelectorPositive = ts.btnSelectorPositive;
            }
            if (ts.btnSelectorNeutral != 0) {
                this.btnSelectorNeutral = ts.btnSelectorNeutral;
            }
            if (ts.btnSelectorNegative != 0) {
                this.btnSelectorNegative = ts.btnSelectorNegative;
            }
            if (ts.widgetColor != 0) {
                this.widgetColor = ts.widgetColor;
            }
//            if (ts.linkColor != null){
//                this.linkColor = ts.linkColor;
//            }
            this.titleGravity = ts.titleGravity;
            this.contentGravity = ts.contentGravity;
            this.btnStackedGravity = ts.btnStackedGravity;
            this.itemsGravity = ts.itemsGravity;
            this.buttonsGravity = ts.buttonsGravity;
        }

        public final Context getContext() {
            return context;
        }

        public Builder title(@StringRes int titleRes) {
            this.title(this.context.getText(titleRes));
            return this;
        }

        public Builder title(@NonNull CharSequence text) {
            this.title = text;
            return this;
        }

        public Builder titleGravity(@NonNull GravityEnum gravityEnum) {
            this.titleGravity = gravityEnum;
            return this;
        }

        public Builder buttonRippleColor(@ColorInt int color) {
            this.buttonRippleColor = color;
            return this;
        }

        public Builder buttonRippleColorRes(@ColorRes int colorRes) {
            return buttonRippleColor(DialogUtils.getColor(context, colorRes));

        }

        public Builder buttonRippleColorAttr(@AttrRes int colorAttr) {
            return buttonRippleColor(DialogUtils.resolveColor(context, colorAttr));
        }

        public Builder titleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            this.titleColorSet = true;
            return this;
        }

        public Builder titleColorRes(@ColorRes int titleColorRes) {
            return titleColor(DialogUtils.getColor(context, titleColorRes));
        }

        public Builder titleColorAttr(@AttrRes int titleColorAttr) {
            return titleColor(DialogUtils.resolveColor(context, titleColorAttr));
        }

        public Builder icon(@NonNull Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder iconRes(@DrawableRes int icon) {
            this.icon = ResourcesCompat.getDrawable(getContext().getResources(), icon, null);
            return this;
        }

        public Builder iconAttr(@AttrRes int iconAttr) {
            this.icon = DialogUtils.resolveDrawable(context, iconAttr);
            return this;
        }

        public Builder content(@NonNull CharSequence content) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set content() when you're using a custom view");
            }
            this.content = content;
            return this;
        }

        public Builder contentRes(@StringRes int contentRes) {
            CharSequence text = this.context.getText(contentRes);
            return content(text);
        }

        public Builder contentColor(@ColorInt int contentColor) {
            this.contentColor = contentColor;
            this.contentColorSet = true;
            return this;
        }

        public Builder contentColorRes(@ColorRes int contentColorRes) {
            contentColor(DialogUtils.getColor(context, contentColorRes));
            return this;
        }

        public Builder contentColorAttr(@AttrRes int contentColorAttr) {
            return contentColor(DialogUtils.resolveColor(context, contentColorAttr));
        }

        public Builder contentGravity(@NonNull GravityEnum contentGravity) {
            this.contentGravity = contentGravity;
            return this;
        }

        public Builder contentLineSpacing(@NonNull float contentLineSpaceMultiplier) {
            this.contentLineSpaceMultiplier = contentLineSpaceMultiplier;
            return this;
        }

        public Builder buttonGravity(@NonNull GravityEnum buttonsGravity) {
            this.buttonsGravity = buttonsGravity;
            return this;
        }

        public Builder btnStackedGravity(@NonNull GravityEnum btnStackedGravity) {
            this.btnStackedGravity = btnStackedGravity;
            return this;
        }

        public Builder positiveText(@NonNull CharSequence positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder positiveTextRes(@StringRes int positiveTextRes) {
            if (positiveTextRes == 0) {
                return this;
            }
            return positiveText(this.context.getText(positiveTextRes));
        }

        public Builder neutralText(@NonNull CharSequence neutralText) {
            this.neutralText = neutralText;
            return this;
        }

        public Builder neutralTextRes(@StringRes int neutralTextRes) {
            if (neutralTextRes == 0) {
                return this;
            }
            return neutralText(this.context.getText(neutralTextRes));
        }

        public Builder negativeText(@NonNull CharSequence negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder negativeTextRes(@StringRes int negativeTextRes) {
            if (negativeTextRes == 0) {
                return this;
            }
            return negativeText(this.context.getText(negativeTextRes));
        }

        public Builder positiveColor(@NonNull ColorStateList positiveColor) {
            this.positiveColor = positiveColor;
            this.positiveColorSet = true;
            return this;
        }

        // TODO: 2017/6/27 根据colorId获取按钮颜色的原理有待研究
        public Builder positiveColorRes(@ColorRes int positiveColorRes) {
            return positiveColor(DialogUtils.getActionTextColorStateList(context, positiveColorRes));
        }

        public Builder positiveColorAttr(@AttrRes int positiveColorAttr) {
            return positiveColor(DialogUtils.resolveActionTextColorStateList(this.context, positiveColorAttr, null));
        }

        public Builder positiveColor(@ColorInt int positiveColor) {
            return positiveColor(DialogUtils.getActionTextStateList(context, positiveColor));
        }

        public Builder positiveFocus(boolean isFocusDefault) {
            this.positiveFocus = isFocusDefault;
            return this;
        }

        public Builder neutralColor(@NonNull ColorStateList neutralColor) {
            this.neutralColor = neutralColor;
            this.neutralColorSet = true;
            return this;
        }

        public Builder neutralColorRes(@ColorRes int neutralColorRes) {
            return neutralColor(DialogUtils.getActionTextColorStateList(context, neutralColorRes));
        }

        public Builder neutralColorAttr(@AttrRes int neutralColorAttr) {
            return neutralColor(DialogUtils.resolveActionTextColorStateList(context, neutralColorAttr, null));
        }

        public Builder neutralColor(@ColorInt int neutralColor) {
            return neutralColor(DialogUtils.getActionTextStateList(context, neutralColor));
        }

        public Builder neutralFocus(boolean isFocusDefault) {
            this.neutralFocus = isFocusDefault;
            return this;
        }

        public Builder negativeColor(@NonNull ColorStateList negativeColor) {
            this.negativeColor = negativeColor;
            this.negativeColorSet = true;
            return this;
        }

        public Builder negativeColorRes(@ColorRes int negativeColorRes) {
            return negativeColor(DialogUtils.getActionTextColorStateList(context, negativeColorRes));
        }

        public Builder negativeColorAttr(@AttrRes int negativeColorAttr) {
            return negativeColor(DialogUtils.resolveActionTextColorStateList(context, negativeColorAttr, null));
        }

        public Builder negativeColor(@ColorInt int negativeColor) {
            return negativeColor(DialogUtils.getActionTextStateList(context, negativeColor));
        }

        public Builder negativeFocus(boolean isFocusDefault) {
            this.negativeFocus = isFocusDefault;
            return this;
        }

        public Builder backgroundColor(@ColorInt int bgColor) {
            this.backgroundColor = bgColor;
            return this;
        }

        public Builder backgroundColorRes(@ColorRes int bgColorRes) {
            return backgroundColor(DialogUtils.getColor(context, bgColorRes));
        }

        public Builder backgroundColorAttr(@AttrRes int bgColorAttr) {
            return backgroundColor(DialogUtils.resolveColor(this.context, bgColorAttr));
        }

        public Builder widgetColor(@ColorInt int widgetColor) {
            this.widgetColor = widgetColor;
            this.widgetColorSet = true;
            return this;
        }

        public Builder widgetColorRes(@ColorRes int widgetColorRes) {
            return widgetColor(DialogUtils.getColor(context, widgetColorRes));
        }

        public Builder widgetColorAttr(@AttrRes int widgetColorAttr) {
            return widgetColor(DialogUtils.resolveColor(this.context, widgetColorAttr));
        }

        public Builder choiceWidgetColor(@Nullable ColorStateList colorStateList) {
            this.choiceWidgetColor = colorStateList;
            return this;
        }

        public Builder dividerColor(@ColorInt int dividerColor) {
            this.dividerColor = dividerColor;
            this.dividerColorSet = true;
            return this;
        }

        public Builder dividerColorRes(@ColorRes int colorRes) {
            return dividerColor(DialogUtils.getColor(context, colorRes));
        }

        public Builder dividerColorAttr(@AttrRes int colorAttr) {
            return dividerColor(DialogUtils.resolveColor(context, colorAttr));
        }

        public Builder maxIconSize(int maxIconSize) {
            this.maxIconSize = maxIconSize;
            return this;
        }

        public Builder maxIconSizeRes(@DimenRes int maxIconSizeRes) {
            return maxIconSize((int) context.getResources().getDimension(maxIconSizeRes));
        }

        public Builder setStackedBehavior(StackBehavior stackedBehavior) {
            this.stackBehavior = stackedBehavior;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes) {
            this.btnSelectorPositive = selectorRes;
            this.btnSelectorNeutral = selectorRes;
            this.btnSelectorNegative = selectorRes;
            return this;
        }

        public Builder btnSelector(@DrawableRes int selectorRes, @NonNull DialogAction dialogAction) {
            switch (dialogAction) {
                default:
                    this.btnSelectorPositive = selectorRes;
                    break;
                case NEUTRAL:
                    this.btnSelectorNeutral = selectorRes;
                    break;
                case NEGATIVE:
                    this.btnSelectorNegative = selectorRes;
                    break;
            }
            return this;
        }

        public Builder customView(@LayoutRes int layoutId, boolean wrapInScrollView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return customView(inflater.inflate(layoutId, null), wrapInScrollView);
        }

        public Builder limitIconToDefaultSize() {
            this.limitIconToDefaultSize = true;
            return this;
        }

        public Builder customView(@NonNull View view, boolean wrapInScrollView) {
            if (this.content != null) {
                throw new IllegalStateException("You cannot use customView() when you have content set.");
            } else if (this.progress > -2 || this.indeterminateProgress) {
                throw new IllegalStateException("You cannot use customView() with a dialog progress");
            }
            if (view.getParent() != null && view.getParent() instanceof ViewGroup) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            this.customView = view;
            this.wrapCustomViewInScroll = wrapInScrollView;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            this.canceledOnTouchOutside = cancelable;
            return this;
        }

        public Builder theme(@NonNull Theme theme) {
            this.theme = theme;
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        /**
         * make this dialog a progress bar
         *
         * @param indeterminate true:circul spinner,  false:a horizontal progress bar
         * @param progressMax   indeterminate=false时进度条最大值
         * @return
         */
        public Builder progress(boolean indeterminate, int progressMax) {
            if (this.customView != null) {
                throw new IllegalStateException("You cannot set progress() when you're using customView");
            }
            if (indeterminate) {
                this.indeterminateProgress = true;
                this.progress = -2;
            } else {
                this.indeterminateIsHorizontalProgress = false;
                this.indeterminateProgress = false;
                this.progress = -1;
                this.progressMax = progressMax;
            }
            return this;
        }

        /**
         * 决定progress的显示模式，唯一设置的地方
         *
         * @param indeterminate
         * @param progressMax
         * @param showMinMax
         * @return
         */
        public Builder progress(boolean indeterminate, int progressMax, boolean showMinMax) {
            this.showMinMax = showMinMax;
            return progress(indeterminate, progressMax);
        }

        /**
         * 用于显示普通progress的百分比进度
         *
         * @param progressPercentFormat
         * @return
         */
        public Builder progressPercentFormat(@NonNull NumberFormat progressPercentFormat) {
            this.progressPercentFormat = progressPercentFormat;
            return this;
        }

        /**
         * 用于显示普通progress的最小/最大值标签
         *
         * @param progressNumberFormat
         * @return
         */
        public Builder progressNumberFormat(@NonNull String progressNumberFormat) {
            this.progressNumberFormat = progressNumberFormat;
            return this;
        }

        public Builder progressIndeterminateStyle(boolean isHorizontal) {
            this.indeterminateIsHorizontalProgress = isHorizontal;
            return this;
        }

        public Builder onShowListener(@NonNull OnShowListener listener) {
            this.onShowListener = listener;
            return this;
        }

        public Builder onCancelListener(@NonNull OnCancelListener listener) {
            this.onCancelListener = listener;
            return this;
        }

        public Builder onDismissListener(@NonNull OnDismissListener listener) {
            this.onDismissListener = listener;
            return this;
        }

        public Builder onKeyListener(@NonNull OnKeyListener listener) {
            this.onKeyListener = listener;
            return this;
        }

        public Builder onPositive(@NonNull SingleButtonCallback callback) {
            this.onPositiveCallback = callback;
            return this;
        }

        public Builder onNegative(@NonNull SingleButtonCallback callback) {
            this.onNegativeCallback = callback;
            return this;
        }

        public Builder onNeutral(@NonNull SingleButtonCallback callback) {
            this.onNeutralCallback = callback;
            return this;
        }

        public Builder onAny(@NonNull SingleButtonCallback callback) {
            this.onAnyCallback = callback;
            return this;
        }

        @UiThread
        public MaterialDialog build() {
            return new MaterialDialog(this);
        }

        @UiThread
        public MaterialDialog show() {
            MaterialDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }
}

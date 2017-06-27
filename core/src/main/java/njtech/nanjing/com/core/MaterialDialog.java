package njtech.nanjing.com.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import njtech.nanjing.com.core.internal.MDButton;
import njtech.nanjing.com.core.internal.MDRootLayout;
import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class MaterialDialog extends DialogBase {

    protected final Builder builder;
    protected ImageView icon;
    protected TextView title;
    protected View titleFrame;
    protected TextView content;
    MDButton positiveButton;
    MDButton neutralButton;
    MDButton negativeButton;

    protected MaterialDialog(Builder builder) {
        super(builder.context, DialogInit.getTheme(builder));
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


    /**
     * 创建MaterialDialog
     */
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

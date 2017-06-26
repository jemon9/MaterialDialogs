package njtech.nanjing.com.core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;

import njtech.nanjing.com.core.internal.MDRootLayout;
import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class MaterialDialog extends DialogBase {

    protected Builder builder;

    protected MaterialDialog(Builder builder) {
        super(builder.context, DialogInit.getTheme(builder));
        this.builder = builder;
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        view = (MDRootLayout) inflater.inflate(DialogInit.getInflateLayout(builder), null);
        DialogInit.init(this);
    }

    /**
     * 创建MaterialDialog
     */
    public static class Builder {
        protected final Context context;
        protected int widgetColor;
        protected ColorStateList positiveColor;
        protected ColorStateList negativeColor;
        protected ColorStateList neutralColor;
        protected int buttonRippleColor = 0;
        protected int titleColor = -1;
        protected int contentColor = -1;
        protected boolean titleColorSet = false;
        protected Drawable icon;
        protected GravityEnum titleGravity = GravityEnum.START;
        protected GravityEnum contentGravity = GravityEnum.START;
        protected GravityEnum btnStackedGravity = GravityEnum.END;
        protected GravityEnum itemsGravity = GravityEnum.START;
        protected GravityEnum buttonsGravity = GravityEnum.START;
        protected CharSequence title;


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

        public Builder icon(@NonNull Drawable icon){
            this.icon = icon;
            return this;
        }

        public Builder iconRes(@DrawableRes int icon){
            this.icon = ResourcesCompat.getDrawable(getContext().getResources(),icon,null);
            return this;
        }

        public Builder iconAttr(@AttrRes int iconAttr){
            this.icon = DialogUtils.resolveDrawable(context,iconAttr);
            return this;
        }
    }
}

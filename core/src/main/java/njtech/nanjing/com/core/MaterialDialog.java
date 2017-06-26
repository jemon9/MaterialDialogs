package njtech.nanjing.com.core;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;

import njtech.nanjing.com.core.internal.MDRootLayout;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class MaterialDialog extends DialogBase {

    protected Builder builder;

    protected MaterialDialog(Builder builder) {
        super(builder.context, DialogInit.getTheme(builder));
        this.builder = builder;
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        view = (MDRootLayout) inflater.inflate(DialogInit.getInflateLayout(builder),null);
        DialogInit.init(this);
    }

    /**
     * 创建MaterialDialog
     */
    public static class Builder {
        protected final Context context;


        public Builder(Context context) {
            this.context = context;
        }
    }
}

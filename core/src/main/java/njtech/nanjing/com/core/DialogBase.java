package njtech.nanjing.com.core;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;

import njtech.nanjing.com.core.internal.MDRootLayout;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class DialogBase extends Dialog implements Dialog.OnShowListener {

    protected MDRootLayout view;
    private OnShowListener showListener;

    public DialogBase(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public View findViewById(@IdRes int id) {
        return view.findViewById(id);
    }

    @Override
    public final void setOnShowListener(@Nullable OnShowListener listener) {
        this.showListener = listener;
    }

    final void setOnShowListenerInternal() {
        super.setOnShowListener(this);
    }

    final void setViewInternal(View view) {
        super.setContentView(view);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (showListener != null) {
            showListener.onShow(dialog);
        }
    }
}

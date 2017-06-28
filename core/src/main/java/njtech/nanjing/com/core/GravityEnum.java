package njtech.nanjing.com.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

/**
 * Created by Heyha on 2017/6/26.
 */

public enum GravityEnum {
    START,
    CENTER,
    END;

    //当我们的minSdkVersion>=17时，使用start/end来代替left/right；当minSdkVersion<17时，旧的平台不支持RTL，start/end属性是未知的，会被忽略
    private static final boolean HAS_RTL = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;

    @SuppressLint("RtlHardcoded")
    public int getGravityInt() {
        switch (this) {
            case START:
                return HAS_RTL ? Gravity.START : Gravity.LEFT;
            case CENTER:
                return Gravity.CENTER_HORIZONTAL;
            case END:
                return HAS_RTL ? Gravity.END : Gravity.RIGHT;
            default:
                throw new IllegalStateException("invalid gravity constant");
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getTextAlignmentInt() {
        switch (this) {
            case CENTER:
                return View.TEXT_ALIGNMENT_CENTER;
            case END:
                return View.TEXT_ALIGNMENT_VIEW_END;
            default:
                return View.TEXT_ALIGNMENT_VIEW_START;
        }
    }
}

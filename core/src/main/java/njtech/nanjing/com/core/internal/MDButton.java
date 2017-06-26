package njtech.nanjing.com.core.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 张志付 on 2017/6/25.
 */
@SuppressLint("AppCompatCustomView")
public class MDButton extends TextView {

    public MDButton(Context context) {
        super(context);
    }

    public MDButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MDButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStack(boolean stacked, boolean force) {

    }
}

package njtech.nanjing.com.core.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.text.AllCapsTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import njtech.nanjing.com.core.GravityEnum;
import njtech.nanjing.com.core.R;
import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by 张志付 on 2017/6/25.
 */
@SuppressLint("AppCompatCustomView")
public class MDButton extends TextView {

    private boolean stacked = false;
    private GravityEnum stackedGravity;
    private int stackedEndPadding;
    private Drawable stackedBackground;
    private Drawable defaultBackground;

    public MDButton(Context context) {
        super(context);
        init(context);
    }

    public MDButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MDButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        stackedEndPadding = context.getResources().getDimensionPixelSize(R.dimen.md_dialog_frame_margin);
        stackedGravity = GravityEnum.END;
    }

    void setStack(boolean stacked, boolean force) {
        if (this.stacked != stacked || force) {
            setGravity(stacked ? (Gravity.CENTER_VERTICAL | stackedGravity.getGravityInt()) : Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                setTextAlignment(stacked ? stackedGravity.getTextAlignmentInt() : TEXT_ALIGNMENT_CENTER);
            }
            DialogUtils.setBackgroundCompat(this,stacked ? stackedBackground : defaultBackground);
            if (stacked){
                setPadding(stackedEndPadding,getPaddingTop(),stackedEndPadding,getPaddingBottom());
            }
            this.stacked = stacked;
        }
    }

    public void setStackedGravity(GravityEnum stackedGravity) {
        this.stackedGravity = stackedGravity;
    }

    /**
     * 设置背景图片
     * @param d
     */
    public void setStackedSelector(Drawable d) {
        stackedBackground = d;
        if (stacked) {
            setStack(true, true);
        }
    }

    public void setDefaultSelector(Drawable d) {
        defaultBackground = d;
        if (!stacked) {
            setStack(false, true);
        }
    }

    /**
     * 设置将所有字母显示为大写
     *
     * @param allCaps
     */
    public void setAllCapsCompat(boolean allCaps) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setAllCaps(allCaps);
        } else {
            if (allCaps) {
                setTransformationMethod(new AllCapsTransformationMethod(getContext()));
            } else {
                setTransformationMethod(null);
            }
        }
    }


}

package njtech.nanjing.com.core.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import njtech.nanjing.com.core.GravityEnum;
import njtech.nanjing.com.core.R;
import njtech.nanjing.com.core.StackBehavior;
import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class MDRootLayout extends ViewGroup {

    private static final int BUTTON_NEUTRAL_INDEX = 0;
    private static final int BUTTON_NEGATIVE_INDEX = 1;
    private static final int BUTTON_POSITIVE_INDEX = 2;

    private boolean reducePaddingNoTitleNoButtons;
    private int noTitlePaddingFull;
    private int buttonPaddingFull;
    private int buttonHorizontalEdgeMargin;
    private int buttonBarHeight;
    private Paint dividerPaint;
    private int dividerHeight;
    private int maxHeight;
    private View titleBar;
    private View content;
    private final MDButton[] buttons = new MDButton[3];
    private GravityEnum buttonGravity = GravityEnum.START;
    private boolean useFullPadding;
    private boolean isStacked;
    private boolean noTitleNoPadding;
    private boolean drawTopDivider = false;
    private boolean drawBottomDivider = false;
    private StackBehavior stackBehavior = StackBehavior.ADAPTIVE;

    public MDRootLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MDRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MDRootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDRootLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Resources resources = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MDRootLayout,
                defStyleAttr, 0);
        this.reducePaddingNoTitleNoButtons = a.getBoolean(R.styleable
                .MDRootLayout_md_reduce_padding_no_title_no_buttons, true);
        a.recycle();
        this.noTitlePaddingFull = resources.getDimensionPixelSize(R.dimen
                .md_notitle_vertical_padding);
        this.buttonPaddingFull = resources.getDimensionPixelSize(R.dimen
                .md_button_frame_vertical_padding);
        this.buttonHorizontalEdgeMargin = resources.getDimensionPixelSize(R.dimen
                .md_button_padding_frame_side);
        this.buttonBarHeight = resources.getDimensionPixelSize(R.dimen.md_buttonbar_height);
        this.dividerPaint = new Paint();
        this.dividerHeight = resources.getDimensionPixelSize(R.dimen.md_divider_height);
        dividerPaint.setColor(DialogUtils.resolveColor(context, R.attr.md_divider_color));
        setWillNotDraw(false);
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setStackBehavior(StackBehavior stackBehavior) {
        this.stackBehavior = stackBehavior;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.dividerPaint.setColor(dividerColor);
        invalidate();
    }

    public void setNoTitleNoPadding() {
        this.noTitleNoPadding = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (height > maxHeight) {
            height = maxHeight;
        }

        useFullPadding = true;
        boolean hasButtons = false;

        //判断按钮是水平排放还是竖直排放 stacked:true竖直，stacked：false水平
        final boolean stacked;
        if (stackBehavior == StackBehavior.ALWAYS) {
            stacked = true;
        } else if (stackBehavior == StackBehavior.NEVER) {
            stacked = false;
        } else {
            int buttonWidth = 0;
            for (MDButton button : buttons) {
                if (button != null && isVisiable(button)) {
                    button.setStack(false, false);
                    measureChild(button, widthMeasureSpec, heightMeasureSpec);
                    buttonWidth += button.getMeasuredWidth();
                    hasButtons = true;
                }
            }
            int buttonBarPadding = content.getResources().getDimensionPixelSize(R.dimen
                    .md_neutral_button_margin);
            final int buttonFrameWidth = width - 2 * buttonBarPadding;
            stacked = buttonFrameWidth > buttonWidth;
        }

        //get stackedHeight按钮的总高度
        int stackedHeight = 0;
        isStacked = stacked;
        if (stacked) {
            for (MDButton button : buttons) {
                if (button != null && isVisiable(button)) {
                    button.setStack(true, false);
                    measureChild(button, widthMeasureSpec, heightMeasureSpec);
                    stackedHeight += button.getMeasuredHeight();
                    hasButtons = true;
                }
            }
        }

        int availableHeight = height;
        int fullPadding = 0;
        int minPadding = 0;
        if (hasButtons) {
            if (isStacked) {
                availableHeight -= stackedHeight;
                fullPadding += 2 * buttonPaddingFull;
                minPadding += 2 * buttonPaddingFull;
            } else {
                availableHeight -= buttonBarHeight;
                fullPadding += 2 * buttonPaddingFull;
            }
        } else {
            fullPadding += 2 * buttonPaddingFull;
        }
        //measure button finished

        //title measure start
        if (isVisiable(titleBar)) {
            titleBar.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec
                    .UNSPECIFIED);
            availableHeight -= titleBar.getMeasuredHeight();
        } else if (!noTitleNoPadding) {
            fullPadding += noTitlePaddingFull;
        }

        //content measure start
        if (isVisiable(content)) {
            content.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec
                    .makeMeasureSpec(availableHeight - minPadding, MeasureSpec.AT_MOST));
            if (content.getMeasuredHeight() <= availableHeight - fullPadding) {
                if (!reducePaddingNoTitleNoButtons || isVisiable(titleBar) || hasButtons) {
                    useFullPadding = true;
                    availableHeight -= content.getMeasuredHeight() + fullPadding;
                } else {
                    useFullPadding = false;
                    availableHeight -= content.getMeasuredHeight() + minPadding;
                }
            } else {
                useFullPadding = false;
                availableHeight = 0;
            }
        }
        setMeasuredDimension(width, height - availableHeight);
    }

    /**
     * judge component(title,content,button) is visiable
     *
     * @param view
     * @return
     */
    private boolean isVisiable(View view) {
        boolean visiable = view != null && view.getVisibility() != GONE;
        if (visiable && view instanceof MDButton) {
            visiable = ((MDButton) view).getText().toString().trim().length() > 0;
        }
        return visiable;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //layout title
        if (isVisiable(titleBar)) {
            int height = titleBar.getMeasuredHeight();
            titleBar.layout(l, t, r, t + height);
        } else {
            t += noTitlePaddingFull;
        }

        //layout content
        if (isVisiable(content)) {
            content.layout(l, t, r, t + content.getMeasuredHeight());
        }

        //layout button
        if (isStacked) {
            b -= buttonPaddingFull;
            for (MDButton button : buttons) {
                if (isVisiable(button)) {
                    button.layout(l, b - button.getMeasuredHeight(), r, b);
                    b -= button.getMeasuredHeight();
                }
            }
        } else {
            int barTop;
            int barBottom = b;
            if (useFullPadding) {
                barBottom -= buttonPaddingFull;
            }
            barTop = barBottom - buttonBarHeight;

            /**
             * START:
             *      neutral negative positive
             *
             * CENTER:
             *      negative neutral positive
             *
             * END:
             *      positive negative neutral
             */
            int offset = buttonHorizontalEdgeMargin;
            int neutralLeft = -1; //用于记录START和CENTER时的positive的mLeft
            int neutralRight = -1; //用于记录END 时的positive的mRight
            if (isVisiable(buttons[BUTTON_POSITIVE_INDEX])) {
                int bl, br;
                if (buttonGravity == GravityEnum.END) {
                    //END
                    bl = l + offset;
                    br = bl + buttons[BUTTON_POSITIVE_INDEX].getMeasuredWidth();
                } else {
                    //START or CENTER
                    br = r - offset;
                    bl = br - buttons[BUTTON_POSITIVE_INDEX].getMeasuredWidth();
                    neutralRight = bl;
                }
                buttons[BUTTON_POSITIVE_INDEX].layout(bl, barTop, br, barBottom);
                offset += buttons[BUTTON_POSITIVE_INDEX].getMeasuredWidth();
            }
            if (isVisiable(buttons[BUTTON_NEGATIVE_INDEX])) {
                int bl, br;
                if (buttonGravity == GravityEnum.CENTER) {
                    //CENTER
                    bl = l + buttonHorizontalEdgeMargin;
                    br = bl + buttons[BUTTON_NEGATIVE_INDEX].getMeasuredWidth();
                    neutralLeft = br;
                } else if (buttonGravity == GravityEnum.START) {
                    //START
                    br = r - offset;
                    bl = br - buttons[BUTTON_NEGATIVE_INDEX].getMeasuredWidth();
                } else {
                    //END
                    bl = l + offset;
                    br = bl + buttons[BUTTON_NEGATIVE_INDEX].getMeasuredWidth();
                }
                buttons[BUTTON_NEGATIVE_INDEX].layout(bl, barTop, br, barBottom);
            }
            if (isVisiable(buttons[BUTTON_NEUTRAL_INDEX])) {
                int bl, br;
                if (buttonGravity == GravityEnum.START) {
                    //START
                    bl = l + buttonHorizontalEdgeMargin;
                    br = bl + buttons[BUTTON_NEUTRAL_INDEX].getMeasuredWidth();
                } else if (buttonGravity == GravityEnum.END) {
                    //END
                    br = r - buttonHorizontalEdgeMargin;
                    bl = br - buttons[BUTTON_NEUTRAL_INDEX].getMeasuredWidth();
                } else {
                    //CENTER
                    if (neutralLeft == -1 && neutralRight != -1) {
                        neutralLeft = neutralRight - buttons[BUTTON_NEUTRAL_INDEX].getMeasuredWidth();
                    } else if (neutralLeft != -1 && neutralRight == -1) {
                        neutralRight = neutralLeft + buttons[BUTTON_NEUTRAL_INDEX].getMeasuredWidth();
                    } else if (neutralRight == -1) {
                        neutralLeft = (r - l) / 2 - buttons[BUTTON_NEUTRAL_INDEX].getMeasuredWidth() / 2;
                        neutralRight = neutralLeft + buttons[BUTTON_NEUTRAL_INDEX].getMeasuredWidth();
                    }
                    buttons[BUTTON_NEUTRAL_INDEX].layout(neutralLeft, barTop, neutralRight, barBottom);
                }
            }
        }

        setUpDividerVisiable(content, true, true);
    }

    private void setUpDividerVisiable(View view, boolean setForTop, boolean setForBottom) {
        if (view == null) {
            return;
        }
        // TODO: 2017/6/26 setUpDividerVisiable

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (content != null) {
            if (drawTopDivider) {
                int topY = content.getTop();
                canvas.drawLine(0, topY - dividerHeight, getMeasuredWidth(), topY, dividerPaint);
            }
            if (drawBottomDivider) {
                int bottomY = content.getBottom();
                canvas.drawLine(0, bottomY, getMeasuredWidth(), bottomY + dividerHeight, dividerPaint);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
//            if (view.getId() == )
        }
    }
}

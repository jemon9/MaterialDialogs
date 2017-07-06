package njtech.nanjing.com.core;

import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import njtech.nanjing.com.core.utils.DialogUtils;

/**
 * Created by Heyha on 2017/7/4.
 */

public class DefaultRVAdapter extends RecyclerView.Adapter<DefaultRVAdapter.DefaultVH> {

    private final MaterialDialog dialog;
    @LayoutRes
    private final int layout;
    private final GravityEnum itemGravity;
    private InternalListCallback callback;


    interface InternalListCallback {
        boolean onItemSelected(MaterialDialog dialog, View itemView, int position, CharSequence text, boolean longPress);
    }

    public DefaultRVAdapter(MaterialDialog dialog, @LayoutRes int layout) {
        this.dialog = dialog;
        this.layout = layout;
        this.itemGravity = dialog.builder.itemsGravity;
    }

    void setCallback(InternalListCallback callback) {
        this.callback = callback;
    }


    @Override
    public DefaultRVAdapter.DefaultVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(this.layout, parent, false);
        DialogUtils.setBackgroundCompat(view, dialog.getListSelector());
        return new DefaultVH(view, this);
    }

    @Override
    public void onBindViewHolder(DefaultRVAdapter.DefaultVH holder, int position) {
        final View view = holder.itemView;
        boolean disabled = DialogUtils.isIn(position, dialog.builder.disabledIndices);
        int itemColor = disabled ? DialogUtils.adjustAlpha(dialog.builder.itemsColor, 0.4f) : dialog.builder.itemsColor;
        holder.itemView.setEnabled(!disabled);
        switch (dialog.listType) {
            case SINGLE: {
                @SuppressWarnings("CutPastedId")
                RadioButton radioButton = (RadioButton) holder.control;
                boolean selected = dialog.builder.selectedIndex == position;
                if (dialog.builder.choiceWidgetColor != null) {
                    MDTintHelper.setTint(radioButton, dialog.builder.choiceWidgetColor);
                } else {
                    MDTintHelper.setTint(radioButton, dialog.builder.widgetColor);
                }
                radioButton.setChecked(selected);
                radioButton.setEnabled(!disabled);
                break;
            }
            case MULTI: {
                @SuppressWarnings("CutPastedId")
                CheckBox checkBox = (CheckBox) holder.control;
                boolean selected = dialog.selectedIndicesList.contains(position);
                if (dialog.builder.choiceWidgetColor != null) {
                    MDTintHelper.setTint(checkBox, dialog.builder.choiceWidgetColor);
                } else {
                    MDTintHelper.setTint(checkBox, dialog.builder.widgetColor);
                }
                checkBox.setChecked(selected);
                checkBox.setEnabled(!disabled);
                break;
            }
        }
        holder.title.setText(dialog.builder.items.get(position));
        holder.title.setTextColor(itemColor);
        setupGravity((ViewGroup) view);
        if (dialog.builder.itemsIds != null) {
            if (position < dialog.builder.itemsIds.length) {
                view.setId(dialog.builder.itemsIds[position]);
            } else {
                view.setId(-1);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup group = (ViewGroup) view;
            if (group.getChildCount() == 2) {
                if (group.getChildAt(0) instanceof CompoundButton) {
                    group.getChildAt(0).setBackground(null);
                } else if (group.getChildAt(1) instanceof CompoundButton) {
                    group.getChildAt(1).setBackground(null);
                }
            }
        }
    }

    private void setupGravity(ViewGroup view) {
        final LinearLayout itemRoot = (LinearLayout) view;
        final int gravityInt = itemGravity.getGravityInt();
        itemRoot.setGravity(gravityInt | Gravity.CENTER_VERTICAL);
        //当listview中有radiobutton或checkbox时，并且从右向左排列时调整顺序
        // TODO: 2017/7/4 需要加深研究
        if (view.getChildCount() == 2) {
            if (itemGravity == GravityEnum.END && !isRTL() && view.getChildAt(0) instanceof CompoundButton) {
                CompoundButton first = (CompoundButton) view.getChildAt(0);
                view.removeView(first);

                TextView second = (TextView) view.getChildAt(0);
                view.removeView(second);

                second.setPadding(second.getPaddingLeft(), second.getPaddingTop(), second.getPaddingRight(), second.getPaddingBottom());
                view.addView(second);
                view.addView(first);
            } else if (itemGravity == GravityEnum.START && isRTL() && view.getChildAt(1) instanceof CompoundButton) {
                CompoundButton first = (CompoundButton) view.getChildAt(1);
                view.removeView(first);

                TextView second = (TextView) view.getChildAt(0);
                view.removeView(second);

                second.setPadding(second.getPaddingLeft(), second.getPaddingTop(), second.getPaddingRight(), second.getPaddingBottom());
                view.addView(first);
                view.addView(second);
            }
        }
    }

    private boolean isRTL() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return false;
        }
        Configuration config = dialog.getBuilder().getContext().getResources().getConfiguration();
        return config.getLayoutDirection() == Configuration.SCREENLAYOUT_LAYOUTDIR_RTL;
    }

    @Override
    public int getItemCount() {
        return dialog.builder.items != null ? dialog.builder.items.size() : 0;
    }

    static class DefaultVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final CompoundButton control;
        final TextView title;
        final DefaultRVAdapter adapter;

        public DefaultVH(View itemView, DefaultRVAdapter adapter) {
            super(itemView);
            control = (CompoundButton) itemView.findViewById(R.id.md_control);
            title = (TextView) itemView.findViewById(R.id.md_title);
            this.adapter = adapter;
            itemView.setOnClickListener(this);
            if (adapter.dialog.builder.listLongCallback != null) {
                itemView.setOnLongClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (adapter.callback != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                CharSequence text = null;
                if (adapter.dialog.builder.items != null && adapter.dialog.builder.items.size() > getAdapterPosition()) {
                    text = adapter.dialog.builder.items.get(getAdapterPosition());
                }
                adapter.callback.onItemSelected(adapter.dialog, v, getAdapterPosition(), text, false);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (adapter.callback != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                CharSequence text = null;
                if (adapter.dialog.builder.items != null && adapter.dialog.builder.items.size() > getAdapterPosition()) {
                    text = adapter.dialog.builder.items.get(getAdapterPosition());
                }
                return adapter.callback.onItemSelected(adapter.dialog, v, getAdapterPosition(), text, true);
            }
            return false;
        }
    }
}

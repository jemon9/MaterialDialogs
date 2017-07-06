package njtech.nanjing.com.materialdialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import njtech.nanjing.com.core.DialogAction;
import njtech.nanjing.com.core.GravityEnum;
import njtech.nanjing.com.core.MaterialDialog;
import njtech.nanjing.com.core.StackBehavior;

public class MainActivity extends AppCompatActivity {

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (thread != null && !thread.isInterrupted() && thread.isAlive()) {
            thread.interrupt();
        }
    }

    public void btn1Click(View view) {
        new MaterialDialog.Builder(this).setStackedBehavior(StackBehavior.ADAPTIVE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(dialog.getContext(), "ok", Toast.LENGTH_SHORT).show();
                    }
                })
                .iconRes(R.mipmap.ic_launcher_round)
                .title("Title")
                .contentRes(R.string.long_input)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(dialog.getContext(), "HAHAH", Toast.LENGTH_SHORT).show();
                    }
                })
                .negativeTextRes(R.string.dialog_cancel)
                .positiveTextRes(R.string.dialog_ok)
                .show();
    }

    public void btn2Click(View view) {
        new MaterialDialog.Builder(this).title("Progress Dialog")
                .content("Please wait....")
                .progress(false, 100, true)
                .progressIndeterminateStyle(true)
                .onCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (thread != null) {
                            thread.interrupt();
                        }
                    }
                })
                .onShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final MaterialDialog materialDialog = (MaterialDialog) dialog;
                        startThread(new Runnable() {
                            @Override
                            public void run() {
                                while (materialDialog.getCurrentProgress() != materialDialog.getMaxProgress() && !Thread.currentThread().isInterrupted()) {
                                    if (materialDialog.isCanceled()) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    materialDialog.incrementProgress(1);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        materialDialog.setContent("Done");
                                    }
                                });
                            }
                        });
                    }
                })
                .show();
    }

    public void btn3click(View view) {
        new MaterialDialog.Builder(this)
                .itemsRes(R.array.socialNetworks)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Toast.makeText(dialog.getContext(), "listcallback----> position:" + position + "\n" + "text:" + text, Toast.LENGTH_SHORT).show();
                    }
                })
                .itemsLongCallback(new MaterialDialog.ListLongCallback() {
                    @Override
                    public boolean onLongSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Toast.makeText(dialog.getContext(), "listlong callback----> position:" + position + "\n" + "text:" + text, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .show();
    }

    public void btn4click(View view) {
        new MaterialDialog.Builder(this)
                .title("single choice")
                .itemsRes(R.array.socialNetworks)
                .itemsCallbackSingleChoice(2, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        Toast.makeText(dialog.getContext(),"position:" + which + "," + text,Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    public void btn5click(View view){
        new MaterialDialog.Builder(this)
                .title("multiple choice")
                .itemsRes(R.array.socialNetworks)
                .alwaysCallMultiChoiceCallback()
                .itemsCallbackMultiChoice(new Integer[]{1, 3}, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] texts) {
                        return true;
                    }
                })
                .negativeText("cancle")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.clearSelectedIndices();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .autoDismiss(false)
                .positiveText("Choose")
                .show();
    }
}

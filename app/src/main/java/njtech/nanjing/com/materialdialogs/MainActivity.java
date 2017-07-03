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
}

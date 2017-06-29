package njtech.nanjing.com.materialdialogs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import njtech.nanjing.com.core.GravityEnum;
import njtech.nanjing.com.core.MaterialDialog;
import njtech.nanjing.com.core.StackBehavior;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View view) {
        new MaterialDialog.Builder(this).setStackedBehavior(StackBehavior.ALWAYS)
                .title("Title")
                .contentRes(R.string.dialog_content)
                .negativeTextRes(R.string.dialog_cancel)
                .positiveTextRes(R.string.dialog_ok)
                .show();
    }
}

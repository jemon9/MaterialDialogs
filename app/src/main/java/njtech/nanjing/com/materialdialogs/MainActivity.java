package njtech.nanjing.com.materialdialogs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import njtech.nanjing.com.core.GravityEnum;
import njtech.nanjing.com.core.MaterialDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View view) {
        new MaterialDialog.Builder(this)
                .title("Hello")
                .content("This is content view")
                .negativeText("取消")
                .positiveText("确定")
                .neutralText("中立")
                .buttonGravity(GravityEnum.CENTER)
                .show();
    }
}

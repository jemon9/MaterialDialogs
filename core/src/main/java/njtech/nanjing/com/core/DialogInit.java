package njtech.nanjing.com.core;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

/**
 * Created by 张志付 on 2017/6/24.
 */

public class DialogInit {

    @StyleRes
    static int getTheme(@NonNull MaterialDialog.Builder builder){
        return 0;
    }

    @LayoutRes
    static int getInflateLayout(@NonNull MaterialDialog.Builder builder){
        return 0;
    }

    static void init(MaterialDialog dialog){

    }
}

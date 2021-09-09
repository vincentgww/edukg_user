package com.fairychild.edukguser;

import android.content.Context;
import android.content.Intent;

public class Share {
    public static void share(Context context, String content){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,content);
        context.startActivity(intent);
    }
}

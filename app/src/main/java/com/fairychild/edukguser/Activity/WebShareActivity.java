package com.fairychild.edukguser.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fairychild.edukguser.R;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;


public class WebShareActivity extends AppCompatActivity implements View.OnClickListener, WbShareCallback {
    public static final String KEY_SHARE_TYPE="key_share_type";
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    /** 界面标题 */
    private TextView mTitleView;
    /** 分享按钮 */
    private Button mSharedBtn;
    private WbShareHandler shareHandler;
    private int mShareType=SHARE_CLIENT;
    int flag=0;

    /** 分享主题*/
    private String itemTitle;
    /** 分享内容*/
    private String itemContent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initViews();
        mShareType=getIntent().getIntExtra(KEY_SHARE_TYPE,SHARE_CLIENT);
        shareHandler=new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);
        Intent intent=getIntent();
        itemTitle=intent.getStringExtra("itemTitle");
        itemContent=intent.getStringExtra("itemContent");
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v){
        if(R.id.share_to_btn==v.getId()){
            mSharedBtn.setText("share done");
            flag=1;
            sendMessage(true);
        }
    }

    /**
     * 初始化界面
     */
    private void initViews(){
        mTitleView=(TextView) findViewById(R.id.share_title);
        mTitleView.setText(R.string.weibosdk_demo_share_to_weibo_title);
        mSharedBtn=(Button) findViewById(R.id.share_to_btn);
        mSharedBtn.setOnClickListener(this);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMessage(boolean hasText) {
        sendMultiMessage(hasText);
    }
    private void sendMultiMessage(boolean hasText) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);

    }
    /**
     * 获取分享的文本模板。
     */
    private String getSharedText() {
        int formatId = R.string.weibosdk_demo_share_text_template;
        String format = getString(formatId);
        String text = format;
        return text;
    }
    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = itemContent;
        textObject.title = itemTitle;
        //textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        shareHandler.doResultIntent(data,this);
    }
    @Override
    public void onWbShareSuccess(){
        Toast.makeText(this,R.string.weibosdk_demo_toast_share_success,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWbShareFail(){
        Toast.makeText(this,
                getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: ",
                Toast.LENGTH_LONG).show();
    }
    @Override
    public void onWbShareCancel(){
        Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
    }

}

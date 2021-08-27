package com.fairychild.edukguser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fairychild.edukguser.MsgAdapter;
import com.fairychild.edukguser.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fairychild.edukguser.Msg;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class QaFragment extends Fragment implements View.OnClickListener{

    public interface QaListener {
        String sendInfo(String course, String inputQuestion);
    }
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    boolean isRunning = false;
    private boolean isSend=false;
    private QaListener listener;
    private String myName="lcs";
    private String responseData;
    private int curr;                   //当前显示的消息条数
    private int jsonLen;                //获取到的json列表长度
    private Handler handler = new Handler(Looper.myLooper()){
        //获取当前进程的Looper对象传给handler
        //在目前的Android开发中，子线程不能改变UI，
        //所以子线程要对UI进行操作需要交给一个Handler对象来执行
        @Override
        public void handleMessage(Message message){
            System.out.println("message: "+message.getData());
            String message_Name = message.getData().getString("name");
            String message_msgC = message.getData().getString("msgContent");
            if(!message_msgC.equals("")){
                if(message_Name.equals(myName))
                    addNewMessage(message_msgC, Msg.TYPE_SENT);
                else
                    addNewMessage(message_msgC,Msg.TYPE_RECEIVED);
            }
        }
    };
    public void addNewMessage(String msg,int type){
        Msg message = new Msg(msg,type);
        msgList.add(message);
        adapter.notifyItemInserted(msgList.size()-1);
        msgRecyclerView.scrollToPosition(msgList.size()-1);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_qa,container,false);
        //Intent intent =getIntent();
        //myName=intent.getStringExtra("username");
        curr=0;
        jsonLen=1;
        isRunning=true;
        inputText = view.findViewById(R.id.input_text);
        send=view.findViewById(R.id.send);
        send.setOnClickListener(this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new
                        LinearLayoutManager(getActivity());
                msgRecyclerView= view.findViewById(R.id.msg_recycler_view);
                msgRecyclerView.setLayoutManager(layoutManager);
                adapter = new MsgAdapter(msgList);
                msgRecyclerView.setAdapter(adapter);
            }
        });
        //new Thread(new Receive(), "接收线程").start();
        //new Thread(new Send(), "发送线程").start();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        //System.out.println(event.message);
        try {
            JSONObject obj = new JSONObject(event.message);
            JSONArray arr = new JSONArray(obj.getString("data"));
            obj = arr.getJSONObject(0);
            String ans = obj.getString("message");
            handleMessage(ans,false);
            System.out.println("obj: "+ans);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void parseJSONWithJSONObject(String jsonData) {  //解析JSON数据函数
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            jsonLen = jsonArray.length();
            for (; curr < jsonLen; curr++) {
                JSONObject jsonObject = jsonArray.getJSONObject(curr);
                String name = jsonObject.getString("name");
                String msgContent = jsonObject.getString("message");
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("msgContent", msgContent);  //往Bundle中存放数据
                message.setData(bundle);//mes利用Bundle传递数据
                handler.sendMessage(message);//用activity中的handler发送消息
            }
        } catch (Exception e) {
            Looper.prepare();
            Toast.makeText(getActivity(), "解析json错误!", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
    String msgEntity;
    @Override
    public void onClick(View view){
        String content = inputText.getText().toString();
        //@SuppressLint("SimpleDateFormat")
        //String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        //StringBuilder sb = new StringBuilder();
       // msgEntity = myName;
        //sb.append(msgEntity).append(" "+content);
        //msgEntity = sb.toString();
        if(!"".equals(content)){
            /*Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name", myName);
            bundle.putString("msgContent", msgEntity);  //往Bundle中存放数据
            message.setData(bundle);//mes利用Bundle传递数据
            handler.sendMessage(message);//用activity中的handler发送消息
            inputText.setText("");
            isSend = true;
            curr++;*/
            handleMessage(content, true);
            inputText.setText("");
            //sb.delete(0,sb.length());
            String [] info = content.split("\\+");
            if(info.length!=2)
                handleMessage("输入格式错误!",false);
            else
                listener.sendInfo(info[0],info[1]);
        }
        //System.out.println("response2"+obj);
    }

    private void handleMessage(String content, boolean meSend){
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        content = date + "\n" + content;
        if(meSend)
            addNewMessage(content, Msg.TYPE_SENT);
        else
            addNewMessage(content,Msg.TYPE_RECEIVED);
    }

    public static QaFragment newInstance(){
        QaFragment indexFragment = new QaFragment();
        return indexFragment;
    }
    @Override
    public void onAttach(Context context) {
        listener = (QaListener) context;
        super.onAttach(context);
    }
    public void sendResponse(String response){
        System.out.println(response);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
   /*class Send implements Runnable{
        @Override
        public void run(){           //发送线程
            while(isRunning){
                if(isSend){
                    RequestBody requestBody = new FormBody.Builder()
                            .add("course",myName)
                            .add("inputQuestion",msgEntity)
                            .build();
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request2 = new Request.Builder()
                                // 指定访问的服务器地址
                                .url(Resource.DiffUrl).post(requestBody)
                                .build();
                        Response response = client.newCall(request2).execute();
                        String responseData = response.body().string();
                        isSend = false;
                    } catch (Exception e) {
                        Looper.prepare();
                        Toast.makeText(ChatRoom.this, "发送失败！",
                                Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }
        }
    }*/

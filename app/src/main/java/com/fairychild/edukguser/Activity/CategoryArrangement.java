package com.fairychild.edukguser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fairychild.edukguser.CustomLayout.DragGridLayout;
import com.fairychild.edukguser.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryArrangement extends AppCompatActivity {
    private DragGridLayout gridLayout1;
    private DragGridLayout gridLayout2;
    private ArrayList<String> mCategory;
    private ArrayList<String> mDelCategory;
    //    private boolean editFlag = false;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_arrangement);
        gridLayout1 = findViewById(R.id.arr_grid1);
        gridLayout1.setIsRemain(true);
        gridLayout2 = findViewById(R.id.arr_grid2);
        mCategory = new ArrayList<>();
        mDelCategory = new ArrayList<>();

        Intent intent = getIntent();
        ArrayList<String> category = (ArrayList<String>) ((Intent) intent).getSerializableExtra("cat");
        ArrayList<String> delCategory = (ArrayList<String>) ((Intent) intent).getSerializableExtra("delCat");

        mCategory = (ArrayList<String>) ((Intent) intent).getSerializableExtra("cat");
        mDelCategory = (ArrayList<String>) ((Intent) intent).getSerializableExtra("delCat");

        gridLayout1.setCanDrag(true);
        gridLayout1.setItems(category);
        gridLayout2.setItems(delCategory);

        gridLayout1.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mCategory.remove(tv.getText().toString().replace("+", ""));
                        mDelCategory.add(tv.getText().toString().replace("+", ""));
                    }
                }
                ).start();
                gridLayout1.removeView(tv);
                gridLayout2.addGridItem(tv.getText().toString());
            }
        });
        gridLayout2.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mDelCategory.remove(tv.getText().toString().replace("+", ""));
                        mCategory.add(tv.getText().toString().replace("+", ""));
                    }}).start();
                gridLayout2.removeView(tv);
                gridLayout1.addGridItem(tv.getText().toString());
//                editFlag = true;
            }
        });
        backBtn=findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCategory.size() <2 ){
                    Toast.makeText(getApplicationContext(), "必须至少选择两个学科", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = getIntent();
                intent.putExtra("cat", mCategory);
                intent.putExtra("delCat", mDelCategory);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}

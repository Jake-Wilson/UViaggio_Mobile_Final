package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;

public class ClassCardViewActivity extends AppCompatActivity implements View.OnClickListener{
    public static ArrayList<UserClass> classList;
    public RecyclerView rvClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_card_view);
        setTitle("Class List");
        classList = UserClass.createInitialClassList();
        Log.d("List:", classList.toString());
        rvClassList = findViewById(R.id.rvClassList);
        ClassCardViewAdapter adapter = new ClassCardViewAdapter(this, classList, this);
        rvClassList.setAdapter(adapter);
        rvClassList.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    public void toAddClass(){
        Intent addIntent = new Intent(this, AddClassActivity.class);
        startActivityForResult(addIntent,0);
    }

//    public void editClass(View view){
//        Intent editIntent = new Intent(this, EditItemActivity.class);
//
//        UserClass c = classList.get((int)view.getTag());
//
//        //TODO: Use putExtra to pass the data to the editing form
//        startActivityForResult(editIntent, 1);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //addClass code
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                UserClass c = new UserClass(
                        data.getStringExtra("name"),
                        data.getStringExtra("instructor"),
                        data.getStringExtra("deptID"),
                        data.getStringExtra("number"),
                        data.getStringExtra("section"),
                        data.getStringExtra("meetingTime"),
                        data.getStringExtra("location"),
                        data.getStringExtra("lat"),
                        data.getStringExtra("lon"));
                classList.add(c);
                //TODO: Sort here or sort once when starting main activity??
                rvClassList.getAdapter().notifyDataSetChanged();
            }
        }

        //editClass code
        //TODO: Instead of removing/adding new, can we find a way to just edit existing?
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                //Swap Items
                UserClass c = new UserClass(
                        data.getStringExtra("name"),
                        data.getStringExtra("instructor"),
                        data.getStringExtra("deptID"),
                        data.getStringExtra("number"),
                        data.getStringExtra("section"),
                        data.getStringExtra("meetingTime"),
                        data.getStringExtra("location"),
                        data.getStringExtra("lat"),
                        data.getStringExtra("lon"));
                classList.add(c);
                classList.remove(data.getIntExtra("Position", 1000000000));
                //TODO:Sort here or somewhere when main activity loads?
                rvClassList.getAdapter().notifyItemChanged(data.getIntExtra("Position", 1000000000));
            }
        }
    }

    public void onClick(View v){
        int from = v.getId();
        if(from == R.id.fab){
            toAddClass();
        }
    }
}

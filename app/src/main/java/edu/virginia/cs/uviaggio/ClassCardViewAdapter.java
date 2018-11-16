package edu.virginia.cs.uviaggio;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClassCardViewAdapter extends RecyclerView.Adapter<ClassCardViewAdapter.ViewHolder> {
    private Context mContext;
    private List<UserClass> classList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView classListText, classListText2, classListText3;
        public CardView classListItem;

        public ViewHolder(View classView){
            super(classView);
            classListText = classView.findViewById(R.id.text1);
            classListText2 = classView.findViewById(R.id.text2);
            classListText3 = classView.findViewById(R.id.text3);
            classListItem = classView.findViewById(R.id.card_view);
        }
    }

    public ClassCardViewAdapter(ClassCardViewActivity activity, ArrayList<UserClass> list, Context context){
        this.classList = list;
        this.mContext = context;
    }

    public List<UserClass> getClassList(){
        return classList;
    }

    @Override
    public ClassCardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View classListView = inflater.inflate(R.layout.user_class, parent, false);
        ViewHolder viewHolder = new ViewHolder(classListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClassCardViewAdapter.ViewHolder viewHolder, int position) {
        UserClass classItem = classList.get(position);
        TextView textView = viewHolder.classListText;
        textView.setText(classItem.getName());
//        viewHolder.classListItem.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, GpsActivity.class);
//                intent.putExtra("lat", classItem.getLat());
//                intent.putExtra("lon", classItem.getLon());
//                mContext.startActivity(intent);
//            }
//        });
        TextView textView2 = viewHolder.classListText2;
        textView2.setText(classItem.getInstructor());

        TextView textView3 = viewHolder.classListText3;
        textView3.setText(classItem.getMeetingTime());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}

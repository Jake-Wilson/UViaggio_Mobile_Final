package edu.virginia.cs.uviaggio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ClassCardViewAdapter extends RecyclerView.Adapter<ClassCardViewAdapter.ViewHolder> {
    private Context mContext;
    private List<UserClass> classList;
    private int expandedPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView classListText, classListText2, classListText3, statsText;
        public CardView classListItem;
        public LinearLayout classListExpand;
        public Button gpsButton;

        public ViewHolder(View classView){
            super(classView);
            classListText = classView.findViewById(R.id.text1);
            classListText2 = classView.findViewById(R.id.text2);
            classListText3 = classView.findViewById(R.id.text3);
            classListItem = classView.findViewById(R.id.card_view);
            classListExpand = classView.findViewById(R.id.llExpandArea);
            statsText = classView.findViewById(R.id.stats);
            gpsButton = classView.findViewById(R.id.gpsStart);
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
        final View classListView = inflater.inflate(R.layout.user_class, parent, false);
        final ViewHolder viewHolder = new ViewHolder(classListView);
        viewHolder.classListItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, GpsActivity.class);
//                Log.d("item in question", classList.get(viewHolder.getAdapterPosition()).getName());
//                Log.d("lat in question", classList.get(viewHolder.getAdapterPosition()).getLat());
//                Log.d("ion in question", classList.get(viewHolder.getAdapterPosition()).getLon());
//                intent.putExtra("name", classList.get(viewHolder.getAdapterPosition()).getName());
//                intent.putExtra("lat", classList.get(viewHolder.getAdapterPosition()).getLat());
//                intent.putExtra("lon", classList.get(viewHolder.getAdapterPosition()).getLon());
//                mContext.startActivity(intent);
                ViewHolder holder = (ViewHolder) v.getTag();
                Integer spot = holder.getLayoutPosition();
                UserClass position = classList.get(spot);
                if (expandedPosition >= 0){
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }

                expandedPosition = holder.getLayoutPosition();
                notifyItemChanged(expandedPosition);
            }
        });
        viewHolder.itemView.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClassCardViewAdapter.ViewHolder viewHolder, int position) {

        UserClass classItem = classList.get(position);
        TextView textView = viewHolder.classListText;
        textView.setTag(position);
        textView.setText(classItem.getName());
        TextView textView2 = viewHolder.classListText2;
        textView2.setTag(position);
        textView2.setText(classItem.getInstructor());
        TextView textView3 = viewHolder.classListText3;
        textView3.setText(classItem.getMeetingTime());
        textView3.setTag(position);
        if (position == expandedPosition){
            viewHolder.classListExpand.setVisibility(View.VISIBLE);
            Button button = viewHolder.gpsButton;
            button.setTag(position);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(mContext instanceof ClassCardViewActivity){
                        ((ClassCardViewActivity)mContext).launchGPS(v);
                    }
                }
            });
        } else{
            viewHolder.classListExpand.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}

package edu.virginia.cs.uviaggio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.OkHttpClient;

public class ClassCardViewAdapter extends RecyclerView.Adapter<ClassCardViewAdapter.ViewHolder> {
    private Context mContext;
    private List<UserClass> classList;
    private int expandedPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView classListText, classListText2, classListText3, leaveTime;
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
            leaveTime = classView.findViewById(R.id.leaveTime);
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
    public ClassCardViewAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        final View classListView = inflater.inflate(R.layout.user_class, parent, false);
        final ViewHolder viewHolder = new ViewHolder(classListView);
        viewHolder.classListItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
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
            if(classItem.getLeaveTime() != 0) {
                Date timeDate = new Date(classItem.getLeaveTime());
                SimpleDateFormat f = new SimpleDateFormat("hh:mma");
                f.setTimeZone(TimeZone.getTimeZone("GMT"));
                viewHolder.leaveTime.setText(f.format(timeDate));
            }else{
                viewHolder.leaveTime.setText("No Data");
            }
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
        // long click or hold to delete item from classlist and db
        viewHolder.classListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Are you sure you wish to delete this class?");
                builder1.setCancelable(true);
                final View toDelete = v;
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ViewHolder holder = (ViewHolder) toDelete.getTag();
                                Integer spot = holder.getLayoutPosition();
                                DatabaseHelper mDBHelper = new DatabaseHelper(mContext);
                                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                                String removeQuery = "DELETE FROM Classes WHERE name = \"" + classList.get(spot).getName() + "\"";
                                db.execSQL(removeQuery);
                                Log.d("removed item from db", removeQuery);
                                classList.remove(classList.get(spot));
                                notifyItemRemoved(spot);
                                dialog.cancel();
                            }
                        }
                );
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}

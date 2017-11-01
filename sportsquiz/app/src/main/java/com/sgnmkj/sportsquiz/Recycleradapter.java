package com.sgnmkj.sportsquiz;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sgnmkj.sportsquiz.utilities.database_db;

import static com.sgnmkj.sportsquiz.MainActivity.context;
import static com.sgnmkj.sportsquiz.utilities.values.answer_clicked;
import static com.sgnmkj.sportsquiz.utilities.values.option_clicked;
import static com.sgnmkj.sportsquiz.utilities.values.options;
import static com.sgnmkj.sportsquiz.utilities.values.options_check;
import static com.sgnmkj.sportsquiz.utilities.values.question_display;

/**
 * Created by bharat.l on 4/4/2017.
 */

public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.Viewholder> {

    database_db obj_database=new database_db(context);
    MainActivity obj_mainactivity=new MainActivity();
    class Viewholder extends RecyclerView.ViewHolder{
        TextView tv_option;
        RadioButton rb_check;

        public Viewholder(View itemView) {
            super(itemView);
        //    Log.e("At view holder"," above declaratrion");
            tv_option=(TextView)itemView.findViewById(R.id.tv_cardname);
            rb_check=(RadioButton)itemView.findViewById(R.id.rb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    options_check[question_display][option_clicked[question_display]]=false;
                    option_clicked[question_display]=position;
                    answer_clicked[question_display]=position;
                    options_check[question_display][position]=true;
                    notifyDataSetChanged();
                    //      rb_check.setChecked(true);
                }
            });
            rb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    options_check[question_display][option_clicked[question_display]]=false;
                    option_clicked[question_display]=position;
                    answer_clicked[question_display]=position;
                    options_check[question_display][position]=true;
                    notifyDataSetChanged();
                }
            });
        }

    }
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout,parent,false);
        Viewholder viewholder=new Viewholder(V);
    //    Log.e("At view holder","onCreateViewholder");
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        holder.tv_option.setText(options[question_display][position]);
     /* if(option_clicked[question_display]==position)
        holder.rb_check.setChecked(true);
        else holder.rb_check.setChecked(false);*/
        holder.rb_check.setChecked(options_check[question_display][position]);
   //     Log.e("atbind"+position,options_check[question_display][position]+"");
    }
    @Override
    public int getItemCount() {
        return 4;
    }

}

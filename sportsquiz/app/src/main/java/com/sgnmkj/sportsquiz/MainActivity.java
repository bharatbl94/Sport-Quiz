package com.sgnmkj.sportsquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import static com.sgnmkj.sportsquiz.utilities.values.answer_clicked;
import static com.sgnmkj.sportsquiz.utilities.values.index_correct;
import static com.sgnmkj.sportsquiz.utilities.values.isInternet;
import static com.sgnmkj.sportsquiz.utilities.values.option_clicked;
import static com.sgnmkj.sportsquiz.utilities.values.options;
import static com.sgnmkj.sportsquiz.utilities.values.options_check;
import static com.sgnmkj.sportsquiz.utilities.values.question_display;
import static com.sgnmkj.sportsquiz.utilities.values.questions;
import static com.sgnmkj.sportsquiz.utilities.values.quiz_count;

public class MainActivity extends AppCompatActivity {
    public static Context context;

    Random rand = new Random();
    RequestQueue rq;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    Button bt_next,bt_pre,bt_submit;
    TextView tv_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_pre=(Button)findViewById(R.id.bt_pre);
        bt_next=(Button)findViewById(R.id.bt_next);
        bt_submit=(Button)findViewById(R.id.bt_submit);
        tv_question=(TextView)findViewById(R.id.tv_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;
        rq= Volley.newRequestQueue(this);
        if(isInternet(this))
            new get_data().execute();
        else {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setMessage("Please Connect to Internet");
            alert.setTitle("No Internet");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            alert.show();
        }

     bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question_display==quiz_count-1){
                    Toast.makeText(MainActivity.this,"This is Last Question\nPress Submit for results.",Toast.LENGTH_SHORT).show();
                    return;
                }
        //        Log.e("at","btnext2");
                ++question_display;
                tv_question.setText(questions[question_display]);
                adapter.notifyDataSetChanged();
            }
        });
        bt_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question_display==0){
                    Toast.makeText(MainActivity.this,"This is First Question",Toast.LENGTH_SHORT).show();
                    return;
                }
           //     Log.e("at","btpre2");
                --question_display;
                tv_question.setText(questions[question_display]);
                adapter.notifyDataSetChanged();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result=0;
                for(int i=0;i<quiz_count;i++){
                    if(index_correct[i]==answer_clicked[i])
                        ++result;
                }
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("You got "+result+" out of "+quiz_count+".\nThanks for using this app.");
                alert.setTitle("est Result");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alert.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class get_data extends AsyncTask<Void ,Void,Void> {
        ProgressDialog pd=new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            pd.setTitle("Please Wait...");pd.setMessage("While we are getting data..!!");
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pd.isShowing())pd.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://opentdb.com/api.php?amount=10&category=21&difficulty=easy&type=multiple&encode=url3986", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("At Response", response.toString());
                    try {
                        JSONArray jsonArray=response.getJSONArray("results");
                        quiz_count=jsonArray.length();
                        questions=new String[quiz_count];
                        options=new String[quiz_count][4];
                        options_check=new boolean[quiz_count][4];
                        index_correct=new int[quiz_count];
                        option_clicked=new int[quiz_count];
                        answer_clicked=new int[quiz_count];
                        for(int i=0;i<quiz_count;i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            int random_generate=rand.nextInt(3);
                       //     questions[i]=jsonObject.getString("question").replaceAll("%20"," ").replaceAll("%3F",".").replaceAll("%27","\"\"");
                            questions[i]=java.net.URLDecoder.decode(jsonObject.getString("question"), "UTF-8");
                            options[i][random_generate]=java.net.URLDecoder.decode(jsonObject.getString("correct_answer"), "UTF-8");
                            JSONArray jsonArray_incorrect=jsonObject.getJSONArray("incorrect_answers");
                            index_correct[i]=random_generate;
                            option_clicked[i]=0;
                            answer_clicked[i]=5;
                            int opt=0;
                            for(int k=0;k<4;k++){
                                if(k!=random_generate){
                                    options[i][k]=java.net.URLDecoder.decode(jsonArray_incorrect.getString(opt), "UTF-8");
                                    ++opt;
                                }
                                options_check[i][k]=false;
                            }
                        }
                        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                        layoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new Recycleradapter();
                        recyclerView.setAdapter(adapter);
                        tv_question.setText(questions[question_display]);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(" volley Error",error.toString());
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                }
            });

            rq.add(jsonObjectRequest);
            return null;
        }
    }
}

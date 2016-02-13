package sanstormsolutions.com.whatshisname.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.adapters.PeopleAdapter;
import sanstormsolutions.com.whatshisname.models.People;

public class MainActivity extends AppCompatActivity {
    public static final String FIREBASE_URL = "https://whatshisnameagain.firebaseio.com"; //Firebase base URL

    //Vars
    private FloatingActionButton mFab = null;
    private Firebase mFirebase;
    private RecyclerView mPeopleRecyclerView = null;
    private PeopleAdapter mPeopleAdapter = null;
    private List<People> ary_fb_peopleList = null;
    private TextView mNoPeopleMsg = null;
    private ImageView mNoPeopleImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebase.setAndroidContext(this); //Initialize Firebase

        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * Method for setting up all views for this activity
     */
    private void setupViews() {

        ary_fb_peopleList = new ArrayList<>();


        mPeopleAdapter = new PeopleAdapter(ary_fb_peopleList, this, R.layout.listitem_user_display);

        mPeopleRecyclerView = (RecyclerView) findViewById(R.id.content_main_rvContactDisplay);
        mPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mPeopleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPeopleRecyclerView.setAdapter(mPeopleAdapter);


        mFab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                // Launch Add Dialog
                addNewPerson();

            }
        });

        mNoPeopleMsg = (TextView) findViewById(R.id.content_main_txtvNoContacts);
        mNoPeopleImg = (ImageView) findViewById(R.id.content_main_imgvNoContacts);

        // Check to see if we have people to list. If we don't, make a sad face.
        if(ary_fb_peopleList.size() == 0){
            mNoPeopleMsg.setVisibility(View.VISIBLE);
            mNoPeopleImg.setVisibility(View.VISIBLE);
        }



    }

    public void addNewPerson(){
        //Show addNewPerson Activity as dialog instead
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }
}

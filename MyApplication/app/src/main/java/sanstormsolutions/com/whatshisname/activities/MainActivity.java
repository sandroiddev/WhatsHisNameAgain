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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.adapters.PeopleAdapter;
import sanstormsolutions.com.whatshisname.data.ApplicationData;
import sanstormsolutions.com.whatshisname.models.People;

public class MainActivity extends AppCompatActivity {
    public static final String device_id = ApplicationData.getDeviceID(ApplicationData.getContext());
    public static final String FIREBASE_URL = "https://whatshisnameagain.firebaseio.com/"+device_id; //Firebase base URL

    //Vars
    private FloatingActionButton mFab = null;
    private RecyclerView mPeopleRecyclerView = null;
    private PeopleAdapter mPeopleAdapter = null;
    private List<People> ary_fb_peopleList = null;
    private TextView mNoPeopleMsg = null;
    private ImageView mNoPeopleImg = null;

    //Firebase
    private Firebase mFirebase;
    private ValueEventListener m_fb_ValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_activity_toolbar_title);
        setSupportActionBar(toolbar);

        setupViews();
        setupListeners();


        mFirebase = new Firebase(FIREBASE_URL + "/people");
        mFirebase.addValueEventListener(m_fb_ValueEventListener);

        ApplicationData.setActivity(this); // reference to the topmost activity

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

        // Launch the about activity
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Resume Firebase Listening
        mFirebase.addValueEventListener(m_fb_ValueEventListener);
        ApplicationData.setActivity(this); // reference to the topmost activity
    }

    @Override
    public void onPause() {
        //Remove the firebase listener
        mFirebase.removeEventListener(m_fb_ValueEventListener);
        super.onPause();
    }

    /***
     * Method for containing all Listeners for this activity
     */
    private void setupListeners() {

        m_fb_ValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // Clear the previous list of people. Good idea to start fresh rather than adding to. Just gets messy that way.
                    ary_fb_peopleList.clear();

                    for (DataSnapshot peopleSnapshot : dataSnapshot.getChildren()) {
                        People peopleData = peopleSnapshot.getValue(People.class);
                        ary_fb_peopleList.add(peopleData);
                    }
                    // Let the adapter know we have work for it to do. Thanks adapter.
                    mPeopleAdapter.notifyDataSetChanged();

                    // Be Happy, you have people listed
                    mNoPeopleImg.setVisibility(View.GONE);
                    mNoPeopleMsg.setVisibility(View.GONE);
                } else {
                    // Sad face, there are no people listed
                    mNoPeopleMsg.setVisibility(View.VISIBLE);
                    mNoPeopleImg.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    /***
     * Method for setting up all views for this activity
     */
    private void setupViews() {

        ary_fb_peopleList = new ArrayList<>();


        mPeopleAdapter = new PeopleAdapter(ary_fb_peopleList, this, R.layout.listitem_user_display);

        mPeopleRecyclerView = (RecyclerView) findViewById(R.id.content_main_rvContactDisplay);
        mPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mPeopleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPeopleRecyclerView.setAdapter(mPeopleAdapter);


        mFab = (FloatingActionButton) findViewById(R.id.activity_main_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Add Dialog
                addNewPerson();
            }
        });

        mNoPeopleMsg = (TextView) findViewById(R.id.content_main_txtvNoContacts);
        mNoPeopleImg = (ImageView) findViewById(R.id.content_main_imgvNoContacts);


    }

    /***
     * Simple Method to launch the Add Person Full Screen Dialog.
     */
    public void addNewPerson() {
        //Show addNewPerson Activity as dialog instead
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }
}

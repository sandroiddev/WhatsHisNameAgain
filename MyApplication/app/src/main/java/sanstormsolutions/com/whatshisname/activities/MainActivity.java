package sanstormsolutions.com.whatshisname.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
    private List<People> mPeopleList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        mPeopleList = new ArrayList<>();

        //Sample Data before Firebase
        People me = new People();
        me.setFirstName("Ron");
        me.setLastName("Burgandy");
        me.setBirthday("Feb. 11, 2016");
        me.setZipCode("75002");
        mPeopleList.add(me);
        People me2 = new People();
        me.setFirstName("Garth");
        me.setLastName("Brooks");
        me.setBirthday("Mar. 15, 1978");
        me.setZipCode("90210");
        mPeopleList.add(me2);
        People me3 = new People();
        me.setFirstName("Musiq");
        me.setLastName("SoulChild");
        me.setBirthday("Aug. 1, 1984");
        me.setZipCode("48226");
        mPeopleList.add(me3);

        mPeopleAdapter = new PeopleAdapter(mPeopleList, this, R.layout.listitem_user_display);

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

    }

    public static class addPersonDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(getActivity());

            //Inflater for the custom layout
            LayoutInflater inflater;
            inflater = getActivity().getLayoutInflater();

            //Set the Layout for the Dialog
            builder.setView(inflater.inflate(R.layout.dialog_add_people, null));

            // Set the Message for the Dialog
            builder.setTitle(getString(R.string.dialog_addNewPerson_headerMsg));

            // Add Save
            builder.setPositiveButton(getString(R.string.dialog_savePerson), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do Something here

                }
            });

            // Add Cancel
            builder.setNegativeButton(getString(R.string.dialog_addPerson_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDialog().cancel();
                        }
                    });


            return builder.create();
        }

    }
    public void addNewPerson(){
        DialogFragment ad = new addPersonDialogFragment();
        ad.show(getSupportFragmentManager(), "addPeople");
    }
}

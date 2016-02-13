package sanstormsolutions.com.whatshisname.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.models.People;

public class EditPeopleActivity extends AppCompatActivity {
    public static final String FIREBASE_URL = "https://whatshisnameagain.firebaseio.com";

    //Views
    private EditText mFirstName = null;
    private EditText mLastName = null;
    private EditText mBirthday = null;
    private EditText mZip = null;

    //Firebase
    private Firebase mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebase.setAndroidContext(this); //Initialize Firebasee
        setContentView(R.layout.activity_edit_people);

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_edit_people_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.edit_people_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something here
            }
        });

        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_edit){
            //Do something here
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to contain all the views for this activity
     */
    private void setupViews(){
        mFirstName = (EditText) findViewById(R.id.content_edit_people_edtxFirstName);
        mLastName = (EditText) findViewById(R.id.content_edit_people_edtxLastName);
        mBirthday = (EditText) findViewById(R.id.content_edit_people_edtxBirthday);
        mZip = (EditText) findViewById(R.id.content_edit_people_edtxZipCode);
    }

    /**
     * Method to save the edits to the specified Person
     */
    private void saveEdits(){
        People coolPerson = new People();
        coolPerson.setFirstName(mFirstName.getText().toString());
        coolPerson.setLastName(mLastName.getText().toString());
        coolPerson.setBirthday(mBirthday.getText().toString());
        coolPerson.setZipCode(mZip.getText().toString());


    }


}

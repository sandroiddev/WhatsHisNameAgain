package sanstormsolutions.com.whatshisname.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import sanstormsolutions.com.whatshisname.data.ApplicationData;
import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.models.People;

public class AddPersonActivity extends AppCompatActivity {
    public static final String device_id = ApplicationData.getDeviceID(ApplicationData.getContext());
    public static final String FIREBASE_URL = "https://whatshisnameagain.firebaseio.com/"+device_id;

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

        setContentView(R.layout.activity_add_people);

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add_people_toolbar);
        toolbar.setTitle(R.string.add_person_activity_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment alert = new CancelAddNewDialogFragment();
                alert.show(getSupportFragmentManager(),"CancelAlert");
            }
        });

        setupViews(); //Setup the views for this activity

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            saveNewPerson();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to contain all of the views for this activity
     */
    private void setupViews() {
        mFirstName = (EditText) findViewById(R.id.content_add_people_edtxFirstName);
        mLastName = (EditText) findViewById(R.id.content_add_people_edtxLastName);
        mBirthday = (EditText) findViewById(R.id.content_add_people_edtxBirthday);
        mZip = (EditText) findViewById(R.id.content_add_people_edtxZipCode);
        mZip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    saveNewPerson();
                    handled = true;
                }
                return handled;
            }
        });

    }

    /**
     * Method to gather the input from the views and save it to Firebase as a new person
     */
    private void saveNewPerson() {
        People coolPerson = new People();
        //Check to see if the fields have been entered, and if so add them to the object.
        if (mFirstName.getText().toString().isEmpty()){
            Toast.makeText(AddPersonActivity.this, "First Name must be entered", Toast.LENGTH_SHORT).show();
            return;
        }else{
            coolPerson.setFirstName(mFirstName.getText().toString());
        }

        if (mLastName.getText().toString().isEmpty()){
            Toast.makeText(AddPersonActivity.this, "Last Name must be entered", Toast.LENGTH_SHORT).show();
            return;
        }else{
            coolPerson.setLastName(mLastName.getText().toString());
        }

        if (mBirthday.getText().toString().isEmpty()){
            Toast.makeText(AddPersonActivity.this, "Birthday must be entered", Toast.LENGTH_SHORT).show();
            return;
        }else{
            coolPerson.setBirthday(mBirthday.getText().toString());
        }

        if (mZip.getText().toString().isEmpty()){
            Toast.makeText(AddPersonActivity.this, "Zip Code must be entered", Toast.LENGTH_SHORT).show();
            return;
        }else{
            coolPerson.setZipCode(mZip.getText().toString());
        }

        //Save Values to Firebase
        mFirebase = new Firebase(FIREBASE_URL);

        // Since we aren't using an unique id in this example, use push() as the uid.
        Firebase peopleRef = mFirebase.child("people");
        Firebase newPostRef = peopleRef.push();
        newPostRef.setValue(coolPerson);

        // Grab the Unique ID generated by the push()
        String uniqueID = newPostRef.getKey();

        // Now that we have the uid, update the record with it for future use.
        Map<String, Object> uid = new HashMap<>();
        uid.put("id",uniqueID);

        Firebase updateRef = peopleRef.child(uniqueID);
        updateRef.updateChildren(uid);

        // Show the user feedback
        Toast.makeText(AddPersonActivity.this, mFirstName.getText().toString() + " " +  mLastName.getText().toString() + " has been added", Toast.LENGTH_SHORT).show();

        // Go back to the display activity
        finish();
    }

    /**
     * Static class that handles the creation of the Add new person dialog
     */
    public static class CancelAddNewDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.add_person_alert_cancel_msg))
                    .setPositiveButton(getString(R.string.add_person_alert_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User wants to cancel the add
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.add_person_alert_continue), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User wants to continue with add
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }

}

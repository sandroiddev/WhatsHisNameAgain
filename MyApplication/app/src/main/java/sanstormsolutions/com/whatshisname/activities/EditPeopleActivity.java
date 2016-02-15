package sanstormsolutions.com.whatshisname.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.data.ApplicationData;
import sanstormsolutions.com.whatshisname.models.People;

public class EditPeopleActivity extends AppCompatActivity {
    public static final String device_id = ApplicationData.getDeviceID(ApplicationData.getContext());
    public static final String FIREBASE_URL = "https://whatshisnameagain.firebaseio.com/"+device_id;
    private static final String TAG = EditPeopleActivity.class.getSimpleName();

    //Views
    private EditText mFirstName = null;
    private EditText mLastName = null;
    private EditText mBirthday = null;
    private EditText mZip = null;

    //Firebase
    private Firebase mFirebase;
    private Firebase.CompletionListener m_fb_CompletionListener = null;


    //Vars
    private People mPeopleData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_people);

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_edit_people_toolbar);
        toolbar.setTitle(R.string.edit_people_activity_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something here
                DialogFragment dialog = new CancelEditDialogFragment();
                dialog.show(getSupportFragmentManager(), "CancelEditDialogFragment");
            }
        });

        // Grab data from the intent
        Intent intent = getIntent();
        mPeopleData = intent.getParcelableExtra("personData");

        setupViews(mPeopleData);

        setupListeners();

        mFirebase = new Firebase(FIREBASE_URL + "/people");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_people, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            saveEdits();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Method to contain all the views for this activity
     *
     * @param mPeopleData parcelable object from the PeopleAdapter
     */
    private void setupViews(People mPeopleData) {
        String firstName = mPeopleData.getFirstName().toString();
        String lastName = mPeopleData.getLastName().toString();
        String dob = mPeopleData.getBirthday().toString();
        String zipCode = mPeopleData.getZipCode().toString();

        mFirstName = (EditText) findViewById(R.id.content_edit_people_edtxFirstName);
        mFirstName.setText(firstName);

        mLastName = (EditText) findViewById(R.id.content_edit_people_edtxLastName);
        mLastName.setText(lastName);

        mBirthday = (EditText) findViewById(R.id.content_edit_people_edtxBirthday);
        mBirthday.setText(dob);

        mZip = (EditText) findViewById(R.id.content_edit_people_edtxZipCode);
        mZip.setText(zipCode);
        mZip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    saveEdits();
                    handled = true;
                }
                return handled;
            }
        });
    }

    /**
     * Method to save the edits to the specified Person to Firebase
     */
    private void saveEdits() {
        String personID = mPeopleData.getId();

        Map<String, Object> editPerson = new HashMap<>();
        editPerson.put("firstName", mFirstName.getText().toString());
        editPerson.put("lastName", mLastName.getText().toString());
        editPerson.put("birthday", mBirthday.getText().toString());
        editPerson.put("zipCode", mZip.getText().toString());

        Firebase updateRef = mFirebase.child(personID);
        updateRef.updateChildren(editPerson, m_fb_CompletionListener);

    }

    /**
     * Method to contain all of the Listeners for this class
     */
    private void setupListeners() {

        // Firebase Listener to see if the transaction was completed
        m_fb_CompletionListener = new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Toast.makeText(EditPeopleActivity.this, "Edits could not be saved", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, firebaseError.getMessage());

                    // Go back to the display activity
                    finish();
                } else {
                    // Show the user feedback
                    Toast.makeText(EditPeopleActivity.this, mFirstName.getText().toString() + " " + mLastName.getText().toString() + " changes have been saved", Toast.LENGTH_SHORT).show();

                    // Go back to the display activity
                    finish();
                }
            }


        };
    }

    /**
     * Static class that handles the creation of the Cancel Edit Alert Dialog
     */
    public static class CancelEditDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.edit_person_alert_cancel_msg))
                    .setPositiveButton(getString(R.string.edit_person_alert_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User wants to cancel the add
                            dialog.dismiss();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.edit_person_alert_continue), new DialogInterface.OnClickListener() {
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

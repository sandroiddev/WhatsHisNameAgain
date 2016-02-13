package sanstormsolutions.com.whatshisname.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.models.People;

public class AddPersonActivity extends AppCompatActivity {
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

        setContentView(R.layout.activity_add_people);

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add_people_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.add_person_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment alert = new CancelAddNewDialogFragment();
                alert.show(getSupportFragmentManager(),"CancelAlert");
            }
        });

        setupViews(); //Setup the views for this activity

        mFirebase.setAndroidContext(this); //Inititalize Firebase

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

    private void setupViews() {
        mFirstName = (EditText) findViewById(R.id.dialog_add_people_edtxFirstName);
        mLastName = (EditText) findViewById(R.id.dialog_add_people_edtxLastName);
        mBirthday = (EditText) findViewById(R.id.dialog_add_people_edtxBirthday);
        mZip = (EditText) findViewById(R.id.dialog_add_people_edtxZipCode);

    }

    private void saveNewPerson() {
        People coolPerson = new People();
        coolPerson.setFirstName(mFirstName.getText().toString());
        coolPerson.setLastName(mLastName.getText().toString());
        coolPerson.setBirthday(mBirthday.getText().toString());
        coolPerson.setZipCode(mZip.getText().toString());

        //Save Values to Firebase
        mFirebase = new Firebase(FIREBASE_URL);
        // Since we aren't using an unique id in this example, use push()
        mFirebase.child("people").push().setValue(coolPerson);

        /*Toast.makeText(AddPersonActivity.this, "New Person Added", Toast.LENGTH_SHORT).show();*/
        Snackbar.make(getCurrentFocus(), "New Person Added", Snackbar.LENGTH_SHORT)
                .setAction("Undo", null).show();

        finish();
    }

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

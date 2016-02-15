package sanstormsolutions.com.whatshisname.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.List;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.activities.EditPeopleActivity;
import sanstormsolutions.com.whatshisname.activities.MainActivity;
import sanstormsolutions.com.whatshisname.data.ApplicationData;
import sanstormsolutions.com.whatshisname.models.People;

/**
 * Created by jsandersii on 2/11/16.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    public static final String device_id = ApplicationData.getDeviceID(ApplicationData.getContext());
    public static final String FIREBASE_URL = "https://whatshisnameagain.firebaseio.com/"+device_id;
    private static final String TAG = PeopleAdapter.class.getSimpleName();

    //Vars
    private List<People> mPeopleList = null;
    private Context mContext = null;
    private int rowLayout;
    private View.OnClickListener mOnClickListener = null;
    private View.OnLongClickListener mOnLongClickListener = null;
    private People mPeopleData = null;
    private String mUserID = null;

    //FireBase
    private Firebase mFirebase;
    private Firebase.CompletionListener m_fb_CompletionListener = null;

    //Constructor
    public PeopleAdapter(List<People> people, Context context, int rowLayout){
        this.mPeopleList = people;
        this.mContext = context;
        this.rowLayout = rowLayout;
    }


    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        setupListeners();

        mFirebase = new Firebase(FIREBASE_URL + "/people");

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PeopleAdapter.ViewHolder holder, int position) {
        People contacts = mPeopleList.get(position);

        if (mPeopleList != null) {

            // Grab the Person details and set it to display
            holder.mFirstName.setText(contacts.getFirstName());
            holder.mLastName.setText(contacts.getLastName());
            holder.mDOB.setText(contacts.getBirthday());
            holder.mZipCode.setText(contacts.getZipCode());
            holder.mNameBubble.setText(nameBubbleGenerator(contacts));

            mPeopleData = contacts;
            mUserID = contacts.getId();

            // Handle clicks on the view. This will enter the edit mode.
            holder.mContent.setOnClickListener(mOnClickListener);

            // Handle Long clicks on the view. This will enter the delete mode.
            holder.mContent.setLongClickable(true);
            holder.mContent.setOnLongClickListener(mOnLongClickListener);
        }


    }

    @Override
    public int getItemCount() {
        return mPeopleList == null ? 0 : mPeopleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Views for the listitem layout
        protected TextView mFirstName = null;
        protected TextView mLastName = null;
        protected TextView mDOB = null;
        protected TextView mZipCode = null;
        protected TextView mNameBubble = null;
        protected RelativeLayout mContent = null;

        public ViewHolder(View itemView) {
            super(itemView);

            mFirstName = (TextView) itemView.findViewById(R.id.listitem_user_display_txtvFirstName);
            mLastName = (TextView) itemView.findViewById(R.id.listitem_user_display_txtvLastName);
            mDOB = (TextView) itemView.findViewById(R.id.listitem_user_display_txtvDOB);
            mZipCode = (TextView) itemView.findViewById(R.id.listitem_user_display_txtvZipCode);
            mNameBubble = (TextView) itemView.findViewById(R.id.listitem_user_display_txtvBubble);
            mContent = (RelativeLayout) itemView.findViewById(R.id.listitem_user_display_rlContent);


        }
    }

    /**
     * Method to contain all event listeners
     */
    private void setupListeners() {

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args;
                args = new Bundle();
                args.putParcelable("personData", mPeopleData);

                Intent intent = new Intent(mContext, EditPeopleActivity.class);
                intent.putExtras(args);
                mContext.startActivity(intent);

            }
        };

        mOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogFragment delete = new DeleteDialogFragment();
                delete.show(ApplicationData.getActivity().getSupportFragmentManager(),"DeletePersonFragment");
                return false;
            }
        };

        // Firebase Listener to see if the transaction was completed
        m_fb_CompletionListener = new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Toast.makeText(mContext, "Delete could not be completed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, firebaseError.getMessage());

                } else {
                    // Show the user feedback
                    Toast.makeText(mContext, "Contact has been deleted", Toast.LENGTH_SHORT).show();

                }
            }


        };
    }

    /***
     * Simple Method to parse the First and Last Name of the contact.
     * @param contacts This is a People Object that will contain the First/Last Names
     * @return Trimmed First and Last Name to just their initials.
     */
    protected String nameBubbleGenerator(People contacts){
        String initials;
        initials = contacts.getFirstName().substring(0,1)+contacts.getLastName().substring(0,1);
        return initials;
    }

    /**
     * Static class that handles the creation of the Cancel Edit Alert Dialog
     */
    public class DeleteDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.delete_person_alert_cancel_msg))
                    .setPositiveButton(getString(R.string.delete_person_alert_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User wants to cancel the add
                            deletePerson();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.delete_person_alert_continue), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //User wants to remove the person
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }

    /**
     * Method to save the edits to the specified Person to Firebase
     */
    private void deletePerson() {
        Firebase updateRef = mFirebase.child(mUserID);
        updateRef.setValue(null, m_fb_CompletionListener);

    }
}

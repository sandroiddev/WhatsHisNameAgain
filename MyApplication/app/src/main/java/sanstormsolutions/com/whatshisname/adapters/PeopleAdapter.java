package sanstormsolutions.com.whatshisname.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sanstormsolutions.com.whatshisname.R;
import sanstormsolutions.com.whatshisname.activities.EditPeopleActivity;
import sanstormsolutions.com.whatshisname.models.People;

/**
 * Created by jsandersii on 2/11/16.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    //Vars
    private List<People> mPeopleList = null;
    private Context mContext = null;
    private int rowLayout;
    private View.OnClickListener mOnClickListener = null;
    private People mPeopleData = null;

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

            // Handle clicks on the view. This will enter the edit mode.
            holder.mContent.setOnClickListener(mOnClickListener);
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
}

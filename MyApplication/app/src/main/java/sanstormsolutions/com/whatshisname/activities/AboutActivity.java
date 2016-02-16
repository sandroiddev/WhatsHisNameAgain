package sanstormsolutions.com.whatshisname.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sanstormsolutions.com.whatshisname.R;

public class AboutActivity extends AppCompatActivity {

    //Vars
    private ImageView mSelfie = null;
    private TextView mLinkedIn = null;
    private TextView mEmail = null;
    private TextView mPhone = null;
    private View.OnClickListener mOnClickListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_about_toolbar);
        toolbar.setTitle(R.string.about_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Setup Listeners();
        setupListeners();

        //Setup Views
        setupViews();

    }

    private void setupListeners() {

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.content_about_txtvWeb:
                        //Launch a browser and proceed to the link clicked.
                        Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.content_about_linkedIn)));
                        startActivity(intentWeb);
                        break;
                    case R.id.content_about_txtvEmail:
                        //Launch a chooser for handling the email
                        String strEmail = getString(R.string.content_about_gmail);
                        Intent intentEmail = new Intent(Intent.ACTION_SEND);
                        intentEmail.setType("message/rfc822");
                        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{strEmail});
                        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "RE: Your Sample Application");
                        startActivity(intentEmail);

                        break;
                    case R.id.content_about_txtvPhone:
                        //Launch the phone dialer
                        String strPhone = getString(R.string.content_about_cell);
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse("tel:" + strPhone));
                        startActivity(intentCall);
                        break;

                }
            }
        };
    }

    private void setupViews(){
        mSelfie = (ImageView) findViewById(R.id.content_about_imgvMe);
        Picasso.with(this).load("http://www.sanstormsolutions.com/wp-content/uploads/2016/02/theboys.jpg").into(mSelfie);

        mLinkedIn = (TextView) findViewById(R.id.content_about_txtvWeb);
        mLinkedIn.setOnClickListener(mOnClickListener);

        mEmail = (TextView) findViewById(R.id.content_about_txtvEmail);
        mEmail.setOnClickListener(mOnClickListener);

        mPhone = (TextView) findViewById(R.id.content_about_txtvPhone);
        mPhone.setOnClickListener(mOnClickListener);
    }
}

package ritwik.login.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import ritwik.login.R;
import ritwik.login.models.User;

public class DataDisplayActivity extends AppCompatActivity
{
	private TextView mTvName, mTvEMail, mTvGender, mTvID;
	private ImageView mIvImage;

	@Override
	public void onBackPressed ()
	{
		super.onBackPressed ();

		finish ();
	}

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_data_display );

		initializeToolbarAndActionBar ();
		initializeViews ();

		User user = getIntent ().getExtras ().getParcelable ( "user" );
		if ( user != null )
			setDataToViews ( user );
		else
			finish ();
	}

	@Override
	protected void onDestroy ()
	{
		super.onDestroy ();
		LoginManager.getInstance ().logOut ();
		Log.e ( "User", "LogOut" );
	}

	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId () )
		{
			case android.R.id.home:
				finish ();
				break;
		}

		return super.onOptionsItemSelected ( item );
	}

	/**
	 * Initialize the Views.
	 */
	private void initializeViews ()
	{
		mTvName = (TextView) findViewById ( R.id.text_name );
		mTvEMail = (TextView) findViewById ( R.id.text_email );
		mTvGender = (TextView) findViewById ( R.id.text_gender );
		mTvID = (TextView) findViewById ( R.id.text_id );
		mIvImage = (ImageView) findViewById ( R.id.image_profile );
	}

	/**
	 * Initialize the Toolbar and ActionBar.
	 */
	private void initializeToolbarAndActionBar ()
	{
		//Instantiating Toolbar and setting it up to ActionBar.
		Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
		setSupportActionBar ( toolbar );

		//Instantiating ActionBar and setting Title along with some other options.
		ActionBar actionBar = getSupportActionBar ();
		if ( actionBar != null )
		{
			actionBar.setTitle ( "User Details" );
			actionBar.setHomeButtonEnabled ( true );
			actionBar.setDisplayHomeAsUpEnabled ( true );
			actionBar.setDisplayShowHomeEnabled ( true );
		}
	}

	/**
	 * Set the data obtained from Shared Preferences to the Views.
	 */
	private void setDataToViews ( User user )
	{
		mTvName.setText ( user.getName () );
		mTvEMail.setText ( user.getEmail () );
		mTvGender.setText ( user.getGender () );
		mTvID.setText ( user.getId () );

		Glide
				.with ( DataDisplayActivity.this )
				.load ( user.getPhoto () )
				.into ( mIvImage );
	}
}
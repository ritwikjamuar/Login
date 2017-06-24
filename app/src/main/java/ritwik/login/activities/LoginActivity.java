package ritwik.login.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ritwik.login.R;
import ritwik.login.models.User;

public class LoginActivity
		extends AppCompatActivity
		implements FacebookCallback<LoginResult>,
		           GraphRequest.GraphJSONObjectCallback,
		           View.OnClickListener
{
	private static final String TAG = LoginActivity.class.getSimpleName();
	private CallbackManager mFBCallbackManager;

	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate ( savedInstanceState );
		initializeFacebook ();
		setContentView ( R.layout.activity_login );

		initializeToolbarAndActionBar ();
		initializeViews ();
	}

	@Override
	public void onClick ( View v )
	{
		switch ( v.getId () )
		{
			case R.id.login_button:
				performLogin ();
				break;
		}
	}

	@Override
	protected void onActivityResult ( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult ( requestCode, resultCode, data );

		//Forwards the Login Results to the methods of Callback Manager.
		mFBCallbackManager.onActivityResult ( requestCode, resultCode, data );
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
	 * Initialize the Facebook SDK.
	 */
	private void initializeFacebook ()
	{
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
		mFBCallbackManager = CallbackManager.Factory.create();
	}

	/**
	 * Initialize the Toolbar and ActionBar.
	 */
	private void initializeToolbarAndActionBar ()
	{
		//Instantiating Toolbar and setting it up to ActionBar.
		Toolbar toolbar = (Toolbar) findViewById ( R.id.login_toolbar );
		setSupportActionBar ( toolbar );

		//Instantiating ActionBar and setting Title along with some other options.
		ActionBar actionBar = getSupportActionBar ();
		if ( actionBar != null )
		{
			actionBar.setTitle ( R.string.login );
			actionBar.setHomeButtonEnabled ( true );
			actionBar.setDisplayShowHomeEnabled ( true );
			actionBar.setDisplayHomeAsUpEnabled ( true );
		}
	}

	/**
	 * Initialize the Views.
	 */
	private void initializeViews ()
	{
		Button mFBLogin = ( Button ) findViewById ( R.id.login_button );
		mFBLogin.setOnClickListener ( LoginActivity.this );
	}

	@Override
	public void onSuccess ( LoginResult loginResult )
	{
		//Acquiring the current access token from Login.
		AccessToken token = loginResult.getAccessToken ();

		//Obtaining the data from Facebook using acquired Access Token.
		GraphRequest request = GraphRequest.newMeRequest ( token, LoginActivity.this );

		//Requesting the specific fields.
		Bundle parameters = new Bundle();
		parameters.putString ( "fields", "id, name, email, gender, birthday, location" );
		request.setParameters ( parameters );

		//Execute the graph request to obtain the User Details.
		request.executeAsync ();
	}

	@Override
	public void onCancel () { showToastMessage ( "Login is cancelled" ); }

	@Override
	public void onError ( FacebookException error ) { showToastMessage ( "Unable to Login to Facebook" ); }

	@Override
	public void onCompleted ( JSONObject object, GraphResponse response )
	{
		int responseCode = 0;
		try { responseCode = response.getConnection().getResponseCode(); }
		catch ( IOException e ) { Log.e( TAG, "getUserDetailsFromFacebook : " + e.getLocalizedMessage()); }

		/*
		 * If Response Code is 200, only then Facebook Data should be parsed.
		 * If Response Code is other than 200, JSONObject will be empty.
		 */
		if ( 200 == responseCode )
			parseFacebookData ( object );
	}

	/**
	 * Parse the data from Facebook.
	 *
	 * @param object JSONObject containing the data fetched from Facebook.
	 */
	private void parseFacebookData ( JSONObject object )
	{
		User user = new User ();

		user.setName ( object.optString("name") );
		user.setEmail ( object.optString("email") );
		user.setGender ( object.optString("gender") );
		user.setId ( object.optString("id") );
		user.setPhoto ( "http://graph.facebook.com/" + object.optString("id") + "/picture?type=large" );

		startDataDisplayActivity ( user );
	}

	/**
	 * Start Data Display Activity.
	 */
	private void startDataDisplayActivity ( User user )
	{
		Intent intent = new Intent ( LoginActivity.this, DataDisplayActivity.class );
		intent.putExtra ( "user", user );
		startActivity ( intent );
		finish ();
	}

	/**
	 * Show Toast message.
	 *
	 * @param message String to be displayed in Toast.
	 */
	private void showToastMessage ( String message )
	{
		Toast
				.makeText (
						LoginActivity.this,
				        message,
				        Toast.LENGTH_SHORT
				)
				.show ();
	}

	/**
	 * Perform Facebook Login.
	 */
	private void performLogin ()
	{
		//Storing the permissions as a list of String.
		List<String> permissions = new ArrayList<> ();
		permissions.add ( "public_profile" );

		//Instantiating the Login Manager with Permissions.
		LoginManager manager = LoginManager.getInstance ();
		manager.logInWithReadPermissions ( LoginActivity.this, permissions );

		//Making manager to register Callback.
		manager.registerCallback ( mFBCallbackManager, LoginActivity.this );
	}
}
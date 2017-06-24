package ritwik.login.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model Class to store data of User fetched from Facebook.
 */
public class User implements Parcelable
{
	private String name;
	private String email;
	private String gender;
	private String id;
	private String photo;

	private User ( Parcel in )
	{
		name = in.readString ();
		email = in.readString ();
		gender = in.readString ();
		id = in.readString ();
		photo = in.readString ();
	}

	public static final Creator<User> CREATOR = new Creator<User> ()
	{
		@Override
		public User createFromParcel ( Parcel in ) { return new User ( in ); }

		@Override
		public User[] newArray ( int size ) { return new User[size]; }
	};

	public User () {}

	public String getName () { return name; }
	public void setName ( String name ) { this.name = name; }

	public String getEmail () { return email; }
	public void setEmail ( String email ) { this.email = email; }

	public String getGender () { return gender; }
	public void setGender ( String gender ) { this.gender = gender; }

	public String getId () { return id; }
	public void setId ( String id ) { this.id = id; }

	public String getPhoto () { return photo; }
	public void setPhoto ( String photo ) { this.photo = photo; }

	@Override
	public int describeContents () { return 0; }

	@Override
	public void writeToParcel ( Parcel dest, int flags )
	{
		dest.writeString ( name );
		dest.writeString ( email );
		dest.writeString ( gender );
		dest.writeString ( id );
		dest.writeString ( photo );
	}
}
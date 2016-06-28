package com.tcs.tvmilp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity

{

	Boolean checked = false;
	ArrayList<String> superUsers;
	boolean isSuperAdmin = false;
	boolean isExist = false;
	String existingUser;
	EditText name;
	EditText phone;
	EditText stream;
	EditText hostel;
	EditText empid;

	private EditText inputName, inputEmail, inputPassword;
	private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPhone, inputLayoutEmpId, inputLayoutHostel;
	Toolbar toolbar;
	InternetChecker internetChecker;

	//this is the register activity that registers the users on the cloud database.


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register2);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("TVM ILP");
		setSupportActionBar(toolbar);

		inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
		inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
		inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
		inputLayoutHostel = (TextInputLayout) findViewById(R.id.input_layout_hostel);
		inputLayoutEmpId = (TextInputLayout) findViewById(R.id.input_layout_empid);

		name = (EditText) findViewById(R.id.input_name);
		stream = (EditText) findViewById(R.id.input_email);
		phone = (EditText) findViewById(R.id.input_phone);
		hostel = (EditText) findViewById(R.id.input_hostel);
		empid =  (EditText) findViewById(R.id.input_empid);



		//	inputName.addTextChangedListener(new MyTextWatcher(inputName));
		//	inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
		//	inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));




		//	name = (EditText) findViewById(R.id.edname);
		//phone = (EditText) findViewById(R.id.edphone);
		//stream = (EditText) findViewById(R.id.edstream);
		//hostel = (EditText) findViewById(R.id.edhostel);
		//empid = (EditText) findViewById(R.id.edempid);
		superUsers = new ArrayList<String>();
		Button register = (Button) findViewById(R.id.btn_register);
		System.out.println("the query is working fine");

		/*
		 * ParseQuery<ParseObject> superQuery = ParseQuery
		 *
		 * .getQuery("User"); superQuery.whereEqualTo("role", "superadmin");
		 * superQuery.findInBackground(new FindCallback<ParseObject>() {
		 *
		 * @Override public void done(List<ParseObject> arg0, ParseException
		 * arg1) { // TODO Auto-generated method stub for (int i = 0; i <
		 * arg0.size(); i++) { ParseObject po = arg0.get(i);
		 * System.out.println("the super user name is" +
		 * po.getString("username"));
		 *
		 * }
		 *
		 * } });
		 */

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {


				MyTask task=new MyTask();
				task.execute();
			}
		});

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		internetChecker=new InternetChecker(getApplicationContext());
		if(!internetChecker.isConnectingToInternet())
		{
			System.out.println("INTERNET CRASHED");
			Toast.makeText(getApplicationContext(), "Connect to Internet", Toast.LENGTH_LONG).show();
		}
	}


	private class MyTask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog dia;
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dia.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dia = ProgressDialog.show(RegisterActivity.this, null,
					"Loading Please wait...");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			// this is the code to identify the super admin

			// this is the code to register a trainee
			String sname = name.getText().toString().trim();

			final String sphone = phone.getText().toString().trim();
			final String email = stream.getText().toString().trim();
			final String shostel = hostel.getText().toString().trim();
			final String sempid = empid.getText().toString().trim();



			List<ParseUser> userlist = new ArrayList<ParseUser>();


			try {
				userlist = ParseUser.getQuery().find();
				for (int i = 0; i < userlist.size(); i++) {
					ParseUser internalPo = userlist.get(i);
					String cUser = internalPo.getString("username");
					System.out.println("the username in the checker is"
							+ cUser);
					if (cUser.equals(sempid)) {
						System.out.println("user already exists");
						isExist = true;
						existingUser = internalPo.getString("username");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// new FindCallback<ParseUser>() {

		/*
		 * @Override public void done(List<ParseUser> arg0,
		 * ParseException arg1) { // TODO Auto-generated method stub
		 *
		 * for(int i=0;i<arg0.size();i++) { ParseObject
		 * internalPo=arg0.get(i); String
		 * cUser=internalPo.getString("username");
		 * System.out.println("the username in the checker is"+cUser);
		 * if(cUser.equals(sempid)) {
		 * System.out.println("user already exists"); isExist=true;
		 * existingUser=internalPo.getString("username"); }
		 *
		 * }
		 *
		 * } });
		 */
			if (!isExist) {
				System.out.println("entered non exist");
				ParseUser pu = new ParseUser();
				pu.setUsername(sempid);
				pu.setPassword("password");

				pu.put("name", sname);
				pu.put("hostel", shostel);
				pu.setEmail(email);
				pu.put("phone", sphone);
				pu.put("role", "trainee");

				pu.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(com.parse.ParseException arg0) {
						// TODO Auto-generated method stub
						if (arg0 == null) {
							Toast.makeText(getApplicationContext(),
									"Success", Toast.LENGTH_LONG).show();

							dia.dismiss();
							String urole = ParseUser.getCurrentUser()
									.getString("role");

							System.out
									.println("the role of the associate is"
											+ urole);

							Intent toLogin = new Intent(
									getApplicationContext(), Login.class);
							toLogin.putExtra("username", sempid);
							startActivity(toLogin);

						}
						if (arg0 != null) {
							Toast.makeText(getApplicationContext(),
									"	INVALID CREDENTIALS", Toast.LENGTH_SHORT)
									.show();
							System.out.println(arg0);
						}
						ParseObject po = new ParseObject("roletable");
						po.put("username", sempid);
						po.put("role", "trainee");
						po.saveInBackground(new SaveCallback() {

							@Override
							public void done(com.parse.ParseException arg0) {
								// TODO Auto-generated method stub

							}
						});
					}
				});

				// Initially this code will set everyone's role as trainee.
				if (!isSuperAdmin) {

				}

			} else {

				// This is the code to make a user who is already registered
				// log in again
				// I will compare the values and if the values are same then
				// I check all the values and
				// if the values are same then ,the user will log in.
				ParseUser.getCurrentUser().getQuery()
						.whereEqualTo("username", sempid)
						.findInBackground(new FindCallback<ParseUser>() {

							@Override
							public void done(List<ParseUser> arg0,
											 com.parse.ParseException arg1) {
								// TODO Auto-generated method stub

								ParseUser innerpu = arg0.get(0);
								String inneremail = innerpu
										.getString("email");
								String hostel = innerpu.getString("hostel");
								String phone = innerpu.getString("phone");
								System.out.println("came to comparison step");
								if (inneremail.equals(email)
										&& hostel.equals(shostel)
										&& phone.equals(sphone)) {
									Intent log = new Intent(
											getApplicationContext(),
											Login.class);
									log.putExtra("username", sempid);
									startActivity(log);

								}

							}
						});

				// this is the code to identify the superadmin
				dia.dismiss();
				ParseUser.getQuery().whereEqualTo("role", "superadmin")
						.findInBackground(new FindCallback<ParseUser>() {

							@Override
							public void done(List<ParseUser> arg0,
											 com.parse.ParseException arg1) {
								// TODO Auto-generated method stub

								for (int i = 0; i < arg0.size(); i++) {
									ParseObject po = arg0.get(i);
									System.out.println("the super user name is"
											+ po.getString("username"));
									String databaseUsername = po
											.getString("username");
									String enteredUsername = empid
											.getText().toString();
									if (databaseUsername
											.equals(enteredUsername)) {
										isSuperAdmin = true;
										System.out
												.println("User is superadmin and will be sent to login activity");
										Intent superIntent = new Intent(
												getApplicationContext(),
												SuperLogin.class);

										superIntent.putExtra("username",
												sempid);
										startActivity(superIntent);

									}

								}

							}
						});

				if (isSuperAdmin) {

				}
			}
			return null;
		}

	}


	private void submitForm() {
		if (!validateName()) {
			return;
		}

		if (!validateEmail()) {
			return;
		}

		if (!validatePassword()) {
			return;
		}

		Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
	}

	private boolean validateName() {
		if (inputName.getText().toString().trim().isEmpty()) {
			inputLayoutName.setError(getString(R.string.err_msg_name));
			requestFocus(inputName);
			return false;
		} else {
			inputLayoutName.setErrorEnabled(false);
		}

		return true;
	}

	private boolean validateEmail() {
		String email = inputEmail.getText().toString().trim();

		if (email.isEmpty() || !isValidEmail(email)) {
			inputLayoutEmail.setError(getString(R.string.err_msg_email));
			requestFocus(inputEmail);
			return false;
		} else {
			inputLayoutEmail.setErrorEnabled(false);
		}

		return true;
	}

	private boolean validatePassword() {
		if (inputPassword.getText().toString().trim().isEmpty()) {
			inputLayoutPhone.setError(getString(R.string.err_msg_password));
			requestFocus(inputPassword);
			return false;
		} else {
			inputLayoutPhone.setErrorEnabled(false);
		}

		return true;
	}

	private static boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}
	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
				case R.id.input_name:
					validateName();
					break;
				case R.id.input_email:
					validateEmail();
					break;
				case R.id.input_phone:
					validatePassword();
					break;
			}
		}
	}

}


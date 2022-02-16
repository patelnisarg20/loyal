package com.example.loyal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {
   TextView loginUser;
   EditText rname,remail,rpass,cpass,birthdate;
   DatePickerDialog.OnDateSetListener onDateSetListener;
   Button btnRegister;
   RadioButton btnGender;
   RadioGroup radioGroup;
   CheckBox checkBox;
   String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
   ProgressDialog progressDialog;
   FirebaseAuth mAuth;
   FirebaseUser mUser;

   FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        loginUser=findViewById(R.id.loginUser);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rname=findViewById(R.id.rname);
        remail=findViewById(R.id.remail);
        rpass=findViewById(R.id.rpass);
        cpass=findViewById(R.id.cpass);
        btnRegister=findViewById(R.id.btnRegister);
        radioGroup=findViewById(R.id.rbGroup);
        checkBox=findViewById(R.id.checkBox);
        birthdate=findViewById(R.id.birthDate);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();




        loginUser.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        });

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              DatePickerDialog datePickerDialog=new DatePickerDialog(
                      RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                      onDateSetListener, year,month, day);
              datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              datePickerDialog.show();
            }
        });
        onDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date= dayOfMonth+"/"+month+"/"+year;
                birthdate.setText(date);
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perforAuth();
            }
        });
    }


    private void perforAuth() {
       String email=remail.getText().toString();
       String password=rpass.getText().toString();
       String confirmpassword=cpass.getText().toString();
        int isSelected= radioGroup.getCheckedRadioButtonId();



    if(!email.matches(emailPattern)) {
        remail.setError("Enter Correct E-mail");
    }
    else if (password.isEmpty() || password.length()<=6)
    {
        rpass.setError("Enter Proper Password");
    }
    else if (!password.equals(confirmpassword))
    {
        cpass.setError("Both Password Not Matched!");
    }
    else if(!checkBox.isChecked()){
            Toast.makeText(RegisterActivity.this, "please check term and condition", Toast.LENGTH_SHORT).show();
        }
       else if(isSelected ==-1){
           Toast.makeText(RegisterActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                return;
            }
    else
        {
        progressDialog.setMessage("Please Wait While Registration...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //for db
        rootNode=FirebaseDatabase.getInstance();
        reference =rootNode.getReference();

            String Email= remail.getText().toString();
            String name = rname.getText().toString();
            String bithdate = birthdate.getText().toString();
            int i=radioGroup.getCheckedRadioButtonId();
            btnGender=(RadioButton)findViewById(i);
            String gender=btnGender.getText().toString().trim();

           UserHelperClass helperClass = new UserHelperClass(name, email, bithdate, gender);
            reference.child(name).setValue(helperClass);




            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser =mAuth.getCurrentUser();
                }
                else
                    {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();

                    }
            }
        });

        }


    }


    private void sendUserToNextActivity() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
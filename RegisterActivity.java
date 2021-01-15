package com.example.shoppingsaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private Button btnReg;
    private TextView login;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        email = findViewById(R.id.email_registration);
        password = findViewById(R.id.password_registration);

        btnReg = findViewById(R.id.btn_register);
        login = findViewById(R.id.txt_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btnReg.setOnClickListener(view -> {
            String tPassword = password.getText().toString().trim();
            String tEmail = email.getText().toString().trim();

            if (TextUtils.isEmpty(tPassword)) {
                password.setError("A Password is required to continue...");
                return;
            }

            if (TextUtils.isEmpty(tEmail)) {
                email.setError("An Email is required to continue...");
                return;
            }

            mDialog.setMessage("Processing please wait.....");
            mDialog.show();

            mAuth.createUserWithEmailAndPassword(tEmail, tPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Successfully Registered, congratulations!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class) );
                    }

                    else
                        {
                        Toast.makeText(getApplicationContext(), "Registration Failed, Please try again!", Toast.LENGTH_SHORT).show();

                        }
                    mDialog.dismiss();
                }
            });


        });
    }
}
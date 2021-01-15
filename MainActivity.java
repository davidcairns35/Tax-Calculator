package com.example.shoppingsaver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;

    private Button btnLogin;
    private TextView register;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    //    if (mAuth.getCurrentUser()!=null)
       // {
        //    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
       // }

        mDialog = new ProgressDialog(this);

        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.password_login);

        btnLogin = findViewById(R.id.btn_login);
        register = findViewById(R.id.txt_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String tPassword = pass.getText().toString().trim();
                String tEmail = email.getText().toString().trim();

                if (TextUtils.isEmpty(tPassword)) {
                pass.setError("A Password is required to continue...");
                return;
            }

            if (TextUtils.isEmpty(tEmail)) {
                email.setError("An Email is required to continue...");
                return;
            }

            mDialog.setMessage("Processing please wait.....");
            mDialog.show();

            mAuth.signInWithEmailAndPassword(tEmail, tPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Login was successful...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class) );
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to login, please try again....", Toast.LENGTH_SHORT).show();

                    }
                    mDialog.dismiss();
                }
            });
        }
    });
}}

package com.mustafasuleymankinik.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminSignIn extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText adminMail,adminPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);
        firebaseAuth=FirebaseAuth.getInstance();
        adminMail=findViewById(R.id.adminMailEditText);
        adminPass=findViewById(R.id.adminPassEditText);
    }

    public void adminLogin(View view) {
        String mail=adminMail.getText().toString();
        String pass=adminPass.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(AdminSignIn.this, "Login process is successful", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(AdminSignIn.this,AdminPanel.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(AdminSignIn.this, "Login process isn't successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
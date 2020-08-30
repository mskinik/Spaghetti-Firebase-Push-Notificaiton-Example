package com.mustafasuleymankinik.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText mailEditText,passEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        mailEditText=findViewById(R.id.editTextTextPersonName);
        passEditText=findViewById(R.id.editTextTextPassword);

    }

    public void login(View view) {
        firebaseAuth.signInWithEmailAndPassword(mailEditText.getText().toString(),passEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            try {
                                Intent intent= new Intent(getApplicationContext(),WaiterPanel.class);
                                startActivity(intent);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Incorrect password or mail address", Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

    public void register(View view) {

        String name=mailEditText.getText().toString();
        String pass=passEditText.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(name,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Register process is successful", Toast.LENGTH_SHORT).show();
                            firebaseUser=firebaseAuth.getCurrentUser();
                            System.out.println("E-mail: "+firebaseUser.getEmail());
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Register process isn't successful:\n"+task.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void enterAdminPanel(View view) {
        Intent intent= new Intent(MainActivity.this,AdminPanel.class);
        startActivity(intent);
    }
}
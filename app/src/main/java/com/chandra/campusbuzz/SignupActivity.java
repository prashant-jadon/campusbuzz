package com.chandra.campusbuzz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText sEmail,sFullName,sPassword,sRepPassword,sUsername;
    Button sLogin,sSignup;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sEmail = findViewById(R.id.editTextEmail);
        sFullName =findViewById(R.id.editTextFullName);
        sPassword=findViewById(R.id.editTextPassword);
        sRepPassword=findViewById(R.id.editTextConfirmPassword);
        sLogin=findViewById(R.id.buttonGoToLogin);
        sSignup=findViewById(R.id.buttonSignUp);
        sUsername = findViewById(R.id.editTextUsername);
        mAuth = FirebaseAuth.getInstance();




        sSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = sFullName.getText().toString();
                String email = sEmail.getText().toString();
                String password=sPassword.getText().toString();
                String username = sUsername.getText().toString();
                String repeatPassword = sRepPassword.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("name",name);
                                hashMap.put("email",email);
                                hashMap.put("uid", mAuth.getUid());
                                hashMap.put("profileImage","https://cdn-icons-png.flaticon.com/512/4211/4211763.png");
                                hashMap.put("about","No about written");
                                hashMap.put("username",username);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("User");
                                reference.child(mAuth.getUid()).setValue(hashMap);
                                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                finish();
                                Toast.makeText(SignupActivity.this, "User SignedUp Successfull", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignupActivity.this, "Failed to create user:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
            }
        });

        sLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignupActivity.this,MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "SignUp or SignIn First", Toast.LENGTH_SHORT).show();
        }
    }
}
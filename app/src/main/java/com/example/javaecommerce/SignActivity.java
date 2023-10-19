package com.example.javaecommerce;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignActivity extends AppCompatActivity {
    private Button singupButton;
    private EditText inputName,inputNumber,inputPass;
    private ProgressDialog loadingBar;

//    private DatabaseReference database;
    public void validatePhoneNumber(String name,String phone,String password){
         DatabaseReference RootRef;
//         FirebaseDatabase database;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(!(datasnapshot.child("users").child(phone).exists())){
                    HashMap<String ,Object> userData= new HashMap<>();
                    userData.put("phone",phone);
                    userData.put("name",name);
                    userData.put("password",password);
                    RootRef.child("users").child(phone).updateChildren(userData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(SignActivity.this, "Congratulation Your account is created", Toast.LENGTH_SHORT).show();
                                     loadingBar.dismiss();
                                     Intent intent=new Intent(SignActivity.this,LoginActivity.class);
                                     startActivity(intent);
                                 }
                                 else{
                                     loadingBar.dismiss();
                                     Toast.makeText(SignActivity.this, "Network Error please try again...", Toast.LENGTH_SHORT).show();
                                 }
                                }
                            });
                }
                else{
                    Toast.makeText(SignActivity.this, "The"+phone+" number already Exits", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SignActivity.this, "Plaese try again using another phone number", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SignActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void  createAccount()
    {
     String name= inputName.getText().toString();
     String phoneNumber=inputNumber.getText().toString();
     String password=inputPass.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Please enter your number.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else{
          loadingBar.setTitle("Create Account");
          loadingBar.setMessage("Please wait ,while we are checking the credentitals");
          loadingBar.setCanceledOnTouchOutside(false);
          loadingBar.show();
          validatePhoneNumber(name,phoneNumber,password);
        }

    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        singupButton=(Button) findViewById(R.id.signup_button);
        inputName=(EditText) findViewById(R.id.signup_name_field);
        inputNumber=(EditText) findViewById(R.id.signup_phone_field);
        inputPass=(EditText) findViewById(R.id.signup_password_feild);
        loadingBar=new ProgressDialog(this);

        singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }
}
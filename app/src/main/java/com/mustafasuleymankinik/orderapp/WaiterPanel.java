package com.mustafasuleymankinik.orderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class WaiterPanel extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Uri uri;
    String notificationId;
    ImageView imageView;
    Bitmap bitmap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UUID uuid;
    String uuidString;
    StorageReference storageReference;
    EditText v1EditText,v2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_panel);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.imageView);
        v1EditText=findViewById(R.id.editTextValue1);
        v2EditText=findViewById(R.id.editTextValue2);

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                notificationId=userId;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.optionsProfile:

            case R.id.optionsLogout:
                firebaseAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&& grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK&&data!=null)
        {
            uri=data.getData();
            if(Build.VERSION.SDK_INT<28)
            {
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), uri);
                try {
                    bitmap = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void getPicture(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        }
        else
        {
            Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    public void completeButton(View view) {
        uuid=UUID.randomUUID();
        uuidString=uuid.toString();
        String value1=v1EditText.getText().toString();
        String value2=v2EditText.getText().toString();
        databaseReference.child("Database").child(uuidString).child("Value1").setValue(value1);
        databaseReference.child("Database").child(uuidString).child("Value2").setValue(value2);
        databaseReference.child("Database").child(uuidString).child("ValuesTime").setValue(ServerValue.TIMESTAMP);
        final String image="images_folder/"+uuidString+".jpg";
        StorageReference storageReference1=storageReference.child(image);
        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference storageReference2=FirebaseStorage.getInstance().getReference(image);
                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child("Database").child(uuidString).child("Image").setValue(uri.toString());
                    }
                });
            }
        });
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+"Values: "+value1+" "+value2+"'}, 'include_player_ids': ['" + notificationId + "']}"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
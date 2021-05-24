package com.vector.CovSewa.ui.myProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vector.CovSewa.Activities.EditProfile;
import com.vector.CovSewa.R;
import com.vector.CovSewa.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.ContentValues.TAG;

public class MyProfileFragment extends Fragment {

    private UserData value;
    private TextView userNameText, emailText, addressText, contactText,userTypeText,userNameText2;
    private ImageView imageView;
    private String userName, email, addressLine1,addressLine2, contact,userType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        userNameText2 = root.findViewById(R.id.userName);
        userNameText = root.findViewById(R.id.userNameProfile);
        emailText = root.findViewById(R.id.userEmailProfile);
        addressText = root.findViewById(R.id.userAddProfile);
        contactText = root.findViewById(R.id.userPhoneProfile);
        userTypeText = root.findViewById(R.id.userTypeProfile);
        imageView = root.findViewById(R.id.profileimage);
        root.findViewById(R.id.editProfile).setOnClickListener(v->{
            if(value!=null){
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.putExtra("user",value);
                startActivity(intent);
            }

        });


        prepareData();
        return root;
    }

    private void prepareData(){

        String UID = FirebaseAuth.getInstance().getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(UID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = dataSnapshot.getValue(UserData.class);
                setImage("ProfileImage/"+value.getContact());
                setData(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public void setData(UserData s){
        userNameText2.setText(s.getName());
        userNameText.setText(s.getName());
        emailText.setText(s.getEmail());
        addressText.setText(String.format("%s,%s", s.getAddLine1(), s.getAddLine2()));
        contactText.setText(s.getContact());
        userTypeText.setText(s.getCategory());
    }

    private void setImage(String userPhoto) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if(userPhoto==null)
            return;
        StorageReference photoReference= storageReference.child(userPhoto);
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void edit(){

    }
}
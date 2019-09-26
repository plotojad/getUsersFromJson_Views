package com.example.testappusers2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {

    int userID;


    final int BANANA = R.drawable.banana;
    final int STRAWBERRY = R.drawable.strawberry;
    final int APPLE = R.drawable.apple;
    final int MALE = R.drawable.male;
    final int FEMALE = R.drawable.female;
    final int GREEN_EYE = R.drawable.greeneye;
    final int BLUE_EYE = R.drawable.blueeye;
    final int BROWN_EYE = R.drawable.redeye;

    Date date = null;
    SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    SimpleDateFormat sdfFormat = new SimpleDateFormat("HH:mm dd.MM.yy", Locale.ENGLISH);
    String tags = "";


    TextView tvUserID;
    TextView tvUserGUID;
    TextView tvUserBalance;
    TextView tvUserAge;
    ImageView ivUserEye;
    ImageView ivUserSex;
    TextView tvUserCompany;
    TextView tvUserMail;
    TextView tvUserPhone;
    TextView tvUserAdress;
    TextView tvUserAbout;
    TextView tvUserReg;
    TextView tvUserLocation;
    TextView tvUserTags;
    ExpandableListView elvUserFriends;
    ImageView ivUserFruit;
    LinearLayout llFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        userID = intent.getIntExtra("position", 0);

        tvUserID = findViewById(R.id.tvUserID);
        tvUserGUID = findViewById(R.id.tvUserGUID);
        tvUserBalance = findViewById(R.id.tvUserBalance);
        tvUserAge = findViewById(R.id.tvUserAge);
        ivUserEye = findViewById(R.id.ivUserEye);
        ivUserSex = findViewById(R.id.ivUserSex);
        tvUserCompany = findViewById(R.id.tvUserCompany);
        tvUserMail = findViewById(R.id.tvUserMail);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvUserAdress = findViewById(R.id.tvUserAdress);
        tvUserAbout = findViewById(R.id.tvUserAbout);
        tvUserReg = findViewById(R.id.tvUserReg);
        tvUserLocation = findViewById(R.id.tvUserLocation);
        tvUserTags = findViewById(R.id.tvUserTags);
        llFriends = findViewById(R.id.llFriends);
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llFriends.getLayoutParams();
        final int parentHeight = params.height;
        elvUserFriends = findViewById(R.id.elvUserFriends);
        ivUserFruit = findViewById(R.id.ivUserFruit);


        setTitle(MainActivity.getUsers().get(userID).getName());

        tvUserID.setText(String.valueOf(MainActivity.getUsers().get(userID).getId()));
        tvUserGUID.setText(MainActivity.getUsers().get(userID).getGuid());
        tvUserBalance.setText(MainActivity.getUsers().get(userID).getBalance());
        tvUserAge.setText(String.valueOf(MainActivity.getUsers().get(userID).getAge()));

        if (MainActivity.getUsers().get(userID).getEyeColor().equals("blue")) {
            ivUserEye.setImageResource(BLUE_EYE);
        } else if (MainActivity.getUsers().get(userID).getEyeColor().equals("green")) {
            ivUserEye.setImageResource(GREEN_EYE);
        } else if (MainActivity.getUsers().get(userID).getEyeColor().equals("brown")) {
            ivUserEye.setImageResource(BROWN_EYE);
        }

        if (MainActivity.getUsers().get(userID).getGender().equals("male")) {
            ivUserSex.setImageResource(MALE);
        } else if (MainActivity.getUsers().get(userID).getGender().equals("female")) {
            ivUserSex.setImageResource(FEMALE);
        }

        tvUserCompany.setText(MainActivity.getUsers().get(userID).getCompany());
        tvUserMail.setText(MainActivity.getUsers().get(userID).getEmail());
        tvUserPhone.setText(MainActivity.getUsers().get(userID).getPhone());
        tvUserAdress.setText(MainActivity.getUsers().get(userID).getAddress());
        tvUserAbout.setText(MainActivity.getUsers().get(userID).getAbout());

        try {
            date = sdfParse.parse(MainActivity.users.get(userID).getRegistered());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvUserReg.setText(sdfFormat.format(date));

        tvUserLocation.setText(String.valueOf(MainActivity.getUsers().get(userID).getLatitude()
                + ", " + MainActivity.getUsers().get(userID).getLongitude()));

        tags = MainActivity.getUsers().get(userID).getTags()[0];
        for (int j = 1; j < MainActivity.getUsers().get(userID).getTags().length; j++) {
            tags += ", " + MainActivity.getUsers().get(userID).getTags()[j];
        }
        tvUserTags.setText(tags);

        elvUserFriends.setAdapter(new ExpandAdapter(this, MainActivity.getUsers().get(userID), MainActivity.getUsers()).friendsToExpandList());

        if (MainActivity.getUsers().get(userID).getFavoriteFruit().equals("banana")) {
            ivUserFruit.setImageResource(BANANA);
        } else if (MainActivity.getUsers().get(userID).getFavoriteFruit().equals("apple")) {
            ivUserFruit.setImageResource(APPLE);
        } else if (MainActivity.getUsers().get(userID).getFavoriteFruit().equals("strawberry")) {
            ivUserFruit.setImageResource(STRAWBERRY);
        }


        elvUserFriends.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView parent, View v, final int groupPosition, long id) {
                if (!parent.isGroupExpanded(groupPosition)) {
                    params.height = llFriends.getHeight() * (MainActivity.getUsers().get(userID).getFriends().size() + 1);
                    llFriends.setLayoutParams(params);
                } else if (parent.isGroupExpanded(groupPosition)) {
                    params.height = parentHeight;
                    llFriends.setLayoutParams(params);
                }
                return false;
            }
        });


        elvUserFriends.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (MainActivity.getUsers().get(MainActivity.getUsers().get(userID).getFriends().get(childPosition).get("id")).isActive()) {
                    Intent intent = new Intent(UserActivity.this, UserActivity.class);
                    int position = MainActivity.getUsers().get(userID).getFriends().get(childPosition).get("id");
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserActivity.this, "User is offline", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void fromMailToSend(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + MainActivity.getUsers().get(userID).getEmail()));
        startActivity(intent);
    }

    public void fromOhoneToCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + MainActivity.getUsers().get(userID).getPhone()));
        startActivity(intent);
    }

    public void fromLocationToGeo(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" +
                MainActivity.getUsers().get(userID).getLatitude() + "," + MainActivity.getUsers().get(userID).getLongitude()));
        startActivity(intent);
    }

}

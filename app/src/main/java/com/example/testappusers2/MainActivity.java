package com.example.testappusers2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    final String USER_NAME_KEY = "userName";
    final String USER_MAIL_KEY = "userMail";
    final String USER_STATUS_KEY = "userStatus";


    final int ONLINE_IMG = android.R.drawable.presence_online;
    final int OFFLINE_IMG = android.R.drawable.presence_offline;


    static List<User> users;
    ArrayList<Map<String, Object>> usersDataList;
    Map<String, Object> map;
    ListView lvMain;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readData();

        if (users == null) {
            usersCreate();
        }


        usersDataList = new ArrayList<>(users.size());
        for (int i = 0; i < users.size(); i++) {
            map = new HashMap<>();
            map.put(USER_NAME_KEY, users.get(i).getName());
            map.put(USER_MAIL_KEY, users.get(i).getEmail());
            if (users.get(i).isActive()) {
                map.put(USER_STATUS_KEY, ONLINE_IMG);
            } else {
                map.put(USER_STATUS_KEY, OFFLINE_IMG);
            }
            usersDataList.add(map);
        }

        String[] from = {USER_NAME_KEY, USER_MAIL_KEY, USER_STATUS_KEY};
        int[] to = {R.id.tvListName, R.id.tvListMail, R.id.ivListStatus};

        SimpleAdapter adapter = new SimpleAdapter(this, usersDataList, R.layout.activity_list_users, from, to);
        lvMain = findViewById(R.id.lvMain);
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (users.get(position).isActive()) {
                    intent = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User is offline", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    void writeData(List<User> userList) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("data.dat", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userList);
            fos.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readData() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("data.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void usersCreate() {
        InputStream inputStream = null;
        try {
            inputStream = getApplicationContext().getAssets().open("users.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            throw new AssertionError();
        }
        users = ListUsersCreate.getUserList(inputStream);
        writeData(users);
    }

    public static List<User> getUsers() {
        return users;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.miRefresh:
                usersCreate();
                this.recreate();
                break;
            case R.id.miExit:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

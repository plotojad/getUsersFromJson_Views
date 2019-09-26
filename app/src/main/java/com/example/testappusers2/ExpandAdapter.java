package com.example.testappusers2;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandAdapter {

    private Context context;
    private User user;
    private List<User> users;

    final String FRIENDS_GROUP = "friendsNames";
    final String USER_DATA_ITEM_A = "itemName";
    final String USER_DATA_ITEM_C = "itemImage";

    final int ONLINE_IMG = android.R.drawable.presence_online;
    final int OFFLINE_IMG = android.R.drawable.presence_offline;

    ArrayList<Map<String, Object>> groupData;
    Map<String, Object> map;
    ArrayList<Map<String, Object>> childDataItem;
    ArrayList<ArrayList<Map<String, Object>>> childData;

    public ExpandAdapter(Context context, User user, List<User> users) {
        this.context = context;
        this.user = user;
        this.users = users;
    }

    public SimpleExpandableListAdapter friendsToExpandList() {

        groupData = new ArrayList<>();

        map = new HashMap<>();
        map.put(FRIENDS_GROUP, "Friends: ");
        groupData.add(map);

        String[] groupFrom = new String[]{FRIENDS_GROUP};
        int[] groupTo = new int[]{R.id.tvElvParent};


        childData = new ArrayList<>();

        childDataItem = new ArrayList<>();
        for (int i = 0; i < user.getFriends().size(); i++) {
            map = new HashMap<>();
            map.put(USER_DATA_ITEM_A, users.get(user.getFriends().get(i).get("id")).getName());
            if (users.get(user.getFriends().get(i).get("id")).isActive()) {
                map.put(USER_DATA_ITEM_C, ONLINE_IMG);
            } else {
                map.put(USER_DATA_ITEM_C, OFFLINE_IMG);
            }
            childDataItem.add(map);
        }
        childData.add(childDataItem);


        String[] childFrom = new String[]{USER_DATA_ITEM_A, USER_DATA_ITEM_C};
        int[] childTo = new int[]{R.id.tvElvChild, R.id.ivElvStatus};


        return new ImageSimpleExpandableList(context, groupData,
                R.layout.activity_template_parent, groupFrom,
                groupTo, childData, R.layout.activity_template_child,
                childFrom, childTo);
    }
}

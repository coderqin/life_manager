package youth.freecoder.graduationthesis.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import youth.freecoder.graduationthesis.R;
import youth.freecoder.graduationthesis.constant.ConstantValue;
import youth.freecoder.graduationthesis.entity.User;
import youth.freecoder.graduationthesis.server.ServerConnection;
import youth.freecoder.graduationthesis.server.ServerUtils;
import youth.freecoder.graduationthesis.utils.BitmapUtils;

public class FriendsPageActivity extends Fragment {
    private static final String TAG = "FriendsPageActivityTest";
    private View contentView = null;

    private ExpandableListView expandableListView;
    private ImageView addContacts;
    private List<MyGroup> myGroupList;
    private Roster roster;

    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.contacts_view, null);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        addContacts = (ImageView) contentView.findViewById(R.id.add_contacts);
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactsActivity.class));
            }
        });

        if (contentView != null) {
            expandableListView = (ExpandableListView) contentView.findViewById(R.id.friend_list_view);
        }
        myGroupList = new ArrayList<MyGroup>();
        MyGroup myGroup;
        roster = ServerConnection.getConnection().getRoster();
        if (roster != null) {
            /**
             * 分组好友
             */
            Collection<RosterGroup> rosterGroups = roster.getGroups();
            for (RosterGroup group : rosterGroups) {
                myGroup = new MyGroup(
                        group.getName(),
                        ServerUtils.getUserList(group.getEntries(), ServerConnection.getConnection())
                );
                myGroupList.add(myGroup);
            }

            /**
             * 未分组好友
             */
            myGroupList.add(new MyGroup(ConstantValue.UN_GROUP_NAME,
                    ServerUtils.getUnGroupUserList(roster)));
            if (myGroupList.size() > 0) {
                myAdapter = new MyAdapter(myGroupList);
                expandableListView.setAdapter(myAdapter);
                expandableListView.setOnChildClickListener(
                        new MyExpandListOnclickListener(myAdapter));
            }
        }

    }

    /**
     * 自定义数据适配器
     */
    private class MyAdapter extends BaseExpandableListAdapter {
        private List<MyGroup> contactLis;

        public MyAdapter(List<MyGroup> contactList) {
            this.contactLis = contactList;
        }

        @Override
        public int getGroupCount() {
            return contactLis.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return contactLis.get(groupPosition).getUsersList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return contactLis.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return contactLis.get(groupPosition).getUsersList().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition,
                                 boolean isExpanded,
                                 View convertView,
                                 ViewGroup parent) {

            GroupsView groupsView = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity().
                        getApplicationContext()).inflate(R.layout.groups_view, null);
                groupsView = new GroupsView(
                        (ImageView) convertView.findViewById(R.id.group_photo),
                        (TextView) convertView.findViewById(R.id.group_name)
                );
                convertView.setTag(groupsView);
            } else {
                groupsView = (GroupsView) convertView.getTag();
            }

            groupsView.getGroup_photo().setImageBitmap(
                    BitmapUtils.getScaleBitmap(getActivity(),
                            R.drawable.group_photo));
            groupsView.getGroup_name().setText(contactLis.get(groupPosition).getGroup_name());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition,
                                 int childPosition,
                                 boolean isLastChild,
                                 View convertView,
                                 ViewGroup parent) {
            GroupDetailView groupDetailView = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity().
                        getApplicationContext()).inflate(R.layout.group_contacts_view, null);
                groupDetailView = new GroupDetailView(
                        (ImageView) convertView.findViewById(R.id.friend_photo),
                        (TextView) convertView.findViewById(R.id.friend_name),
                        (TextView) convertView.findViewById(R.id.friend_status)
                );
                convertView.setTag(groupDetailView);
            } else {
                groupDetailView = (GroupDetailView) convertView.getTag();
            }
            groupDetailView.getFriendPhoto().setImageBitmap(
                    BitmapUtils.getScaleBitmap(getActivity(),
                            R.drawable.friends));
            groupDetailView.getFriendName().setText(
                    contactLis.get(groupPosition).getUsersList().get(childPosition)
                            .getUsername());

            groupDetailView.getFriendStatus().setText(
                    contactLis.get(groupPosition).getUsersList().get(childPosition)
                            .getStatus() == 1 ? "在线" : "离线");
            groupDetailView.getFriendStatus().setTextColor(
                    contactLis.get(groupPosition).getUsersList().get(childPosition)
                            .getStatus() == 0 ? Color.GRAY : Color.GREEN);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    /**
     * 组视图对象
     */
    private class GroupsView {
        private ImageView group_photo;
        private TextView group_name;

        private GroupsView(ImageView group_photo, TextView group_name) {
            this.group_photo = group_photo;
            this.group_name = group_name;
        }

        public TextView getGroup_name() {
            return group_name;
        }

        public void setGroup_name(TextView group_name) {
            this.group_name = group_name;
        }

        public ImageView getGroup_photo() {
            return group_photo;
        }

        public void setGroup_photo(ImageView group_photo) {
            this.group_photo = group_photo;
        }
    }

    /**
     * 组详情视图对象
     */
    private class GroupDetailView {
        private ImageView friendPhoto;
        private TextView friendName;
        private TextView friendStatus;

        private GroupDetailView(ImageView friendPhoto, TextView friendName, TextView friendStatus) {
            this.friendPhoto = friendPhoto;
            this.friendName = friendName;
            this.friendStatus = friendStatus;
        }

        public ImageView getFriendPhoto() {
            return friendPhoto;
        }

        public void setFriendPhoto(ImageView friendPhoto) {
            this.friendPhoto = friendPhoto;
        }

        public TextView getFriendName() {
            return friendName;
        }

        public void setFriendName(TextView friendName) {
            this.friendName = friendName;
        }

        public TextView getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(TextView friendStatus) {
            this.friendStatus = friendStatus;
        }
    }

    /**
     * 自定义分组对象
     */
    private class MyGroup {
        private String group_name;
        private List<User> usersList;

        private MyGroup(String group_name, List<User> usersList) {
            this.group_name = group_name;
            this.usersList = usersList;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public List<User> getUsersList() {
            return usersList;
        }

        public void setUsersList(List<User> usersList) {
            this.usersList = usersList;
        }
    }


    private class MyExpandListOnclickListener implements ExpandableListView.OnChildClickListener {
        private MyAdapter myAdapter;

        private MyExpandListOnclickListener(MyAdapter myAdapter) {
            this.myAdapter = myAdapter;
        }

        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            TextView clickFriendName = (TextView) v.findViewById(R.id.friend_name);
            if (clickFriendName != null) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("tochatUser", clickFriendName.getText().toString());
                startActivity(intent);
            }
            return false;
        }
    }

}
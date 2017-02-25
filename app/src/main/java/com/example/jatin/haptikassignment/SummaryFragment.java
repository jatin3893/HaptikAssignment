package com.example.jatin.haptikassignment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryFragment extends Fragment {

    private MessageManager mMessageManager = null;
    private Map<String, List<Message>> mUserMessageMap = null;

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    public void setMessageManager(MessageManager msgManager) {
        this.mMessageManager = msgManager;

        // organize data according to the Users.
        this.mUserMessageMap = new HashMap<>();
        for (Message msg: this.mMessageManager.getMessageList()) {
            if (!mUserMessageMap.containsKey(msg.getUsername()))
                mUserMessageMap.put(msg.getUsername(), new ArrayList<Message>());

            List<Message> msgList = mUserMessageMap.get(msg.getUsername());
            msgList.add(msg);
        }

        // Add UserViews to the Summary Fragment
        LinearLayout userSummaryLayout = (LinearLayout) getView().findViewById(R.id.summary_users);
        userSummaryLayout.removeAllViews();
        for (String username: this.mUserMessageMap.keySet()) {
            userSummaryLayout.addView(new UserView(User.getUser(username),
                                        this.mUserMessageMap.get(username),
                                        this.getContext()));
        }
    }
}

class UserView extends RelativeLayout implements FavoriteToggleListener {
    UserView(User user, List<Message> msgList, Context context) {
        super(context);

        // Register listeners for favorite messages
        for (Message msg: msgList)
            msg.addFavoriteToggleListener(this);

        // init UI
        inflate(context, R.layout.user, this);

        TextView username = (TextView) findViewById(R.id.user_username);
        TextView name = (TextView) findViewById(R.id.user_name);
        TextView messages = (TextView) findViewById(R.id.user_messages);
        TextView favorite = (TextView) findViewById(R.id.user_favorite);
        ImageView userImage = (ImageView) findViewById(R.id.user_image);

        username.setText(user.getUsername());
        name.setText(user.getName());
        messages.setText(String.valueOf(msgList.size()));
        favorite.setText(String.valueOf(0));
        ImageUtils.imageFromUrl(userImage, user.getImageUrl());
    }

    @Override
    public void onFavoriteToggled(boolean value) {
        // Increment/Decrement count of favorite messages for the current user.
        // If needed for any other purposes, maintain the count in the User object.
        TextView favorite = (TextView) findViewById(R.id.user_favorite);
        int current = Integer.parseInt(favorite.getText().toString());
        if (value)
            favorite.setText(String.valueOf(current + 1));
        else
            favorite.setText(String.valueOf(current - 1));
    }
}
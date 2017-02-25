package com.example.jatin.haptikassignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jatin on 24-02-2017.
 */

class MessageManager {
    List<Message> mMessageList = null;
    MessageManager(JSONObject jsonObj) {
        mMessageList = new ArrayList<>();
        try {
            JSONArray messageList = jsonObj.getJSONArray("messages");
            for (int i = 0; i < messageList.length(); i++)
                mMessageList.add(new Message(messageList.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    List<Message> getMessageList() {
        return mMessageList;
    }
}

class Message {
    private String mBody;
    private Date mTimestamp;
    private User mUser;
    private boolean mFavorite;
    private List<FavoriteToggleListener> mFavoriteToggleListenerList;

    Message(JSONObject jsonMsg) {
        try {
            mBody = jsonMsg.getString("body");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            mTimestamp = df.parse(jsonMsg.getString("message-time"));
            mFavorite = false;
            mUser = User.getUser(jsonMsg.getString("username"),
                        jsonMsg.getString("Name"),
                        jsonMsg.getString("image-url"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        mFavoriteToggleListenerList = new ArrayList<>();
    }

    String getBody() {
        return mBody;
    }

    String getTimestamp() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return  df.format(mTimestamp);
    }

    String getName() {
        return mUser.getName();
    }

    String getUsername() {
        return mUser.getUsername();
    }

    String getImageUrl() {
        return mUser.getImageUrl();
    }

    boolean isFavorite() {
        return mFavorite;
    }

    void toggleFavorite() {
        mFavorite = !mFavorite;

        for (FavoriteToggleListener listener: mFavoriteToggleListenerList) {
            listener.onFavoriteToggled(mFavorite);
        }
    }

    void addFavoriteToggleListener(FavoriteToggleListener listener) {
        mFavoriteToggleListenerList.add(listener);
    }
}

class User {
    private String mUsername;
    private String mName;
    private String mImageUrl;

    static HashMap<String, User> sUserCache = new HashMap<>();

    static User getUser(String username, String name, String imageUrl) {
        if (!sUserCache.containsKey(username))
            sUserCache.put(username, new User(username, name, imageUrl));
        return sUserCache.get(username);
    }

    static User getUser(String username) {
        if (sUserCache.containsKey(username))
            return sUserCache.get(username);
        return null;
    }

    private User(String username, String name, String imageUrl) {
        this.mUsername = username;
        this.mName = name;
        this.mImageUrl = imageUrl;
    }

    String getName() { return this.mName; }
    String getImageUrl() { return this.mImageUrl; }
    String getUsername() { return this.mUsername; }

}

interface FavoriteToggleListener {
    void onFavoriteToggled(boolean value);
}
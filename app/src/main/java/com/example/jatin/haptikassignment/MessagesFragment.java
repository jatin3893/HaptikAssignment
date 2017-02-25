package com.example.jatin.haptikassignment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesFragment extends Fragment {

    private MessageManager mMessageManager = null;
    private LinearLayout mConversationLayout = null;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setMessageManager(MessageManager msgManager) {
        this.mMessageManager = msgManager;

        mConversationLayout = (LinearLayout) getView().findViewById(R.id.conversationLayout);
        mConversationLayout.removeAllViews();
        for (Message msg: this.mMessageManager.getMessageList()) {
            mConversationLayout.addView(new MessageView(msg, this.getContext()));
        }
    }
}

class MessageView extends RelativeLayout {
    Message mMessage;
    public MessageView(Message msg, Context context) {
        super(context);
        this.mMessage = msg;

        // init UI
        inflate(context, R.layout.message, this);

        TextView msgBody = (TextView) findViewById(R.id.message_body);
        TextView msgTimestamp = (TextView) findViewById(R.id.message_timestamp);
        TextView msgName = (TextView) findViewById(R.id.message_name);
        ImageView msgImage = (ImageView) findViewById(R.id.message_image);

        msgBody.setText(msg.getBody());
        msgTimestamp.setText(msg.getTimestamp());
        msgName.setText(msg.getName());
        ImageUtils.imageFromUrl(msgImage, msg.getImageUrl());

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MessageView.this.toggleFavorite();
                return false;
            }
        });
    }

    private void toggleFavorite() {
        this.mMessage.toggleFavorite();
        TextView msgBody = (TextView) findViewById(R.id.message_body);
        if (this.mMessage.isFavorite()) {
            msgBody.getBackground().setColorFilter(ContextCompat.getColor(this.getContext(), R.color.colorAccent), PorterDuff.Mode.SRC);
        } else {
            msgBody.getBackground().setColorFilter(ContextCompat.getColor(this.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC);
        }
    }
}
package com.example.whatsappclone.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Group_Chat;
import com.example.whatsappclone.Models.Message;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatController extends RecyclerView.Adapter {

    // Define instance variables
    ArrayList<Message> messagesModel;
    Context context;
    String recId;


    public ChatController(ArrayList<Message> messagesModel, Context context) {
        this.messagesModel = messagesModel;
        this.context = context;
    }

    public ChatController(ArrayList<Message> messagesModel, Context context, String recId) {
        this.messagesModel = messagesModel;
        this.context = context;
        this.recId = recId;
    }

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    // onCreateViewHolder is called when the RecyclerView needs a new view holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            // Inflate the layout for a message sent by the current user
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            // Return a new SenderViewHolder with the inflated view
            return new SenderViewHolder(view);
        } else {
            // Inflate the layout for a message sent by another user
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciver, parent, false);
            // Return a new ReceiverViewHolder with the inflated view
            return new ReceiverViewHolder(view);
        }
    }

    // getItemViewType determines the view type for a given position in the RecyclerView
    @Override
    public int getItemViewType(int position) {
        if (messagesModel.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())) {
            // If the message was sent by the current user, return the SENDER_VIEW_TYPE
            return SENDER_VIEW_TYPE;
        } else {
            // Otherwise, return the RECEIVER_VIEW_TYPE
            return RECEIVER_VIEW_TYPE;
        }
    }

    // onBindViewHolder is called when a new message needs to be displayed in a view holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messagesModel.get(position);





        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete ?")
                        .setMessage("Sure You Want To Delete !?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("Chats")
                                        .child(senderRoom)
                                        .child(message.getMessageId())
                                        .setValue(null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        if (holder.getClass() == SenderViewHolder.class) {
            Date date = new Date(message.getTime());
            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
            String strDate = format.format(date);
            ((SenderViewHolder) holder).senderTime.setText(strDate.toString());
            // If the message was sent by the current user, set the text of the senderMsg TextView in the SenderViewHolder
            ((SenderViewHolder) holder).senderMsg.setText(message.getMessage());
        } else if (holder.getClass() == ReceiverViewHolder.class) {
            Date date = new Date(message.getTime());
            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
            String strDate = format.format(date);
            ((ReceiverViewHolder) holder).receiverTime.setText(strDate.toString());
            // Otherwise, set the text of the receiverMsg TextView in the ReceiverViewHolder
            ((ReceiverViewHolder) holder).receiverMsg.setText(message.getMessage());
        }

    }

    // getItemCount returns the number of messages in the ArrayList
    @Override
    public int getItemCount() {
        return messagesModel.size();
    }

    // Define a custom ViewHolder for messages sent by other users till here Ok
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    // Define a custom ViewHolder for messages sent by the current user
    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}

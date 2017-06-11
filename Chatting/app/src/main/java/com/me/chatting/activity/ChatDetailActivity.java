package com.me.chatting.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.me.chatting.R;
import com.me.chatting.constant.UserInfo;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by sjk on 17-6-10.
 */

public class ChatDetailActivity extends BaseActivity {

    @BindView(R.id.message_list_view)
    public ListView messageLv;
    @BindView(R.id.input)
    public EditText inputEt;
    @BindView(R.id.send)
    public Button sendBtn;

    private List<Message> messages = new ArrayList<>();
    private ChatMessageAdapter adapter;

    private String toJidStr = "james@vitalemon";  // 对方账号
    private EntityBareJid toEntityBareJid;
    private ChatManager chatManager;
    private Chat chat;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ChatMessageAdapter(this, R.layout.chat_detail_item, messages);
        messageLv.setAdapter(adapter);

        chatManager = ChatManager.getInstanceFor(LoginActivity.conn);
        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, final Message message, Chat chat) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addItem(message);
                    }
                });
            }
        });
        chatManager.addOutgoingListener(new OutgoingChatMessageListener() {
            @Override
            public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {

            }
        });

        try {
            toEntityBareJid = JidCreate.entityBareFrom(toJidStr);
            chat = chatManager.chatWith(toEntityBareJid);
        } catch (XmppStringprepException xse) {
            xse.printStackTrace();
        }
    }

    @OnClick(R.id.send) void attemptSend() {
        String content = inputEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        try {
            chat.send(content);

            Message message = new Message(toJidStr, content);
            message.addSubject("from", UserInfo.userAccount);   // 标记是当前用户发出的
            addItem(message);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }

    private void addItem(Message newMessage) {
        messages.add(newMessage);
        adapter.notifyDataSetChanged();
        messageLv.smoothScrollToPosition(messages.size() - 1);
    }

    private void removeLastItem() {
        messages.remove(messages.size() - 1);
        adapter.notifyDataSetChanged();
        messageLv.smoothScrollToPosition(messages.size() - 1);
    }

    @Override
    public void onBackPressed() {

        /* 保证应用不会直接退出 */

        moveTaskToBack(true);
    }

    /*聊天消息列表的adapter*/
    private static class ChatMessageAdapter extends ArrayAdapter<Message> {

        private Context context;
        private int resourceId;
        private List<Message> messages;

        public ChatMessageAdapter(Context context, int resourceId, List<Message> messages) {
            super(context, resourceId, messages);
            this.context = context;
            this.resourceId = resourceId;
            this.messages = messages;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Message message = messages.get(position);

            /*取得View中的控件*/

            View view = null;
            ViewHolder viewHolder = null;
            if (convertView == null) {
                view = LayoutInflater.from(this.context).inflate(this.resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.leftItem = (CardView) view.findViewById(R.id.left_item);
                viewHolder.rightItem = (CardView) view.findViewById(R.id.right_item);
                viewHolder.leftContent = (TextView) view.findViewById(R.id.left_content);
                viewHolder.rightContent = (TextView) view.findViewById(R.id.right_content);
                view.setTag(viewHolder);
            } else {    // convertView是缓存的view，可以直接重用
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }


            /*判断消息是发出去的，还是收到的*/

            // Use JivePropertiesManager?
            if (message.getSubject("from") != null && UserInfo.userAccount.equals(message.getSubject("from"))) {  // 如果是我发出的消息
                viewHolder.leftItem.setVisibility(View.GONE);
                viewHolder.rightItem.setVisibility(View.VISIBLE);
                viewHolder.rightContent.setText(message.getBody());
            } else {    // 如果是对方发来的消息
                viewHolder.leftItem.setVisibility(View.VISIBLE);
                viewHolder.rightItem.setVisibility(View.GONE);
                viewHolder.leftContent.setText(message.getBody());
            }

            return view;
        }


        // 设置ViewHolder放进缓存View中，也可以被重用
        // 不用每次都findViewById（解析xml）
        static class ViewHolder {
            CardView leftItem;
            CardView rightItem;
            TextView leftContent;
            TextView rightContent;
        }
    }
}

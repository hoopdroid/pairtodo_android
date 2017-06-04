package com.pairtodopremium.ui.main.chat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.chat.Message;
import com.pairtodopremium.data.response.userData.UserInfo;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.network.api.ApiManager;
import com.pairtodopremium.network.api.job.JobMessageService;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.ui.main.chat.models.DefaultUser;
import com.pairtodopremium.ui.main.chat.views.messages.MessageInput;
import com.pairtodopremium.ui.main.chat.views.messages.MessagesList;
import com.pairtodopremium.ui.main.chat.views.messages.MessagesListAdapter;
import com.pairtodopremium.db.PrefRepository;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;

import java.util.List;

import butterknife.Bind;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;

public class ChatActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;
    private MessageInput input;
    private String userToken;
    private String token;

    private DefaultUser user;
    private DefaultUser couple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messages_list_default);

        //removeBackgroundJob();

        initChatMembers();

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.cancelAllRequests();
                onBackPressed();
            }
        });

        token = PrefRepository.getToken(this);

        String chat = getString(R.string.chat);
        mToolbar.setTitle(chat + " " + couple.getName());

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        initMessagesAdapter();

        input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(final CharSequence input) {
                if (isNetworkAvailable())
                    DataService.init().sendMessage(new DataService.onMessageTask() {
                        @Override
                        public void onMessageResult() {
                            //Toast.makeText(ChatActivity.this, "message added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onMessageError() {
                            //Toast.makeText(ChatActivity.this, "message not delievered", Toast.LENGTH_SHORT).show();
                            //int a = 5;

                        }
                    }, input.toString().trim(), userToken);

                else
                    Toast.makeText(ChatActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    private void initChatMembers() {
        UserInfo info = SettingsRepository.getUserInfo(realm);
        user = new DefaultUser(info.getId(), info.getName(), info.getPhoto(), true);
        couple = new DefaultUser(info.getPairId(), info.getPairName(), info.getPairPhoto(), true);
    }

    private List<Message> linkMessagesToMembers(List<Message> messages) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).fromId().equals(user.getId()))
                messages.get(i).setUserMessage(user);
            else messages.get(i).setUserMessage(couple);
        }
        return messages;
    }

    @Override
    public void onBackPressed() {
        removePeriodicJob();
        finish();
    }

    private void initMessagesAdapter() {
        userToken = PrefRepository.getToken(this);
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(ChatActivity.this).load(url).into(imageView);
            }
        };

        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setIncomingLayout(R.layout.item_incoming_msg);
        holdersConfig.setOutcomingLayout(R.layout.item_outcoming_msg);
        adapter = new MessagesListAdapter<>(this, user.getId(), holdersConfig, imageLoader);

        messagesList.setAdapter(adapter);

        loadMessagesArchive();
    }

    private void loadMessagesArchive() {
        if (isNetworkAvailable())
            DataService.init().getMessagesArchive(new DataService.onMessageGetTask() {
                @Override
                public void onMessageResult(List<Message> messages) {
                    List<Message> messagesLinked = linkMessagesToMembers(messages);
                    if (messages.size() > 0) {
                        adapter.addToEnd(messagesLinked, true);
                        cacheManager.addMessagesToCache(messagesLinked);
                    }
                    loadNewMessages();
                }

                @Override
                public void onMessageError(String message) {
                    //Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }, userToken);
        else {
            adapter.addToEnd(linkMessagesToMembers(cacheManager.getMessagesArchive()), true);
            Toast.makeText(ChatActivity.this, R.string.network_error_local, Toast.LENGTH_SHORT).show();
        }

//        List<Message> messages = cacheManager.getMessagesArchive();
//        if (messages.size() > 0)
//            adapter.addToEnd(messages, true);

        //loadNewMessages();

    }

    private void loadNewMessages() {
        Handler handler = new Handler();
        final SmartScheduler jobScheduler = SmartScheduler.getInstance(ChatActivity.this);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Job job = JobMessageService.getInstance().
                        getNewMessages(new SmartScheduler.JobScheduledCallback() {
                            @Override
                            public void onJobScheduled(Context context, final Job job) {
                                if (job != null) {
                                    Log.d("GETTING MESSAGES", "FROM ACTIVE with 1 sec");
                                    DataService.init().getNewMessages(new DataService.onMessageGetTask() {
                                        @Override
                                        public void onMessageResult(List<Message> messages) {
                                            if (messages.size() > 0) {
                                                List<Message> messagesLinked = linkMessagesToMembers(messages);
                                                cacheManager.addMessagesToCache(messagesLinked);
                                                PrefRepository.setLastMessage(ChatActivity.this,
                                                        messagesLinked.get(messagesLinked.size() - 1).
                                                                getUserMessage().getId());
                                                for (int i = 0; i < messagesLinked.size(); i++) {
                                                    adapter.addToStart(messagesLinked.get(i), true);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onMessageError(String message) {
                                            //Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }, token);
                                }
                            }
                        });
                jobScheduler.addJob(job);
            }
        });

    }

    public void removeBackgroundJob() {

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JobMessageService.JOB_MESSAGE_BACKGROUND_ID)) {
            //Toast.makeText(this, "No job exists with JobID: " + JobMessageService.JOB_MESSAGE_ACTIVE_ID, Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobScheduler.removeJob(JobMessageService.JOB_MESSAGE_BACKGROUND_ID)) {
            //Toast.makeText(this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removePeriodicJob() {

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JobMessageService.JOB_MESSAGE_ACTIVE_ID)) {
            //Toast.makeText(ChatActivity.this, "No job exists with JobID: " + JobMessageService.JOB_MESSAGE_ACTIVE_ID, Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobScheduler.removeJob(JobMessageService.JOB_MESSAGE_ACTIVE_ID)) {
            //Toast.makeText(ChatActivity.this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d("GETTING MESSAGES", "ON STOP AND OPEN BACKGROUND");
        removePeriodicJob();
        super.onStop();
    }
}

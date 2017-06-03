package com.pairtodobeta.ui.main.tasks;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pairtodobeta.PairTodoApplication;
import com.pairtodobeta.R;
import com.pairtodobeta.data.DataService;
import com.pairtodobeta.data.entities.Task;
import com.pairtodobeta.data.response.chat.Message;
import com.pairtodobeta.data.response.userData.UserInfo;
import com.pairtodobeta.data.settings.SettingsRepository;
import com.pairtodobeta.network.api.ApiManager;
import com.pairtodobeta.network.api.job.JobMessageService;
import com.pairtodobeta.ui.BaseActivity;
import com.pairtodobeta.ui.main.chat.ChatSamplesListAdapter;
import com.pairtodobeta.ui.main.chat.models.DefaultUser;
import com.pairtodobeta.ui.main.chat.views.messages.MessageInput;
import com.pairtodobeta.ui.main.chat.views.messages.MessagesList;
import com.pairtodobeta.ui.main.chat.views.messages.MessagesListAdapter;
import com.pairtodobeta.db.PrefRepository;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;


public class JobChatActivity extends BaseActivity {
    private static final String ARG_TYPE = "type";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;
    private MessageInput input;
    private String userToken;
    private int selectionCount;
    private Menu menu;
    private ChatSamplesListAdapter.ChatSample.Type type;
    private String token;

    private DefaultUser user;
    private DefaultUser couple;

    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messages_list_default);

        initChatMembers();

        mTask = getIntent().getParcelableExtra("Job");

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiManager.cancelAllRequests();
                onBackPressed();
            }
        });

        token = PrefRepository.getToken(this);

        mToolbar.setTitle(R.string.task_discussion);

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        initMessagesAdapter();

        input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(final CharSequence input) {
                DataService.init().sendJobMessage(new DataService.onMessageTask() {
                    @Override
                    public void onMessageResult() {
                        int a = 5;
                    }

                    @Override
                    public void onMessageError() {
                        int a = 5;
                    }
                }, token, mTask.getId(), input.toString().trim());
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
            {
                if (messages.get(i).fromId().equals(user.getId()))
                    messages.get(i).setUserMessage(user);
                else messages.get(i).setUserMessage(couple);
            }
        }
        return messages;
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            adapter.unselectAllItems();
        }
        finish();
    }

    private void initMessagesAdapter() {
        userToken = PrefRepository.getToken(this);
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(JobChatActivity.this).load(url).into(imageView);
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
        DataService.init().getJobMessages(new DataService.onMessageGetTask() {
            @Override
            public void onMessageResult(List<Message> messages) {
                List<Message> tempList = new ArrayList<Message>();

                for (int i = 0; i < messages.size(); i++) {
                    if (Objects.equals(messages.get(i).jobId(), mTask.getId()))
                        tempList.add(messages.get(i));
                }
                    tempList.add(0 ,Message.create("", "", "", "", "", mTask.getTitle(), mTask.getCreateDate()));
                    adapter.addToEnd(linkMessagesToMembers(tempList), true);
                    loadNewMessages();

            }

            @Override
            public void onMessageError(String message) {
                int a = 5;

            }
        }, token, token);
    }

    private void loadNewMessages() {
        Handler handler = new Handler();
        final SmartScheduler jobScheduler = SmartScheduler.getInstance(JobChatActivity.this);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Job job = JobMessageService.getInstance().
                        getNewMessages(new SmartScheduler.JobScheduledCallback() {
                            @Override
                            public void onJobScheduled(Context context, final Job job) {
                                if (job != null) {
                                    Log.d("GETTING MESSAGES", "FROM ACTIVE with 3 sec");
                                    DataService.init().getNewJobMessages(new DataService.onMessageGetTask() {
                                        @Override
                                        public void onMessageResult(List<Message> messages) {
                                            if (messages.size() > 0) {
                                                List<Message> messagesLinked = linkMessagesToMembers(messages);
                                                for (int i = 0; i < messages.size(); i++) {
                                                    if (messagesLinked.get(i).jobId().equals(mTask.getId()))
                                                        adapter.addToStart(messagesLinked.get(i), true);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onMessageError(String message) {
                                            Toast.makeText(JobChatActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    }, token);
                                }
                            }
                        });
                jobScheduler.addJob(job);
            }
        });

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
        PairTodoApplication.checkPulseInBackground(this, token);
        super.onStop();
    }
}

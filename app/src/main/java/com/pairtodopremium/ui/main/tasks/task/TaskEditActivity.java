package com.pairtodopremium.ui.main.tasks.task;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.entities.Task;
import com.pairtodopremium.data.response.tasks.ChangeTaskResponse;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.ui.BaseActivity;
import com.squareup.picasso.Picasso;

public class TaskEditActivity extends BaseActivity {
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.taskName) EditText mTaskName;
  @Bind(R.id.tv_task_descr) EditText mTaskDesc;

  @Bind(R.id.executorImage) ImageView mExecutorImage;
  @Bind(R.id.fab) FloatingActionButton mMarkTaskBtn;

  private Task mTask;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_edit);

    mTask = getIntent().getParcelableExtra("Task");
    setSupportActionBar(mToolbar);

    initViewElements();
  }

  private void initViewElements() {

    mToolbar.setTitle("");
    mToolbar.setNavigationIcon(R.drawable.ic_cancel24);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    mToolbar.inflateMenu(R.menu.menu_task);

    mTaskName.setText(mTask.getTitle());
    if (mTask.getDescription().equals("") && !mTask.getList().equals("")) {
      mTaskDesc.setText(mTask.getList());
    } else {
      mTaskDesc.setText(mTask.getDescription());
    }

    if (mTask.getExecutorId().equals(SettingsRepository.getUserInfo(realm).getId())) {
      String photo = SettingsRepository.getUserInfo(realm).getPhoto();
      Picasso.with(this).load(photo).into(mExecutorImage);
    } else {
      String photo = SettingsRepository.getUserInfo(realm).getPairPhoto();
      Picasso.with(this).load(photo).into(mExecutorImage);
    }

    mMarkTaskBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        int executor;
        String pairId = SettingsRepository.getUserInfo(realm).getPairId();
        String id = SettingsRepository.getUserInfo(realm).getId();

        if (mTask.getExecutorId().equals(pairId)) {
          executor = 1;
        } else {
          executor = 0;
        }

        String desc = mTaskDesc.getText().toString().trim();
        String list = mTask.getList();
        if (mTask.getDescription().equals("") && !mTask.getList().equals("")) {
          list = mTaskDesc.getText().toString().trim();
          desc = "";
        }

        Task tempTask =
            new Task(mTaskName.getText().toString().trim(), mTask.getTermDate(), desc, list, "",
                "0", String.valueOf(executor));

        DataService.init().editTask(new DataService.onChangeTask() {
          @Override public void onChangeTaskResult(ChangeTaskResponse response) {
            setResult(RESULT_OK);
            finish();
          }

          @Override public void onChangeTaskError() {
            // TODO Show error view
          }
        }, tempTask, mTask.getId(), PrefRepository.getToken(TaskEditActivity.this));
      }
    });
  }
}

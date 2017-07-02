package com.pairtodopremium.ui.main.tasks.task;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.entities.Task;
import com.pairtodopremium.data.response.tasks.ChangeTaskResponse;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.utils.Constants;
import com.squareup.picasso.Picasso;

public class TaskActivity extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.taskName) TextView mTaskName;
  @Bind(R.id.tv_task_descr) TextView mTaskDesc;
  @Bind(R.id.dut_date_text) RelativeTimeTextView mDueDate;
  @Bind(R.id.executorImage) ImageView mExecutorImage;
  @Bind(R.id.fab) FloatingActionButton mMarkTaskBtn;
  @Bind(R.id.jobChatBtn) Button mJobChatBtn;
  @Bind(R.id.image_task) ImageView mTaskImage;

  private Task mTask;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task);

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
    long time = Long.parseLong(mTask.getTermDate()) * 1000;
    if (time == 0) {
      mDueDate.setText(R.string.no_time_limit);
    } else {
      mDueDate.setReferenceTime(time);
    }

    if (mTask.getExecutorId().equals(SettingsRepository.getUserInfo(realm).getId())) {
      String photo = SettingsRepository.getUserInfo(realm).getPhoto();
      Picasso.with(this).load(photo).into(mExecutorImage);
    } else {
      String photo = SettingsRepository.getUserInfo(realm).getPairPhoto();
      Picasso.with(this).load(photo).into(mExecutorImage);
    }

    if (!mTask.getImage().equals("")) {
      mTaskImage.setVisibility(View.VISIBLE);
      Picasso.with(this).load(mTask.getImage()).into(mTaskImage);
      mTaskImage.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          ActivityRouter.startPhotoActivity(TaskActivity.this, mTask.getImage(), mTaskImage,
              Constants.IMAGE_TRANSITION);
        }
      });
    }

    mMarkTaskBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        DataService.init().markTask(new DataService.onChangeTask() {
          @Override public void onChangeTaskResult(ChangeTaskResponse response) {
            setResult(RESULT_OK);
            finish();
          }

          @Override public void onChangeTaskError() {
            // TODO Show error view
          }
        }, PrefRepository.getToken(TaskActivity.this), mTask.getId());
      }
    });

    mJobChatBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startJobChatActivity(TaskActivity.this, mTask);
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_task, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_edit:
        setResult(RESULT_OK);
        finish();
        ActivityRouter.startEditTaskActivity(TaskActivity.this, mTask, Constants.CHANGE_TASK);
        return true;
      case R.id.action_remove:
        DataService.init().removeTask(new DataService.onChangeTask() {
          @Override public void onChangeTaskResult(ChangeTaskResponse response) {
            setResult(RESULT_OK);
            finish();
          }

          @Override public void onChangeTaskError() {
            // TODO Show error view
          }
        }, PrefRepository.getToken(TaskActivity.this), mTask.getId());
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}

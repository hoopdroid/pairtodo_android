package com.pairtodopremium.ui.main.tasks.new_task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.entities.Task;
import com.pairtodopremium.data.response.signup.BasicResponse;
import com.pairtodopremium.data.response.tasks.UploadImageData;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.db.PrefRepository;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.pairtodopremium.utils.FileUtil.reduceFile;

public class AddTaskActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.noteTitle)
    EditText noteTitleEdit;
    @Bind(R.id.note_description)
    EditText noteDescriptionEdit;
    @Bind(R.id.dut_date_text)
    TextView dueTextDate;
    @Bind(R.id.discrete_slider)
    DiscreteSlider discreteSlider;
    @Bind(R.id.tick_mark_labels_rl)
    RelativeLayout tickMarkLabelsRelativeLayout;
    @Bind(R.id.yourselfTask)
    TextView yourSelfTask;
    @Bind(R.id.coupleTask)
    TextView coupleTask;

    @Bind(R.id.show_subtask_view)
    ImageView showSubTaskView;
    @Bind(R.id.subtask_view)
    ViewGroup subtaskView;

    @Bind(R.id.add_list_note_item)
    ImageView listItemAddBtn;
    @Bind(R.id.list_note_item)
    EditText listNoteEdit;
    @Bind(R.id.currentList)
    TextView allSubTasksView;

    @Bind(R.id.photoTaskImage)
    ImageView photoTaskImage;
    @Bind(R.id.progressImage)
    ProgressBar progressImage;
    String pickedPhoto = "";
    private List<String> subTasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initViewElements();

        subTasksList = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        if (progressImage.getVisibility() == View.VISIBLE)
            Toast.makeText(this, "Image loading. Please wait for result!", Toast.LENGTH_SHORT).show();
        else
            super.onBackPressed();
    }

    private void initViewElements() {
        coupleTask.setSelected(true);

        photoTaskImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openGallery(AddTaskActivity.this, 0);
            }
        });

        showSubTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDescriptionEdit.setVisibility(View.GONE);
                subtaskView.setVisibility(View.VISIBLE);
                showSubTaskView.setVisibility(View.GONE);
            }
        });

        listItemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subTasksList.add(listNoteEdit.getText().toString().trim());
                listNoteEdit.setText("");
                allSubTasksView.setText(Arrays.toString(subTasksList.toArray()));
            }
        });

        coupleTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourSelfTask.setSelected(false);
                coupleTask.setSelected(true);
                coupleTask.setTextColor(getResources().getColor(R.color.colorAccent));
                yourSelfTask.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        yourSelfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coupleTask.setSelected(false);
                yourSelfTask.setSelected(true);
                yourSelfTask.setTextColor(getResources().getColor(R.color.colorAccent));
                coupleTask.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        mToolbar.setTitle(R.string.new_task);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dueDate = "0";
                if (dueTextDate.getText().toString().trim().equals(getString(R.string.today)))
                    dueDate = String.valueOf(System.currentTimeMillis() / 1000);
                if (dueTextDate.getText().toString().equals(getString(R.string.tommorow)))
                    dueDate = String.valueOf(System.currentTimeMillis() / 1000 + 86400);
                if (dueTextDate.getText().toString().equals(getString(R.string.no_time_limit)))
                    dueDate = "0";
                if (!dueTextDate.getText().toString().trim().equals(getString(R.string.today))
                        && !dueTextDate.getText().toString().equals(getString(R.string.tommorow)) &&
                        !dueTextDate.getText().toString().equals(getString(R.string.no_time_limit))) {
                    Date date;
                    DateFormat dateFormat = new SimpleDateFormat("dd.M.yyyy");
                    try {
                        date = dateFormat.parse(dueTextDate.getText().toString());
                        dueDate = String.valueOf((long) date.getTime() / 1000);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                String description = noteDescriptionEdit.getText().toString().trim();
                String list = "";
                if (subTasksList.size() > 0) {
                    list = subTasksList.toString();
                    list = list.substring(1, list.length() - 1);
                    description = "";
                }

                Task task = new Task(noteTitleEdit.getText().toString().trim(),
                        dueDate, description,
                        list, pickedPhoto, "0", String.valueOf(coupleTask.isSelected() ? 1 : 0));
                if (progressImage.getVisibility() != View.VISIBLE) {
                    DataService.init().addTask(new DataService.onAddTask() {
                                                   @Override
                                                   public void onAddTaskResult(BasicResponse response) {
                                                       setResult(RESULT_OK);
                                                       finish();
                                                   }

                                                   @Override
                                                   public void onAddTaskError() {
                                                       // TODO Show error
                                                   }
                                               }, task,
                            PrefRepository.getToken(AddTaskActivity.this));
                } else
                    Toast.makeText(AddTaskActivity.this,
                            "Image loading. Please wait for result and try again!", Toast.LENGTH_SHORT).show();
            }
        });

        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                int childCount = tickMarkLabelsRelativeLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    TextView tv = (TextView) tickMarkLabelsRelativeLayout.getChildAt(i);
                    if (i == position)
                        tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    else
                        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                switch (position) {
                    case 0:
                        dueTextDate.setText(getString(R.string.no_time_limit));
                        break;
                    case 1:
                        dueTextDate.setText(getString(R.string.today));
                        break;
                    case 2:
                        dueTextDate.setText(getString(R.string.tommorow));
                        break;
                    case 3:
                        openDatePicker();
                        dueTextDate.setText(R.string.date);
                        break;
                }
            }


        });

        tickMarkLabelsRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tickMarkLabelsRelativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                addTickMarkTextLabels();
            }
        });

        Calendar today = Calendar.getInstance();

        dueTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
    }

    private void openDatePicker() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);
                dueTextDate.setText(day1 + "." + month1 + "." + year1);
            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(this,
                android.R.style.Theme_Material_Dialog, datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle(getString(R.string.select_date));
        datePicker.show();
    }


    private void addTickMarkTextLabels() {
        int tickMarkCount = discreteSlider.getTickMarkCount();
        float tickMarkRadius = discreteSlider.getTickMarkRadius();
        int width = tickMarkLabelsRelativeLayout.getMeasuredWidth();

        int discreteSliderBackdropLeftMargin = DisplayUtility.dp2px(this, 32);
        int discreteSliderBackdropRightMargin = DisplayUtility.dp2px(this, 32);
        float firstTickMarkRadius = tickMarkRadius;
        float lastTickMarkRadius = tickMarkRadius;
        int interval = (width - (discreteSliderBackdropLeftMargin + discreteSliderBackdropRightMargin) - ((int) (firstTickMarkRadius + lastTickMarkRadius)))
                / (tickMarkCount - 1);

        String[] tickMarkLabels = {getString(R.string.no_time_limit), getString(R.string.today)
                , getString(R.string.tommorow), getString(R.string.date)};
        int tickMarkLabelWidth = DisplayUtility.dp2px(this, 40);

        for (int i = 0; i < tickMarkCount; i++) {
            TextView tv = new TextView(this);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    tickMarkLabelWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

            tv.setText(tickMarkLabels[i]);
            tv.setGravity(Gravity.CENTER);
            if (i == discreteSlider.getPosition())
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            else
                tv.setTextColor(getResources().getColor(R.color.colorPrimary));

            int left = discreteSliderBackdropLeftMargin + (int) firstTickMarkRadius + (i * interval) - (tickMarkLabelWidth / 2);

            layoutParams.setMargins(left,
                    0,
                    0,
                    0);
            tv.setLayoutParams(layoutParams);

            tickMarkLabelsRelativeLayout.addView(tv);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    Toast.makeText(AddTaskActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                    progressImage.setVisibility(View.GONE);
                    photoTaskImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                    progressImage.setVisibility(View.VISIBLE);
                    photoTaskImage.setVisibility(View.GONE);

                    try {
                        DataService.init().sendPhoto(reduceFile(imageFile), new DataService.onAddPhotoTask() {
                            @Override
                            public void onAddPhotoResult(UploadImageData response) {
                                if (response.getResult() != null) {
                                    pickedPhoto = response.getResult().imageTempName;
                                    progressImage.setVisibility(View.GONE);
                                    photoTaskImage.setVisibility(View.VISIBLE);
                                    if (!response.getResult().imageUrl.equals(""))
                                        Picasso.with(AddTaskActivity.this)
                                                .load(response.getResult().imageUrl).into(photoTaskImage);
                                } else {
                                    Toast.makeText(AddTaskActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                                    progressImage.setVisibility(View.GONE);
                                    photoTaskImage.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onErrorPhotoResult() {
                                Toast.makeText(AddTaskActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                                progressImage.setVisibility(View.GONE);
                                photoTaskImage.setVisibility(View.VISIBLE);
                            }
                        }, PrefRepository.getToken(AddTaskActivity.this));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    if (source == EasyImage.ImageSource.CAMERA) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AddTaskActivity.this);
                        if (photoFile != null) photoFile.delete();
                    }
                }
            });
        }
    }


}

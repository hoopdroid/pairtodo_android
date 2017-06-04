package com.pairtodopremium.ui.intro.first_task;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.entities.Task;
import com.pairtodopremium.data.response.signup.BasicResponse;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.BaseActivity;
import com.pairtodopremium.db.PrefRepository;

import butterknife.Bind;

public class FirstTaskActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_task);

        mToolbar.setTitle("Шаг 3. Поставьте задачу");

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
                DataService.init().addTask(new DataService.onAddTask() {
                    @Override
                    public void onAddTaskResult(BasicResponse response) {
                        ActivityRouter.startMainActivity(FirstTaskActivity.this);
                    }

                    @Override
                    public void onAddTaskError() {
                        //TODO Show error view
                    }
                }, new Task(noteTitleEdit.getText().toString().trim(),
                        "0", noteDescriptionEdit.getText().toString().trim()
                        , "", "", "0", "0"), PrefRepository.getToken(FirstTaskActivity.this));
            }
        });

        // Detect when slider position changes
        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
                int childCount = tickMarkLabelsRelativeLayout.getChildCount();
                for(int i= 0; i<childCount; i++){
                    TextView tv = (TextView) tickMarkLabelsRelativeLayout.getChildAt(i);
                    if(i == position)
                        tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    else
                        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                switch (position){
                    case 0:
                        dueTextDate.setText("Без срока");
                        break;
                    case 1:
                        dueTextDate.setText("Сегодня");
                        break;
                    case 2:
                        dueTextDate.setText("Завтра");
                        break;
                    case 3:
                        dueTextDate.setText("23.02.16");
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

        String[] tickMarkLabels = {"Без срока", "Сегодня", "Завтра", "Дата"};
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

//                    tv.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

            int left = discreteSliderBackdropLeftMargin + (int) firstTickMarkRadius + (i * interval) - (tickMarkLabelWidth / 2);

            layoutParams.setMargins(left,
                    0,
                    0,
                    0);
            tv.setLayoutParams(layoutParams);

            tickMarkLabelsRelativeLayout.addView(tv);
        }
    }
}

package com.pairtodopremium.ui.main.tasks;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pairtodopremium.R;
import com.pairtodopremium.data.entities.Task;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.utils.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoupleTasksAdapter extends RecyclerView.Adapter<CoupleTasksAdapter.ViewHolder> {

    private final Context context;
    private List<Task> tasksList;
    private int lastPosition = -1;

    public CoupleTasksAdapter(Context context, List<Task> newsList, String executorId) {
        this.context = context;
        this.tasksList = newsList;
        for (int i = 0; i < newsList.size() ; i++) {
            if(!Objects.equals(tasksList.get(i).getExecutorId(), executorId))
                tasksList.remove(i);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new ViewHolder(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        viewHolder.postText.setText(tasksList.get(i).getTitle());
        if (!Objects.equals(tasksList.get(i).getTermDate(), "0")) {
            viewHolder.dueDateText.setVisibility(View.VISIBLE);
            Calendar mydate = Calendar.getInstance();
            mydate.setTimeInMillis(Long.parseLong(tasksList.get(i).getTermDate()) * 1000);
            viewHolder.dueDateText.setReferenceTime(Long.parseLong(tasksList.get(i).getTermDate()) * 1000);
            long currentTime = System.currentTimeMillis() / 1000;
            long taskTime = Long.parseLong(tasksList.get(i).getTermDate());
            if (currentTime >= taskTime && taskTime!=0){
                viewHolder.dueDateText.setTextColor(Color.RED);
                viewHolder.postText.setTextColor(Color.RED);
            }
        }
        if (tasksList.get(i).getIsImportant().equals("1"))
            viewHolder.isFavoriteIcon.setVisibility(View.VISIBLE);

        if (tasksList.get(i).getTermDate().equals("0"))
            viewHolder.dueDateText.setVisibility(View.GONE);

        if (tasksList.get(i).getIsFinish().equals("1")){
            viewHolder.postText.setPaintFlags(viewHolder.postText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.taskDescription.setPaintFlags(viewHolder.taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.dueDateText.setPaintFlags(viewHolder.dueDateText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (tasksList.get(i).getList() != null && tasksList.get(i).getList() != "") {
            viewHolder.isListIncludedIcon.setVisibility(View.VISIBLE);
            viewHolder.taskDescription.setText(tasksList.get(i).getList());
        }

        else {
            if (!Objects.equals(tasksList.get(i).getDescription(), "")){
                viewHolder.taskDescription.setVisibility(View.VISIBLE);
                viewHolder.taskDescription.setText(tasksList.get(i).getDescription());}
            else viewHolder.taskDescription.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Context context;
        @Bind(R.id.post_container)
        CardView cardView;
        @Bind(R.id.tv_name)
        TextView postText;
        @Bind(R.id.tv_due_date)
        RelativeTimeTextView dueDateText;
        @Bind(R.id.favorite_item_icon)
        ImageView isFavoriteIcon;
        @Bind(R.id.list_item_icon)
        ImageView isListIncludedIcon;
        @Bind(R.id.tv_task_descr)
        TextView taskDescription;

        public ViewHolder(View view, final Context context) {
            super(view);

            ButterKnife.bind(this, view);
            cardView.setOnClickListener(this);
            this.context = context;
        }


        @Override
        public void onClick(View v) {

            if (v == cardView) {
                ActivityRouter.startTaskActivity((Activity)context, tasksList.get(getPosition()), Constants.CHANGE_TASK);
            }
        }

    }

    private void addAnimationToPostItem(ViewHolder viewHolder, int i) {
        Animation animation = AnimationUtils.loadAnimation(context,
                (i > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_to_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = i;
    }
}
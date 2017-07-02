package com.pairtodopremium.ui.main.profile.noCouple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pairtodopremium.R;
import com.pairtodopremium.data.response.searchCouple.User;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

  private final Context context;
  private List<User> users;

  public UsersAdapter(Context context, List<User> users) {
    this.context = context;
    this.users = users;
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
    return new ViewHolder(view, viewGroup.getContext());
  }

  @Override public void onBindViewHolder(final ViewHolder viewHolder, int i) {

    viewHolder.userNickName.setText(users.get(i).nicName());
    Picasso.with(context).load(users.get(i).photo()).into(viewHolder.userImage);
  }

  @Override public int getItemCount() {
    return users.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected Context context;
    @Bind(R.id.container) ViewGroup container;
    @Bind(R.id.userImage) CircularImageView userImage;
    @Bind(R.id.userNickname) TextView userNickName;

    public ViewHolder(View view, final Context context) {
      super(view);

      ButterKnife.bind(this, view);
      container.setOnClickListener(this);
      this.context = context;
    }

    @Override public void onClick(View v) {

      if (v == container) {

      }
    }
  }
}
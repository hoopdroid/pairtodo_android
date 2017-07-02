package com.pairtodopremium.ui.main.chat.views.messages.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pairtodopremium.R;
import com.pairtodopremium.ui.main.chat.models.StickersItem;
import com.squareup.picasso.Picasso;
import java.util.List;
import rx.functions.Action1;

public class PreviewStickersAdapter
    extends RecyclerView.Adapter<PreviewStickersAdapter.ViewHolder> {

  private final Context context;
  private List<StickersItem> stickersPreviewList;
  private Action1<String> previewStickerClickAction;
  private int selectedPosition = 0;

  public PreviewStickersAdapter(Context context, List<StickersItem> stickersItems,
      @NonNull Action1<String> itemPreviewStickerClickAction) {
    this.context = context;
    this.previewStickerClickAction = itemPreviewStickerClickAction;
    this.stickersPreviewList = stickersItems;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.sticker_preview_item, viewGroup, false);
    return new ViewHolder(view, viewGroup.getContext());
  }

  @Override public void onBindViewHolder(final ViewHolder viewHolder, int i) {
    if (selectedPosition == viewHolder.getAdapterPosition()) {
      viewHolder.previewHolder.setBackgroundColor(
          context.getResources().getColor(R.color.colorPrimary));
    } else {
      viewHolder.previewHolder.setBackgroundColor(Color.TRANSPARENT);
    }

    Picasso.with(context)
        .load(stickersPreviewList.get(i).getPreview())
        .into(viewHolder.previewImage);

    viewHolder.previewImage.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        notifyItemChanged(selectedPosition);
        selectedPosition = viewHolder.getAdapterPosition();
        notifyItemChanged(selectedPosition);
        previewStickerClickAction.call(
            stickersPreviewList.get(viewHolder.getAdapterPosition()).getSticker());
      }
    });
  }

  @Override public int getItemCount() {
    return stickersPreviewList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    protected Context context;
    @Bind(R.id.sticker_preview_image) ImageView previewImage;
    @Bind(R.id.sticker_preview_holder) ViewGroup previewHolder;

    public ViewHolder(View view, final Context context) {
      super(view);

      ButterKnife.bind(this, view);
      this.context = context;
    }
  }
}
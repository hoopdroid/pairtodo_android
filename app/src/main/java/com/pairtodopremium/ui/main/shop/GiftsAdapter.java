package com.pairtodopremium.ui.main.shop;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.florent37.picassopalette.PicassoPalette;
import com.pairtodopremium.R;
import com.pairtodopremium.data.entities.shop.StorageGift;
import com.pairtodopremium.router.ActivityRouter;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.ViewHolder> {
  private final Context context;
  private List<StorageGift> storageGiftList;

  public GiftsAdapter(Context context, List<StorageGift> storageGiftList) {
    this.context = context;
    this.storageGiftList = storageGiftList;
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gift, viewGroup, false);
    return new ViewHolder(view, viewGroup.getContext());
  }

  @Override public void onBindViewHolder(final ViewHolder viewHolder, int i) {
    Picasso.with(context)
        .load(storageGiftList.get(i).getPreview())
        .into(viewHolder.giftImage,
            PicassoPalette.with(storageGiftList.get(i).getPreview(), viewHolder.giftImage)
                .use(PicassoPalette.Profile.MUTED_DARK)
                .intoBackground(viewHolder.giftName));
    if (Locale.getDefault().getLanguage().equals("en")) {
      viewHolder.giftName.setText(storageGiftList.get(i).getNameEn());
    } else {
      viewHolder.giftName.setText(storageGiftList.get(i).getNameRu());
    }
  }

  @Override public int getItemCount() {
    return storageGiftList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected Context context;
    @Bind(R.id.card_view) CardView container;
    @Bind(R.id.shape_image_grid) ImageView giftImage;
    @Bind(R.id.shape_image_name) TextView giftName;

    public ViewHolder(View view, final Context context) {
      super(view);

      ButterKnife.bind(this, view);
      container.setOnClickListener(this);
      this.context = context;
    }

    @Override public void onClick(View v) {

      if (v == container) {
        ActivityRouter.startSendGiftActivity(context, storageGiftList.get(getPosition()));
      }
    }
  }
}

package com.pairtodopremium.ui.main.profile.gifts;

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
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.florent37.picassopalette.PicassoPalette;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pairtodopremium.R;
import com.pairtodopremium.data.entities.shop.GiftsData;
import com.pairtodopremium.data.entities.shop.StorageGift;
import com.pairtodopremium.data.response.gifts.Gift;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.utils.Constants;
import com.pairtodopremium.utils.JsonHelper;
import com.squareup.picasso.Picasso;
import java.lang.reflect.Type;
import java.util.List;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.ViewHolder> {

  private final Context context;
  private List<Gift> giftList;
  private List<StorageGift> storageGifts;

  public GiftsAdapter(Context context, List<Gift> giftList) {
    this.context = context;
    this.giftList = giftList;
    Gson gson = new Gson();
    Type type = new TypeToken<GiftsData>() {
    }.getType();
    GiftsData data = gson.fromJson(JsonHelper.loadJSONFromAsset(context, "gifts"), type);
    storageGifts = data.getStorageGifts();
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_my_gift, viewGroup, false);
    return new ViewHolder(view, viewGroup.getContext());
  }

  @Override public void onBindViewHolder(final ViewHolder viewHolder, int i) {

    for (int j = 0; j < storageGifts.size(); j++) {
      if (giftList.get(i).template().equals(storageGifts.get(j).getTovarName())) {
        Picasso.with(context)
            .load(storageGifts.get(j).getPreview())
            .into(viewHolder.previewGift,
                PicassoPalette.with(storageGifts.get(i).getPreview(), viewHolder.previewGift)
                    .use(PicassoPalette.Profile.MUTED)
                    .intoBackground(viewHolder.giftName));
        break;
      }
    }
    viewHolder.giftName.setText(giftList.get(i).text());
    viewHolder.giftTime.setReferenceTime(Long.parseLong(giftList.get(i).createDate()) * 1000);
  }

  @Override public int getItemCount() {
    return giftList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected Context context;
    @Bind(R.id.card_view) CardView container;
    @Bind(R.id.shape_image_name) TextView giftName;
    @Bind(R.id.giftType) RelativeTimeTextView giftTime;
    @Bind(R.id.previewGift) ImageView previewGift;

    public ViewHolder(View view, final Context context) {
      super(view);

      ButterKnife.bind(this, view);
      container.setOnClickListener(this);
      this.context = context;
    }

    @Override public void onClick(View v) {

      if (v == container) {

        for (int j = 0; j < storageGifts.size(); j++) {
          if (giftList.get(getPosition()).template().equals(storageGifts.get(j).getTovarName())) {
            ActivityRouter.startPhotoActivity(context, storageGifts.get(j).getFull(), previewGift,
                Constants.IMAGE_TRANSITION);
          }
        }
      }
    }
  }
}
package com.pairtodobeta.ui.main.shop;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.pairtodobeta.R;
import com.pairtodobeta.data.entities.shop.StickersItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StickerPacksAdapter extends RecyclerView.Adapter<StickerPacksAdapter.ViewHolder> {
    private final Context context;
    private List<StickersItem> stickersPreviewList;

    public StickerPacksAdapter(Context context, List<StickersItem> stickersPreviewList) {
        this.context = context;
        this.stickersPreviewList = stickersPreviewList;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sticker, viewGroup, false);
        return new ViewHolder(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {


        Picasso.with(context).load(stickersPreviewList.get(i).getPreview()).into(viewHolder.giftImage,
                PicassoPalette.with(stickersPreviewList.get(i).getPreview(), viewHolder.giftImage)
                        .use(PicassoPalette.Profile.MUTED_DARK)
                        .intoBackground(viewHolder.giftName));
        if (Locale.getDefault().getLanguage().equals("en"))
            viewHolder.giftName.setText(stickersPreviewList.get(i).getNameEn());
        else viewHolder.giftName.setText(stickersPreviewList.get(i).getNameRu());


    }


    @Override
    public int getItemCount() {
        return stickersPreviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Context context;
        @Bind(R.id.card_view)
        CardView container;
        @Bind(R.id.shape_image_grid)
        ImageView giftImage;
        @Bind(R.id.shape_image_name)
        TextView giftName;

        public ViewHolder(View view, final Context context) {
            super(view);

            ButterKnife.bind(this, view);
            container.setOnClickListener(this);
            this.context = context;
        }


        @Override
        public void onClick(View v) {

            if (v == container) {
            }
        }
    }

}

package com.pairtodopremium.ui.main.chat.views.messages.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pairtodopremium.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.ViewHolder> {

    private final Context context;
    private List<String> stickersUrlsList;
    private Action1<String> stickerSelectAction;
    private String rootAssetFolder;

    public StickersAdapter(Context context, String root, List<String> stickersItems,
                           @NonNull Action1<String> itemPreviewStickerClickAction) {
        this.context = context;
        this.stickerSelectAction = itemPreviewStickerClickAction;
        this.stickersUrlsList = stickersItems;
        this.rootAssetFolder = root;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_item, viewGroup, false);
        return new ViewHolder(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        Picasso.with(context).load("file:///android_asset/stickers/" + rootAssetFolder + "/" + stickersUrlsList.get(i))
                .into(viewHolder.stickerImage);
        int a = 5;
    }


    @Override
    public int getItemCount() {
        return stickersUrlsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Context context;
        @Bind(R.id.sticker_image)
        ImageView stickerImage;


        public ViewHolder(View view, final Context context) {
            super(view);

            ButterKnife.bind(this, view);
            this.context = context;
            stickerImage.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if (v == stickerImage) {
                stickerSelectAction.call(stickersUrlsList.get(getPosition()));
            }
        }

    }
}
package com.pairtodobeta.ui.main.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pairtodobeta.R;
import com.pairtodobeta.data.entities.shop.GiftsData;
import com.pairtodobeta.data.entities.shop.StickersData;
import com.pairtodobeta.data.entities.shop.ThemesData;
import com.pairtodobeta.ui.BaseActivity;
import com.pairtodobeta.utils.JsonHelper;

import java.lang.reflect.Type;

import butterknife.Bind;

public class ShopCategoryActivity extends BaseActivity {
    private static final int RC_CHANGE_THEME = 0;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rv)
    RecyclerView mRecyclerView;

    private String selectionCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_category);

        selectionCategory = getIntent().getStringExtra("Category");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (selectionCategory.equals("gifts")) {
            fillGifts();
            mToolbar.setTitle(R.string.gifts_name);
        }

        if (selectionCategory.equals("themes")) {
            fillThemes();
            mToolbar.setTitle(R.string.themes_name);
        }

        if (selectionCategory.equals("stickers")) {
            fillStickers();
            mToolbar.setTitle(R.string.sticker_name);
        }
    }

    private void fillStickers() {
        Gson gson = new Gson();
        Type type = new TypeToken<StickersData>() {
        }.getType();

        com.pairtodobeta.data.entities.shop.StickersData data =
                gson.fromJson(JsonHelper.loadJSONFromAsset(this, "stickers"), type);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        StickerPacksAdapter adapter = new StickerPacksAdapter(this, data.getStickers());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == RC_CHANGE_THEME){
            recreate();
        }
    }

    private void fillThemes() {
        Gson gson = new Gson();
        Type type = new TypeToken<ThemesData>() {
        }.getType();

        ThemesData data = gson.fromJson(JsonHelper.loadJSONFromAsset(this, "themes"), type);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        ThemesAdapter adapter = new ThemesAdapter(this, data.getThemas());
        mRecyclerView.setAdapter(adapter);
    }

    private void fillGifts() {
        Gson gson = new Gson();
        Type type = new TypeToken<GiftsData>() {
        }.getType();
        GiftsData data = gson.fromJson(JsonHelper.loadJSONFromAsset(this, "gifts"),
                type);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        GiftsAdapter adapter = new GiftsAdapter(this, data
                .getStorageGifts());
        mRecyclerView.setAdapter(adapter);
    }

}

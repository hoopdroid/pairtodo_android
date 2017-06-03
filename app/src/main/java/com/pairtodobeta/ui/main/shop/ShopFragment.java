package com.pairtodobeta.ui.main.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pairtodobeta.R;
import com.pairtodobeta.router.ActivityRouter;
import com.pairtodobeta.ui.main.BaseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopFragment extends BaseFragment {
    @Bind(R.id.themes_container)
    ViewGroup themesContainer;
    @Bind(R.id.gifts_container)
    ViewGroup giftsContainer;
    @Bind(R.id.stickers_container)
    ViewGroup stickersContainer;

    @Bind(R.id.img_themes_image)
    ImageView themeImage;
    @Bind(R.id.img_gifts_image)
    ImageView giftsImage;
    @Bind(R.id.img_stickers_image)
    ImageView stickersImage;


    public ShopFragment() {
    }

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, convertView);

        themeImage.setImageBitmap(getRandomAssetsImage("themes/full"));
        giftsImage.setImageBitmap(getRandomAssetsImage("gifts/full"));
        stickersImage.setImageBitmap(getRandomAssetsImage("stickers/preview"));

        giftsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityRouter.startCategoryShopActivity(getActivity(), "gifts");
            }
        });

        themeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityRouter.startCategoryShopActivity(getActivity(), "themes");
            }
        });

        stickersImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityRouter.startCategoryShopActivity(getActivity(),"stickers");
            }
        });

        return convertView;
    }

    private Bitmap getRandomAssetsImage(String assetsPath) {
        String[] f = new String[0];
        try {
            f = getActivity().getAssets().list(assetsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String randomSelection = f[randInt(0, f.length - 1)];

        InputStream is = null;
        try {
            is = this.getResources().getAssets().open(assetsPath + "/" + randomSelection);
        } catch (IOException e) {
            Log.w("EL", e);
        }

        return BitmapFactory.decodeStream(is);


    }

    private int randInt(int minimum, int maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }

}

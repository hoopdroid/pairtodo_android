package com.pairtodopremium.ui.main.shop;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pairtodopremium.R;
import com.pairtodopremium.data.entities.shop.Theme;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.utils.Themes;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;

public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ViewHolder> {
  private final Context context;
  private List<Theme> themeList;

  public ThemesAdapter(Context context, List<Theme> themeList) {
    this.context = context;
    this.themeList = themeList;
  }

  @Override public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.theme_item, viewGroup, false);
    return new ViewHolder(view, viewGroup.getContext());
  }

  @Override public void onBindViewHolder(final ViewHolder viewHolder, int i) {

    Picasso.with(context).load(themeList.get(i).getFull()).into(viewHolder.themeImage);
    if (Locale.getDefault().getLanguage().equals("en")) {
      viewHolder.themeName.setText(themeList.get(i).getNameEn());
    } else {
      viewHolder.themeName.setText(themeList.get(i).getNameRu());
    }

    viewHolder.themePrice.setText(themeList.get(i).getPrice());
  }

  @Override public int getItemCount() {
    return themeList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected Context context;
    @Bind(R.id.card_view) CardView container;
    @Bind(R.id.shape_image) ImageView themeImage;
    @Bind(R.id.shape_image_name) TextView themeName;
    @Bind(R.id.theme_price) TextView themePrice;

    public ViewHolder(View view, final Context context) {
      super(view);

      ButterKnife.bind(this, view);
      container.setOnClickListener(this);
      this.context = context;
    }

    @Override public void onClick(View v) {

      if (v == container) {
        switch (themeList.get(getPosition()).getTovarName()) {
          case "thema0":
            PrefRepository.setTheme(context, Themes.THEME_DEFAULT);
            break;
          case "thema1":
            PrefRepository.setTheme(context, Themes.THEME_PURPLE_HAZE);
            break;
          case "thema2":
            PrefRepository.setTheme(context, Themes.THEME_PARIS);
            break;
          case "thema3":
            PrefRepository.setTheme(context, Themes.THEME_BEAUTY);
            break;
          case "thema4":
            PrefRepository.setTheme(context, Themes.THEME_WITCH);
            break;
          case "thema5":
            PrefRepository.setTheme(context, Themes.THEME_ROAD);
            break;
          case "thema6":
            PrefRepository.setTheme(context, Themes.THEME_CUTE_PURPLE);
            break;
          case "thema7":
            PrefRepository.setTheme(context, Themes.THEME_SUMMER);
            break;
          case "thema8":
            PrefRepository.setTheme(context, Themes.THEME_CAT);
            break;
          case "thema9":
            PrefRepository.setTheme(context, Themes.THEME_SEA);
            break;
        }

        Toast.makeText(context, "Закройте и откройте приложение, чтобы применить тему!",
            Toast.LENGTH_SHORT).show();
      }
    }
  }
}
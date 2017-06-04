package com.pairtodopremium.data.entities.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemesData {

@SerializedName("themas")
@Expose
private List<Theme> themas = null;

public List<Theme> getThemas() {
return themas;
}

public void setThemas(List<Theme> themas) {
this.themas = themas;
}

}
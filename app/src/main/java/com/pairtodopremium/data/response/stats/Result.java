package com.pairtodopremium.data.response.stats;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

@SerializedName("me")
@Expose
private Me me;
@SerializedName("pair")
@Expose
private Pair pair;

public Me getMe() {
return me;
}

public void setMe(Me me) {
this.me = me;
}

public Pair getPair() {
return pair;
}

public void setPair(Pair pair) {
this.pair = pair;
}

}
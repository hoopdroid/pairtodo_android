package com.pairtodopremium.ui.main.chat.views.messages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.Space;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pairtodopremium.R;
import com.pairtodopremium.ui.main.chat.models.StickersData;
import com.pairtodopremium.ui.main.chat.views.messages.adapters.PreviewStickersAdapter;
import com.pairtodopremium.ui.main.chat.views.messages.adapters.StickersAdapter;
import com.pairtodopremium.utils.Constants;
import com.pairtodopremium.utils.JsonHelper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import rx.functions.Action1;

public class MessageInput extends RelativeLayout implements View.OnClickListener, TextWatcher {

  protected EditText messageInput;
  protected ImageButton messageSendButton;
  protected ViewGroup stickersView;
  protected ImageView stickersButton;
  protected Space buttonSpace;
  protected RecyclerView previewStickersView;
  protected RecyclerView stickersGridView;

  private CharSequence input;
  private InputListener inputListener;
  private boolean isStickersOpened = false;
  private Context context;

  public MessageInput(Context context) {
    super(context);
    init(context);
  }

  public MessageInput(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public MessageInput(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  /**
   * Set callback to be invoked when user entered his input
   *
   * @param inputListener input callback
   */
  public void setInputListener(InputListener inputListener) {
    this.inputListener = inputListener;
  }

  /**
   * Returns EditText for messages input
   *
   * @return EditText
   */
  public EditText getInputEditText() {
    return messageInput;
  }

  /**
   * Returns `submit` button
   *
   * @return ImageButton
   */
  public ImageButton getButton() {
    return messageSendButton;
  }

  @Override public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.messageSendButton) {
      boolean isSubmitted = onSubmit();
      if (isSubmitted) {
        messageInput.setText("");
      }
    }
    if (id == R.id.stickersButton) {
      if (!isStickersOpened) {
        openStickersView();
      } else {
        closeStickersView();
      }
    }
  }

  private void closeStickersView() {
    stickersButton.setImageResource(R.drawable.ic_sticker36dp);
    stickersView.setVisibility(GONE);
    isStickersOpened = false;
    messageInput.setEnabled(true);
    messageInput.setHint(R.string.message);
  }

  private void openStickersView() {
    stickersButton.setImageResource(R.drawable.ic_cancel24_black);
    stickersView.setVisibility(VISIBLE);
    isStickersOpened = true;
    messageInput.setEnabled(false);
    messageInput.setHint(R.string.select_sticker);

    Gson gson = new Gson();
    Type type = new TypeToken<StickersData>() {
    }.getType();
    StickersData data = gson.fromJson(JsonHelper.loadJSONFromAsset(context, "stickers"), type);

    RecyclerView.LayoutManager layoutManager =
        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    previewStickersView.setLayoutManager(layoutManager);
    RecyclerView.LayoutManager gridManager = new GridLayoutManager(context, 3);
    stickersGridView.setLayoutManager(gridManager);

    PreviewStickersAdapter adapter =
        new PreviewStickersAdapter(context, data.getStickers(), new Action1<String>() {
          @Override public void call(final String sticker) {
            //get images from name url
            String[] selectedStickers = listAssetFiles("stickers/" + sticker);
            StickersAdapter stickersAdapter =
                new StickersAdapter(context, sticker, Arrays.asList(selectedStickers),
                    new Action1<String>() {
                      @Override public void call(String s) {
                        String stickerCode;
                        if (Character.isDigit(s.charAt(3))) {
                          stickerCode =
                              Constants.STICKER.concat(sticker.concat("_" + s.substring(1, 2)));
                        } else {
                          stickerCode =
                              Constants.STICKER.concat(sticker.concat("_" + s.substring(1, 3)));
                        }
                        inputListener.onSubmit(stickerCode);
                      }
                    });
            stickersGridView.setAdapter(stickersAdapter);
          }
        });
    previewStickersView.setAdapter(adapter);
  }

  /**
   * This method is called to notify you that, within s,
   * the count characters beginning at start have just replaced old text that had length before
   */
  @Override public void onTextChanged(CharSequence s, int start, int count, int after) {
    input = s;
    messageSendButton.setEnabled(input.length() > 0);
  }

  /**
   * This method is called to notify you that, within s,
   * the count characters beginning at start are about to be replaced by new text with length after.
   */
  @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  /**
   * This method is called to notify you that, somewhere within s, the text has been changed.
   */
  @Override public void afterTextChanged(Editable editable) {

  }

  private boolean onSubmit() {
    return inputListener != null && inputListener.onSubmit(input);
  }

  private void init(Context context, AttributeSet attrs) {
    init(context);
    this.context = context;
    MessageInputStyle style = MessageInputStyle.parse(context, attrs);

    this.messageInput.setMaxLines(style.getInputMaxLines());
    this.messageInput.setHint(style.getInputHint());
    this.messageInput.setText(style.getInputText());
    this.messageInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getInputTextSize());
    this.messageInput.setTextColor(style.getInputTextColor());
    this.messageInput.setHintTextColor(style.getInputHintColor());
    this.messageInput.setBackground(style.getInputBackground());
    setCursor(style.getInputCursorDrawable());

    this.messageSendButton.setBackground(style.getInputButtonBackground());
    this.messageSendButton.setImageDrawable(style.getInputButtonIcon());
    this.messageSendButton.getLayoutParams().width = style.getInputButtonWidth();
    this.messageSendButton.getLayoutParams().height = style.getInputButtonHeight();
    this.buttonSpace.getLayoutParams().width = style.getInputButtonMargin();

    if (getPaddingLeft() == 0
        && getPaddingRight() == 0
        && getPaddingTop() == 0
        && getPaddingBottom() == 0) {
      setPadding(style.getInputDefaultPaddingLeft(), style.getInputDefaultPaddingTop(),
          style.getInputDefaultPaddingRight(), style.getInputDefaultPaddingBottom());
    }
  }

  private void init(Context context) {
    inflate(context, R.layout.message_input, this);

    messageInput = (EditText) findViewById(R.id.messageInput);
    messageSendButton = (ImageButton) findViewById(R.id.messageSendButton);
    buttonSpace = (Space) findViewById(R.id.buttonSpace);
    stickersView = (ViewGroup) findViewById(R.id.stickers_view);
    stickersButton = (ImageView) findViewById(R.id.stickersButton);
    previewStickersView = (RecyclerView) findViewById(R.id.sticker_preview_list);
    stickersGridView = (RecyclerView) findViewById(R.id.stickers_grid_view);

    messageSendButton.setOnClickListener(this);
    messageInput.addTextChangedListener(this);
    stickersButton.setOnClickListener(this);
    messageInput.setText("");
  }

  private void setCursor(Drawable drawable) {
    try {
      Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
      f.setAccessible(true);
      f.set(this.messageInput, drawable);
    } catch (Exception ignore) {
    }
  }

  //Method for get files from Assets Folder and sub folder
  private String[] listAssetFiles(String path) {
    String[] list;
    try {
      list = context.getAssets().list(path);
      if (list.length > 0) {
        return list;
      }
    } catch (IOException e) {
    }
    return null;
  }

  /**
   * Interface definition for a callback to be invoked when user entered his input
   */
  public interface InputListener {

    /**
     * Fires when user press send button.
     *
     * @param input input entered by user
     * @return if input text is valid, you must return {@code true} and input will be cleared,
     * otherwise return false.
     */
    boolean onSubmit(CharSequence input);
  }

  public interface StickersButtonListener {
    void onStickersOpen();

    void onStickersClose();
  }
}

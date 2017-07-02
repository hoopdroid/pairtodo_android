package com.pairtodopremium.ui.main.profile;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.chat.Message;
import com.pairtodopremium.data.response.userData.UserDataResponse;
import com.pairtodopremium.data.response.userData.UserInfo;
import com.pairtodopremium.data.settings.SettingsRepository;
import com.pairtodopremium.db.PrefRepository;
import com.pairtodopremium.router.ActivityRouter;
import com.pairtodopremium.ui.main.BaseFragment;
import com.pairtodopremium.ui.main.chat.models.DefaultDialog;
import com.pairtodopremium.ui.main.chat.models.DefaultUser;
import com.pairtodopremium.ui.main.chat.views.messages.MessagesListAdapter;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.pairtodopremium.ui.BaseActivity.realm;

public class ProfileFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
  @Bind(R.id.userProfilePhoto) CircularImageView userProfilePhoto;
  @Bind(R.id.userProfileName) TextView userProfileName;
  @Bind(R.id.stats) ViewGroup statsBtn;
  @Bind(R.id.gifts) ViewGroup giftsBtn;
  @Bind(R.id.content_main) ViewGroup contentMain;
  @Bind(R.id.noCoupleView) ViewGroup noCoupleView;
  @Bind(R.id.searchCoupleBtn) Button searchCoupleBtn;
  @Bind(R.id.userContent) ViewGroup userContent;
  @Bind(R.id.settings) ImageView settings;
  @Bind(R.id.chatLoading) ProgressBar chatLoading;
  @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefresh;
  @Bind(R.id.user_menu) ViewGroup userMenu;

  private UserInfo info;
  private DialogsList dialogsListView;

  public ProfileFragment() {
  }

  public static ProfileFragment newInstance() {
    return new ProfileFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View convertView = inflater.inflate(R.layout.fragment_profile, container, false);
    ButterKnife.bind(this, convertView);

    dialogsListView = (DialogsList) convertView.findViewById(R.id.dialogsList);
    initViewElements();
    swipeRefresh.setRefreshing(true);
    userContent.setVisibility(View.VISIBLE);
    contentMain.setVisibility(View.VISIBLE);
    info = SettingsRepository.getUserInfo(realm);
    if (info != null) {
      if (info.getPairId().equals("0")) {
        contentMain.setVisibility(View.GONE);
        noCoupleView.setVisibility(View.VISIBLE);
        userMenu.setVisibility(View.GONE);
      } else {
        contentMain.setVisibility(View.VISIBLE);
        noCoupleView.setVisibility(View.GONE);
        userMenu.setVisibility(View.VISIBLE);
      }
      userProfileName.setText(info.getName());
      Picasso.with(getActivity()).load(info.getPhoto()).into(userProfilePhoto);
    }

    getInfo();

    return convertView;
  }

  private void initViewElements() {
    statsBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startStatsActivity(getActivity());
      }
    });

    giftsBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startGiftsActivity(getActivity());
      }
    });

    userProfilePhoto.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        EasyImage.openGallery(getActivity(), 0);
      }
    });

    searchCoupleBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startSearchCoupleActivity(getActivity());
      }
    });

    settings.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ActivityRouter.startSettingsActivity(getActivity());
      }
    });

    swipeRefresh.setOnRefreshListener(this);
  }

  private void getMessageArchive() {
    if (getActivity() != null && isAdded()) {
      List<Message> messages = cacheManager.getMessagesArchive();
      if (messages.size() > 0) {
        initChatElements(messages.get(messages.size() - 1));
      } else {
        initChatElements(
            Message.create("", "", "", "", "", getString(R.string.start_conversation_with_couple),
                ""));
      }

      contentMain.setVisibility(View.VISIBLE);
      chatLoading.setVisibility(View.GONE);
    }
  }

  private void initChatElements(Message lastMessage) {
    DialogsListAdapter dialogsListAdapter = new DialogsListAdapter<>(new ImageLoader() {
      @Override public void loadImage(ImageView imageView, String url) {
        if (url != null && !Objects.equals(url, "")) {
          Picasso.with(getActivity()).load(url).into(imageView);
        }
      }
    });

    ArrayList<DefaultDialog> list = new ArrayList<>();
    ArrayList<IUser> users = new ArrayList<>();

    DefaultUser user = new DefaultUser(info.getId(), info.getName(), info.getPhoto(), true);
    DefaultUser couple =
        new DefaultUser(info.getPairId(), info.getPairName(), info.getPairPhoto(), true);
    if (lastMessage.fromId().equals(info.getId())) {
      lastMessage.setUserMessage(user);
    } else {
      lastMessage.setUserMessage(couple);
    }

    Message secretStickerMessage;
    DefaultDialog defaultDialog;
    if (MessagesListAdapter.checkIfStickerEncode(lastMessage.getText())) {
      secretStickerMessage =
          Message.create(lastMessage.isRead(), lastMessage.getId(), lastMessage.toId(),
              lastMessage.jobId(), lastMessage.id(), getString(R.string.sticker),
              lastMessage.createDate());
      if (lastMessage.fromId().equals(info.getId())) {
        secretStickerMessage.setUserMessage(user);
      } else {
        secretStickerMessage.setUserMessage(couple);
      }

      defaultDialog = new DefaultDialog("1",
          getActivity().getString(R.string.dialog_with) + " " + couple.getName(),
          couple.getAvatar(), users, secretStickerMessage, 0);

      list.add(defaultDialog);
    } else if (isAdded() && getActivity() != null) {
      defaultDialog = new DefaultDialog("1",
          getActivity().getString(R.string.dialog_with) + " " + couple.getName(),
          couple.getAvatar(), users, lastMessage, 0);
      list.add(defaultDialog);
    }

    users.add(user);
    users.add(couple);

    dialogsListAdapter.setItems(list);
    dialogsListView.setAdapter(dialogsListAdapter);
    dialogsListAdapter.setOnDialogClickListener(
        new DialogsListAdapter.OnDialogClickListener<DefaultDialog>() {
          @Override public void onDialogClick(DefaultDialog dialog) {
            ActivityRouter.startChatActivity(getActivity());
          }
        });
  }

  @Override public void onRefresh() {
    updateProfileData();
  }

  private void updateProfileData() {
    initViewElements();
    swipeRefresh.setRefreshing(true);
    userContent.setVisibility(View.VISIBLE);
    contentMain.setVisibility(View.VISIBLE);
    info = SettingsRepository.getUserInfo(realm);
    if (info != null) {
      if (info.getPairId().equals("0")) {
        contentMain.setVisibility(View.GONE);
        noCoupleView.setVisibility(View.VISIBLE);
        userMenu.setVisibility(View.GONE);
      } else {

      }
      userProfileName.setText(info.getName());
      Picasso.with(getActivity()).load(info.getPhoto()).into(userProfilePhoto);
    }

    getInfo();
  }

  private void getInfo() {
    if (info != null) getMessageArchive();

    if (isAdded()) {
      DataService.init().getUserInfo(new DataService.onUserData() {
        @Override public void onUserDataResult(UserDataResponse response) {
          if (isAdded()) {
            if (response.getUserInfo() != null && getActivity() != null) {
              if (info != null) {
                if (!response.getUserInfo().getPhoto().equals(info.getPhoto())) {
                  Picasso.with(getActivity())
                      .load(response.getUserInfo().getPhoto())
                      .into(userProfilePhoto);
                }
              }
              if (info != null) {
                if (!response.getUserInfo().getName().equals(info.getName())) {
                  userProfileName.setText(response.getUserInfo().getName());
                }
              }
              SettingsRepository.addUserInfo(realm, response.getUserInfo());
              if (response.getUserInfo().getPairId().equals("0")) {
                contentMain.setVisibility(View.GONE);
                noCoupleView.setVisibility(View.VISIBLE);
                userMenu.setVisibility(View.GONE);
              } else {
                if (info != null) getMessageArchive();
              }
              swipeRefresh.setRefreshing(false);
            }
          }
        }

        @Override public void onUserDataError() {
          // TODO Show error view
          if (isAdded()) {
            swipeRefresh.setRefreshing(false);
          }
        }
      }, PrefRepository.getToken(getActivity()));
    }
  }
}

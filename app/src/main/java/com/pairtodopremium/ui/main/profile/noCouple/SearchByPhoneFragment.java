package com.pairtodopremium.ui.main.profile.noCouple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.pairtodopremium.R;
import com.pairtodopremium.data.DataService;
import com.pairtodopremium.data.response.invite.InviteResponse;
import com.pairtodopremium.db.PrefRepository;

public class SearchByPhoneFragment extends Fragment {
  @Bind(R.id.input_phone) EditText inputCouplePhone;
  @Bind(R.id.input_my_phone) EditText inputMyPhone;
  @Bind(R.id.btn_confirm_send_couple) Button btnConfirmSendCouple;

  public SearchByPhoneFragment() {
  }

  public static SearchByPhoneFragment newInstance() {

    return new SearchByPhoneFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View convertView = inflater.inflate(R.layout.fragment_search_couple_phone, container, false);
    ButterKnife.bind(this, convertView);

    btnConfirmSendCouple.setVisibility(View.VISIBLE);
    btnConfirmSendCouple.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        DataService.init().sendInviteCode(new DataService.onInviteCouple() {
                                            @Override public void onInviteResult(InviteResponse inviteResponse) {
                                              Toast.makeText(getActivity(), R.string.inivite_send, Toast.LENGTH_SHORT).show();
                                            }

                                            @Override public void onInviteError() {
                                              Toast.makeText(getActivity(), R.string.error_try_again, Toast.LENGTH_SHORT).show();
                                            }
                                          }, PrefRepository.getToken(getActivity()), inputMyPhone.getText().toString().trim(),
            inputCouplePhone.getText().toString().trim());
      }
    });

    return convertView;
  }
}

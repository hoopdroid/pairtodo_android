package com.pairtodopremium.ui.main.profile.noCouple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.pairtodopremium.data.response.searchCouple.User;
import com.pairtodopremium.db.PrefRepository;
import java.util.List;

public class SearchByNameFragment extends Fragment {

  @Bind(R.id.input_query) EditText inputQuery;

  @Bind(R.id.rv) RecyclerView usersList;

  @Bind(R.id.btn_search_couple) Button btnConfirmSendCouple;

  public SearchByNameFragment() {
  }

  public static SearchByNameFragment newInstance() {

    return new SearchByNameFragment();
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View convertView = inflater.inflate(R.layout.fragment_search_couple_name, container, false);
    ButterKnife.bind(this, convertView);

    btnConfirmSendCouple.setVisibility(View.VISIBLE);

    usersList.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    usersList.setLayoutManager(layoutManager);

    btnConfirmSendCouple.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        DataService.init().searchPairByNameOrEmail(new DataService.onSearchUser() {
                                                     @Override public void onSearchUserResult(List<User> users) {
                                                       Toast.makeText(getActivity(), users.toString(), Toast.LENGTH_SHORT).show();
                                                       UsersAdapter adapter = new UsersAdapter(getActivity(), users);
                                                       usersList.setAdapter(adapter);
                                                     }

                                                     @Override public void onSearchUserError() {

                                                     }
                                                   }, PrefRepository.getToken(getActivity()), inputQuery.getText().toString().trim(),
            "nic_name_or_email");
      }
    });

    return convertView;
  }
}

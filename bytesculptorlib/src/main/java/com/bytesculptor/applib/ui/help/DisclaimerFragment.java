package com.bytesculptor.applib.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bytesculptor.applib.R;
import com.bytesculptor.applib.utilities.ExternalLinksHelper;


public class DisclaimerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disclaimer, container, false);
        TextView feedback = view.findViewById(R.id.tvFeedbackMailLink);
        if (feedback != null) {
            feedback.setOnClickListener(v -> {
                ExternalLinksHelper.sendFeedbackMail(requireContext(), "My Speech App");
            });
        }
        return view.getRootView();
    }

}
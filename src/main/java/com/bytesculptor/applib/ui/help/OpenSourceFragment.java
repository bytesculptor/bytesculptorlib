/*
 * Copyright (c) 2017 - 2021  Byte Sculptor Software  - All Rights Reserved
 *
 * All information contained herein is and remains the property of Byte Sculptor Software.
 * Unauthorized copying of this file, via any medium, is strictly prohibited unless prior
 * written permission is obtained from Byte Sculptor Software.
 *
 * Romeo Rondinelli - bytesculptor@gmail.com
 *
 */

package com.bytesculptor.applib.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bytesculptor.applib.R;


public class OpenSourceFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_source, container, false);
    }
}
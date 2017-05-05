package com.frankemerald.depicfree.Listeners;

import android.widget.SeekBar;

import com.frankemerald.depicfree.MainActivity;
import com.frankemerald.depicfree.UIHelpers.UIHelper;
import com.frankemerald.depicfree.Views.TouchImageView;

/**
 * Created by Mark on 24.10.2015.
 */
public class SeekbarListener  implements SeekBar.OnSeekBarChangeListener{

    MainActivity activity;

    public SeekbarListener(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(activity.currentContainerSelected<1)
            return;
        ((TouchImageView) activity.findViewById(UIHelper.imageViewsIds[activity.currentContainerSelected])).setAlpha((float) progress/100f);
        activity.alphaList[activity.currentContainerSelected] = progress*255/100;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
}

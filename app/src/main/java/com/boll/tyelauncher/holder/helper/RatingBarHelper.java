package com.boll.tyelauncher.holder.helper;

package com.toycloud.launcher.holder.helper;

import android.view.View;
import android.widget.RatingBar;
import com.toycloud.launcher.R;
import com.toycloud.launcher.api.response.MappingInfoResponse;

public class RatingBarHelper {
    private View mParentView;
    private RatingBar mRatingBar;
    private RatingBar mRatingBar1Light;
    private RatingBar mRatingBar2Light;
    private View mTV2Line;

    public RatingBarHelper(View rootView) {
        this.mParentView = rootView.findViewById(R.id.fl_lights);
        this.mRatingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        this.mRatingBar1Light = (RatingBar) rootView.findViewById(R.id.ratingBar_1_Light);
        this.mRatingBar2Light = (RatingBar) rootView.findViewById(R.id.ratingBar_2_Lights);
        this.mTV2Line = rootView.findViewById(R.id.tv_2_line);
    }

    public void showLights(MappingInfoResponse data, int coverPoints) {
        if (data == null) {
            this.mParentView.setVisibility(8);
            this.mTV2Line.setVisibility(0);
        } else if (coverPoints <= 0) {
            this.mParentView.setVisibility(8);
            this.mTV2Line.setVisibility(0);
        } else {
            this.mParentView.setVisibility(0);
            this.mTV2Line.setVisibility(8);
            MappingInfoResponse.PresentKGMasteryItemsBean userAnchorGraphToday = data.getData().getPresentKGMasteryItems();
            if (userAnchorGraphToday == null) {
                this.mParentView.setVisibility(8);
                return;
            }
            double degree = userAnchorGraphToday.getMasterDegree();
            if (degree * 100.0d > 90.0d) {
                this.mRatingBar.setRating(5.0f);
                this.mRatingBar.setVisibility(0);
                this.mRatingBar1Light.setVisibility(8);
                this.mRatingBar2Light.setVisibility(8);
            } else if (degree * 100.0d >= 75.0d) {
                this.mRatingBar.setRating(4.0f);
                this.mRatingBar.setVisibility(0);
                this.mRatingBar1Light.setVisibility(8);
                this.mRatingBar2Light.setVisibility(8);
            } else if (degree * 100.0d >= 65.0d) {
                this.mRatingBar.setRating(3.0f);
                this.mRatingBar.setVisibility(0);
                this.mRatingBar1Light.setVisibility(8);
                this.mRatingBar2Light.setVisibility(8);
            } else if (degree * 100.0d >= 35.0d) {
                this.mRatingBar.setRating(2.0f);
                this.mRatingBar.setVisibility(8);
                this.mRatingBar1Light.setVisibility(8);
                this.mRatingBar2Light.setVisibility(0);
            } else {
                this.mRatingBar.setRating(1.0f);
                this.mRatingBar.setVisibility(8);
                this.mRatingBar1Light.setVisibility(0);
                this.mRatingBar1Light.setNumStars(3);
                this.mRatingBar2Light.setVisibility(8);
            }
        }
    }
}
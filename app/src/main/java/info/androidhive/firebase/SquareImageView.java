package info.androidhive.firebase;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by DC on 11/3/2017.
 */

public class SquareImageView extends AppCompatImageView{
    public SquareImageView(Context context) {
        super(context);
    }
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int measuredWidth = (int)(getMeasuredWidth()*0.7);

        setMeasuredDimension(measuredWidth, measuredWidth);

    }
}

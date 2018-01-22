package cc.devjo.desound.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Josue on 15/02/2016.
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    //Medida Pixeles espaciado
    private int mItemOffset;

    public ItemOffsetDecoration(Context context, @IntegerRes int IntegerResId){
        int itenOffsetDp = context.getResources().getInteger(IntegerResId);
        mItemOffset = convertToPx(itenOffsetDp, context.getResources().getDisplayMetrics());
    }

    public int convertToPx(int offsetDp, DisplayMetrics metrics){
        return offsetDp * (metrics.densityDpi /160);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset,mItemOffset,mItemOffset,mItemOffset);
    }
}

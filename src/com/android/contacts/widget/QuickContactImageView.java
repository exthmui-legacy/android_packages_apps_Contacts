package com.android.contacts.widget;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.contacts.R;
import com.android.contacts.lettertiles.LetterTileDrawable;

/**
 * An {@link ImageView} designed to display QuickContact's contact photo. When requested to draw
 * {@link LetterTileDrawable}'s, this class instead draws a different default avatar drawable.
 *
 * In addition to supporting {@link ImageView#setColorFilter} this also supports a {@link #setTint}
 * method.
 *
 * This entire class can be deleted once use of LetterTileDrawable is no longer used
 * inside QuickContactsActivity at all.
 */
public class QuickContactImageView extends ImageView {

    private Drawable mOriginalDrawable;
    private BitmapDrawable mBitmapDrawable;
    private int mTintColor;
    private boolean mIsBusiness;

    private boolean mThemed;
    private boolean mBusinessThemed;

    public QuickContactImageView(Context context) {
        this(context, null);
    }

    public QuickContactImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickContactImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mThemed = context.getResources().getBoolean(R.bool.contact_image_themed);
        mBusinessThemed = context.getResources().getBoolean(R.bool.business_contact_image_themed);
    }

    public void setTint(int color) {
        if (mBitmapDrawable == null || mBitmapDrawable.getBitmap() == null
                || mBitmapDrawable.getBitmap().hasAlpha()) {
            setBackgroundColor(color);
        } else {
            setBackground(null);
        }
        mTintColor = color;
        postInvalidate();
    }

    public boolean isBasedOffLetterTile() {
        if (mOriginalDrawable instanceof LetterTileDrawable) {
            return !isThemed();
        }
        return false;
    }

    private boolean isThemed()
    {
        if (mIsBusiness) {
            return mBusinessThemed;
        } else {
            return mThemed;
        }
    }

    public void setIsBusiness(boolean isBusiness) {
        mIsBusiness = isBusiness;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        // There is no way to avoid all this casting. Blending modes aren't equally
        // supported for all drawable types.
        final BitmapDrawable bitmapDrawable;
        if (drawable == null || drawable instanceof BitmapDrawable) {
            bitmapDrawable = (BitmapDrawable) drawable;
        } else if (drawable instanceof LetterTileDrawable) {
            if (!mIsBusiness) {
                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(
                        R.drawable.person_image);
            } else {
                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(
                        R.drawable.business_image);
            }
        } else {
            throw new IllegalArgumentException("Does not support this type of drawable");
        }

        mOriginalDrawable = drawable;
        mBitmapDrawable = bitmapDrawable;
        if (!isThemed()) {
            setTint(mTintColor);
        }
        super.setImageDrawable(bitmapDrawable);
    }

    @Override
    public void setColorFilter(final ColorFilter cf) {
        if (isThemed()) {
            super.setColorFilter(null);
        } else {
            super.setColorFilter(cf);
        }
    }

    @Override
    public Drawable getDrawable() {
        return mOriginalDrawable;
    }
}

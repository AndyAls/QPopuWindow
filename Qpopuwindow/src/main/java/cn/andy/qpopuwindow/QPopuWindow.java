package cn.andy.qpopuwindow;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.opengl.Visibility;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 仿qq消息长按弹窗，复制，粘贴，转发，，，，，，
 *
 * @author andy
 * <http>https://blog.csdn.net/andy_l1
 */
 public class QPopuWindow extends PopupWindow {

    private static final int DEFAULT_NORMAL_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_PRESSED_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_PADDING_LEFT = 16;
    private static final int DEFAULT_TEXT_PADDING_TOP = 6;
    private static final int DEFAULT_TEXT_PADDING_RIGHT = 16;
    private static final int DEFAULT_TEXT_PADDING_BOTTOM = 6;
    private static final int DEFAULT_NORMAL_BACKGROUND_COLOR = 0xCC000000;
    private static final int DEFAULT_PRESSED_BACKGROUND_COLOR = 0xE7777777;
    private static final int DEFAULT_BACKGROUND_RADIUS = 5;
    private static final int DEFAULT_DIVIDER_COLOR = 0x9AFFFFFF;
    private static final int DEFAULT_DIVIDER_WIDTH = 1;
    private static final int DEFAULT_TEXT_DRAWABLE_SIZE = 24;

    private Context mContext;
    private GradientDrawable mCornerBackground;
    private StateListDrawable mLeftItemBackground;
    private StateListDrawable mRightItemBackground;
    private StateListDrawable mCornerItemBackground;
    public Builder builder;
    private static QPopuWindow popupList = null;
    private ColorStateList mTextColorStateList;
    private int mRawX;
    private int mRawY;

    private QPopuWindow(Context context) {
        super(context);
        this.mContext = context;
        builder = new Builder();
    }

    public static synchronized QPopuWindow getInstance(Context context) {
        if (popupList == null) {
            popupList = new QPopuWindow(context);
        }
        return popupList;
    }


    private class Config {

        private int normalTextColor = DEFAULT_NORMAL_TEXT_COLOR;
        private int pressedTextColor = DEFAULT_PRESSED_TEXT_COLOR;
        private int textSize = DEFAULT_TEXT_SIZE;
        private int textPaddingLeft = dp2px(DEFAULT_TEXT_PADDING_LEFT);
        private int textPaddingTop = dp2px(DEFAULT_TEXT_PADDING_TOP);
        private int textPaddingRight = dp2px(DEFAULT_TEXT_PADDING_RIGHT);
        private int textPaddingBottom = dp2px(DEFAULT_TEXT_PADDING_BOTTOM);
        private int normalBackgroundColor = DEFAULT_NORMAL_BACKGROUND_COLOR;
        private int pressedBackgroundColor = DEFAULT_PRESSED_BACKGROUND_COLOR;
        private int radius = dp2px(DEFAULT_BACKGROUND_RADIUS);
        private int dividerColor = DEFAULT_DIVIDER_COLOR;
        private int dividerWidth = dp2px(DEFAULT_DIVIDER_WIDTH);
        private int textDrawableSize = dp2px(DEFAULT_TEXT_DRAWABLE_SIZE);

        private float indicatorViewWidth = dp2px(18);
        private float indicatorViewHeight = dp2px(9);
        private PopupWindow mPopupWindow = null;
        private List<String> mPopupItemList;
        private OnPopuListItemClickListener mListener;
        private int position;
        private View mAnchorView;
        private int mPopupWindowWidth;
        private int mPopupWindowHeight;

        private View mIndicatorView;
        private int mIndicatorWidth;
        private int mIndicatorHeight;
        private List<Drawable> textDrawableList;
        private boolean visibility=true;

        private Config() {
            mIndicatorView = getDefaultIndicatorView(mContext, normalBackgroundColor, indicatorViewWidth, indicatorViewHeight);
        }
    }

    public class Builder {

        private Config config;

        private Builder() {
            config = new Config();

        }


        /**
         * 绑定anchorView <b>必须调用</b>
         *
         * @param anchorView anchorView
         * @param position   anchorView的条目位置,通过回调返回
         */
        public Builder bindView(View anchorView, int position) {

            config.position = position;
            config.mAnchorView = anchorView;
            return builder;
        }


        /**
         * 设置数据源 <b>此方法必须调用
         */
        public Builder setPopupItemList(String[] itemDataSource) {
            if (itemDataSource != null) {
                config.mPopupItemList = new ArrayList<>();
                config.mPopupItemList.clear();
                config.mPopupItemList.addAll(Arrays.asList(itemDataSource));
            }
            return builder;
        }

        /**
         * 设置触摸屏幕的绝对位置  <b>必须调用</b>
         *
         * @param rawX x坐标
         * @param rawY y坐标
         */
        public Builder setPointers(int rawX, int rawY) {
            mRawX = rawX;
            mRawY = rawY;
            return builder;
        }

        /**
         * 设置item单元的padding
         */
        public Builder setTextPadding(int left, int top, int right, int bottom) {
            config.textPaddingLeft = left;
            config.textPaddingTop = top;
            config.textPaddingRight = right;
            config.textPaddingBottom = bottom;
            return this;
        }

        /**
         * 设置item字体大小
         */
        public Builder setTextSize(int size) {
            config.textSize = size;
            return builder;
        }

        /**
         * 设置弹窗圆角
         */
        public Builder setRadius(int radius) {
            config.radius = radius;
            return builder;
        }

        /**
         * 设置指针大小
         */
        public Builder setIndicatorViewSize(int width, int height) {
            config.indicatorViewWidth = width;
            config.indicatorViewHeight = height;
            return builder;
        }

        /**
         * 设置item 单元点击状态颜色
         */
        public Builder setPressedBackgroundColor(@ColorInt int color) {
            config.pressedBackgroundColor = color;
            return builder;
        }

        /**
         * 设置item 单元正常状态颜色
         */
        public Builder setNormalBackgroundColor(@ColorInt int color) {
            config.normalBackgroundColor = color;
            return builder;
        }

        /**
         * 设置item 单元的字体颜色
         */
        public Builder setTextColor(@ColorInt int color) {
            config.normalTextColor = color;
            return builder;
        }

        /**
         * 设置item 单元的图标,默认在字体上方
         *
         * @param drawableRes 建议和item的长度设置一样,一一对应
         * @see #setPopupItemList(String[])
         */
        public Builder setTextDrawableRes(@DrawableRes Integer[] drawableRes) {
            if (drawableRes != null) {
                List<Integer> drawables = Arrays.asList(drawableRes);
                config.textDrawableList = new ArrayList<>();
                config.textDrawableList.clear();
                for (int i = 0; i < drawables.size(); i++) {
                    Drawable drawable = mContext.getResources().getDrawable(drawables.get(i));
                    config.textDrawableList.add(drawable);
                }
            }

            return builder;
        }

        /**
         * 设置item 单元的图标大小
         *
         * @param size 默认24px
         */
        public Builder setTextDrawableSize(int size) {
            config.textDrawableSize = size;
            return builder;
        }

        /**
         * item 单元点击回调
         */
        public Builder setOnPopuListItemClickListener(OnPopuListItemClickListener listener) {
            config.mListener = listener;
            return builder;
        }

        /**
         * 设置item 分割线是否可见
         * @param visibility  true 可见, false 不可见 默认是true
         * @return this
         */
        public Builder setDividerVisibility(boolean visibility){

            config.visibility=visibility;
            return builder;
        }
        /**
         * 弹窗显示
         */
        public void show() {

            checkAnchorView();

            if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
                return;
            }

            if (config.mPopupWindow == null) {

                setPopupListBgAndRadius(config);
                setTextColorStateList(config);

                LinearLayout contentView = createContentView();
                LinearLayout popupListContainer = createContainerView();
                contentView.addView(popupListContainer);

                if (config.mIndicatorView != null) {
                    addIndicatorView(contentView);
                }

                if (config.mPopupItemList == null) {
                    throw new NullPointerException("QPopuWindow item dataSources is null,please make sure (Builder)setPopupItemList() invoked");
                } else {
                    addPopuListItem(popupListContainer);

                }

                if (config.mPopupWindowWidth == 0) {
                    config.mPopupWindowWidth = getViewWidth(popupListContainer);
                }

                if (config.mIndicatorView != null && config.mIndicatorWidth == 0) {
                    if (config.mIndicatorView.getLayoutParams().width > 0) {
                        config.mIndicatorWidth = config.mIndicatorView.getLayoutParams().width;
                    } else {
                        config.mIndicatorWidth = getViewWidth(config.mIndicatorView);
                    }
                }
                if (config.mIndicatorView != null && config.mIndicatorHeight == 0) {
                    if (config.mIndicatorView.getLayoutParams().height > 0) {
                        config.mIndicatorHeight = config.mIndicatorView.getLayoutParams().height;
                    } else {
                        config.mIndicatorHeight = getViewHeight(config.mIndicatorView);
                    }
                }
                if (config.mPopupWindowHeight == 0) {
                    config.mPopupWindowHeight = getViewHeight(popupListContainer) + config.mIndicatorHeight;
                }
                config.mPopupWindow = new PopupWindow(contentView, config.mPopupWindowWidth, config.mPopupWindowHeight, true);
                config.mPopupWindow.setTouchable(true);
                config.mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            }
            if (config.mIndicatorView != null) {
                setIndicatorLoaction();
            }
            if (!config.mPopupWindow.isShowing()) {
                config.mPopupWindow.showAtLocation(config.mAnchorView, Gravity.CENTER,
                        mRawX - getScreenWidth(mContext) / 2,
                        mRawY - getScreenHeight(mContext) / 2 - config.mPopupWindowHeight);

                //备用 弹窗显示的位置,如果手指触摸屏幕的位置给的不准确,弹窗的位置会偏移过大, 这个可以准确显示在anchorView正上方
                /*config.mPopupWindow.showAsDropDown(config.mAnchorView, 0,
                        (int) -(getViewHeight(config.mAnchorView) + config.mPopupWindowHeight + config.indicatorViewHeight));*/
            }
            config.mPopupWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    release();
                }
            });
        }


        /**
         * 动态设置指针的位置
         */
        private void setIndicatorLoaction() {
            float marginLeftScreenEdge = mRawX;
            float marginRightScreenEdge = getScreenWidth(mContext) - mRawX;
            if (marginLeftScreenEdge < config.mPopupWindowWidth / 2f) {
                // in case of the draw of indicator out of corner's bounds
                if (marginLeftScreenEdge < config.mIndicatorWidth / 2f + config.radius) {
                    config.mIndicatorView.setTranslationX(config.mIndicatorWidth / 2f + config.radius - config.mPopupWindowWidth / 2f);
                } else {
                    config.mIndicatorView.setTranslationX(marginLeftScreenEdge - config.mPopupWindowWidth / 2f);
                }
            } else if (marginRightScreenEdge < config.mPopupWindowWidth / 2f) {
                if (marginRightScreenEdge < config.mIndicatorWidth / 2f + config.radius) {
                    config.mIndicatorView.setTranslationX(config.mPopupWindowWidth / 2f - config.mIndicatorWidth / 2f - config.radius);
                } else {
                    config.mIndicatorView.setTranslationX(config.mPopupWindowWidth / 2f - marginRightScreenEdge);
                }
            } else {
                config.mIndicatorView.setTranslationX(0);
            }
        }

        /**
         * @param popupListContainer item 布局
         *                           <br>
         *                           添加pop item
         */
        private void addPopuListItem(LinearLayout popupListContainer) {
            for (int i = 0; i < config.mPopupItemList.size(); i++) {
                TextView textView = new TextView(mContext);
                textView.setTextColor(mTextColorStateList);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.textSize);
                textView.setPadding(config.textPaddingLeft, config.textPaddingTop, config.textPaddingRight, config.textPaddingBottom);
                textView.setClickable(true);
                textView.setGravity(Gravity.CENTER);
                textView.setText(config.mPopupItemList.get(i));
                //处理文字和图标的关系,建议文字和图片设置时一一对应
                if (config.textDrawableList != null && config.textDrawableList.size() > 0) {
                    Drawable drawable = null;
                    if (config.textDrawableList.size() >= config.mPopupItemList.size()) {
                        drawable = config.textDrawableList.get(i);
                    } else {
                        if (i < config.textDrawableList.size()) {
                            drawable = config.textDrawableList.get(i);
                        } else {
                            drawable = config.textDrawableList.get(config.textDrawableList.size() - 1);
                        }
                    }
                    drawable.setBounds(0, 0, config.textDrawableSize, config.textDrawableSize);
                    textView.setCompoundDrawables(null, drawable, null, null);
                    textView.setCompoundDrawablePadding(2);
                }
                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (config.mListener != null) {
                            config.mListener.onPopuListItemClick(config.mAnchorView, config.position, finalI);
                        }
                        hidePopupListWindow(config);
                    }
                });
                if (config.mPopupItemList.size() > 1 && i == 0) {
                    textView.setBackground(mLeftItemBackground);
                } else if (config.mPopupItemList.size() > 1 && i == config.mPopupItemList.size() - 1) {
                    textView.setBackground(mRightItemBackground);
                } else if (config.mPopupItemList.size() == 1) {
                    textView.setBackground(mCornerItemBackground);
                } else {
                    textView.setBackground(getCenterItemBackground(config));
                }
                popupListContainer.addView(textView);
                if (config.visibility){
                    if (config.mPopupItemList.size() > 1 && i != config.mPopupItemList.size() - 1) {
                        View divider = new View(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(config.dividerWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                        layoutParams.gravity = Gravity.CENTER;
                        divider.setLayoutParams(layoutParams);
                        divider.setBackgroundColor(config.dividerColor);
                        popupListContainer.addView(divider);
                    }
                }

            }
        }

        /**
         * @param contentView 父布局
         *                    <br>
         *                    添加指针
         */
        private void addIndicatorView(LinearLayout contentView) {
            LinearLayout.LayoutParams layoutParams;
            if (config.mIndicatorView.getLayoutParams() == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = (LinearLayout.LayoutParams) config.mIndicatorView.getLayoutParams();
            }
            layoutParams.gravity = Gravity.CENTER;
            config.mIndicatorView.setLayoutParams(layoutParams);
            ViewParent viewParent = config.mIndicatorView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeView(config.mIndicatorView);
            }
            contentView.addView(config.mIndicatorView);
        }

        /**
         * 不绑定anchorView 主动抛异常
         *
         * @see #bindView(View, int)
         */
        private void checkAnchorView() {

            if (config.mAnchorView == null) {
                throw new NullPointerException("QPopuWindow AnchorView is null,please make sure (Builder)bindView() invoked");
            }
        }
    }

    /**
     * @return 绘制 pop item布局
     */
    @NonNull
    private LinearLayout createContainerView() {
        LinearLayout popupListContainer = new LinearLayout(mContext);
        popupListContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        popupListContainer.setOrientation(LinearLayout.HORIZONTAL);
        popupListContainer.setBackground(mCornerBackground);
        return popupListContainer;
    }


    /**
     * @return 绘制父布局
     */
    @NonNull
    private LinearLayout createContentView() {
        LinearLayout contentView = new LinearLayout(mContext);
        contentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        contentView.setOrientation(LinearLayout.VERTICAL);
        return contentView;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private void setTextColorStateList(Config config) {
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        int[] colors = new int[]{config.pressedTextColor, config.normalTextColor};
        mTextColorStateList = new ColorStateList(states, colors);
    }

    /**
     * 绘制背景和圆角
     */
    private void setPopupListBgAndRadius(Config config) {
        // left
        GradientDrawable leftItemPressedDrawable = new GradientDrawable();
        leftItemPressedDrawable.setColor(config.pressedBackgroundColor);
        leftItemPressedDrawable.setCornerRadii(new float[]{
                config.radius, config.radius,
                0, 0,
                0, 0,
                config.radius, config.radius});
        GradientDrawable leftItemNormalDrawable = new GradientDrawable();
        leftItemNormalDrawable.setColor(Color.TRANSPARENT);
        leftItemNormalDrawable.setCornerRadii(new float[]{
                config.radius, config.radius,
                0, 0,
                0, 0,
                config.radius, config.radius});
        mLeftItemBackground = new StateListDrawable();
        mLeftItemBackground.addState(new int[]{android.R.attr.state_pressed}, leftItemPressedDrawable);
        mLeftItemBackground.addState(new int[]{}, leftItemNormalDrawable);
        // right
        GradientDrawable rightItemPressedDrawable = new GradientDrawable();
        rightItemPressedDrawable.setColor(config.pressedBackgroundColor);
        rightItemPressedDrawable.setCornerRadii(new float[]{
                0, 0,
                config.radius, config.radius,
                config.radius, config.radius,
                0, 0});
        GradientDrawable rightItemNormalDrawable = new GradientDrawable();
        rightItemNormalDrawable.setColor(Color.TRANSPARENT);
        rightItemNormalDrawable.setCornerRadii(new float[]{
                0, 0,
                config.radius, config.radius,
                config.radius, config.radius,
                0, 0});
        mRightItemBackground = new StateListDrawable();
        mRightItemBackground.addState(new int[]{android.R.attr.state_pressed}, rightItemPressedDrawable);
        mRightItemBackground.addState(new int[]{}, rightItemNormalDrawable);
        // corner
        GradientDrawable cornerItemPressedDrawable = new GradientDrawable();
        cornerItemPressedDrawable.setColor(config.pressedBackgroundColor);
        cornerItemPressedDrawable.setCornerRadius(config.radius);
        GradientDrawable cornerItemNormalDrawable = new GradientDrawable();
        cornerItemNormalDrawable.setColor(Color.TRANSPARENT);
        cornerItemNormalDrawable.setCornerRadius(config.radius);
        mCornerItemBackground = new StateListDrawable();
        mCornerItemBackground.addState(new int[]{android.R.attr.state_pressed}, cornerItemPressedDrawable);
        mCornerItemBackground.addState(new int[]{}, cornerItemNormalDrawable);
        mCornerBackground = new GradientDrawable();
        mCornerBackground.setColor(config.normalBackgroundColor);
        mCornerBackground.setCornerRadius(config.radius);
    }

    /**
     * 绘制指针
     */
    private View getDefaultIndicatorView(Context context, final int normalBackgroundColor, final float indicatorViewWidth, final float indicatorViewHeight) {
        ImageView indicator = new ImageView(context);
        Drawable drawable = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Path path = new Path();
                Paint paint = new Paint();
                paint.setColor(normalBackgroundColor);
                paint.setStyle(Paint.Style.FILL);
                path.moveTo(0f, 0f);
                path.lineTo(indicatorViewWidth, 0f);
                path.lineTo(indicatorViewWidth / 2, indicatorViewHeight);
                path.close();
                canvas.drawPath(path, paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSLUCENT;
            }

            @Override
            public int getIntrinsicWidth() {
                return (int) indicatorViewWidth;
            }

            @Override
            public int getIntrinsicHeight() {
                return (int) indicatorViewHeight;
            }
        };
        indicator.setImageDrawable(drawable);
        return indicator;
    }


    private void hidePopupListWindow(Config config) {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        if (config.mPopupWindow != null && config.mPopupWindow.isShowing()) {
            config.mPopupWindow.dismiss();
            config.mPopupWindow = null;
        }
    }

    /**
     * 绘制中间view的背景
     *
     * @param config config
     * @return centerItemBackground
     */
    private StateListDrawable getCenterItemBackground(Config config) {
        StateListDrawable centerItemBackground = new StateListDrawable();
        GradientDrawable centerItemPressedDrawable = new GradientDrawable();
        centerItemPressedDrawable.setColor(config.pressedBackgroundColor);
        GradientDrawable centerItemNormalDrawable = new GradientDrawable();
        centerItemNormalDrawable.setColor(Color.TRANSPARENT);
        centerItemBackground.addState(new int[]{android.R.attr.state_pressed}, centerItemPressedDrawable);
        centerItemBackground.addState(new int[]{}, centerItemNormalDrawable);
        return centerItemBackground;
    }

    private int getViewWidth(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return view.getMeasuredWidth();
    }

    private int getViewHeight(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return view.getMeasuredHeight();
    }

    /**
     * 弹框消失时释放一些资源
     */
    private void release() {
        if (mContext != null) {
            mContext = null;
        }
        if (popupList != null) {
            popupList = null;
        }
        if (builder != null) {
            builder = null;
        }
    }


    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, mContext.getResources().getDisplayMetrics());
    }

    public interface OnPopuListItemClickListener {

        /**
         * @param anchorView         pop绑定的view
         * @param anchorViewPosition 绑定view的position
         * @param position           popListItem  position
         */
        void onPopuListItemClick(View anchorView, int anchorViewPosition, int position);
    }
}

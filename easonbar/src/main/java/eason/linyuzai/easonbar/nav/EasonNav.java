package eason.linyuzai.easonbar.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import eason.linyuzai.easonbar.entity.EasonEntity;
import eason.linyuzai.easonbar.nav.component.EasonNavActionViewClickListener;
import eason.linyuzai.easonbar.nav.component.EasonNavAdapter;
import eason.linyuzai.easonbar.nav.component.EasonNavController;
import eason.linyuzai.easonbar.nav.component.NavLocation;

/**
 * Created by linyuzai on 2018/5/9.
 *
 * @author linyuzai
 */

public class EasonNav extends FrameLayout implements View.OnClickListener {

    public static final int ACTION_BUTTON_LEFT_LEFT = 0;
    public static final int ACTION_BUTTON_LEFT_RIGHT = 1;
    public static final int ACTION_BUTTON_RIGHT_LEFT = 2;
    public static final int ACTION_BUTTON_RIGHT_RIGHT = 3;
    public static final int ACTION_BUTTON_CENTER = 4;

    private ActionView leftLeftView;
    private ActionView leftRightView;
    private ActionView rightLeftView;
    private ActionView rightRightView;
    private ActionView centerView;

    private EasonNavAdapter adapter;

    private EasonNavController controller = new Controller();

    private EasonNavActionViewClickListener listener;

    public EasonNav(@NonNull Context context) {
        super(context);
        initialize();
    }

    public EasonNav(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public EasonNav(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EasonNav(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        setBackgroundColor(Color.WHITE);
    }

    public EasonNavController getController() {
        return controller;
    }

    public EasonNavActionViewClickListener getOnActionButtonClickListener() {
        return listener;
    }

    public void setOnActionButtonClickListener(EasonNavActionViewClickListener listener) {
        this.listener = listener;
    }

    public ActionView getActionButton(int location) {
        switch (location) {
            case ACTION_BUTTON_LEFT_LEFT:
                return leftLeftView;
            case ACTION_BUTTON_LEFT_RIGHT:
                return leftRightView;
            case ACTION_BUTTON_RIGHT_LEFT:
                return rightLeftView;
            case ACTION_BUTTON_RIGHT_RIGHT:
                return rightRightView;
            case ACTION_BUTTON_CENTER:
                return centerView;
        }
        return null;
    }

    private class Controller implements EasonNavController {

        private Entity leftLeftEntity;
        private Entity leftRightEntity;
        private Entity rightLeftEntity;
        private Entity rightRightEntity;
        private Entity centerEntity;

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Entity> T getEntity(@NavLocation int location) {
            switch (location) {
                case ACTION_BUTTON_LEFT_LEFT:
                    return (T) leftLeftEntity;
                case ACTION_BUTTON_LEFT_RIGHT:
                    return (T) leftRightEntity;
                case ACTION_BUTTON_RIGHT_LEFT:
                    return (T) rightLeftEntity;
                case ACTION_BUTTON_RIGHT_RIGHT:
                    return (T) rightRightEntity;
                case ACTION_BUTTON_CENTER:
                    return (T) centerEntity;
                default:
                    return null;
            }
        }

        @Override
        public <T extends Entity> void setEntity(T entity, @NavLocation int location) {
            switch (location) {
                case ACTION_BUTTON_LEFT_LEFT:
                    leftLeftEntity = entity;
                case ACTION_BUTTON_LEFT_RIGHT:
                    leftRightEntity = entity;
                case ACTION_BUTTON_RIGHT_LEFT:
                    rightLeftEntity = entity;
                case ACTION_BUTTON_RIGHT_RIGHT:
                    rightRightEntity = entity;
                case ACTION_BUTTON_CENTER:
                    centerEntity = entity;
            }
        }

        @Override
        public <T extends Entity> void setEntityWithLayout(T entity, @NavLocation int location) {
            setEntity(entity, location);
            update(location);
        }

        @Override
        public void update(@NavLocation int location) {
            switch (location) {
                case ACTION_BUTTON_LEFT_LEFT:
                    if (leftLeftView != null && !leftLeftView.isInitialized()) {
                        addView(leftLeftView);
                        leftLeftView.setInitialized(true);
                    }
                    break;
                case ACTION_BUTTON_LEFT_RIGHT:
                    if (leftRightView != null && !leftRightView.isInitialized()) {
                        addView(leftRightView);
                        leftRightView.setInitialized(true);
                    }
                    break;
                case ACTION_BUTTON_RIGHT_LEFT:
                    if (rightLeftView != null && !rightLeftView.isInitialized()) {
                        addView(rightLeftView);
                        rightLeftView.setInitialized(true);
                    }
                    break;
                case ACTION_BUTTON_RIGHT_RIGHT:
                    if (rightRightView != null && !rightRightView.isInitialized()) {
                        addView(rightRightView);
                        rightRightView.setInitialized(true);
                    }
                    break;
                case ACTION_BUTTON_CENTER:
                    if (centerView != null && !centerView.isInitialized()) {
                        addView(centerView);
                        centerView.setInitialized(true);
                    }
                    break;
            }
        }

        @Override
        public void update() {
            update(ACTION_BUTTON_LEFT_LEFT);
            update(ACTION_BUTTON_LEFT_RIGHT);
            update(ACTION_BUTTON_RIGHT_LEFT);
            update(ACTION_BUTTON_RIGHT_RIGHT);
            update(ACTION_BUTTON_CENTER);
        }
    }

    public static class Entity extends EasonEntity {
        private static final String BACKGROUND_DRAWABLE = "background_drawable";
        private static final String MARGIN_LEFT = "margin_left";
        private static final String MARGIN_RIGHT = "margin_right";

        public Drawable getBackgroundDrawable() {
            return (Drawable) get(getKey(BACKGROUND_DRAWABLE));
        }

        public void setBackgroundDrawable(Drawable drawable) {
            add(getKey(BACKGROUND_DRAWABLE), drawable);
        }

        public int getMarginLeft() {
            Object left = get(getKey(MARGIN_LEFT));
            if (left == null)
                return 0;
            return (int) left;
        }

        public void setMarginLeft(int marginLeft) {
            add(getKey(MARGIN_LEFT), marginLeft);
        }

        public int getMarginRight() {
            Object right = get(getKey(MARGIN_RIGHT));
            if (right == null)
                return 0;
            return (int) right;
        }

        public void setMarginRight(int marginRight) {
            add(getKey(MARGIN_RIGHT), marginRight);
        }
    }

    public static class TextEntity extends Entity {
        private static final String TEXT = "text";
        private static final String TEXT_COLOR = "text_color";
        private static final String TEXT_SIZE = "text_size";
        private static final String PADDING_LEFT = "padding_left";
        private static final String PADDING_RIGHT = "padding_right";

        public TextEntity() {
            super();
            setText("");
            setTextColor(Color.BLACK);
            setTextSize(14f);
            setPaddingLeft(0);
            setPaddingRight(0);
        }

        public String getText() {
            return (String) get(getKey(TEXT));
        }

        public void setText(String text) {
            add(getKey(TEXT), text);
        }

        public int getTextColor() {
            Object color = get(getKey(TEXT_COLOR));
            if (color == null)
                return Color.BLACK;
            return (int) color;
        }

        public void setTextColor(int color) {
            add(getKey(TEXT_COLOR), color);
        }

        public float getTextSize() {
            Object size = get(getKey(TEXT_SIZE));
            if (size == null)
                return 14f;
            return (float) size;
        }

        public void setTextSize(float textSize) {
            add(getKey(TEXT_SIZE), textSize);
        }

        public int getPaddingLeft() {
            Object left = get(getKey(PADDING_LEFT));
            if (left == null)
                return 0;
            return (int) left;
        }

        public void setPaddingLeft(int paddingLeft) {
            add(getKey(PADDING_LEFT), paddingLeft);
        }

        public int getPaddingRight() {
            Object right = get(getKey(PADDING_RIGHT));
            if (right == null)
                return 0;
            return (int) right;
        }

        public void setPaddingRight(int paddingRight) {
            add(getKey(PADDING_RIGHT), paddingRight);
        }
    }

    public static class ImageEntity extends Entity {
        private static final String DRAWABLE = "drawable";
        private static final String RATIO = "ratio";

        public Drawable getDrawable() {
            return (Drawable) get(getKey(DRAWABLE));
        }

        public void setDrawable(Drawable drawable) {
            add(getKey(DRAWABLE), drawable);
        }

        public float getRatio() {
            Object ratio = get(getKey(RATIO));
            if (ratio == null)
                return 0.7f;
            return (float) ratio;
        }

        public void setRatio(float ratio) {
            add(getKey(RATIO), ratio);
        }
    }

    public static class ActionView<T extends Entity> extends FrameLayout {

        private int location;

        private T entity;

        private boolean isInitialized;

        public ActionView(@NonNull Context context) {
            super(context);
        }

        public ActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public ActionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ActionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(@NavLocation int location) {
            this.location = location;
        }

        public ActionView setEntity(T entity) {
            this.entity = entity;
            return this;
        }

        public T getEntity() {
            return entity;
        }

        public boolean isInitialized() {
            return isInitialized;
        }

        public void setInitialized(boolean initialized) {
            isInitialized = initialized;
        }

        @SuppressLint("RtlHardcoded")
        public void initialize(int height, @NavLocation int location) {
            this.location = location;
            int gravity = Gravity.NO_GRAVITY;
            int marginLeft = entity.getMarginLeft();
            int marginRight = entity.getMarginRight();
            switch (location) {
                case ACTION_BUTTON_LEFT_LEFT:
                    gravity = Gravity.LEFT;
                    break;
                case ACTION_BUTTON_LEFT_RIGHT:
                    gravity = Gravity.LEFT;
                    marginLeft += height;
                    break;
                case ACTION_BUTTON_RIGHT_LEFT:
                    gravity = Gravity.RIGHT;
                    marginRight += height;
                    break;
                case ACTION_BUTTON_RIGHT_RIGHT:
                    gravity = Gravity.RIGHT;
                    break;
                case ACTION_BUTTON_CENTER:
                    gravity = Gravity.CENTER;
                    break;
            }
            int width = height;
            if (entity instanceof TextEntity)
                width = LayoutParams.WRAP_CONTENT;
            LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT, gravity);
            params.leftMargin = marginLeft;
            params.rightMargin = marginRight;
            setLayoutParams(params);
            setBackgroundDrawable(entity.getBackgroundDrawable());
        }
    }

    public static class TextActionView extends ActionView<TextEntity> {

        private TextView text;

        public TextActionView(@NonNull Context context) {
            super(context);
        }

        public TextActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TextActionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TextActionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void initialize(int height, int location) {
            super.initialize(height, location);
            TextEntity entity = getEntity();
            text = new TextView(getContext());
            text.setText(entity.getText());
            text.setTextColor(entity.getTextColor());
            text.setTextSize(entity.getTextSize());
            text.setPadding(entity.getPaddingLeft(), 0, entity.getPaddingRight(), 0);
            addView(text, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        }

        public TextView getTextView() {
            return text;
        }
    }

    public static class ImageActionView extends ActionView<ImageEntity> {

        private ImageView image;

        public ImageActionView(@NonNull Context context) {
            super(context);
        }

        public ImageActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public ImageActionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ImageActionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void initialize(int height, int location) {
            super.initialize(height, location);
            ImageEntity entity = getEntity();
            image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image.setImageDrawable(entity.getDrawable());
            int widthAndHeight = (int) (height * entity.getRatio());
            addView(image, new LayoutParams(widthAndHeight, widthAndHeight, Gravity.CENTER));
        }

        public ImageView getImageView() {
            return image;
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null && v instanceof ActionView) {
            listener.onActionButtonClick((ActionView) v, ((ActionView) v).getLocation());
        }
    }
}

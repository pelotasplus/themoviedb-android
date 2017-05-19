package pl.pelotasplus.themoviedb.demo;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CustomDataBindings {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            Picasso
                    .with(view.getContext())
                    .load(url)
                    .into(view);
        }
    }
}

package com.example.hp.demouplayout.adapter;

import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.hp.demouplayout.CategoryInterface;
import com.example.hp.demouplayout.ClubRequestManager;
import com.example.hp.demouplayout.R;
import com.example.hp.demouplayout.entities.Category;

import java.util.List;

/**
 * Created by kelvin on 07/09/16.
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.CustomViewHolder> {

    private List<Category> categoryList;
    CategoryInterface categoryInterface;

    public MenuListAdapter(CategoryInterface interf, List<Category> categories) {
        this.categoryInterface = interf;
        this.categoryList = categories;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_row, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final Category category = categoryList.get(position);

        holder.textView.setText(category.getNombre().toUpperCase());
        //holder.imageView.setImageDrawable(category.getDrawable());

        if(position == 0)
        {
            holder.imageView.setImageDrawable(ResourcesCompat.getDrawable(holder.imageView.getResources(), category.getDrawable(), null));

        }else {

            ImageLoader imageLoader = ClubRequestManager.getInstance(holder.textView.getContext()).getImageLoader();

            //holder.imageView.setImageUrl(category.getIcono(), imageLoader);

            imageLoader.get(category.getIcono(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {

                        holder.imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryInterface.getPlacesFromServer(category.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;

        public CustomViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.text);
            imageView = (ImageView) view.findViewById(R.id.image);

        }
    }
}
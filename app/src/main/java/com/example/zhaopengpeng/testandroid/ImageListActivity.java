package com.example.zhaopengpeng.testandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/*****************************************
 * 文  件： 
 * 描  述： 
 * 作  者： zhaopengpeng 
 * 日  期： 2017/11/25
 *****************************************/

public class ImageListActivity extends AppCompatActivity implements RequestListener<String,GlideDrawable>{

    private String[] imageUrls = {
            "https://static.baydn.com/media/media_store/image/f1672263006c6e28bb9dee7652fa4cf6.jpg",
            "https://static.baydn.com/media/media_store/image/8c997fae9ebb2b22ecc098a379cc2ca3.jpg",
            "https://static.baydn.com/media/media_store/image/2a4616f067285b4bd59fe5401cd7106b.jpeg",
            "https://static.baydn.com/media/media_store/image/b0e3ab329c8d8218d2af5c8dfdc21125.jpg",
            "https://static.baydn.com/media/media_store/image/670abb28408a9a0fc3dd9666e5ca1584.jpeg",
            "https://static.baydn.com/media/media_store/image/1e8d675468ab61f4e5bdebd4bcb5f007.jpeg",
            "https://static.baydn.com/media/media_store/image/9b2f93cbfa104dae1e67f540ff14a4c2.jpg",
            "https://static.baydn.com/media/media_store/image/f5e0631e00a09edbbf2eb21eb71b4d3c.jpeg"
    };

    public static void startActivity(Context context){
        context.startActivity(new Intent(context, ImageListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_list_activity);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter adapter = new ListAdapter();
        mRecyclerView.setAdapter(adapter);
        setTitle("图片列表");
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ImageListActivity.this).inflate(R.layout.image_list_item, parent, false));
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(ImageListActivity.this)
                    .load(imageUrls[position])
                    .placeholder(R.drawable.place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.NORMAL)
                    .crossFade()
                    .listener(ImageListActivity.this)
                    .error(R.drawable.place_holder)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imageUrls.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            }

        }
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        return false;
    }
}
package com.andhradroid.app.staggergridview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private final Context mContext;
    private List<String> mData;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public void add(String s, int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView image;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.simple_text);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    List<Data.Holder> images;

    public SimpleAdapter(Context context, String[] data) {
        mContext = context;
        Gson gson = new Gson();
        try {
            InputStream ios = mContext.getAssets().open("data.json");
            Type collectionType = new TypeToken<Collection<Data.Holder>>() {
            }.getType();
            images = gson.fromJson(new InputStreamReader(ios), collectionType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true).build();
        ;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options).enableLogging().build();
        config.createDefault(mContext);
        ImageLoader.getInstance().init(config);

//        ImageLoader.getInstance().displayImage("http://asset.whatsayapp.com/server/cue_bg/bali_inside.jpg", image);
        if (data != null)
            mData = new ArrayList<String>(Arrays.asList(data));
        else mData = new ArrayList<String>();
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.simple_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        double positionHeight = getPositionRatio(position);
        holder.title.setTextColor(Color.RED);

        if (position % 5 != 0) {
            holder.image.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.GONE);
//            ((DynamicHeightImageView) holder.itemView).setHeightRatio(positionHeight);
            ((DynamicHeightImageView1) holder.image).setHeightRatio(positionHeight);
        } else {
//            ((DynamicHeightImageView) holder.itemView).setHeightRatio(0.0);
            ((DynamicHeightImageView1) holder.image).setHeightRatio(0.0);
            holder.image.setVisibility(View.GONE);
            holder.title.setVisibility(View.VISIBLE);
        }
        //
        holder.title.setText(mData.get(position));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Position =" + position, Toast.LENGTH_SHORT).show();
            }
        });
//        if (position % 2 == 0) {
////            holder.itemView.setBackgroundColor(Color.BLUE);
//            holder.image.setImageResource(R.drawable.ic_launcher);
//        } else {
////            holder.itemView.setBackgroundColor(Color.YELLOW);
//            holder.image.setImageResource(R.drawable.profile);
//        }
        if (position < images.size()) {
            ImageLoader.getInstance().displayImage(images.get(position).getImageFilename(), holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher);
        }

        if (position % 5 == 0) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(false);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d("'", "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private final Random mRandom = new Random();

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
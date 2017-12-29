package com.example.howldevelopment.bcofficial.adapters;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.howldevelopment.bcofficial.R;
import com.example.howldevelopment.bcofficial.models.api_classes.EventClass;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.vstechlab.easyfonts.EasyFonts;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import jp.wasabeef.fresco.processors.BlurPostprocessor;

/**
 * Created by Marno on 2017/11/01.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private Context context;
    private ArrayList<EventClass> events;
    private LayoutInflater mInflater;

    public EventsAdapter(Context context,ArrayList<EventClass> events) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_item_1, parent, false);
        EventViewHolder holder = new EventViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        LocalDate localeDate = new LocalDate(events.get(position).date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
        String date = localeDate.getDayOfMonth() + " " + formatter.print(localeDate) + " " + localeDate.getYear();
        holder.tvDate.setText(date);

        holder.tvName.setText(events.get(position).name);
        if (events.get(position).cost.equals("0")) {
            holder.tvCost.setText("Free entrance");
        } else {
            holder.tvCost.setText("R" + events.get(position).cost + " Per Person");
        }

        holder.tvInterested.setText(events.get(position).interested + " People interested");

        ImageRequest request;
        if (events.get(position).postprocessing.equals("0")) {
            Uri uri = Uri.parse(context.getResources().getString(R.string.url_images) + events.get(position).picture);
            request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(400,225))
                    .build();
        } else {
            Postprocessor postprocessor = new BlurPostprocessor(context,10);
            Uri uri = Uri.parse(context.getResources().getString(R.string.url_images) + events.get(position).picture);
            request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(400,225))
                    .setPostprocessor(postprocessor)
                    .build();
        }

        holder.imgPoster.setController(Fresco.newDraweeControllerBuilder()
        .setOldController(holder.imgPoster.getController())
        .setImageRequest(request).build());

        //holder.imgPoster.setImageURI(uri);
        PointF focusPoint = new PointF(0.5f, 0.5f);
        holder.imgPoster.getHierarchy().setActualImageFocusPoint(focusPoint);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDate;
        TextView tvCost;
        TextView tvInterested;
        SimpleDraweeView imgPoster;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvDate = (TextView) itemView.findViewById(R.id.tvEventDate);
            tvCost = (TextView) itemView.findViewById(R.id.tvEventPrice);
            tvInterested = (TextView) itemView.findViewById(R.id.tvEventInterested);
            imgPoster = (SimpleDraweeView) itemView.findViewById(R.id.imgEventImage);
            createHeadings();
        }

        private void createHeadings() {
            tvName.setTypeface(EasyFonts.caviarDreamsBold(context));
            tvDate.setTypeface(EasyFonts.caviarDreamsBold(context));
            tvCost.setTypeface(EasyFonts.caviarDreams(context));
            tvInterested.setTypeface(EasyFonts.caviarDreams(context));
        }
    }

}



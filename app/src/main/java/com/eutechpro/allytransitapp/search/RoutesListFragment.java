package com.eutechpro.allytransitapp.search;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.eutechpro.allytransitapp.R;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.svg.SvgDecoder;
import com.eutechpro.allytransitapp.svg.SvgDrawableTranscoder;
import com.eutechpro.allytransitapp.svg.SvgSoftwareLayerSetter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RoutesListFragment extends Fragment {
    private ImageView noDataView;
    private ImageView beforeDataView;
    private RoutesListAdapter adapter;
    private OnRouteSelected onRouteSelected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.routes_recycler_view);
        noDataView = (ImageView) view.findViewById(R.id.no_data_view);
        beforeDataView = (ImageView) view.findViewById(R.id.before_data_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RoutesListAdapter(getContext());
    }

    /**
     * After setting content to the fragment, it will be refreshed.
     * @param contentList List of routes to present.
     */
    public void setContent(List<Route> contentList) {
        adapter.setRoutes(contentList);
        adapter.notifyDataSetChanged();
        if (contentList == null || contentList.size() == 0) {
            noDataView.setVisibility(View.VISIBLE);
            beforeDataView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.GONE);
            beforeDataView.setVisibility(View.GONE);
        }
    }

    public void setOnRouteSelected(OnRouteSelected onRouteSelected) {
        this.onRouteSelected = onRouteSelected;
    }

    /*
     * Inner classes/interfaces
     */
    private class RoutesListAdapter extends RecyclerView.Adapter<RouteViewHolder> {
        private List<Route> routes;
        private Context context;
        private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

        public RoutesListAdapter(Context context) {
            this.context = context;
            this.routes = new ArrayList<>();

            requestBuilder = Glide.with(getContext())
                                  .using(Glide.buildStreamModelLoader(Uri.class, getContext()), InputStream.class)
                                  .from(Uri.class)
                                  .as(SVG.class)
                                  .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                                  .sourceEncoder(new StreamEncoder())
                                  .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                                  .decoder(new SvgDecoder())
                                  .placeholder(R.drawable.ic_before_data)
                                  .error(R.drawable.ic_no_data)
                                  .animate(android.R.anim.fade_in)
                                  .listener(new SvgSoftwareLayerSetter<Uri>());
        }

        public void setRoutes(List<Route> routes) {
            this.routes = routes;
        }

        @Override
        public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.search_list_item, parent, false);

            return new RouteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RouteViewHolder viewHolder, int position) {
            final Route route = routes.get(position);

            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(Uri.parse(route.getProvider().getProviderIconUrl()))
                    .into(viewHolder.providerIcon);


            viewHolder.type.setText(route.getProvider().getDisplayName());
            viewHolder.duration.setText(context.getString(R.string.search_list_item_duration, route.getDurationInMinutes()));
            if (route.getPrice() != null) {
                Float price = ((float)route.getPrice().getAmount()) / 100;
                viewHolder.price.setVisibility(View.VISIBLE);
                viewHolder.price.setText(context.getString(R.string.search_list_item_price, price.toString()));
            }else{
                viewHolder.price.setVisibility(View.GONE);
            }
            viewHolder.mainView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRouteSelected != null) {
                        onRouteSelected.selectedRoute(route);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return routes.size();
        }
    }

    private class RouteViewHolder extends RecyclerView.ViewHolder {
        public View mainView;
        public ImageView providerIcon;
        public TextView type;
        public TextView duration;
        public TextView price;

        public RouteViewHolder(View view) {
            super(view);
            mainView = view;
            providerIcon = (ImageView) view.findViewById(R.id.route_provider);
            type = (TextView) view.findViewById(R.id.route_type);
            duration = (TextView) view.findViewById(R.id.route_duration);
            price = (TextView) view.findViewById(R.id.route_price);
        }
    }

    /**
     * Callback interface for notifying outer activity/fragment about selected route.
     */
    public interface OnRouteSelected {
        void selectedRoute(Route route);
    }
}

package com.example.leejuwon.social_service.cluster;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by leejuwon on 2017-08-04.
 */

public class OwnRendring extends DefaultClusterRenderer<Info> {

    public OwnRendring(Context context, GoogleMap map, ClusterManager<Info> clusterManager) {
        super(context, map, clusterManager);
    }

    protected void onBeforeClusterItemRendered(Info item, MarkerOptions markerOptions){
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}

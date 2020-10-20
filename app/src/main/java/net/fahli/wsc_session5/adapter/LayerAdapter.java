package net.fahli.wsc_session5.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.fahli.wsc_session5.R;
import net.fahli.wsc_session5.entity.Layer;
import java.util.ArrayList;
public class LayerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Layer> layers;
    int endPoint = 0;
    public LayerAdapter(Context context, ArrayList<Layer> layers, int endPoint) {
        this.context = context;
        this.layers = layers;
        this.endPoint = endPoint;
    }


    @Override
    public int getCount() {
        return layers.size();
    }

    @Override
    public Object getItem(int position) {
        return layers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layer_item, parent, false);

        TextView txtRockType, txtDepth;
        LinearLayout rockLayer;

        txtRockType = convertView.findViewById(R.id.txtRockType);
        txtDepth = convertView.findViewById(R.id.txtLayerDepth);
        rockLayer = convertView.findViewById(R.id.rockLayer);

        Layer layer = layers.get(position);

        if (layer.getRockTypeID() == -1) {
            txtRockType.setText(layer.getRockTypeName());
            txtDepth.setText(String.valueOf(layer.getStartPoint()) + " m");
            rockLayer.setBackgroundColor(Color.parseColor(layer.getColor()));
            txtRockType.setTextColor(Color.WHITE);
        } else {
            int maxHeight = 500;
            int blockHeight = layer.getEndPoint() - layer.getStartPoint();
            float unitHeight = (((float)endPoint / (float)maxHeight) / 100) * (float)blockHeight;
            unitHeight = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int)unitHeight, context.getResources().getDisplayMetrics());
            ViewGroup.LayoutParams params = rockLayer.getLayoutParams();
            params.height = (int) unitHeight;
            rockLayer.setLayoutParams(params);
            txtRockType.setText(layer.getRockTypeName());
            txtDepth.setText(String.valueOf(layer.getStartPoint()) + " m");
            rockLayer.setBackgroundColor(Color.parseColor(layer.getColor()));
        }

        return convertView;
    }
}

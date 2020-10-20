package net.fahli.wsc_session5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.fahli.wsc_session5.adapter.LayerAdapter;
import net.fahli.wsc_session5.entity.Layer;
import net.fahli.wsc_session5.helper.HttpCallback;
import net.fahli.wsc_session5.helper.HttpConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements HttpCallback {
    Spinner spnWell;
    TextView txtCapacity;
    ListView listView;

    HashMap<Integer, String> wellMap = new HashMap<>();

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnWell = findViewById(R.id.spnWell);
        txtCapacity = findViewById(R.id.txtCapacity);
        listView = findViewById(R.id.listView);

        dialog = new AlertDialog.Builder(MainActivity.this).create();
        new HttpConnection.sendRequest(MainActivity.this, 1, null,"GET").execute("http://10.0.2.2/wsc/session5/wells.php");

        spnWell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", wellMap.get(spnWell.getSelectedItemPosition()));
                new HttpConnection.sendRequest(MainActivity.this, 2, params, "POST").execute("http://10.0.2.2/wsc/session5/layers.php");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCompleted(String data, int requestCode) {
        if (requestCode == 1) {
            ArrayList<String> wells = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    wells.add(object.getString("Name"));
                    wellMap.put(i, object.getString("ID"));
                }
                spnWell.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, wells));
            } catch (Exception e) {
                if (!dialog.isShowing()) {
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setTitle("Oh no");
                    dialog.setMessage(e.getMessage());
                    dialog.show();
                }
            }
        } else if (requestCode == 2) {
            ArrayList<Layer> layers = new ArrayList<>();
            try {
                JSONObject well = new JSONObject(data);
                txtCapacity.setText(well.getString("Capacity") + " m3");
                JSONArray array = well.getJSONArray("layers");
                int endPoint = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Layer layer = new Layer();
                    layer.setColor(object.getString("Color"));
                    layer.setEndPoint(object.getInt("EndPoint"));
                    layer.setStartPoint(object.getInt("StartPoint"));
                    layer.setLayerID(object.getInt("ID"));
                    layer.setRockTypeID(object.getInt("RockTypeID"));
                    layer.setRockTypeName(object.getString("RockTypeName"));
                    layer.setWellID(well.getInt("WellID"));
                    layers.add(layer);

                    if ((i + 1) == array.length()) {
                        endPoint = layer.getEndPoint();
                    }
                }

                Layer layer = new Layer();
                layer.setColor("#000000");
                layer.setStartPoint(well.getInt("GasOilDepth"));
                layer.setWellID(well.getInt("WellID"));
                layer.setRockTypeID(-1);
                layer.setRockTypeName("Oil / Gas");
                layers.add(layer);

                LayerAdapter adapter = new LayerAdapter(MainActivity.this, layers, endPoint);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                if (!dialog.isShowing()) {
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setTitle("Oh no");
                    dialog.setMessage(e.getMessage());
                    dialog.show();
                }
            }
        }
    }
}
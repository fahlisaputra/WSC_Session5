package net.fahli.wsc_session5.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Layer implements Parcelable {
    int WellID, LayerID, RockTypeID, StartPoint, EndPoint;
    String RockTypeName, Color;

    public Layer() {
    }

    protected Layer(Parcel in) {
        WellID = in.readInt();
        LayerID = in.readInt();
        RockTypeID = in.readInt();
        StartPoint = in.readInt();
        EndPoint = in.readInt();
        RockTypeName = in.readString();
        Color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(WellID);
        dest.writeInt(LayerID);
        dest.writeInt(RockTypeID);
        dest.writeInt(StartPoint);
        dest.writeInt(EndPoint);
        dest.writeString(RockTypeName);
        dest.writeString(Color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Layer> CREATOR = new Creator<Layer>() {
        @Override
        public Layer createFromParcel(Parcel in) {
            return new Layer(in);
        }

        @Override
        public Layer[] newArray(int size) {
            return new Layer[size];
        }
    };

    public int getWellID() {
        return WellID;
    }

    public void setWellID(int wellID) {
        WellID = wellID;
    }

    public int getLayerID() {
        return LayerID;
    }

    public void setLayerID(int layerID) {
        LayerID = layerID;
    }

    public int getRockTypeID() {
        return RockTypeID;
    }

    public void setRockTypeID(int rockTypeID) {
        RockTypeID = rockTypeID;
    }

    public int getStartPoint() {
        return StartPoint;
    }

    public void setStartPoint(int startPoint) {
        StartPoint = startPoint;
    }

    public int getEndPoint() {
        return EndPoint;
    }

    public void setEndPoint(int endPoint) {
        EndPoint = endPoint;
    }

    public String getRockTypeName() {
        return RockTypeName;
    }

    public void setRockTypeName(String rockTypeName) {
        RockTypeName = rockTypeName;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}

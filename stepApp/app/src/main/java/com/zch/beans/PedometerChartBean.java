package com.zch.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class PedometerChartBean implements Parcelable {
    //24*60
    private int[] arrayData;
    private int index;

    public PedometerChartBean(){
        index = 0;
        arrayData = new int[1440];
    }

    protected PedometerChartBean(Parcel in) {
        arrayData=in.createIntArray();
        index=in.readInt();
    }

    public static final Creator<PedometerChartBean> CREATOR=new Creator<PedometerChartBean>() {
        @Override
        public PedometerChartBean createFromParcel(Parcel in) {
            return new PedometerChartBean(in);
        }

        @Override
        public PedometerChartBean[] newArray(int size) {
            return new PedometerChartBean[size];
        }
    };

    public int[] getArrayData() {
        return arrayData;
    }

    public void setArrayData(int[] arrayData) {
        this.arrayData=arrayData;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index=index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(arrayData);
        dest.writeInt(index);
    }

    public void reset(){
        index=0;
        for(int i = 0;i<arrayData.length;i++){
            arrayData[i] = 0;
        }
    }

}

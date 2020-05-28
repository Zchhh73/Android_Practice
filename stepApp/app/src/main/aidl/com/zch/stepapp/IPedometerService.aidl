// IPedometerService.aidl
package com.zch.stepapp;
import com.zch.beans.PedometerChartBean;
interface IPedometerService {

    void startCount();
    void stepCount();
    void resetCount();
    int getStepsCount();
    double getCalorie();
    double getDistance();
    void saveData();
    void setSensitivity(double sensitivity);
    double getSensitivity();
    void setInterval(int interval);
    int getInterval();
    long getStartTimeStamp();
    int getServiceStatus();
    PedometerChartBean getChartData();

}

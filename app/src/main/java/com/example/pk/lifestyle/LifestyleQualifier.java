package com.example.pk.lifestyle;

import java.util.ArrayList;
import java.util.Collections;

public class LifestyleQualifier {

    public static final float DORMANCY_STATE = 0.6f;
    public static final float DISTANCE_STATE = 5.0f;
    public static final float PENULTIMATE_COLOR_VALUE_AT_DORMANCY_STATE = 98.0f;
    public static final float SLOW_STEP = 2.0f;
    public static final float FAST_STEP = 4.0f;
    public static final float PENULTIMATE_COLOR_VALUE_AT_DISTANCE_STATE = 2.0f;
    public static final float PENULTIMATE_COLOR_VALUE_AT_RUNNING_STATE = 4.0f;

    public float getColor(float color, ArrayList<AxisModel> axisModels) {
        if (getDifferenceBetweenMaxMin(getAxisList(axisModels, 'x')) < DORMANCY_STATE
                || getDifferenceBetweenMaxMin(getAxisList(axisModels, 'y')) < DORMANCY_STATE
                || getDifferenceBetweenMaxMin(getAxisList(axisModels, 'z')) < DORMANCY_STATE) {

            if (color <= PENULTIMATE_COLOR_VALUE_AT_DORMANCY_STATE) {
                color += SLOW_STEP;
            }
        }

        if ((getDifferenceBetweenMaxMin(getAxisList(axisModels, 'x')) > DORMANCY_STATE
                && getDifferenceBetweenMaxMin(getAxisList(axisModels, 'x')) < DISTANCE_STATE)

                || (getDifferenceBetweenMaxMin(getAxisList(axisModels, 'y')) > DORMANCY_STATE
                && getDifferenceBetweenMaxMin(getAxisList(axisModels, 'y')) < DISTANCE_STATE)

                || ((getDifferenceBetweenMaxMin(getAxisList(axisModels, 'z')) > DORMANCY_STATE
                && getDifferenceBetweenMaxMin(getAxisList(axisModels, 'z')) < DISTANCE_STATE))) {

            if (color >= PENULTIMATE_COLOR_VALUE_AT_DISTANCE_STATE) {
                color -= SLOW_STEP;
            }
        }

        if (getDifferenceBetweenMaxMin(getAxisList(axisModels, 'x')) > DISTANCE_STATE
                || getDifferenceBetweenMaxMin(getAxisList(axisModels, 'y')) > DISTANCE_STATE
                || getDifferenceBetweenMaxMin(getAxisList(axisModels, 'z')) > DISTANCE_STATE) {

            if (color >= PENULTIMATE_COLOR_VALUE_AT_RUNNING_STATE) {
                color -= FAST_STEP;
            }
        }

        return color;
    }

    private ArrayList<Float> getAxisList(ArrayList<AxisModel> axisModels, char axis) {
        ArrayList<Float> axisList = new ArrayList<>();

        switch (axis) {
            case 'x':
                for (AxisModel axisModel : axisModels) {
                    axisList.add(axisModel.axisX);
                }
                break;
            case 'y':
                for (AxisModel axisModel : axisModels) {
                    axisList.add(axisModel.axisY);
                }
                break;
            case 'z':
                for (AxisModel axisModel : axisModels) {
                    axisList.add(axisModel.axisZ);
                }
                break;
        }

        return axisList;
    }

    /**
     * This method calculate difference between max and min values of list.
     *
     * @param list - list
     * @return - difference between max and min values of list
     */
    private float getDifferenceBetweenMaxMin(ArrayList<Float> list) {
        return Collections.max(list) - Collections.min(list);
    }
}

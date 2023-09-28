package org.stormdev.mcwipeout.frame.opening.helpers;
/*
  Created by Stormbits at 9/28/2023
*/

import lombok.Getter;
import org.stormdev.mcwipeout.frame.team.WPoint;
import toxi.geom.Spline3D;
import toxi.geom.Vec3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Getter
public class SplineGenerator {

    private List<WPoint> output, input;

    public SplineGenerator(String fileId) {
        input = new ArrayList<>();
        output = new ArrayList<>();

        //TODO: Load from file
    }

    public void exec() {
        Spline3D spline = new Spline3D();
        for (WPoint wPoint : input) {
            spline.add(wPoint.toVector());
        }

        for (Vec3D vec : spline.getDecimatedVertices(0.05F)) {
            output.add(new WPoint(vec.x, vec.y, vec.z));
        }
    }

    public int getClosest(WPoint point) {
        HashMap<Double, WPoint> map = new HashMap<>();
        for (WPoint ridePoint3D : input) {
            map.put(ridePoint3D.toLocation().distance(point.toLocation()), ridePoint3D);
        }
        return input.indexOf(map.get(Collections.min(map.keySet())));
    }
}

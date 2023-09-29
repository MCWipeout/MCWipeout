package org.stormdev.mcwipeout.utils.math;
/*
  Created by Stormbits at 9/28/2023
*/

import lombok.Getter;
import org.bukkit.util.Vector;
import org.joml.Matrix3d;
import org.stormdev.mcwipeout.utils.helpers.WLocation;

@Getter
public class RotationMatrix {

    private Matrix3d matrix;

    private WLocation location;

    private RotationMatrix(Vector axis, double angle, WLocation location) {
        this.location = location;

        double xAxis = axis.getX();
        double yAxis = axis.getY();
        double zAxis = axis.getZ();

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double xSquared = xAxis * xAxis;
        double ySquared = yAxis * yAxis;
        double zSquared = zAxis * zAxis;

        double cos1 = 1 - cos;

        double m00 = cos + xSquared * cos1;
        double m01 = xAxis * yAxis * cos1 - zAxis * sin;
        double m02 = xAxis * zAxis * cos1 + yAxis * sin;

        double m10 = yAxis * xAxis * cos1 + zAxis * sin;
        double m11 = cos + ySquared * cos1;
        double m12 = yAxis * zAxis * cos1 - xAxis * sin;

        double m20 = zAxis * xAxis * cos1 - yAxis * sin;
        double m21 = zAxis * yAxis * cos1 + xAxis * sin;
        double m22 = cos + zSquared * cos1;

        matrix = new Matrix3d(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    public static RotationMatrix of(Vector axis, double angle, WLocation location) {
        return new RotationMatrix(axis, angle, location);
    }

    public double getX() {
        return matrix.m00 * location.getX() + matrix.m01 * location.getY() + matrix.m02 * location.getZ();
    }

    public double getY() {
        return matrix.m10 * location.getX() + matrix.m11 * location.getY() + matrix.m12 * location.getZ();
    }

    public double getZ() {
        return matrix.m20 * location.getX() + matrix.m21 * location.getY() + matrix.m22 * location.getZ();
    }
}

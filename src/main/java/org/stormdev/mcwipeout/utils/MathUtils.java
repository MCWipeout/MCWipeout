package org.stormdev.mcwipeout.utils;
/*
  Created by Stormbits at 8/21/2023
*/

public class MathUtils {

    public static double lerp(double a, double b, int progress) {
        return a + progress * (b - a);
    }
}

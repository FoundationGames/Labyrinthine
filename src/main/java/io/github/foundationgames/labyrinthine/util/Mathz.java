package io.github.foundationgames.labyrinthine.util;

import net.minecraft.util.math.*;

public class Mathz {
    //BlockPos to Vec3d
    public static Vec3d bp2v3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3d v3dfromPosAndYAngleAndLen(Vec3d pos, double yAngle, double length) {
        double x1 = pos.x, y1 = pos.z;
        double ac = yAngle, ab = 90, cb = 180 - (ab+ac);
        double c = length, a = c*Math.sin(cb), b = c*Math.sin(ac);
        double x2=x1+a, y2=y1+b;
        return pos.add(a, 0, b);
    }

    public static Vec3d v3dfromYaw(Vec3d pos, double yaw) {
        float g = (float)(-yaw * 0.017453292F);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        return pos.add(i, 0, h);
    }

    public static Vec3d multiplyVec3d(Vec3d vec, double factor) {
        return new Vec3d(vec.x*factor, vec.y*factor, vec.z*factor);
    }

    //Pixel count to float
    public static float px2flt(int pixels) {
        return (float)pixels / 16;
    }
}

package nl.twinkelmc.rides.rides.utils

import net.minecraft.server.v1_16_R3.Vector3f
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector
import java.lang.Math.toDegrees
import kotlin.math.*

class RenderUtils {
    
    fun getFormula(t: Double, point1: Double, point2: Double, control1: Double, control2: Double): Double {
        return point1 * (1 - t).pow(3.0) + control1 * 3 * (1 - t).pow(2.0) * t + control2 * 3 * (1 - t) * t.pow(2.0) + point2 * t.pow(3.0)
    }

    fun getCorrectArmorStandAngle(yaw1: Double, pitch1: Double, roll1: Double): EulerAngle {
        val pitch = pitch1 / 180 * PI + 0.001
        val yaw = yaw1 / 180 * PI + 0.001
        val roll = (roll1 + 90) / 180 * PI + 0.001
        val VecX = cos(pitch) * cos(yaw)
        val VecZ = cos(pitch) * sin(yaw)
        val VecY = sin(pitch)
        val dir = Vector(VecX, VecY, VecZ)
        val Vec2X = -cos(yaw) * sin(pitch) * sin(roll) - sin(yaw) * cos(roll)
        val Vec2Z = -sin(yaw) * sin(pitch) * sin(roll) + cos(yaw) * cos(roll)
        val Vec2Y = cos(pitch) * sin(roll)
        val up = Vector(Vec2X, Vec2Y, Vec2Z)
        val quaternion = Quaternion()
        val rotation: Quaternion = quaternion.fromLookDirection(dir, up)!!
        val qx: Double = rotation.x
        val qy: Double = rotation.y
        val qz: Double = rotation.z
        val qw: Double = rotation.w
        val rx = 1.0 + 2.0 * (-qy * qy - qz * qz)
        val ry = 2.0 * (qx * qy + qz * qw)
        val rz = 2.0 * (qx * qz - qy * qw)
        val uz = 2.0 * (qy * qz + qx * qw)
        val fz = 1.0 + 2.0 * (-qx * qx - qy * qy)
        return if (abs(rz) < 1.0 - 1E-15) {
            EulerAngle(atan2(uz, fz), asin(rz), atan2(-ry, rx))
        } else {
            val sign = if (rz < 0) -1.0 else 1.0
            EulerAngle(0.0, sign * 90.0, -sign * 2.0 * atanTwo(qx, qw))
        }
    }

    fun getCorrectArmorStand3F(yaw1: Double, pitch1: Double, roll1: Double): Vector3f? {
        val pitch = pitch1 / 180 * PI + 0.001
        val yaw = yaw1 / 180 * PI + 0.001
        val roll = (roll1 + 90) / 180 * PI + 0.001
        val VecX = cos(pitch) * cos(yaw)
        val VecZ = cos(pitch) * sin(yaw)
        val VecY = sin(pitch)
        val dir = Vector(VecX, VecY, VecZ)
        val Vec2X = -cos(yaw) * sin(pitch) * sin(roll) - sin(yaw) * cos(roll)
        val Vec2Z = -sin(yaw) * sin(pitch) * sin(roll) + cos(yaw) * cos(roll)
        val Vec2Y = cos(pitch) * sin(roll)
        val up = Vector(Vec2X, Vec2Y, Vec2Z)
        val quaternion = Quaternion()
        val rotation: Quaternion = quaternion.fromLookDirection(dir, up)!!
        val qx: Double = rotation.x
        val qy: Double = rotation.y
        val qz: Double = rotation.z
        val qw: Double = rotation.w
        val rx = 1.0 + 2.0 * (-qy * qy - qz * qz)
        val ry = 2.0 * (qx * qy + qz * qw)
        val rz = 2.0 * (qx * qz - qy * qw)
        val uz = 2.0 * (qy * qz + qx * qw)
        val fz = 1.0 + 2.0 * (-qx * qx - qy * qy)
        return if (abs(rz) < 1.0 - 1E-15) {
            Vector3f((atan2(uz, fz) / PI * 180).toFloat(), (asin(rz) / PI * 180).toFloat(), (atan2(-ry, rx) / PI * 180).toFloat())
        } else {
            val sign = if (rz < 0) -1.0 else 1.0
            Vector3f(0.0.toFloat(), (sign * 90.0 / PI * 180).toFloat(), (-sign * 2.0 * atanTwo(qx, qw) / PI * 180).toFloat())
        }
    }

    fun atanTwo(y: Double, x: Double): Float {
        return toDegrees(atan2(y, x)).toFloat()
    }

    private fun mxatan(arg: Double): Double {
        val argsq = arg * arg
        var value = (((16.15364129822302 * argsq + 268.42548195503974) * argsq + 1153.029351540485) * argsq + 1780.406316433197) * argsq + 896.7859740366387
        value /= ((((argsq + 58.95697050844462) * argsq + 536.2653740312153) * argsq + 1666.7838148816338) * argsq + 2079.33497444541) * argsq + 896.7859740366387
        return value * arg
    }

    private fun msatan(arg: Double): Double {
        return if (arg < 0.41421356237309503) mxatan(arg) else if (arg > 2.414213562373095) 1.5707963267948966 - mxatan(1.0 / arg) else 0.7853981633974483 + mxatan((arg - 1.0) / (arg + 1.0))
    }

    private fun atan(arg: Double): Double {
        return if (arg > 0.0) msatan(arg) else -msatan(-arg)
    }

    private fun atan2(arg1: Double, arg2: Double): Double {
        var arg1 = arg1
        return if (arg1 + arg2 == arg1) {
            if (arg1 >= 0.0) 1.5707963267948966 else -1.5707963267948966
        } else {
            arg1 = atan(arg1 / arg2)
            if (arg2 < 0.0) if (arg1 <= 0.0) arg1 + 3.141592653589793 else arg1 - 3.141592653589793 else arg1
        }
    }

    fun getCorrectArmorStandAngle(dir: Vector, up: Vector): EulerAngle {
        val quaternion = Quaternion()
        val rotation: Quaternion = quaternion.fromLookDirection(dir, up)!!
        val qx: Double = rotation.x
        val qy: Double = rotation.y
        val qz: Double = rotation.z
        val qw: Double = rotation.w
        val rx = 1.0 + 2.0 * (-qy * qy - qz * qz)
        val ry = 2.0 * (qx * qy + qz * qw)
        val rz = 2.0 * (qx * qz - qy * qw)
        val uz = 2.0 * (qy * qz + qx * qw)
        val fz = 1.0 + 2.0 * (-qx * qx - qy * qy)
        return if (abs(rz) < 1.0 - 1E-15) {
            EulerAngle(kotlin.math.atan2(uz, fz), asin(rz), kotlin.math.atan2(-ry, rx))
        } else {
            val sign = if (rz < 0) -1.0 else 1.0
            EulerAngle(0.0, sign * 90.0, -sign * 2.0 * atanTwo(qx, qw))
        }
    }

}
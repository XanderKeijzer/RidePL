package nl.twinkelmc.rides.rides.continuous.droomvlucht

import nl.twinkelmc.rides.Main
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

class TrainInstance(val droomvlucht: Droomvlucht, var position: Double) : BukkitRunnable() {

    var trainFront: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, helmet = droomvlucht.modelTrain)
    var computerFront: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, helmet = droomvlucht.modelComputerFront)
    var seatLeftFront: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, name = "dvseat")
    var seatRightFront: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, name = "dvseat")

    var trainBack: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, helmet = droomvlucht.modelTrain)
    var computerBack: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, helmet = droomvlucht.modelComputerBack)
    var seatLeftBack: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, name = "dvseat")
    var seatRightBack: ArmorStand = Main.instance.standCreator.createNC(location = droomvlucht.track[position.toInt()].loc, visible = false, name = "dvseat")

    init {
        this.runTaskTimer(Main.instance, 0L, 1L)
    }

    //    private val trainFrontSeatLeft: ArmorStand
//    private val trainSeatRight: ArmorStand
//    private val trainsFollow: ArmorStand
//    private val compF: ArmorStand
//    private val trainFSeatLeft: ArmorStand
//    private val trainFSeatRight: ArmorStand

    private var targetSpeed = 0.0
    private var speed = 0.0
    private var stop = false

    fun remove() {
        trainFront.remove()
        computerFront.remove()
        seatLeftFront.remove()
        seatRightFront.remove()
        trainBack.remove()
        computerBack.remove()
        seatLeftBack.remove()
        seatRightBack.remove()
        this.cancel()
    }

    fun stop() {
        this.stop = !this.stop
    }

    override fun run() {

        var stop = false
        var slow = false
        var noSpeed = false
        for(train in droomvlucht.trains) {
            if (train != this) {
                var trainPos = train.position
                if (trainPos <= position)
                    trainPos += droomvlucht.track.size
                if (trainPos.distance(position) < 1100)
                    stop = true
                if (trainPos.distance(position) < 1450) {
                    slow = true
                }
                if (trainPos.distance(position) < 2000) {
                    noSpeed = true
                }
            }
        }

        targetSpeed = when {
            this.stop -> 0.0
            stop -> 0.0
            slow -> 6.0
            (position.toInt() in 33000..52000 && !noSpeed) -> 22.0
            else -> 8.0
        }

        if (speed > targetSpeed) {
            speed -= 0.18
            if (speed < targetSpeed)
                speed = targetSpeed
        } else if (speed < targetSpeed) {
            speed += 0.18
            if (speed > targetSpeed)
                speed = targetSpeed
        }
        position += speed
        if (position >= droomvlucht.track.size)
            position -= droomvlucht.track.size
        moveToPosition(position.toInt())
    }

    private fun moveToPosition(position: Int) {
        var pos = position
        if (droomvlucht.track[pos].loc.chunk.isLoaded) {

            trainFront.teleportNC(droomvlucht.track[pos].loc)
            trainFront.headPose = droomvlucht.track[pos].rotRotated

            computerFront.teleportNC(droomvlucht.track[pos].loc.copy().add(droomvlucht.track[pos].up.copy().multiply(0.95)))
            computerFront.headPose = droomvlucht.track[pos].rot

            moveSeatsFront(pos)

            pos -= 315
            if (pos < 0)
                pos += droomvlucht.track.size
            trainBack.teleportNC(droomvlucht.track[pos].loc)
            trainBack.headPose = droomvlucht.track[pos].rotRotated

            computerBack.teleportNC(droomvlucht.track[pos].loc.copy().add(droomvlucht.track[pos].up.copy().multiply(0.95)))
            computerBack.headPose = droomvlucht.track[pos].rot

            moveSeatsBack(pos)
        }
    }


    private fun moveSeatsFront(pos: Int) {
        val trackPos = droomvlucht.track[pos]
        val locL = trackPos.loc.copy()
        val locR = trackPos.loc.copy()
        val back = trackPos.dirRotated.copy().multiply(-0.3)
        val down = trackPos.upRotated.copy().multiply(-1.0)
        val left = trackPos.leftRotated.copy().multiply(0.6)
        val right = trackPos.leftRotated.copy().multiply(-0.6)
        val rotation = (atan2(trackPos.dirRotated.x, trackPos.dirRotated.z)/ PI * -180.0).toFloat()
        locL.add(left).add(back).add(down)
        locR.add(right).add(back).add(down)

        seatLeftFront.teleportNC(locL)
        seatRightFront.teleportNC(locR)

        seatLeftFront.setRotation(rotation, 0.0F)
        seatRightFront.setRotation(rotation, 0.0F)
    }

    private fun moveSeatsBack(pos: Int) {
        val trackPos = droomvlucht.track[pos]
        val locL = trackPos.loc.copy()
        val locR = trackPos.loc.copy()
        val back = trackPos.dirRotated.copy().multiply(-0.3)
        val down = trackPos.upRotated.copy().multiply(-1.0)
        val left = trackPos.leftRotated.copy().multiply(0.6)
        val right = trackPos.leftRotated.copy().multiply(-0.6)
        val rotation = (atan2(trackPos.dirRotated.x, trackPos.dirRotated.z)/ PI * -180.0).toFloat()
        locL.add(left).add(back).add(down)
        locR.add(right).add(back).add(down)

        seatLeftBack.teleportNC(locL)
        seatRightBack.teleportNC(locR)

        seatLeftBack.setRotation(rotation, 0.0F)
        seatRightBack.setRotation(rotation, 0.0F)
    }

    private fun Double.distance(t: Double): Int {
        return abs(t - this).toInt()
    }

    private fun ArmorStand.teleportNC(loc: Location) {
        this.velocity = loc.toVector().subtract(this.location.toVector())
    }

    private fun Location.copy(): Location {
        return Location(this.world, this.x, this.y, this.z)
    }

    private fun Vector.copy(): Vector {
        return Vector(this.x, this.y, this.z)
    }

}
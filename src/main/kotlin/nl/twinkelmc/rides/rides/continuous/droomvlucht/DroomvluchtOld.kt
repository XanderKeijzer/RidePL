package nl.twinkelmc.rides.rides.continuous.droomvlucht

class DroomvluchtOld {

}
//
//import net.minecraft.server.v1_16_R3.EntityArmorStand
//import nl.twinkelmc.rides.Main
//import nl.twinkelmc.rides.rides.continuous.CartPosition
//import org.apache.commons.csv.CSVFormat
//import org.apache.commons.csv.CSVParser
//import org.bukkit.Bukkit
//import org.bukkit.Location
//import org.bukkit.Material
//import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand
//import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity
//import org.bukkit.entity.ArmorStand
//import org.bukkit.entity.Player
//import org.bukkit.inventory.meta.Damageable
//import org.bukkit.inventory.ItemStack
//import org.bukkit.scheduler.BukkitRunnable
//import org.bukkit.util.Vector
//import java.nio.file.Files
//import java.nio.file.Paths
//import kotlin.math.PI
//import kotlin.math.atan2
//import kotlin.math.tan
//
//class DroomvluchtOld: BukkitRunnable() {
//
//    private val fullSpeed = 7.0
//    private val track = arrayListOf<CartPosition>()
//    private var speed = fullSpeed
//    private val trainAmount = 20
//    private var position = 0.0
//    private val trains = arrayListOf<ArmorStand>()
//    private val comp = arrayListOf<ArmorStand>()
//    private val trainSeatLeft = arrayListOf<ArmorStand>()
//    private val trainSeatRight = arrayListOf<ArmorStand>()
//    private val trainsFollow = arrayListOf<ArmorStand>()
//    private val compF = arrayListOf<ArmorStand>()
//    private val trainFSeatLeft = arrayListOf<ArmorStand>()
//    private val trainFSeatRight = arrayListOf<ArmorStand>()
//    private var pause = false
//
//    private fun model(id: Int): ItemStack {
//        val model = ItemStack(Material.DIAMOND_SWORD)
//        val data = model.itemMeta!!
//        data.isUnbreakable = true
//        (data as Damageable).damage = id
//        model.itemMeta = data
//        return model
//    }
//
//    fun start() {
//        read()
//        val trainClearance = track.size / trainAmount
//        var pos: Double
//        for (i in 1..trainAmount) {
//            pos = position + trainClearance * i
//            if (pos >= track.size)
//                pos -= track.size
//            track[pos.toInt()].loc.chunk.load()
//            val model = model(25)
//            val model2 = model(27)
//            val model3 = model(28)
//            trains.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, helmet = model, visible = false))
//            comp.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, helmet = model2, visible = false))
//            trainSeatLeft.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, visible = false, name = "dvseat"))
//            trainSeatRight.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, visible = false, name = "dvseat"))
//            pos -= 350.0
//            if (pos < 0.0)
//                pos += track.size
//            trainsFollow.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, helmet = model, visible = false))
//            compF.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, helmet = model3, visible = false))
//            trainFSeatLeft.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, visible = false, name = "dvseat"))
//            trainFSeatRight.add(Main.instance.standCreator.createNC(location = track[pos.toInt()].loc, visible = false, name = "dvseat"))
//        }
//        this.runTaskTimer(Main.instance, 0L, 1L)
//    }
//
//    fun stop() {
//        this.cancel()
//        for (train in trains)
//            train.remove()
//        trains.clear()
//        for (train in trainsFollow)
//            train.remove()
//        trainsFollow.clear()
//        for (train in trainSeatLeft)
//            train.remove()
//        trainSeatLeft.clear()
//        for (train in trainSeatRight)
//            train.remove()
//        trainSeatRight.clear()
//        for (train in trainFSeatLeft)
//            train.remove()
//        trainFSeatLeft.clear()
//        for (train in trainFSeatRight)
//            train.remove()
//        trainFSeatRight.clear()
//        for (train in comp)
//            train.remove()
//        comp.clear()
//        for (train in compF)
//            train.remove()
//        compF.clear()
//    }
//
//    fun pause() {
//        pause = !pause
//    }
//
//    private fun Vector.copy(): Vector {
//        return Vector(this.x, this.y, this.z)
//    }
//
//    private fun Location.copy(): Location {
//        return Location(this.world, this.x, this.y, this.z)
//    }
//
//    private fun moveSeats(pos: Int, i: Int) {
//        val trackPos = track[pos]
//        val locL = trackPos.loc.copy()
//        val locR = trackPos.loc.copy()
//        val back = trackPos.dir.copy().multiply(-0.3)
//        val down = trackPos.up.copy().multiply(1.0)
//        val left = trackPos.left.copy().multiply(0.6)
//        val right = trackPos.left.copy().multiply(-0.6)
//        locL.add(left).add(back).add(down)
//        trainSeatLeft[i - 1].teleportNC(locL)
//        locR.add(right).add(back).add(down)
//        trainSeatRight[i - 1].teleportNC(locR)
//        val rotation = (atan2(trackPos.dir.x, trackPos.dir.z)/ PI * -180.0).toFloat()
//        trainSeatLeft[i - 1].setRotation(rotation, 0.0F)
//        trainSeatRight[i - 1].setRotation(rotation, 0.0F)
//    }
//
//    private fun moveSeats2(pos: Int, i: Int) {
//        val trackPos = track[pos]
//        val locL = trackPos.loc.copy()
//        val locR = trackPos.loc.copy()
//        val back = trackPos.dir.copy().multiply(-0.3)
//        val down = trackPos.up.copy().multiply(1.0)
//        val left = trackPos.left.copy().multiply(0.6)
//        val right = trackPos.left.copy().multiply(-0.6)
//        locL.add(left).add(back).add(down)
//        trainFSeatLeft[i - 1].teleportNC(locL)
//        locR.add(right).add(back).add(down)
//        trainFSeatRight[i - 1].teleportNC(locR)
//        val rotation = (atan2(trackPos.dir.x, trackPos.dir.z)/ PI * -180.0).toFloat()
//        trainFSeatLeft[i - 1].setRotation(rotation, 0.0F)
//        trainFSeatRight[i - 1].setRotation(rotation, 0.0F)
//    }
//
//    private fun moveComp(pos: Int, i: Int) {
//        val up = track[pos].up.copy().multiply(-0.95)
//        val loc = track[pos].loc.copy().add(up)
//        comp[i - 1].teleportNC(loc)
//        comp[i - 1].headPose = track[pos].trot
//    }
//
//    private fun moveComp2(pos: Int, i: Int) {
//        val up = track[pos].up.copy().multiply(-0.95)
//        val loc = track[pos].loc.copy().add(up)
//        compF[i - 1].teleportNC(loc)
//        compF[i - 1].headPose = track[pos].trot
//    }
//
//    private fun ArmorStand.teleportNC(loc: Location) {
//        this.velocity = loc.toVector().subtract(this.location.toVector())
//    }
//
//    override fun run() {
//        val trainClearance = track.size / trainAmount
//        var pos: Double
//        for (i in 1..trainAmount) {
//            pos = position + trainClearance * i
//            while (pos >= track.size) {
//                pos -= track.size
//            }
//            if (track[pos.toInt()].loc.chunk.isLoaded) {
//                trains[i - 1].teleportNC(track[pos.toInt()].loc)
//                trains[i - 1].headPose = track[pos.toInt()].rot
//                moveSeats(pos.toInt(), i)
//                moveComp(pos.toInt(), i)
//                pos -= 315.0
//                if (pos < 0.0)
//                    pos += track.size
//                trainsFollow[i - 1].teleportNC(track[pos.toInt()].loc)
//                trainsFollow[i - 1].headPose = track[pos.toInt()].rot
//                moveSeats2(pos.toInt(), i)
//                moveComp2(pos.toInt(), i)
//            }
//        }
//        position += speed
//        if (pause) {
//            if (speed > 0.0) {
//                speed -= 0.2
//            }
//        } else {
//            if (speed < fullSpeed) {
//                speed += 0.2
//            }
//        }
//
//    }
//
//    fun showLoc(player: Player, pos: Int) {
//        player.teleport(track[pos].loc)
//    }
//
//    private fun read() {
//        val reader = Files.newBufferedReader(Paths.get("plugins/TMC-Rides/DV.csv"))
//        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withQuote(null).withNullString(""))
//        //val location = Location(Bukkit.getWorld("world"), 125.5, 101.5, 129.5)
//        var targetRot = 0.0
//        var currentRot = 0.0
//        val location = Location(Bukkit.getWorld("world"), 50.5, 83.5, -716.0)
//        location.add(-44.0, -6.0, -16.0)
//        for ((count, csvRecord) in csvParser.withIndex()) {
//            when (count) {
//                0 -> continue
//                4000 -> targetRot = -90.0
//                5500 -> targetRot = 0.0
//                7600 -> targetRot = 90.0
//                10000 -> targetRot = 0.0
//                12600 -> targetRot = 90.0
//                16000 -> targetRot = 0.0
//                21000 -> targetRot = -90.0
//                22700 -> targetRot = 0.0
//                25000 -> targetRot = -90.0
//                32000 -> targetRot = 0.0
//            }
//            val name = csvRecord.get(0)
//            val data = name.split("\t")
//            var x = 0.0; var y = 0.0; var z = 0.0
//            var fx = 0.0; var fy = 0.0; var fz = 0.0
//            var ux = 0.0; var uy = 0.0; var uz = 0.0
//            var lx = 0.0; var ly = 0.0; var lz = 0.0
//            for ((index, value) in data.withIndex()) {
//                when(index) {
//                    1 -> x = value.toDouble()
//                    2 -> y = value.toDouble()
//                    3 -> z = value.toDouble()
//                    4 -> fx = value.toDouble()
//                    5 -> fy = value.toDouble()
//                    6 -> fz = value.toDouble()
//                    7 -> lx = value.toDouble()
//                    8 -> ly = value.toDouble()
//                    9 -> lz = value.toDouble()
//                    10 -> ux = value.toDouble()
//                    11 -> uy = value.toDouble()
//                    12 -> uz = value.toDouble()
//                }
//            }
//            if (currentRot < targetRot) {
//                currentRot += 0.2
//            } else if (currentRot > targetRot) {
//                currentRot -= 0.2
//            }
//            val up = Vector(ux, uy, uz)
//            val tdir = Vector(fx, fy, fz)
//            val tleft = Vector(lx, ly, lz)
//            val tangle = Main.instance.renderUtils.getCorrectArmorStandAngle(tdir, up)
//            val dir = Vector(fx, fy, fz).rotateAroundAxis(up, (currentRot / 180 * PI))
//            val left = Vector(lx, ly, lz).rotateAroundAxis(up, (currentRot / 180 * PI))
//            val newLoc = Location(location.world, location.x + x, location.y + y, location.z + z)
//            val angle = Main.instance.renderUtils.getCorrectArmorStandAngle(dir, up)
//            val rVec = up.multiply(-1.0)
//            newLoc.add(rVec)
//            track.add(CartPosition(newLoc, angle, up, dir, left, tangle, up, tdir, tleft))
//        }
//    }
//
//}
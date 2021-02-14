package nl.twinkelmc.rides.rides.continuous.droomvlucht

import nl.twinkelmc.rides.Main
import nl.twinkelmc.rides.rides.continuous.CartPosition
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.Vector
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.PI

class Droomvlucht {

    val track = arrayListOf<CartPosition>()
    val trains = arrayListOf<TrainInstance>()

    val modelTrain = model(25)
    val modelRestraints = model(26)
    val modelComputerFront = model(27)
    val modelComputerBack = model(28)

    fun start() {
        if (track.size == 0)
            read()
        for (i in 1..28) {
            trains.add(TrainInstance(this, (i * 1475.0) - 1475.0))
        }
    }

    fun stop() {
        for (train in trains) {
            train.remove()
        }
    }

    fun pauseAll() {
        for (train in trains) {
            train.stop()
        }
    }

    fun pause(player: Player) {
        for (train in trains) {
            if (track[train.position.toInt()].loc.distance(player.location) < 3)
                train.stop()
        }
    }

    fun pause(int: Int) {
        trains[int].stop()
    }

    private fun read() {
        var targetRot = -90.0
        var currentRot = -90.0
        val reader = Files.newBufferedReader(Paths.get("plugins/TMC-Rides/DV.csv"))
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withQuote(null).withNullString(""))
        val location = Location(Bukkit.getWorld("world"), 7.0, 76.25, -726.75)
        //.add(-44.0, -6.0, -16.0)
        for ((count, csvRecord) in csvParser.withIndex()) {
            when (count) {
                0 -> continue
                1 -> targetRot = -90.0
                100 -> targetRot = 0.0
                11500 -> targetRot = -90.0
                13000 -> targetRot = 0.0
                15000 -> targetRot = 90.0
                17500 -> targetRot = 0.0
                20000 -> targetRot = 90.0
                23500 -> targetRot = 0.0
                28500 -> targetRot = -90.0
                30000 -> targetRot = 0.0
                32500 -> targetRot = -90.0
            }
            val data = csvRecord.get(0).split("\t")
            var x = 0.0; var y = 0.0; var z = 0.0
            var fx = 0.0; var fy = 0.0; var fz = 0.0
            var ux = 0.0; var uy = 0.0; var uz = 0.0
            var lx = 0.0; var ly = 0.0; var lz = 0.0
            for ((index, value) in data.withIndex()) {
                when(index) {
                    1 -> x = value.toDouble()
                    2 -> y = value.toDouble()
                    3 -> z = value.toDouble()
                    4 -> fx = value.toDouble()
                    5 -> fy = value.toDouble()
                    6 -> fz = value.toDouble()
                    7 -> lx = value.toDouble()
                    8 -> ly = value.toDouble()
                    9 -> lz = value.toDouble()
                    10 -> ux = value.toDouble()
                    11 -> uy = value.toDouble()
                    12 -> uz = value.toDouble()
                }
            }
            if      (currentRot < targetRot) { currentRot += 0.2 }
            else if (currentRot > targetRot) { currentRot -= 0.2 }
            val newLoc = Location(location.world, location.x + x, location.y + y, location.z + z)
            val up = Vector(ux, uy, uz)
            val dir = Vector(fx, fy, fz)
            val left = Vector(lx, ly, lz)
            val angle = Main.instance.renderUtils.getCorrectArmorStandAngle(dir, up)
            val dirRotated = Vector(fx, fy, fz).rotateAroundAxis(up, (currentRot / 180 * PI))
            val leftRotated = Vector(lx, ly, lz).rotateAroundAxis(up, (currentRot / 180 * PI))
            val angleRotated = Main.instance.renderUtils.getCorrectArmorStandAngle(dirRotated, up)
            newLoc.add(up.copy().multiply(-1.0))
            track.add(CartPosition(newLoc, angle, up, dir, left, angleRotated, up, dirRotated, leftRotated))
        }
    }

    private fun model(id: Int): ItemStack {
        val model = ItemStack(Material.DIAMOND_SWORD)
        val data = model.itemMeta!!
        data.isUnbreakable = true
        (data as Damageable).damage = id
        model.itemMeta = data
        return model
    }

    private fun Vector.copy(): Vector {
        return Vector(this.x, this.y, this.z)
    }

    private fun Location.copy(): Location {
        return Location(this.world, this.x, this.y, this.z)
    }

}
package nl.twinkelmc.rides.rides.coaster

import nl.twinkelmc.rides.Main
import nl.twinkelmc.rides.rides.coaster.objects.TrackPosition
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.nio.file.Files
import java.nio.file.Paths

class TrackRenderer {

    val track = arrayListOf<TrackPosition>()
    private val stands = arrayListOf<ArmorStand>()

    fun showTrack(ride: String, vec: Vector) {
        read(ride, vec)
        render()
    }

    private fun render() {
        for ((index, pos) in track.withIndex()) {
            if (index % 100 == 0) {
                stands.add(Main.instance.standCreator.create(location = pos.loc, head = pos.rot, visible = false, helmet = ItemStack(Material.ACACIA_TRAPDOOR)))
            }
        }
    }

    fun clear() {
        for (stand in stands) {
            stand.remove()
        }
        stands.clear()
        track.clear()
    }

    private fun read(ride: String, vec: Vector) {
        val reader = Files.newBufferedReader(Paths.get("plugins/TMC-Rides/$ride.csv"))
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withQuote(null).withNullString(""))
        val location = Location(Bukkit.getWorld("world"), vec.x, vec.y, vec.z)
        var first = true
        for (csvRecord in csvParser) {
            if (first) {
                first = false
                continue
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
            val newLoc = Location(location.world, location.x + x, location.y + y, location.z + z)
            val up = Vector(ux, uy, uz)
            val dir = Vector(fx, fy, fz)
            val left = Vector(lx, ly, lz)
            val angle = Main.instance.renderUtils.getCorrectArmorStandAngle(dir, up)
            track.add(TrackPosition(newLoc, angle, up, dir, left))
        }
    }

}
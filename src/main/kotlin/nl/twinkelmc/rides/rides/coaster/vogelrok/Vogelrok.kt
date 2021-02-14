package nl.twinkelmc.rides.rides.coaster.vogelrok

import nl.twinkelmc.rides.Main
import nl.twinkelmc.rides.rides.coaster.objects.TrackPosition
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.Vector

class Vogelrok {

    val track = arrayListOf<TrackPosition>()
    val trains = arrayListOf<TrainInstance>()
    val modelFront = model(16)
    val modelCenter = model(17)
    val modelBack = model(18)

    var dispatch = false
    var stateTrain0 = "station"
    var stateTrain1 = "block 3"
    var stateTrain2 = "block 2"
    var emergency = false

    fun setState(instance: TrainInstance, state: String) {
        for (i in 0..2) {
            if (trains[i] == instance) {
                when (i) {
                    0 -> stateTrain0 = state
                    1 -> stateTrain1 = state
                    2 -> stateTrain2 = state
                }
            }
        }
    }

    fun getState(instance: TrainInstance): String {
        for (i in 0..2) {
            if (trains[i] == instance) {
                return when (i) {
                    0 -> stateTrain0
                    1 -> stateTrain1
                    2 -> stateTrain2
                    else -> ""
                }
            }
        }
        return ""
    }

    fun isSectionEmtpy(instance: TrainInstance, state: String): Boolean {
        var isEmpty = true
        for (i in 0..2) {
            if (trains[i] != instance) {
                when (i) {
                    0 -> if (stateTrain0 == state) isEmpty = false
                    1 -> if (stateTrain1 == state) isEmpty = false
                    2 -> if (stateTrain2 == state) isEmpty = false
                }
            }
        }
        return isEmpty
    }

    fun start() {
        load()
        trains.add(TrainInstance(this, 5600.0, trains.size))
        trains.add(TrainInstance(this, 3700.0, trains.size))
        trains.add(TrainInstance(this, 0.0, trains.size))
    }

    fun stop() {
        for (train in trains) train.stop()
        trains.clear()
    }

    private fun load() {
        track.addAll(Main.instance.trackLoader.getTrack("Vogelrok", Vector(696.86, 73.5, -740.1)).list)
    }

    private fun model(id: Int): ItemStack {
        val model = ItemStack(Material.DIAMOND_SWORD)
        val data = model.itemMeta!!
        data.isUnbreakable = true
        (data as Damageable).damage = id
        model.itemMeta = data
        return model
    }

}
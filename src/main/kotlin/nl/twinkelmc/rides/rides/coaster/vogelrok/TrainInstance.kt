package nl.twinkelmc.rides.rides.coaster.vogelrok

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import nl.twinkelmc.rides.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.atan2

class TrainInstance(private val vogelrok: Vogelrok, var position: Double, val id: Int): BukkitRunnable() {

    private val gravity = 3.0
    private val distance = 300
    private val rolling = 12.0

    var carts = arrayListOf<ArmorStand>()
    var seats = arrayListOf<ArrayList<ArmorStand>>()
    var speed = 15.0
    var previousLocation = vogelrok.track[position.toInt()]

    init {
        for(i in 0..5) {
            var cartPos = position.toInt() + i * -distance
            if (cartPos < 0) cartPos += vogelrok.track.size
            val model = when(i) {
                0 -> vogelrok.modelFront
                5 -> vogelrok.modelBack
                else -> vogelrok.modelCenter
            }
            val cartSeats = arrayListOf<ArmorStand>()
            carts.add(Main.instance.standCreator.createNC(location = vogelrok.track[cartPos].loc, helmet = model, visible = false))
            for (j in 0..3) {
                cartSeats.add(Main.instance.standCreator.createNC(location = vogelrok.track[cartPos].loc, visible = false, name = "vogelrok.train.$id.seat"))
            }
            seats.add(cartSeats)
        }
        this.runTaskTimer(Main.instance, 0L, 1L)
    }

    override fun run() {

        for (player in Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("Train 1: ${vogelrok.getState(vogelrok.trains[0])} || Train 2: ${vogelrok.getState(vogelrok.trains[1])} || Train 3: ${vogelrok.getState(vogelrok.trains[2])}"))
            //player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${vogelrok.dispatch}, ${isBetween(4800, 6700)}, ${internalBetween(position, 6400, 6600)}, $speed"))
        }

        val gravityAcc = (previousLocation.loc.y - vogelrok.track[position.toInt()].loc.y) * gravity
        speed += gravityAcc
        previousLocation = vogelrok.track[position.toInt()]

        // Block 1 (Snake Brakes)
        if (isBetween(55000, 58300)) {
            vogelrok.setState(this, "block 1")
            if (!vogelrok.emergency) {
                if (speed < rolling) speed += 0.2
                if (speed > 40.0) slowDown(0.7)
                if (!vogelrok.isSectionEmtpy(this, "block 2") && isBetween(57500, 58300)) slowDown(0.7)
            }
        } else if (vogelrok.getState(this) == "block 1") {
            vogelrok.setState(this, "block 2")
        }

        // Block 2 (Final brakes)
        if (inverseBetween(position, 79100, 700) || inverseBetween(position.movePos(-distance * 5.0), 79100, 700)) {
            vogelrok.setState(this, "block 2")
            if (!vogelrok.emergency) {
                if (speed < rolling) speed += 0.2
                if (speed > rolling) slowDown(0.8)
                if (speed > rolling + 20) slowDown(0.5)
                if (!vogelrok.isSectionEmtpy(this, "block 3") && (internalBetween(position, 300, 700) || internalBetween(position.movePos(-distance * 5.0), 300, 700))) slowDown(0.8)
            }
        }

        // Block 3 (Station block)
        if (isBetween(2400, 4600)) {
            vogelrok.setState(this, "block 3")
            if (!vogelrok.emergency) {
                if (speed < rolling) speed += 0.2
                if (speed > rolling) slowDown(0.5)
                if (!vogelrok.isSectionEmtpy(this, "station") && isBetween(4000, 4600)) slowDown(0.5)
            }
        }

        // Lift Hill
        if (isBetween(9300, 15600)) {
            vogelrok.setState(this, "lift hill")
            if (!vogelrok.emergency) {
                if (speed < rolling && vogelrok.isSectionEmtpy(this, "block 1")) speed += 0.2
                if (speed < 0.0) speed = 0.0
            }
        }

        // Station
        if (isBetween(4800, 6700)) {
            vogelrok.setState(this, "station")
            if (!vogelrok.emergency) {
                if (vogelrok.dispatch && speed < 25.0 && vogelrok.isSectionEmtpy(this, "lift hill")) { speed += 0.3 }
                else {
                    if (internalBetween(position, 6160, 6600)) {
                        slowDown(0.2)
                        if (speed == 0.0) {
                            Main.instance.panelStorage.activePanels["vogelrok"]!!.buttons[6].equipment?.helmet = model(10)
                        }
                    }
                    else if (speed < rolling) { speed += 0.2 }
                    else if (speed > rolling) { slowDown(0.3) }
                }
            }
        } else if (vogelrok.getState(this) == "station") {
            vogelrok.setState(this, "lift hill")
            vogelrok.dispatch = false
        }

        if (vogelrok.emergency) {
            slowDown(0.7)
            if (speed < 0.0) speed = 0.0
        }

        position = position.movePos(speed)
        for(i in 0..5) {
            val cartPos = if ((position + i * -distance) < 0 ) position + i * -distance + vogelrok.track.size else position + i * -distance
            val trackPos = vogelrok.track[cartPos.toInt()]
            carts[i].teleportNC(trackPos.loc.copy().add(trackPos.up.copy().multiply(1.2)))
            carts[i].headPose = trackPos.rot
            val cartSeats = seats[i]
            val seatLocLeft = trackPos.loc.copy().add(trackPos.left.copy().multiply(0.5)).add(trackPos.up.copy().multiply(0.75)).add(Vector(0.0, -0.5, 0.0))
            val seatLocRight = trackPos.loc.copy().add(trackPos.left.copy().multiply(-0.5)).add(trackPos.up.copy().multiply(0.75)).add(Vector(0.0, -0.5, 0.0))
            val seatLocLeftB = seatLocLeft.copy().add(trackPos.dir.copy().multiply(-1.2))
            val seatLocRightB = seatLocRight.copy().add(trackPos.dir.copy().multiply(-1.2))
            cartSeats[0].teleportNC(seatLocLeft)
            cartSeats[1].teleportNC(seatLocRight)
            cartSeats[2].teleportNC(seatLocLeftB)
            cartSeats[3].teleportNC(seatLocRightB)
            val rotation = (atan2(trackPos.dir.x, trackPos.dir.z) / PI * -180.0).toFloat()
            for (seat in cartSeats)
                seat.setRotation(rotation, 0.0F)
        }
    }

    private fun model(id: Int): ItemStack {
        val model = ItemStack(Material.DIAMOND_AXE)
        val data = model.itemMeta!!
        data.isUnbreakable = true
        (data as Damageable).damage = id
        model.itemMeta = data
        return model
    }

    private fun isBetween(start: Int, end: Int): Boolean {
        return internalBetween(position, start, end) || internalBetween(position.movePos(-distance * 5.0), start, end)
    }

    private fun internalBetween(pos: Double, start: Int, end: Int): Boolean {
        if (pos > start && pos < end) {
            return true
        }
        return false
    }

    private fun inverseBetween(pos: Double, start: Int, end: Int): Boolean {
        if (pos > start || pos < end) {
            return true
        }
        return false
    }

    private fun slowDown(acc: Double) {
        if (speed > 0.0) {
            speed -= acc
            if (speed < 0.0) speed = 0.0
        } else if (speed < 0.0) {
            speed += acc
            if (speed > 0.0) speed = 0.0
        }
    }

    private fun Double.movePos(to: Double): Double {
        var new = this + to
        if (new < 0.0) new += vogelrok.track.size
        if (new > vogelrok.track.size) new = 0.0
        return new
    }

    fun stop() {
        for (cart in carts) cart.remove()
        carts.clear()
        this.cancel()
        for (seats in seats) {
            for (seat in seats) {
                seat.remove()
            }
        }
        seats.clear()
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
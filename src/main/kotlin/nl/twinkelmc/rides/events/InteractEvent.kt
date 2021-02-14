package nl.twinkelmc.rides.events

import nl.twinkelmc.rides.Main
import nl.twinkelmc.rides.panel.Panel
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class InteractEvent: Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.name.contains("seat")) {
            event.rightClicked.addPassenger(event.player)
        }
        if (event.rightClicked.name == "panelStand") {
            event.isCancelled = true
        }
        if (event.rightClicked.name.contains("locked"))
            event.isCancelled = true
        for (panel in Main.instance.panelStorage.panels) {
            if (panel.value.location.distance(event.player.location) < 5) {
                rayTrace(event.player, panel.value, panel.key)
            }
        }
    }

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        for (panel in Main.instance.panelStorage.panels) {
            if (panel.value.location.distance(event.player.location) < 5) {
                rayTrace(event.player, panel.value, panel.key)
            }
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

    private fun rayTrace(player: Player, panel: Panel, ride: String) {
        val precision = 10
        val pitch = -player.location.pitch / 180 * PI
        val yaw = -(player.location.yaw + 90) / 180 * PI
        val x = cos(pitch) * cos(yaw) / precision
        val y = sin(pitch) / precision
        val z = cos(pitch) * sin(-yaw) / precision
        val pos = player.location.add(0.0, if (player.isSneaking) 1.27 else 1.62, 0.0)

        for (i in 0..400) {
            pos.add(x, y, z)

            val hit = hitbox(pos.copy().add(0.0, -1.9, 0.0), panel)
            if (hit != -1) {
                if (panel.buttons[hit].model == 9) {
                    val rotation = 90 - Main.instance.panelStorage.activePanels[ride]!!.buttons[hit].location.yaw
                    Main.instance.panelStorage.activePanels[ride]!!.buttons[hit].setRotation(rotation, 0F)
                }
                if (panel.buttons[hit].model == 8) {
                    val newPos = 0.03 - ( panel.location.copy().add(panel.buttons[hit].offset).y - Main.instance.panelStorage.activePanels[ride]!!.buttons[hit].location.y  )
                    Main.instance.panelStorage.activePanels[ride]!!.buttons[hit].teleport(panel.location.copy().add(panel.buttons[hit].offset).add(0.0, -newPos, 0.0))
                }
                if (panel.buttons[hit].model == 10) {
                    val meta = Main.instance.panelStorage.activePanels[ride]!!.buttons[hit].equipment?.helmet?.itemMeta
                    val damage = (meta as Damageable).damage
                    if (damage == 10) {
                        Main.instance.panelStorage.activePanels[ride]!!.buttons[hit].equipment?.helmet = model(7)
                        if (ride == "vogelrok") {
                            Main.instance.vogelrok.dispatch = true
                        }
                    }
                }
                if (panel.buttons[hit].model == 8 && ride == "droomvlucht") {
                    Main.instance.droomvlucht.pauseAll()
                }
                return
            }
        }
    }

    private fun showParticle(loc: Location) {
        loc.world?.spawnParticle(Particle.REDSTONE, loc.x, loc.y, loc.z, 1, Particle.DustOptions(Color.AQUA, 0.1F))
    }

    private fun hitbox(loc: Location, panel: Panel): Int {
        for((index, button) in panel.buttons.withIndex()) {
            if (panel.location.copy().add(button.offset).distance(loc) < 0.08)
                return index
        }
        return -1
    }

    private fun Location.copy(): Location {
        return Location(this.world, this.x, this.y, this.z)
    }

}
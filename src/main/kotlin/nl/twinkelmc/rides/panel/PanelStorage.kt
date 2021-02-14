package nl.twinkelmc.rides.panel

import nl.twinkelmc.rides.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector
import java.io.File
import kotlin.math.PI

class PanelStorage {

    val panels = hashMapOf<String, Panel>()
    val activePanels = hashMapOf<String, ActivePanel>()

    fun showPanel(panel: String) {

        if (panels.containsKey(panel)) {
            val panelObject = panels[panel]!!
            val panelStand = Main.instance.standCreator.create(name = "panelStand",location = panelObject.location.copy(), visible = false, helmet = model(panelObject.model), head = EulerAngle(0.0, panelObject.rotation / 180 * PI, 0.0))
            val buttonStands = arrayListOf<ArmorStand>()
            for (button in panelObject.buttons) {
                buttonStands.add(Main.instance.standCreator.create(name = "panelStand",location = (panelObject.location.copy().add(button.offset)), visible = false, helmet = model(button.model), head = EulerAngle(0.0, panelObject.rotation / 180 * PI, 0.0)))
            }
            activePanels[panel] = ActivePanel(panelStand, buttonStands)
        }

    }

    private fun Location.copy(): Location {
        return Location(this.world, this.x, this.y, this.z)
    }

    fun removePanel(panel: String) {

        if (activePanels.containsKey(panel)) {

            val panelObject = activePanels[panel]!!
            panelObject.panel.remove()
            for (button in panelObject.buttons)
                button.remove()
            activePanels.remove(panel)

        }

    }

    fun loadPanels() {
        val yaml = YamlConfiguration.loadConfiguration(File("plugins/TMC-Rides/panels.yml"))
        val rides = arrayListOf<String>()
        for (key in yaml.getKeys(false)) {
            Bukkit.broadcastMessage(key)
            removePanel(key)
            loadPanel(key, yaml)
            showPanel(key)
        }
    }

    fun removePanels() {
        for (panelObject in activePanels.values) {
            panelObject.panel.remove()
            for (button in panelObject.buttons)
                button.remove()
        }
    }

    private fun loadPanel(ride: String, yaml: YamlConfiguration) {
        for (key in yaml.getKeys(true)) {
            if (key.startsWith("$ride.panel")) {
                val info = yaml.getString(key)!!.split(" ")
                panels[ride] = Panel(Location(Bukkit.getWorld(info[1]), info[2].toDouble(), info[3].toDouble(), info[4].toDouble()), info[5].toDouble(), info[0].toInt())
            } else if (key.startsWith("$ride.buttons.")) {
                if (panels.containsKey(ride)) {
                    val panel = panels[ride]!!
                    val info = yaml.getString(key)!!.split(" ")
                    panel.buttons.add(Button(panel = panel, model = info[0].toInt(), offset = Vector(info[1].toDouble(), info[2].toDouble(), info[3].toDouble())))
                }
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

}
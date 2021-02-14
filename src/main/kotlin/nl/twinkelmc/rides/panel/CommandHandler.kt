package nl.twinkelmc.rides.panel

import nl.twinkelmc.rides.Main
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class CommandHandler: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender is Player) {

            when (args[0]) {
                "createpanel" -> {
                    Main.instance.panelStorage.removePanel(args[1])
                    Main.instance.panelStorage.panels[args[1]] = Panel(Location(sender.world, args[2].toDouble(), args[3].toDouble(), args[4].toDouble()), args[5].toDouble(), args[6].toInt())
                    Main.instance.panelStorage.showPanel(args[1])
                }
                "showpanel" -> {
                    //sender.sendMessage(Main.instance.panelStorage.panels[args[1]].toString())
                    Main.instance.panelStorage.showPanel(args[1])
                }
                "addbutton" -> {
                    Main.instance.panelStorage.removePanel(args[1])
                    if (Main.instance.panelStorage.panels.containsKey(args[1])) {
                        val panel = Main.instance.panelStorage.panels[args[1]]!!
                        panel.buttons.add(Button(panel = panel, model = args[2].toInt(), offset = Vector(args[3].toDouble(), args[4].toDouble(), args[5].toDouble())))
                    }
                    Main.instance.panelStorage.showPanel(args[1])
                }
                "clearbuttons" -> {
                    Main.instance.panelStorage.removePanel(args[1])
                    if (Main.instance.panelStorage.panels.containsKey(args[1])) {
                        val panel = Main.instance.panelStorage.panels[args[1]]!!
                        panel.buttons.clear()
                    }
                    Main.instance.panelStorage.showPanel(args[1])
                }
                "rempanel" -> {
                    //sender.sendMessage(Main.instance.panelStorage.panels[args[1]].toString())
                    Main.instance.panelStorage.removePanel(args[1])
                }
                "delpanel" -> {
                    Main.instance.panelStorage.panels.remove(args[1])
                }
            }

        }

        return false

    }

}
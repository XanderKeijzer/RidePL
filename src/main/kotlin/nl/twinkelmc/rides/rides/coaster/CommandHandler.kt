package nl.twinkelmc.rides.rides.coaster

import nl.twinkelmc.rides.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class CommandHandler: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isNotEmpty()) {
            when(args[0]) {
                "vogelrok" -> {
                    when(args[1]) {
                        "dispatch" -> Main.instance.vogelrok.dispatch = true
                        "emergency" -> Main.instance.vogelrok.emergency = !Main.instance.vogelrok.emergency
                    }

                }
            }
        }
        return false
    }
}
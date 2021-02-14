package nl.twinkelmc.rides.rides

import nl.twinkelmc.rides.Main
import nl.twinkelmc.rides.rides.utils.RenderUtils
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class CommandHandler: CommandExecutor {

    private val renderUtils = RenderUtils()
    private val stands = arrayListOf<ArmorStand>()
    private val standCreator = Main.instance.standCreator

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.size == 1) {
            when (args[0]) {
                "dvstart" -> Main.instance.droomvlucht.start()
                "dvstop" -> Main.instance.droomvlucht.stop()
                "pauseall" -> Main.instance.droomvlucht.pauseAll()

                "removetrack" -> Main.instance.trackRenderer.clear()

                "pause" -> {
                    if (sender is Player)
                        Main.instance.droomvlucht.pause(sender)
                }
                "sit" -> {
                    if (sender is Player) {
                        var closestDistance = 100000000.0
                        var closestEntity = sender.world.entities[0]
                        for (entity in sender.world.entities) {
                            if (entity.name == "dvseat" && entity.passengers.size == 0) {
                                if (entity.location.distance(sender.location) < closestDistance) {
                                    closestDistance = entity.location.distance(sender.location)
                                    closestEntity = entity
                                }
                            }
                        }
                        closestEntity.addPassenger(sender)
                    }
                }
                "killtrack" -> {
                    if (sender is Player) {
                        for (entity in sender.world.entities) {
                            if (entity is ArmorStand) {
                                if (entity.equipment?.helmet?.type == Material.BIRCH_TRAPDOOR) {
                                    entity.remove()
                                }
                            }
                        }
                    }
                }
                "killnear" -> {
                    if (sender is Player) {
                        for (entity in sender.world.entities) {
                            if (entity.location.distance(sender.location) < 100) {
                                if (entity.type == EntityType.ARMOR_STAND) {
                                    entity.remove()
                                }
                            }
                        }
                    }
                }
            }


            return true
//
//            if (args[0] == "start") {
//                render()
//            } else {
//                for (stand in stands) {
//                    stand.remove()
//                }
//                stands.clear()
//            }
//        } else if (args.size == 2 && sender is Player) {
//            when (args[0]) {
//                "goto" -> Main.instance.droomvlucht.showLoc(sender, args[1].toInt())
//            }
//        }
        } else if (args.size == 2) {
            if (args[0] == "goto") {
                if (sender is Player)
                    sender.teleport(Main.instance.droomvlucht.track[args[1].toInt()].loc)
            }
            if (args[0] == "pause")
                Main.instance.droomvlucht.pause(args[1].toInt())
        } else if (args[0] == "render") {
            Main.instance.trackRenderer.showTrack(args[1], Vector(args[2].toDouble(), args[3].toDouble(), args[4].toDouble()))
        } else if (args[0] == "find") {
            if (sender is Player) {
                sender.teleport(Main.instance.trackRenderer.track[args[1].toInt()].loc)
            }
        }

        if (args[0] == "teststand") {

            if (sender is Player) {

                val id = args[1].toInt()
                Main.protocolCreator.spawnEntity(id, sender.location, arrayListOf(sender))
                Main.protocolCreator.setHelmet(id, ItemStack(Material.STONE), arrayListOf(sender))
                sender.sendMessage("Created client stand at your location")

            }

        }

        return false
    }


//    private fun render() {
//        val reader = Files.newBufferedReader(Paths.get("plugins/TMC-Rides/DV.csv"))
//        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withQuote(null).withNullString(""))
//        val location = Location(Bukkit.getWorld("world"), 50.5, 83.5, -716.0)
//        location.add(-44.0, -6.0, -16.0)
//
//        for ((count, csvRecord) in csvParser.withIndex()) {
//            if (count == 0 || count % 66 != 0)
//                continue
//
//            val name = csvRecord.get(0)
//            val data = name.split("\t")
//            var x = 0.0; var y = 0.0; var z = 0.0
//            var fx = 0.0; var fy = 0.0; var fz = 0.0
//            var ux = 0.0; var uy = 0.0; var uz = 0.0
//
//            for ((index, value) in data.withIndex()) {
//                when(index) {
//                    1 -> x = value.toDouble()
//                    2 -> y = value.toDouble()
//                    3 -> z = value.toDouble()
//                    4 -> fx = value.toDouble()
//                    5 -> fy = value.toDouble()
//                    6 -> fz = value.toDouble()
//                    10 -> ux = value.toDouble()
//                    11 -> uy = value.toDouble()
//                    12 -> uz = value.toDouble()
//                }
//            }
//
//            val dir = Vector(fx, fy, fz)
//            val up = Vector(ux, uy, uz)
//            val newLoc = Location(location.world, location.x + x, location.y + y, location.z + z)
//            val angle = getCorrectArmorStandAngle(dir, up)
//
//            stands.add(standCreator.create(newLoc, visible = false, head = angle, helmet = ItemStack(Material.BIRCH_TRAPDOOR)))
//        }
//    }

//    private fun getCorrectArmorStandAngle(dir: Vector, up: Vector): EulerAngle {
//        val quaternion = Quaternion()
//        val rotation: Quaternion = quaternion.fromLookDirection(dir, up)!!
//        val qx: Double = rotation.x
//        val qy: Double = rotation.y
//        val qz: Double = rotation.z
//        val qw: Double = rotation.w
//        val rx = 1.0 + 2.0 * (-qy * qy - qz * qz)
//        val ry = 2.0 * (qx * qy + qz * qw)
//        val rz = 2.0 * (qx * qz - qy * qw)
//        val uz = 2.0 * (qy * qz + qx * qw)
//        val fz = 1.0 + 2.0 * (-qx * qx - qy * qy)
//        return if (abs(rz) < 1.0 - 1E-15) {
//            EulerAngle(atan2(uz, fz), asin(rz), atan2(-ry, rx))
//        } else {
//            val sign = if (rz < 0) -1.0 else 1.0
//            EulerAngle(0.0, sign * 90.0, -sign * 2.0 * renderUtils.atanTwo(qx, qw))
//        }
//    }

}
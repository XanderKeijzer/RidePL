package nl.twinkelmc.rides.rides.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.util.EulerAngle

class StandCreator {

    fun create(location: Location, name: String = "", gravity: Boolean = false, visible: Boolean = true, nameVisible: Boolean = false, small: Boolean = false, head: EulerAngle = EulerAngle(0.0, 0.0, 0.0), helmet: ItemStack = ItemStack(Material.AIR)): ArmorStand {

        val stand = location.world?.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
        stand.setGravity(gravity)
        stand.isVisible = visible
        stand.isCustomNameVisible = nameVisible
        stand.headPose = head
        stand.isSmall = small
        if (name != "") stand.customName = name
        if (helmet.type != Material.AIR) stand.equipment?.helmet = helmet
        return stand

    }

    fun createNC(location: Location, name: String = "", visible: Boolean = true, nameVisible: Boolean = false, small: Boolean = false, head: EulerAngle = EulerAngle(0.0, 0.0, 0.0), helmet: ItemStack = ItemStack(Material.AIR)): ArmorStand {
        val stand = location.world?.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
        stand.setGravity(true)
        stand.isVisible = visible
        stand.isCustomNameVisible = nameVisible
        stand.headPose = head
        stand.isSmall = small
        if (name != "") stand.customName = name
        if (helmet.type != Material.AIR) stand.equipment?.helmet = helmet
        (stand as CraftEntity).handle.noclip = true
        return stand

    }

}
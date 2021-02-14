package nl.twinkelmc.rides.modules

class EntityID {

    private var nextId: Int = 100000000

    fun nextID(): Int {
        nextId++
        return nextId
    }

}
package com.reydder.posion

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class PoisonComponent(
    private var damagePerTick: Float,
    private var tickInterval: Float,
    private var remainingTicks: Int,
    private var elapsedTime: Float = 0F
): Component<EntityStore?> {

    constructor(): this(3F, 0.5F, 8)

    constructor(poisonComponent: PoisonComponent): this(
        poisonComponent.damagePerTick,
        poisonComponent.tickInterval,
        poisonComponent.remainingTicks,
        poisonComponent.elapsedTime)

    override fun clone(): Component<EntityStore?> {
        return PoisonComponent(this)
    }

    fun getDamagePerTick(): Float = damagePerTick
    fun getTickInterval(): Float = tickInterval
    fun getRemainingTicks(): Int = remainingTicks
    fun getElapsedTime(): Float = elapsedTime

    fun addElapsedTime(dt: Float) {
        elapsedTime += dt
    }

    fun resetElapsedTime() {
        elapsedTime = 0f
    }

    fun decrementRemainingTicks() {
        remainingTicks--
    }

    fun isExpired(): Boolean {
        return remainingTicks <= 0
    }
}
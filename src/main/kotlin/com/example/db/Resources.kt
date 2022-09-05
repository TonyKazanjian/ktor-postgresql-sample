package com.example.db

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/customers")
class Customers {
    @Serializable
    @Resource("new")
    class New(val parent: Customers = Customers())

    @Serializable
    @Resource("{id}")
    class Id(val parent: Customers = Customers(), val id: Long) {
        @Serializable
        @Resource("edit")
        class Edit(val parent: Id)
    }
}
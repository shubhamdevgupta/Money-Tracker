package com.androiddev.moneytracker

class MessagesModel {
    var message: String = ""
    var isOutgoing: Boolean = false

    constructor()
    constructor(
        message: String,
        isOutgoing: Boolean
    ) {
        this.message = message
        this.isOutgoing = isOutgoing
    }
}


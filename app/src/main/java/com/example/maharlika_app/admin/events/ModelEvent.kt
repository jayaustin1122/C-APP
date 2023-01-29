package com.example.maharlika.ui.admin.events

class ModelEvent {

    var id : String = ""
    var eventsTitle : String = ""
    var eventsDescription : String = ""
    var timestamp : Long = 0
    var uid : String = ""
    var image : String = ""
    var currentDate : String = ""
    var currentTime : String = ""


    //empty constructor required by firebase
    //param
    constructor()
    constructor(id: String, eventsTitle: String, eventsDescription: String, timestamp: Long, uid: String,image : String,currentDate: String, currentTime: String) {
        this.id = id
        this.eventsTitle = eventsTitle
        this.eventsDescription = eventsDescription
        this.timestamp = timestamp
        this.uid = uid
        this.image
        this.currentDate = currentDate
        this.currentTime = currentTime
    }

}
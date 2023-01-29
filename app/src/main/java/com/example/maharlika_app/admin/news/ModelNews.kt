package com.example.maharlika_app.admin.news

class ModelNews {
    var id : String = ""
    var newsTitle : String = ""
    var newsDescription : String = ""
    var timestamp : Long = 0
    var uid : String = ""
    var image : String = ""
    var currentDate : String = ""
    var currentTime : String = ""

    constructor()
    constructor(
        id: String,
        eventsTitle: String,
        eventsDescription: String,
        timestamp: Long,
        uid: String,
        image: String,
        currentDate: String,
        currentTime: String
    ) {
        this.id = id
        this.newsTitle = eventsTitle
        this.newsDescription = eventsDescription
        this.timestamp = timestamp
        this.uid = uid
        this.image = image
        this.currentDate = currentDate
        this.currentTime = currentTime
    }
}
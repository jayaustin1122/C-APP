package com.example.maharlika_app.user.notfications

class ModelNotif {

    var uidNotif : String = ""
    var NotifTitle : String = ""
    var NotifDescription : String = ""
    var imageNotif : String = ""
    var currentDateNotif : String = ""
    var currentTimeNotif : String = ""
    var id : String = ""

    constructor()
    constructor(
        uidNotif: String,
        NotifTitle: String,
        NotifDescription: String,
        imageNotif: String,
        currentDateNotif: String,
        currentTimeNotif: String,
        id: String
    ) {
        this.uidNotif = uidNotif
        this.NotifTitle = NotifTitle
        this.NotifDescription = NotifDescription
        this.imageNotif = imageNotif
        this.currentDateNotif = currentDateNotif
        this.currentTimeNotif = currentTimeNotif
        this.id = id
    }


}
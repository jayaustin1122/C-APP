package com.example.maharlika_app.model

class UserModel {
        var uid: String = ""
        var email: String = ""
        var password: String = ""
        var fullName: String = ""
        var address: String = ""
        var image: String = ""
        var userType: String = ""
        var currentDate: String = ""
        var currentTime: String = ""
        var id : String = ""


        constructor(
                uid: String,
                email: String,
                password: String,
                fullName: String,
                address: String,
                image: String,
                userType: String,
                currentDate: String,
                currentTime: String,
                id: String
        ) {
                this.uid = uid
                this.email = email
                this.password = password
                this.fullName = fullName
                this.address = address
                this.image = image
                this.userType = userType
                this.currentDate = currentDate
                this.currentTime = currentTime
                this.id = id
        }

        constructor()
}

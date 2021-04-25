package com.example.votingapp.models

 class Contestants {
     var name: String? = null
    var post:String? = null
    var image:String? = null
    var userId:String? = null
    lateinit var totalCount:String
     constructor(name:String?,post:String?,image:String?, userId:String?, totalCount:String){
         this.name = name
         this.post = post
         this.image = image
         this.userId = userId
         this.totalCount = totalCount
     }

     constructor(){}
 }
package com.example.votingapp.models

 class Contestants {
     var name: String? = null
    var post:String? = null
    var image:String? = null
    var userId:String? = null
    var totalCount:Int? = null

     constructor(name:String?,post:String?,image:String?, userId:String?, totalCount:Int?){
         this.name = name
         this.post = post
         this.image = image
         this.userId = userId
         this.totalCount = totalCount
     }

     constructor(){}
 }
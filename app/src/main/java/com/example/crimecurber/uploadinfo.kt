package com.example.crimecurber

class uploadinfo {
    var videoLocation: String? = null
    var videourl: String? = null

    constructor() {}
    constructor(name: String?, url: String?) {
        videoLocation = name
        videourl = url
    }
}
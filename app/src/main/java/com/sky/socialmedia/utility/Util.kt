package com.sky.socialmedia.utility

fun String.determineUserName(userName: String?): String {
    var charArray: ArrayList<Char> = arrayListOf()

    userName?.let { name ->

        for (char in name) {
            if (char == '@') {
                break
            }
            charArray.add(char)

        }
    }
    return charArray.joinToString(separator = "")
}
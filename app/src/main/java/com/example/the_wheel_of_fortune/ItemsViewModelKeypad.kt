package com.example.the_wheel_of_fortune

class ItemsViewModelKeypad(text: Char,vis:Boolean) {
    var text: Char? = null
    var vis: Boolean? = false
    init{
        this.text=text;
        this.vis=vis;
    }
}

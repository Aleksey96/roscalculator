package com.example.calculatorrb

import android.content.Context
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

class CustomAdapter constructor(/*@get:JvmName("getContext_")*/private val _context: Context,
                                                               private val _textViewResourceId:Int,
                                                               private  val _objects:Array<String>):
    ArrayAdapter<String>(_context,_textViewResourceId,_objects) {
    companion object {
        var flag = false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup):View {
        var cnvertView:View
        if (convertView == null)
        {
            cnvertView=inflate(_context, _textViewResourceId, null)}
        else
            cnvertView=convertView
        if (flag != false) {
            val tv = convertView as? TextView
            tv?.setText(_objects[position])
        }
        return cnvertView
    }
}

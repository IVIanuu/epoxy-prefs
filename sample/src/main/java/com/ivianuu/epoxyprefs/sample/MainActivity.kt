package com.ivianuu.epoxyprefs.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.ivianuu.epoxyprefs.*
import com.ivianuu.epoxyprefs.ext.dependency
import com.ivianuu.epoxyprefs.ext.urlClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.buildModelsWith2 {
           for (i in 0 until 100) {
               categoryPreference(this@MainActivity) {
                   key("category_$i")
                   title("Category $i")
               }

               switchPreference(this@MainActivity) {
                   key("my_switch_$i")
                   title("Switch")
                   summary("Nice a switch")
               }

               editTextPreference(this@MainActivity) {
                   key("my_edit_text_$i")
                   title("Edit text")
                   summary("Edit text")
                   dialogHint("Hello lets type something")
                   dependency("my_switch_$i", true)
               }

               preference(this@MainActivity) {
                   key("my_key_$i")
                   title("Title")
                   summary("This is a summary.")
                   dependency("my_switch_$i", true)
                   urlClickListener { "www.google.de" }
               }

               checkboxPreference(this@MainActivity) {
                   key("my_checkbox_$i")
                   title("CheckBox")
                   dependency("my_switch_$i", true)
                   summary("Oh a checkbox")
               }

               radioButtonPreference(this@MainActivity) {
                   key("my_radio_$i")
                   title("Radio")
                   dependency("my_switch_$i", true)
                   summary("A radio button")
               }

               seekBarPreference(this@MainActivity) {
                   key("my_seekbar_$i")
                   title("SeekBar")
                   max(100)
                   summary("Hey there im a seekbar")
                   dependency("my_switch_$i", true)
               }

               singleItemListPreference(this@MainActivity) {
                   key("single_item_list_$i")
                   title("Single item list")
                   entries(arrayOf("1", "2", "3"))
                   entryValues(arrayOf("1", "2", "3"))
                   negativeButtonText("Cancel")
                   dependency("my_switch_$i", true)
               }

               multiSelectListPreference(this@MainActivity) {
                   key("multi_select_list_$i")
                   title("Multi select list")
                   entries(arrayOf("A", "B", "C"))
                   entryValues(arrayOf("A", "B", "C"))
                   positiveButtonText("OK")
                   negativeButtonText("Cancel")
                   dependency("my_switch_$i", true)
               }

               preference(this@MainActivity) {
                   key("my_key1_$i")
                   title("Another Title")
                   summary("This is another summary.")
                   dependency("my_switch_$i", true)
               }
           }
        }
    }

    private fun EpoxyRecyclerView.buildModelsWith2(callback: EpoxyController.() -> Unit) {
        buildModelsWith(callback)
    }
}

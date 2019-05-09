package com.ivianuu.epoxyprefs.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.epoxyprefs.*
import kotlinx.android.synthetic.main.activity_main.list

class MainActivity : AppCompatActivity() {

    private var nonPersistentState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addItemDecoration(PreferenceDividerDecoration(this))

        list.buildPreferencesWith {
            for (i in 0..100) {
                categoryPreference {
                    key("category_$i")
                    title("Category $i")
                }

                switchPreference {
                    key("my_switch_$i")
                    title("Switch")
                    summary("Nice a switch")
                }

                if (sharedPreferences.getBoolean("my_switch_$i", false)) {
                    editTextPreference {
                        key("my_edit_text_$i")
                        title("Edit text")
                        summary("Edit text")
                        dialogHint("Hello lets type something")
                    }
                }

                preference {
                    key("my_key_$i")
                    //  title("Title")
                    summary("This is a summary.")
                    dependency("my_switch_$i", true)
                    onClickUrl { "https://www.google.de/" }
                }

                checkboxPreference {
                    key("my_checkbox_$i")
                    title("CheckBox")
                    dependency("my_switch_$i", true)
                    summary("Oh a checkbox")
                    defaultValue(nonPersistentState)
                    persistent(false)
                    onChange { newValue ->
                        nonPersistentState = newValue
                        requestModelBuild()
                        return@onChange true
                    }
                }

                radioButtonPreference {
                    key("my_radio_$i")
                    title("Radio")
                    dependency("my_switch_$i", true)
                    summary("A radio button")
                }

                seekBarPreference {
                    key("my_seekbar_$i")
                    title("SeekBar")
                    max(100)
                    summary("Hey there im a seekbar")
                    dependency("my_switch_$i", true)
                }

                singleItemListPreference {
                    key("single_item_list_$i")
                    title("Single item list")
                    entries(arrayOf("1", "2", "3"))
                    entryValues(arrayOf("1", "2", "3"))
                    dependency("my_switch_$i", true)
                }

                multiSelectListPreference {
                    key("multi_select_list_$i")
                    title("Multi select list")
                    entries(arrayOf("A", "B", "C"))
                    entryValues(arrayOf("A", "B", "C"))
                    dependency("my_switch_$i", true)
                }

                preference {
                    key("my_key1_$i")
                    title("Another Title")
                    summary("This is another summary.")
                    dependency("my_switch_$i", true)
                }
            }
        }
    }
}
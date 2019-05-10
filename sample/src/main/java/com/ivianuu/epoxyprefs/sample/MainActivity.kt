package com.ivianuu.epoxyprefs.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.epoxyprefs.*
import kotlinx.android.synthetic.main.activity_main.list

class MainActivity : AppCompatActivity() {

    private var nonPersistentState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addItemDecoration(PreferenceDividerDecoration(this))

        val epoxyController = preferenceEpoxyController {
            for (i in 0..100) {
                CategoryPreference {
                    key("category_$i")
                    title("Category $i")
                }

                SwitchPreference {
                    key("my_switch_$i")
                    title("Switch")
                    summary("Nice a switch")
                }

                val switchDependency =
                    AbstractPreferenceModel.Dependency("my_switch_$i", true)

                if (context.getOrDefault("my_switch_$i", false)) {
                    EditTextListPreference {
                        key("my_edit_text_$i")
                        title("Edit Text")
                        summary("Edit Text")
                        dialogHint("Hello lets type something")
                    }
                }

                Preference {
                    key("my_key_$i")
                    title("Title")
                    summary("This is a summary.")
                    onClick {
                        Toast.makeText(
                            this@MainActivity,
                            "Hello", Toast.LENGTH_SHORT
                        ).show()
                        return@onClick true
                    }
                    dependencies(switchDependency)
                }

                CheckboxPreference {
                    key("my_checkbox_$i")
                    title("CheckBox")
                    dependencies(switchDependency)
                    defaultValue(nonPersistentState)
                    isPersistent(false)
                    onChange {
                        nonPersistentState = it
                        requestModelBuild()
                        return@onChange true
                    }
                }

                RadioButtonPreference {
                    key("my_radio_$i")
                    title("Radio")
                    dependencies(switchDependency)
                    summary("A radio button")
                }

                SeekBarListPreference {
                    key("my_seekbar_$i")
                    title("SeekBar")
                    max(100)
                    summary("He there im a seekbar")
                    dependencies(switchDependency)
                }

                MultiSelectListPreference {
                    key("multi_select_list_$i")
                    title("Multi select list")
                    entries(arrayOf("A", "B", "C"))
                    entryValues(arrayOf("A", "B", "C"))
                    defaultValue(setOf("B", "C"))
                    dependencies(switchDependency)
                }

                Preference {
                    key("my_key1_$i")
                    title("Another Title")
                    summary("This is another summary.")
                    dependencies(switchDependency)
                }
            }
        }

        list.setControllerAndBuildModels(epoxyController)
    }
}
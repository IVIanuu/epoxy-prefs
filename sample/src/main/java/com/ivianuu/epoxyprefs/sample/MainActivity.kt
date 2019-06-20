package com.ivianuu.epoxyprefs.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.epoxyprefs.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var nonPersistentState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.addItemDecoration(PreferenceDividerDecoration(this))

        val epoxyController = preferenceEpoxyController {
            Preference {
                key("single_line")
                title("Single line")
            }

            Preference {
                key("with summary")
                title("With summary")
                summary("Lolo")
            }

            Preference {
                key("with icon")
                title("With icon")
                iconRes(R.drawable.abc_ic_clear_material)
            }

            Preference {
                key("preserve icon space")
                title("Preserve icon space")
                preserveIconSpace(true)
            }

            SeekBarPreference {
                key("single line seek bar")
                title("Single line seek bar")
            }

            SeekBarPreference {
                key("seek bar with summary")
                title("Seek bar with summary")
                summary("This is a summary")
            }

            SeekBarPreference {
                key("seek bar with icon")
                title("Seek bar with icon")
                iconRes(R.drawable.abc_ic_clear_material)
            }

            SeekBarPreference {
                key("seek bar with preserve icon space")
                title("Seek bar with preserve icon space")
                preserveIconSpace(true)
            }

            for (i in 0..100) {
                CategoryPreference {
                    key("category_$i")
                    title("Category $i")
                    preserveIconSpace(true)
                }

                SwitchPreference {
                    key("my_switch_$i")
                    summary("Nice a switch")
                    title("Switch")
                    preserveIconSpace(true)
                }

                val switchDependency =
                    AbstractPreferenceModel.Dependency("my_switch_$i", true)

                if (context.getOrDefault("my_switch_$i", false)) {
                    EditTextPreference {
                        key("my_edit_text_$i")
                        title("Edit Text")
                        summary("Edit Text")
                        dialogHint("Hello lets type something")
                        preserveIconSpace(true)
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
                    preserveIconSpace(true)
                }

                CheckBoxPreference {
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
                    preserveIconSpace(true)
                }

                RadioButtonPreference {
                    key("my_radio_$i")
                    title("Radio")
                    summary("A radio button")
                    dependencies(switchDependency)
                    preserveIconSpace(true)
                }

                SeekBarPreference {
                    key("my_seekbar_$i")
                    title("SeekBar")
                    summary("He there im a seekbar")
                    max(100)
                    dependencies(switchDependency)
                    preserveIconSpace(true)
                }

                MultiSelectListPreference {
                    key("multi_select_list_$i")
                    title("Multi select list")
                    entries(arrayOf("A", "B", "C"))
                    entryValues(arrayOf("A", "B", "C"))
                    defaultValue(setOf("B", "C"))
                    dependencies(switchDependency)
                    preserveIconSpace(true)
                }

                Preference {
                    key("my_key1_$i")
                    title("Another Title")
                    summary("This is another summary.")
                    dependencies(switchDependency)
                    preserveIconSpace(true)
                }
            }
        }

        list.setControllerAndBuildModels(epoxyController)
    }
}
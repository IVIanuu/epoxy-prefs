package com.ivianuu.epoxyprefs.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivianuu.epoxyprefs.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var nonPersistentState = false

    private val expandedPositions = mutableSetOf<Int>()

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
                key("long pref")
                title("Loooong pref")
                iconRes(R.drawable.abc_ic_clear_material)
                summary("Lololololloodvnvjkfnxkjvnkcnfjbknjfckbnkjfb fxkbkjse jgk grskjefnengsrog srjg josr grjk rggr kjcf kbxfj bjkd bk jxkf b jjxfk bjkf xkkx ake ks kjves")
            }

            SeekBarPreference {
                key("single line seek bar")
                title("Single line seek bar")
                showSeekBarValue(false)
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

            SwitchPreference {
                key("no preserve switch")
                title("No Preserve Switch")
                preserveIconSpace(false)
            }

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
                    EditTextPreference {
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
                }

                RadioButtonPreference {
                    key("my_radio_$i")
                    title("Radio")
                    summary("A radio button")
                    dependencies(switchDependency)
                }

                SeekBarPreference {
                    key("my_seekbar_$i")
                    title("SeekBar")
                    summary("He there im a seekbar")
                    max(100)
                    dependencies(switchDependency)
                }

                if (expandedPositions.contains(i)) {
                    SingleItemListPreference {
                        key("single_item_list_$i")
                        title("Single item list")
                        entries(arrayOf("A", "B", "C"))
                        entryValues(arrayOf("A", "B", "C"))
                        defaultValue("A")
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
                } else {
                    ExpandPreference {
                        key("expand_$i")
                        title("Show more")
                        summary("Multi select, single list, another pref")
                        dependencies(switchDependency)
                        onClick {
                            expandedPositions.add(i)
                            requestModelBuild()
                            return@onClick true
                        }
                    }
                }
            }
        }

        list.setControllerAndBuildModels(epoxyController)
    }
}
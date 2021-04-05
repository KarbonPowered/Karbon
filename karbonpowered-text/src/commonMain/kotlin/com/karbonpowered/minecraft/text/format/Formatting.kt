package com.karbonpowered.minecraft.text.format

// TODO: Class copied from MC sources, need will remake it
enum class Formatting(
    val formatName: String,
    val code: Char,
    val isModifier: Boolean,
    val colorIndex: Int = -1,
    val colorValue: Int? = null as Int?
) {
    BLACK("BLACK", '0', 0, 0), DARK_BLUE("DARK_BLUE", '1', 1, 170), DARK_GREEN(
        "DARK_GREEN",
        '2',
        2,
        43520
    ),
    DARK_AQUA("DARK_AQUA", '3', 3, 43690), DARK_RED("DARK_RED", '4', 4, 11141120), DARK_PURPLE(
        "DARK_PURPLE",
        '5',
        5,
        11141290
    ),
    GOLD("GOLD", '6', 6, 16755200), GRAY("GRAY", '7', 7, 11184810), DARK_GRAY(
        "DARK_GRAY",
        '8',
        8,
        5592405
    ),
    BLUE("BLUE", '9', 9, 5592575), GREEN("GREEN", 'a', 10, 5635925), AQUA("AQUA", 'b', 11, 5636095), RED(
        "RED",
        'c',
        12,
        16733525
    ),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 16733695), YELLOW("YELLOW", 'e', 14, 16777045), WHITE(
        "WHITE",
        'f',
        15,
        16777215
    ),
    OBFUSCATED("OBFUSCATED", 'k', true), BOLD("BOLD", 'l', true), STRIKETHROUGH(
        "STRIKETHROUGH",
        'm',
        true
    ),
    UNDERLINE("UNDERLINE", 'n', true), ITALIC("ITALIC", 'o', true), RESET("RESET", 'r', -1, null as Int?);

    val stringValue: String

    constructor(name: String, code: Char, colorIndex: Int, colorValue: Int?) : this(
        name,
        code,
        false,
        colorIndex,
        colorValue
    )

    val isColor: Boolean
        get() = !isModifier && this != RESET

    fun getName(): String {
        return name.toLowerCase()
    }

    override fun toString(): String {
        return stringValue
    }

    companion object {
        const val colorChar = '§'
        private val BY_NAME: Map<String, Formatting> =
            values().associateBy { sanitize(it.name) }
        private val FORMATTING_CODE_PATTERN = Regex("(?i)§[0-9A-FK-OR]")

        private fun sanitize(name: String): String {
            return name.toLowerCase().replace("[^a-z]".toRegex(), "")
        }

        fun strip(string: String?): String? {
            return if (string == null) null else FORMATTING_CODE_PATTERN.replace(string, "")
        }

        fun byName(name: String?): Formatting? {
            return if (name == null) null else BY_NAME[sanitize(name)] as Formatting?
        }

        fun byColorIndex(colorIndex: Int): Formatting? {
            return if (colorIndex < 0) {
                RESET
            } else {
                val var1: Array<Formatting> = values()
                val var2 = var1.size
                for (var3 in 0 until var2) {
                    val formatting = var1[var3]
                    if (formatting.colorIndex == colorIndex) {
                        return formatting
                    }
                }
                null
            }
        }

        fun byCode(code: Char): Formatting? {
            val c: Char = code.toString().toLowerCase().get(0)
            val var2: Array<Formatting> = values()
            val var3 = var2.size
            for (var4 in 0 until var3) {
                val formatting = var2[var4]
                if (formatting.code == c) {
                    return formatting
                }
            }
            return null
        }

        fun getNames(colors: Boolean, modifiers: Boolean): Collection<String> {
            val list: MutableList<String> = mutableListOf()
            val var3: Array<Formatting> = values()
            val var4 = var3.size
            for (var5 in 0 until var4) {
                val formatting = var3[var5]
                if ((!formatting.isColor || colors) && (!formatting.isModifier || modifiers)) {
                    list.add(formatting.getName())
                }
            }
            return list
        }
    }

    init {
        stringValue = "§$code"
    }
}

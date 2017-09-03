
package sheet.format


import effect.apply
import effect.effError
import effect.effValue
import effect.split
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.UnexpectedValue
import lulo.value.ValueError
import lulo.value.ValueParser
import theme.ColorTheme
import java.io.Serializable



/**
 * Icon
 */
sealed class Icon : Serializable
{

    object Sword : Icon()

    object Shield : Icon()

    object DiceRoll : Icon()

    object DiceRollFilled : Icon()

    object Coins : Icon()

    object Parchment : Icon()

    object SwordOutline : Icon()

    object Adventure : Icon()

    object Mind : Icon()

    object Running : Icon()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Icon> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "sword"             -> effValue<ValueError,Icon>(Icon.Sword)
                "shield"            -> effValue<ValueError,Icon>(Icon.Shield)
                "dice_roll"         -> effValue<ValueError,Icon>(Icon.DiceRoll)
                "dice_roll_filled"  -> effValue<ValueError,Icon>(Icon.DiceRollFilled)
                "coins"             -> effValue<ValueError,Icon>(Icon.Coins)
                "parchment"         -> effValue<ValueError,Icon>(Icon.Parchment)
                "sword_outline"     -> effValue<ValueError,Icon>(Icon.SwordOutline)
                "adventure"         -> effValue<ValueError,Icon>(Icon.Adventure)
                "mind"              -> effValue<ValueError,Icon>(Icon.Mind)
                "running"           -> effValue<ValueError,Icon>(Icon.Running)
                else                -> effError<ValueError,Icon>(
                        UnexpectedValue("Icon", doc.text, doc.path))
            }
            else            -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }


}

data class IconFormat(val colorTheme : ColorTheme,
                      val size : IconSize)
                       : Serializable
{

    companion object
    {

        private val defaultColorTheme = ColorTheme.black
        private val defaultIconSize   = IconSize.default()

        fun fromDocument(doc : SchemaDoc) : ValueParser<IconFormat> = when (doc)
        {
            is DocDict ->
            {
                apply(::IconFormat,
                      // Color Theme
                      split(doc.maybeAt("color_theme"),
                            effValue(defaultColorTheme),
                            { ColorTheme.fromSchemaDoc(it) }),
                      // Size
                      split(doc.maybeAt("size"),
                            effValue(defaultIconSize),
                            { IconSize.fromSchemaDoc(it) })
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }

    }

}


/**
 * Icon Size
 */
data class IconSize(val width : Int, val height : Int) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<IconSize> = when (doc)
        {
            is DocDict ->
            {
                apply(::IconSize, doc.int("width"), doc.int("width"))
            }
            else       -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }

        fun default() = IconSize(20, 20)
    }

}


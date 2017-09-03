
package theme


import effect.apply
import effect.effError
import effect.effValue
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import sheet.Page
import sheet.PageName
import java.io.Serializable



/**
 * Theme Id
 */
sealed class ThemeId : Serializable
{

    object Light : ThemeId()


    object Dark : ThemeId()


    data class Custom(val name : String) : ThemeId()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ThemeId> = when (doc)
        {
            is DocText ->
            {
                when (doc.text)
                {
                    "light" -> effValue<ValueError,ThemeId>(ThemeId.Light)
                    "dark"  -> effValue<ValueError,ThemeId>(ThemeId.Dark)
                    else    -> effValue<ValueError,ThemeId>(ThemeId.Custom(doc.text))
                }
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Theme Color Id
 */
data class ThemeColorId(val themeId : ThemeId, val colorId : ColorId) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ThemeColorId> = when (doc)
        {
            is DocDict ->
            {
                apply(::ThemeColorId,
                      // Theme Id
                      doc.at("theme_id").apply { ThemeId.fromSchemaDoc(it) },
                      // Color Id
                      doc.at("color_id").apply { ColorId.fromSchemaDoc(it) }
                      )

            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Color Theme
 */
data class ColorTheme(val themeColorIds : MutableSet<ThemeColorId>) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ColorTheme> = when (doc)
        {
            is DocDict ->
            {
                apply(::ColorTheme,
                      // Name
                      doc.list("theme_color_ids").apply {
                          it.mapSetMut { ThemeColorId.fromSchemaDoc(it) } }
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        // BUILT-IN THEMES
        // -----------------------------------------------------------------------------------------

        val transparent = ColorTheme(mutableSetOf(ThemeColorId(ThemeId.Light, ColorId.Transparent),
                ThemeColorId(ThemeId.Dark, ColorId.Transparent)))

        val black = ColorTheme(mutableSetOf(ThemeColorId(ThemeId.Light, ColorId.Black),
                ThemeColorId(ThemeId.Dark, ColorId.Black)))

        val white = ColorTheme(mutableSetOf(ThemeColorId(ThemeId.Light, ColorId.White),
                ThemeColorId(ThemeId.Dark, ColorId.White)))
    }



}
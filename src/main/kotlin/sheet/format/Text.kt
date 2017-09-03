
package sheet.format


import effect.effApply
import effect.effError
import effect.effValue
import effect.split
import lulo.document.*
import lulo.schema.Prim
import lulo.value.UnexpectedType
import lulo.value.UnexpectedValue
import lulo.value.ValueError
import lulo.value.ValueParser
import theme.ColorTheme
import java.awt.Font
import java.io.Serializable
import java.util.*



/**
 * Text Format
 */
data class TextFormat(val style : TextStyle,
                      val position : Position,
                      val height : Height,
                      val padding : Spacing,
                      val margins : Spacing,
                      val alignment: Alignment,
                      val verticalAlignment: VerticalAlignment)
                        : java.io.Serializable
{


    companion object
    {

        private val defaultStyle             = TextStyle.default()
        private val defaultPosition          = Position.Top
        private val defaultHeight            = Height.Wrap
        private val defaultPadding           = Spacing.default()
        private val defaultMargins           = Spacing.default()
        private val defaultAlignment         = Alignment.Center
        private val defaultVerticalAlignment = VerticalAlignment.Middle


        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextFormat> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::TextFormat,
                        // Style
                        split(doc.maybeAt("style"),
                                effValue(defaultStyle),
                                { TextStyle.fromSchemaDoc(it) }),
                        // Position
                        split(doc.maybeAt("position"),
                                effValue<ValueError,Position>(defaultPosition),
                                { Position.fromSchemaDoc(it) }),
                        // Height
                        split(doc.maybeAt("height"),
                                effValue<ValueError,Height>(defaultHeight),
                                { Height.fromSchemaDoc(it) }),
                        // Padding
                        split(doc.maybeAt("padding"),
                                effValue(defaultPadding),
                                { Spacing.fromSchemaDoc(it) }),
                        // Margins
                        split(doc.maybeAt("margins"),
                                effValue(defaultMargins),
                                { Spacing.fromSchemaDoc(it) }),
                        // Alignment
                        split(doc.maybeAt("alignment"),
                                effValue<ValueError,Alignment>(defaultAlignment),
                                { Alignment.fromSchemaDoc(it) }),
                        // Vertical Alignment
                        split(doc.maybeAt("vertical_alignment"),
                                effValue<ValueError,VerticalAlignment>(defaultVerticalAlignment),
                                { VerticalAlignment.fromSchemaDoc(it) })
                )
            }
                else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() = TextFormat(defaultStyle,
                                   defaultPosition,
                                   defaultHeight,
                                   defaultPadding,
                                   defaultMargins,
                                   defaultAlignment,
                                   defaultVerticalAlignment)

    }

}


/**
 * Text Style
 */
data class TextStyle(val colorTheme : ColorTheme,
                     val size : TextSize,
                     val font : TextFont,
                     val fontStyle : TextFontStyle,
                     val isUnderlined : IsUnderlined,
                     val alignment : Alignment,
                     val backgroundColorTheme : ColorTheme)
                     : Serializable
{

    companion object
    {

        private val defaultColorTheme           = ColorTheme.black
        private val defaultTextSize             = TextSize(16.0f)
        private val defaultFont                 = TextFont.FiraSans
        private val defaultFontStyle            = TextFontStyle.Regular
        private val defaultIsUnderlined         = IsUnderlined(false)
        private val defaultAlignment            = Alignment.Center
        private val defaultBackgroundColorTheme = ColorTheme.transparent


        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextStyle> = when (doc)
        {
            is DocDict ->
            {
                effApply(::TextStyle,
                        // Color Theme
                        split(doc.maybeAt("color_theme"),
                                effValue(defaultColorTheme),
                                { ColorTheme.fromSchemaDoc(it) }),
                        // Size
                        split(doc.maybeAt("size"),
                                effValue(defaultTextSize),
                                { TextSize.fromSchemaDoc(it) }),
                        // Font
                        split(doc.maybeAt("font"),
                                effValue<ValueError,TextFont>(defaultFont),
                                { TextFont.fromSchemaDoc(it) }),
                        // Font Style
                        split(doc.maybeAt("font_style"),
                                effValue<ValueError,TextFontStyle>(defaultFontStyle),
                                { TextFontStyle.fromDocument(it) }),
                        // Is Underlined?
                        split(doc.maybeAt("is_underlined"),
                                effValue(defaultIsUnderlined),
                                { IsUnderlined.fromSchemaDoc(it) }),
                        // Alignment
                        split(doc.maybeAt("alignment"),
                                effValue<ValueError,Alignment>(defaultAlignment),
                                { Alignment.fromSchemaDoc(it) }),
                        // Color
                        split(doc.maybeAt("background_color_theme"),
                                effValue(defaultBackgroundColorTheme),
                                { ColorTheme.fromSchemaDoc(it) })
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() = TextStyle(defaultColorTheme,
                                  defaultTextSize,
                                  defaultFont,
                                  defaultFontStyle,
                                  defaultIsUnderlined,
                                  defaultAlignment,
                                  defaultBackgroundColorTheme)

    }


}



/**
 * Text Size
 */
data class TextSize(val sp : Float) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextSize> = when (doc)
        {
            is DocNumber -> effValue(TextSize(doc.number.toFloat()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }

}


/**
 * Is Underlined
 */
data class IsUnderlined(val value : Boolean) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<IsUnderlined> = when (doc)
        {
            is DocBoolean -> effValue(IsUnderlined(doc.boolean))
            else          -> effError(UnexpectedType(DocType.BOOLEAN, docType(doc), doc.path))
        }
    }

}


/**
 * Text Font
 */
sealed class TextFont : java.io.Serializable
{

    object FiraSans : TextFont()


    object Merriweather : TextFont()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<TextFont> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "roboto"       -> effValue<ValueError,TextFont>(TextFont.FiraSans)
                "merriweather" -> effValue<ValueError,TextFont>(TextFont.Merriweather)
                else           -> effError<ValueError,TextFont>(
                                        UnexpectedValue("TextFont", doc.text, doc.path))
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }


        fun default() = TextFont.FiraSans

    }


}


/**
 * Text Font
 */
sealed class TextFontStyle : Serializable
{

    object Regular : TextFontStyle()


    object Bold : TextFontStyle()


    object Italic : TextFontStyle()


    object BoldItalic : TextFontStyle()


    object Light : TextFontStyle()


    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<TextFontStyle> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "regular"     -> effValue<ValueError,TextFontStyle>(TextFontStyle.Regular)
                "bold"        -> effValue<ValueError,TextFontStyle>(TextFontStyle.Bold)
                "italic"      -> effValue<ValueError,TextFontStyle>(TextFontStyle.Italic)
                "bold_italic" -> effValue<ValueError,TextFontStyle>(TextFontStyle.BoldItalic)
                "light"       -> effValue<ValueError,TextFontStyle>(TextFontStyle.Light)
                else          -> effError<ValueError,TextFontStyle>(
                                    UnexpectedValue("TextFontStyle", doc.text, doc.path))
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }

        fun default() = TextFontStyle.Regular
    }

}



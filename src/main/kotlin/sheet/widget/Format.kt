
package sheet.widget


import effect.effApply
import effect.effError
import effect.effValue
import effect.split
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import sheet.format.Alignment
import sheet.format.Corners
import sheet.format.Spacing
import theme.ColorTheme
import java.io.Serializable



/**
 * Widget Format
 */
data class WidgetFormat(var width : WidgetWidth,
                        var alignment : Alignment,
                        var backgroundColorTheme : ColorTheme,
                        val corners : Corners,
                        val margins : Spacing,
                        val padding : Spacing)
                         : Serializable
{

    companion object
    {

        private val defaultWidth                = WidgetWidth.default()
        private val defaultAlignment            = Alignment.Center
        private val defaultBackgroundColorTheme = ColorTheme.transparent
        private val defaultCorners              = Corners.default()
        private val defaultMargins              = Spacing.default()
        private val defaultPadding              = Spacing.default()


        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetFormat> = when (doc)
        {
            is DocDict -> effApply(::WidgetFormat,
                    // Width
                    split(doc.maybeAt("width"),
                            effValue(defaultWidth),
                            { WidgetWidth.fromSchemaDoc(it) }),
                    // Alignment
                    split(doc.maybeAt("alignment"),
                            effValue<ValueError,Alignment>(defaultAlignment),
                            { Alignment.fromSchemaDoc(it) }),
                    // Background Color Theme
                    split(doc.maybeAt("background_color_theme"),
                            effValue(defaultBackgroundColorTheme),
                            { ColorTheme.fromSchemaDoc(it) }),
                    // Corners
                    split(doc.maybeAt("corners"),
                            effValue<ValueError,Corners>(defaultCorners),
                            { Corners.fromSchemaDoc(it) }),
                    // Margins
                    split(doc.maybeAt("margins"),
                            effValue(defaultMargins),
                            { Spacing.fromSchemaDoc(it) }),
                    // Padding
                    split(doc.maybeAt("padding"),
                            effValue(defaultPadding),
                            { Spacing.fromSchemaDoc(it) })
            )
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() : WidgetFormat =
                WidgetFormat(defaultWidth,
                        defaultAlignment,
                        defaultBackgroundColorTheme,
                        defaultCorners,
                        defaultMargins,
                        defaultPadding)
    }

}


/**
 * Widget Width
 */
data class WidgetWidth(val value : Int) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetWidth> = when (doc)
        {
            is DocNumber -> effValue(WidgetWidth(doc.number.toInt()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }

        fun default() = WidgetWidth(1)
    }

}


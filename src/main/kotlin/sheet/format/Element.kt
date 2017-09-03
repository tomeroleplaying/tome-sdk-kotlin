
package sheet.format


import effect.effApply
import effect.effError
import effect.effValue
import effect.split
import lulo.document.DocDict
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import theme.ColorTheme
import java.io.Serializable



/**
 * Element Format
 */
data class ElementFormat(val position : Position,
                         val height : Height,
                         val padding : Spacing,
                         val margins : Spacing,
                         val backgroundColorTheme : ColorTheme,
                         val corners : Corners,
                         val alignment : Alignment,
                         val verticalAlignment : VerticalAlignment)
                          : Serializable
{


    companion object
    {

        private val defaultPosition             = Position.Top
        private val defaultHeight               = Height.Wrap
        private val defaultPadding              = Spacing.default()
        private val defaultMargins              = Spacing.default()
        private val defaultBackgroundColorTheme = ColorTheme.black
        private val defaultCorners              = Corners.default()
        private val defaultAlignment            = Alignment.Center
        private val defaultVerticalAlignment    = VerticalAlignment.Middle


        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ElementFormat> = when (doc)
        {
            is DocDict ->
            {
                effApply(::ElementFormat,
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
                        // Background Color Theme
                        split(doc.maybeAt("background_color_theme"),
                                effValue(defaultBackgroundColorTheme),
                                { ColorTheme.fromSchemaDoc(it) }),
                        // Corners
                        split(doc.maybeAt("corners"),
                                effValue(defaultCorners),
                                { Corners.fromSchemaDoc(it) }),
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
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() = ElementFormat(defaultPosition,
                                      defaultHeight,
                                      defaultPadding,
                                      defaultMargins,
                                      defaultBackgroundColorTheme,
                                      defaultCorners,
                                      defaultAlignment,
                                      defaultVerticalAlignment)

    }

}
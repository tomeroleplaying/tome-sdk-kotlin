
package sheet


import effect.effError
import effect.effValue
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import sheet.format.*
import theme.ColorTheme
import java.io.Serializable



/**
 * Group Row
 */
data class GroupRow(val format : GroupRowFormat,
                    val index : Int,
                    val widgets : MutableList<Widget>) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc, index : Int) : ValueParser<GroupRow> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::GroupRow,
                      // Format
                      doc.at("format").apply { GroupRowFormat.fromSchemaDoc(it) },
                      // Index
                      effValue(index),
                      // Widgets
                      doc.list("widgets").apply {
                          it.map { Widget.fromSchemaDoc(it) } }
                      )

            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}



/**
 * Group Row Format
 */
data class GroupRowFormat(val backgroundColorTheme : ColorTheme,
                       val margins: Spacing,
                       val padding : Spacing,
                       val alignment : Alignment,
                       val verticalAlignment : VerticalAlignment,
                       val showDivider : Boolean,
                       val dividerColorTheme : ColorTheme)
                        : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<GroupRowFormat> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::GroupRowFormat,
                      // Name
                      doc.at("background_color_theme").apply { ColorTheme.fromSchemaDoc(it) },
                      // Margins
                      doc.at("margins").apply { Spacing.fromSchemaDoc(it) },
                      // Padding
                      doc.at("padding").apply { Spacing.fromSchemaDoc(it) },
                      // Alignment
                      doc.at("alignment").apply { Alignment.fromSchemaDoc(it) },
                      // Vertical Alignment
                      doc.at("vertical_alignment").apply { VerticalAlignment.fromSchemaDoc(it) },
                      // Show Divider
                      doc.boolean("show_divider"),
                      // Divider Color Theme
                      doc.at("divider_color_theme").apply { ColorTheme.fromSchemaDoc(it) }
                      )
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

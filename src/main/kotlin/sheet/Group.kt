
package sheet


import effect.effError
import effect.effValue
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import sheet.format.Corners
import sheet.format.DividerMargins
import sheet.format.DividerThickness
import sheet.format.Spacing
import theme.ColorTheme
import java.io.Serializable



/**
 * Group
 */
data class Group(val format : GroupFormat,
                 val index : Int,
                 val rows : MutableList<GroupRow>) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc, index : Int) : ValueParser<Group> = when (doc)
        {
            is DocDict ->
            {
                apply(::Group,
                      // Format
                      doc.at("format").apply { GroupFormat.fromSchemaDoc(it) },
                      // Index
                      effValue(index),
                      // Rows
                      doc.list("rows").apply {
                          it.mapIndexedMut { doc, index ->
                              GroupRow.fromSchemaDoc(doc, index) } }
                      )

            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Group Format
 */
data class GroupFormat(val backgroundColorTheme : ColorTheme,
                       val margins: Spacing,
                       val padding : Spacing,
                       val corners : Corners,
                       val showDivider : Boolean,
                       val dividerColorTheme : ColorTheme,
                       val dividerMargins : DividerMargins,
                       val dividerThickness : DividerThickness)
                        : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<GroupFormat> = when (doc)
        {
            is DocDict ->
            {
                apply(::GroupFormat,
                      // Name
                      doc.at("background_color_theme").apply { ColorTheme.fromSchemaDoc(it) },
                      // Margins
                      doc.at("margins").apply { Spacing.fromSchemaDoc(it) },
                      // Padding
                      doc.at("padding").apply { Spacing.fromSchemaDoc(it) },
                      // Corners
                      doc.at("corners").apply { Corners.fromSchemaDoc(it) },
                      // Show Divider
                      doc.boolean("show_divider"),
                      // Divider Color Theme
                      doc.at("divider_color_theme").apply { ColorTheme.fromSchemaDoc(it) },
                      // Divider Margins
                      doc.at("divider_margins").apply { DividerMargins.fromSchemaDoc(it) },
                      // Divider Thickness
                      doc.at("divider_thickness").apply { DividerThickness.fromSchemaDoc(it) }
                      )
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

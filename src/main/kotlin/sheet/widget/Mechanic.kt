
package sheet.widget


import effect.effApply
import effect.effError
import effect.effValue
import effect.split
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.UnexpectedValue
import lulo.value.ValueError
import lulo.value.ValueParser
import sheet.format.ElementFormat
import sheet.format.TextFormat
import java.io.Serializable



/**
 * Mechanic Widget Format
 */
data class MechanicWidgetFormat(val widgetFormat : WidgetFormat,
                                val viewType : MechanicWidgetViewType,
                                val headerFormat : TextFormat,
                                val mechanicHeaderFormat : TextFormat,
                                val mechanicSummaryFormat : TextFormat,
                                var mechanicFormat : ElementFormat)
{


    companion object
    {

        val defaultWidgetFormat             = WidgetFormat.default()
        val defaultViewType                 = MechanicWidgetViewType.Boxes
        val defaultHeaderFormat             = TextFormat.default()
        val defaultMechanicHeaderFormat     = TextFormat.default()
        val defaultMechanicSummaryFormat    = TextFormat.default()
        val defaultMechanicFormat           = ElementFormat.default()


        fun fromDocument(doc: SchemaDoc): ValueParser<MechanicWidgetFormat> = when (doc)
        {
            is DocDict ->
            {
                effApply(::MechanicWidgetFormat,
                        // Widget Format
                        split(doc.maybeAt("widget_format"),
                                effValue(defaultWidgetFormat),
                                { WidgetFormat.fromSchemaDoc(it) }),
                        // View Type
                        split(doc.maybeAt("view_type"),
                                effValue<ValueError,MechanicWidgetViewType>(defaultViewType),
                                { MechanicWidgetViewType.fromDocument(it) }),
                        // Header Format
                        split(doc.maybeAt("header_format"),
                                effValue(defaultHeaderFormat),
                                { TextFormat.fromSchemaDoc(it) }),
                        // Mechanic Header Style
                        split(doc.maybeAt("mechanic_header_format"),
                                effValue(defaultMechanicHeaderFormat),
                                { TextFormat.fromSchemaDoc(it) }),
                        // Mechanic Summary Style
                        split(doc.maybeAt("mechanic_summary_format"),
                                effValue(defaultMechanicSummaryFormat),
                                { TextFormat.fromSchemaDoc(it) }),
                        // Mechanic Format
                        split(doc.maybeAt("mechanic_format"),
                                effValue(defaultMechanicFormat),
                                { ElementFormat.fromSchemaDoc(it) })
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }


        fun default() = MechanicWidgetFormat(defaultWidgetFormat,
                defaultViewType,
                defaultHeaderFormat,
                defaultMechanicHeaderFormat,
                defaultMechanicSummaryFormat,
                defaultMechanicFormat)

    }

}


sealed class MechanicWidgetViewType : Serializable
{


    object Boxes : MechanicWidgetViewType()


    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<MechanicWidgetViewType> = when (doc)
        {
            is DocText -> when (doc.text)
            {
                "boxes" -> effValue<ValueError,MechanicWidgetViewType>(
                        MechanicWidgetViewType.Boxes)
                else    -> effError<ValueError,MechanicWidgetViewType>(
                        UnexpectedValue("MechanicWidgetViewType", doc.text, doc.path))
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }
}


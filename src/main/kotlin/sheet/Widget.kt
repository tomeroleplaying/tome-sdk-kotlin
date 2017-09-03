
package sheet


import effect.effError
import effect.effValue
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import sheet.widget.MechanicWidgetFormat
import java.io.Serializable



/**
 * Widget
 */
sealed class Widget(open val widgetId : WidgetId) : Serializable
{

}



/**
 * Widget Id
 */
data class WidgetId(var value : String) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetId> = when (doc)
        {
            is DocText -> effValue(WidgetId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))

        }
    }
}



data class WidgetMechanic(override val widgetId : WidgetId,
                          val format : MechanicWidgetFormat,
                          var categoryId : MechanicCategoryId) : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetMechanic> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetMechanic,
                        // Widget Id
                        doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { MechanicWidgetFormat.fromSchemaDoc(it) },
                             // Category Id
                             doc.at("category_id").apply { MechanicCategoryId.fromSchemaDoc(it) }
                             )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class WidgetNumber(override val widgetId : WidgetId,
                        val format : NumberWidgetFormat,
                        var valueVariableId : VariableId,
                        var description : NumberWidgetDescription)
                         : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetNumber> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetNumber,
                        // Widget Id
                        doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { NumberWidgetFormat.fromSchemaDoc(it) },
                             // Value Variable Id
                             doc.at("value_variable_id").apply { VariableId.fromSchemaDoc(it) },
                             // Description
                             doc.at("description").apply { NumberWidgetDescription.fromSchemaDoc(it) }
                             )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class WidgetPoints(override val widgetId : WidgetId,
                        val format : PointsWidgetFormat,
                        var limitValueVariableId : VariableId,
                        var currentValueVariableId : VariableId,
                        var label : PointsWidgetLabel)
                         : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetPoints> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetPoints,
                        // Widget Id
                        doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { PointsWidgetFormat.fromSchemaDoc(it) },
                             // Limit Value Variable Id
                             doc.at("limit_value_variable_id").apply { VariableId.fromSchemaDoc(it) },
                             // Current Value Variable Id
                             doc.at("current_value_variable_id").apply { VariableId.fromSchemaDoc(it) },
                             // Label
                             doc.at("description").apply { PointsWidgetLabel.fromSchemaDoc(it) }
                             )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class WidgetQuote(override val widgetId : WidgetId,
                       val format : QuoteWidgetFormat,
                       var viewType : QuoteWidgetViewType,
                       var quoteVariableId : VariableId,
                       var sourceVariableId : VariableId?)
                        : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetQuote> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetQuote,
                        // Widget Id
                        doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { QuoteWidgetFormat.fromSchemaDoc(it) },
                             // View Type
                             doc.at("view_type").apply { QuoteWidgetViewType.fromSchemaDoc(it) },
                             // Quote Variable Id
                             doc.at("quote_variable_id").apply { VariableId.fromSchemaDoc(it) },
                             // Source Variable Id
                             doc.at("source_variable_id").apply { VariableId.fromSchemaDoc(it) }
                             )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class WidgetStory(override val widgetId : WidgetId,
                       val format : StoryWidgetFormat,
                       var story : MutableList<StoryPart>)
                        : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetStory> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetStory,
                        // Widget Id
                        doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { StoryWidgetFormat.fromSchemaDoc(it) },
                             // Story
                             doc.list("story").apply {
                                 it.mapMut { StoryPart.fromSchemaDoc(it) } }
                             )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class WidgetTable(override val widgetId : WidgetId,
                       var name : TableWidgetName,
                       val format : TableWidgetFormat,
                       val columns : MutableList<TableWidgetColumn>,
                       val rows : MutableList<TableWidgetRow>,
                       val sort : TableWidgetSort)
                        : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetTable> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetTable,
                        // Widget Id
                        doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                            // Name
                            doc.at("name").apply { TableWidgetName.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { TableWidgetFormat.fromSchemaDoc(it) },
                             // Columns
                             doc.list("columns").apply {
                                 it.mapMut { TableWidgetColumn.fromSchemaDoc(it) } },
                             // Rows
                             doc.list("rowsj").apply {
                                 it.mapMut { TableWidgetRow.fromSchemaDoc(it) } },
                                // Sort
                                doc.at("sort").apply { TableWidgetSort.fromSchemaDoc(it) }
                            )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


data class WidgetText(override val widgetId : WidgetId,
                       val format : TextWidgetformat,
                       val valueVariableId : VariableId)
                        : Widget(widgetId)
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<WidgetText> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::WidgetText,
                            // Widget Id
                            doc.at("widget_id").apply { WidgetId.fromSchemaDoc(it) },
                             // Format
                             doc.at("format").apply { TextWigetFormat.fromSchemaDoc(it) },
                            // Value Variable Id
                            doc.at("value_variable_id").apply { VariableId.fromSchemaDoc(it) }
                            )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

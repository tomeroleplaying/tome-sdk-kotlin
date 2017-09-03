
package sheet


import effect.apply
import effect.effError
import effect.effValue
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import sheet.format.Spacing
import theme.ColorTheme
import java.io.Serializable



/**
 * Page
 */
data class Page(var name : PageName,
                val format : PageFormat,
                var index : Int,
                val groups : MutableList<Group>) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc, index : Int) : ValueParser<Page> = when (doc)
        {
            is DocDict ->
            {
                apply(::Page,
                      // Name
                      doc.at("name").apply { PageName.fromSchemaDoc(it) },
                      // Format
                      doc.at("format").apply { PageFormat.fromSchemaDoc(it) },
                      // Index
                      effValue(index),
                      // Groups
                      doc.list("groups").apply {
                          it.mapIndexedMut { doc, index ->  Group.fromSchemaDoc(doc, index) } }
                      )

            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Page Name
 */
data class PageName(var value : String) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<PageName> = when (doc)
        {
            is DocText -> effValue(PageName(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))

        }
    }
}


/**
 * Page Index
 */
data class PageIndex(var value : Int) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<PageIndex> = when (doc)
        {
            is DocNumber -> effValue(PageIndex(doc.number.toInt()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))

        }
    }
}


/**
 * Page Format
 */
data class PageFormat(val backgroundColorTheme : ColorTheme,
                      val padding : Spacing)
                        : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<sheet.PageFormat> = when (doc)
        {
            is DocDict ->
            {
                apply(::PageFormat,
                      // Name
                      doc.at("background_color_theme").apply { ColorTheme.fromSchemaDoc(it) },
                      // Padding
                      doc.at("padding").apply { Spacing.fromSchemaDoc(it) }
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}

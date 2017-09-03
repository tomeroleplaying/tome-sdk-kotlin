
package sheet


import effect.apply
import effect.effError
import effect.effValue
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import sheet.format.Icon
import java.io.Serializable



/**
 * Section
 */
data class Section(val name : SectionName,
                   val pages : MutableList<Page>,
                   val icon : Icon) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Section> = when (doc)
        {
            is DocDict ->
            {
                apply(::Section,
                      // Name
                      doc.at("name").apply { SectionName.fromSchemaDoc(it) },
                      // Pages
                      doc.list("pages").apply {
                          it.mapIndexedMut { doc, index ->  Page.fromSchemaDoc(doc, index) } },
                      // Name
                      doc.at("icon").apply { Icon.fromSchemaDoc(it) }
                      )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Section Name
 */
data class SectionName(var value : String) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<SectionName> = when (doc)
        {
            is DocText -> effValue(SectionName(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))

        }
    }
}


package sheet.format


import effect.effError
import effect.effValue
import lulo.document.DocNumber
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Divider Thickness
 */
data class DividerThickness(var value : Int) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DividerThickness> = when (doc)
        {
            is DocNumber -> effValue(DividerThickness(doc.number.toInt()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))

        }
    }
}


/**
 * Divider Margins
 */
data class DividerMargins(var value : Double) : Serializable
{
    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<DividerMargins> = when (doc)
        {
            is DocNumber -> effValue(DividerMargins(doc.number))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))

        }
    }
}


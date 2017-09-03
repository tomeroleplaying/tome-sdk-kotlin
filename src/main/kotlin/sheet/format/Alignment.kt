
package sheet.format


import effect.effError
import effect.effValue
import lulo.document.DocText
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.UnexpectedValue
import lulo.value.ValueError
import lulo.value.ValueParser



/**
 * Alignment
 */
sealed class Alignment
{

    object Left : Alignment()


    object Center : Alignment()


    object Right : Alignment()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Alignment> = when (doc)
        {
            is DocText ->
            {
                when (doc.text)
                {
                    "left"   -> effValue<ValueError,Alignment>(Alignment.Left)
                    "center" -> effValue<ValueError,Alignment>(Alignment.Center)
                    "right"  -> effValue<ValueError,Alignment>(Alignment.Right)
                    else     -> effError<ValueError,Alignment>(
                                   UnexpectedValue("Alignment", doc.text, doc.path))
                }
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Vertical Alignment
 */
sealed class VerticalAlignment
{

    object Top : VerticalAlignment()


    object Middle : VerticalAlignment()


    object Bottom : VerticalAlignment()


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<VerticalAlignment> = when (doc)
        {
            is DocText ->
            {
                when (doc.text)
                {
                    "top"    -> effValue<ValueError,VerticalAlignment>(VerticalAlignment.Top)
                    "middle" -> effValue<ValueError,VerticalAlignment>(VerticalAlignment.Middle)
                    "bottom" -> effValue<ValueError,VerticalAlignment>(VerticalAlignment.Bottom)
                    else     -> effError<ValueError,VerticalAlignment>(
                                   UnexpectedValue("VerticalAlignment", doc.text, doc.path))
                }
            }
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}
